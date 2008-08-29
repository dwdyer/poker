package org.uncommons.poker.game.cards;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.uncommons.maths.combinatorics.CombinationGenerator;

/**
 * @author Daniel Dyer
 */
class LookupTableGenerator
{
    private static final int LOOKUP_TABLE_SIZE = 133784560;
    
    private static final SevenCardHandEvaluator EVALUATOR = new SevenCardHandEvaluator();

    public byte[] generateTable()
    {
        final byte[] lookupTable = new byte[LOOKUP_TABLE_SIZE];

        System.out.println("Generating hand evaluation look-up table, this may take several minutes...");
        CombinationGenerator<PlayingCard> generator = new CombinationGenerator<PlayingCard>(PlayingCard.values(), 7);
        long start = System.currentTimeMillis();
        long count = 0;
        for (List<PlayingCard> hand : generator)
        {
            int index = mapHandToIndex(hand);
            lookupTable[index] = (byte) EVALUATOR.evaluate(hand).getRanking().ordinal();
            ++count;
            if (count % 13378456 == 0)
            {
                System.out.println(count * 100 / lookupTable.length + "% complete");
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("7-card look-up table generated in " + (elapsed / 1000) + " seconds.");

        return lookupTable;
    }


    public void saveTable(byte[] lookupTable, File file) throws IOException
    {
        long start = System.currentTimeMillis();
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        try
        {
            outputStream.write(lookupTable);
            outputStream.flush();
        }
        finally
        {
            outputStream.close();
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("7-card look-up table saved in " + (elapsed / 1000) + " seconds.");
    }


    public byte[] loadTable(File file) throws IOException
    {
        long start = System.currentTimeMillis();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        try
        {
            byte[] lookupTable = new byte[LOOKUP_TABLE_SIZE];
            int count = 0;
            while (count < LOOKUP_TABLE_SIZE)
            {
                int read = inputStream.read(lookupTable, count, LOOKUP_TABLE_SIZE - count);
                count += read;
                if (read == -1)
                {
                    throw new EOFException("Incomplete look-up table file.");
                }
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("7-card look-up table loaded in " + (elapsed / 1000) + " seconds.");
            return lookupTable;
        }
        finally
        {
            inputStream.close();
        }
    }


    /**
     * Minimal perfect hash for an ordered combination of 7 cards.
     * @param hand 7 cards, descending order of rank, no duplicates.
     * @return Minimal perfect hash value.
     */
    private int mapHandToIndex(List<PlayingCard> hand)
    {
        return choose(hand.get(0).ordinal(), 1)
               + choose(hand.get(1).ordinal(), 2)
               + choose(hand.get(2).ordinal(), 3)
               + choose(hand.get(3).ordinal(), 4)
               + choose(hand.get(4).ordinal(), 5)
               + choose(hand.get(5).ordinal(), 6)
               + choose(hand.get(6).ordinal(), 7);
    }


    private int choose(int n, int k)
    {
        if (n < 0 || k < 0)
        {
            throw new IllegalArgumentException("Invalid negative parameter in choose()");
        }

        if (n < k)
        {
            return 0;  // special case
        }
        if (n == k)
        {
            return 1;
        }

        int delta, iMax;

        if (k < n-k) // ex: Choose(100,3)
        {
            delta = n-k;
            iMax = k;
        }
        else         // ex: Choose(100,97)
        {
            delta = k;
            iMax = n-k;
        }

        int ans = delta + 1;

        for (int i = 2; i <= iMax; ++i)
        {
            ans = (ans * (delta + i)) / i;
        }

        return ans;
    }
}
