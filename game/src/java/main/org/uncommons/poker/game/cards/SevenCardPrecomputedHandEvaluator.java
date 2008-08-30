package org.uncommons.poker.game.cards;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.uncommons.maths.combinatorics.CombinationGenerator;
import org.uncommons.util.ConsoleProgressDisplay;

/**
 * A {@link HandEvaluator} that uses tables of pre-computed ranked hands to quickly
 * evaluate 7-card hands.  This implementation generates about 550Mb of look-up tables,
 * so requires a significant amount of heap space.
 * @author Daniel Dyer
 */
public class SevenCardPrecomputedHandEvaluator implements HandEvaluator
{
    private static final int FIVE_CARD_COMBINATIONS = 2598960;
    private static final int SEVEN_CARD_COMBINATIONS = 133784560;
    private static final int FIVE_PERCENT = SEVEN_CARD_COMBINATIONS / 20;

    private static final File MAPPING_FILE = new File("mappings.dat");

    private static final HandEvaluator FIVE_CARD_EVALUATOR = new FiveCardHandEvaluator();
    private static final HandEvaluator SEVEN_CARD_EVALUATOR = new SevenCardHandEvaluator();
    private static final ConsoleProgressDisplay CONSOLE = new ConsoleProgressDisplay();

    private final RankedHand[] fiveCardLookupTable;
    private final int[] sevenCardMappings;

    public SevenCardPrecomputedHandEvaluator()
    {
        fiveCardLookupTable = generateFiveCardLookupTable();
        sevenCardMappings = generateSevenCardMappings();
    }


    private RankedHand[] generateFiveCardLookupTable()
    {
        CONSOLE.start("Generating 5-card hand evaluation look-up table...");

        final RankedHand[] lookupTable = new RankedHand[FIVE_CARD_COMBINATIONS];
        CombinationGenerator<PlayingCard> generator = new CombinationGenerator<PlayingCard>(PlayingCard.reverseValues(), 5);
        for (List<PlayingCard> hand : generator)
        {
            int hash = CardUtils.fiveCardHash(hand);
            RankedHand rankedHand = FIVE_CARD_EVALUATOR.evaluate(hand);
            lookupTable[hash] = rankedHand;
        }

        CONSOLE.finish(true);

        return lookupTable;
    }


    /**
     * Generate a table of mappings from seven-card hands to entries in a 5-card look-up table.
     * This will return ~134 million integers, which requires about 535Mb of memory.
     * @return An array of indices for mapping
     * {@link CardUtils#sevenCardHash(java.util.List) 7-card hashes} to an entry in the
     * {@link #generateFiveCardLookupTable() 5-card look-up table}.
     */
    private int[] generateSevenCardMappings()
    {
        try
        {
            return loadMappings(MAPPING_FILE);
        }
        catch (IOException ex)
        {
            CONSOLE.finish(false);
        }

        CONSOLE.start("Generating 7-card to 5-card mappings...");
        CONSOLE.update(0);

        int[] mappings = new int[SEVEN_CARD_COMBINATIONS];
        CombinationGenerator<PlayingCard> generator = new CombinationGenerator<PlayingCard>(PlayingCard.reverseValues(), 7);
        long count = 0;
        for (List<PlayingCard> hand : generator)
        {
            int index = CardUtils.sevenCardHash(hand);
            PlayingCard[] cards = SEVEN_CARD_EVALUATOR.evaluate(hand).getCards();
            CardUtils.fiveCardSort(cards);
            int fiveCardIndex = CardUtils.fiveCardHash(cards);
            mappings[index] = fiveCardIndex;
            ++count;
            if (count % FIVE_PERCENT == 0)
            {
                CONSOLE.update((int) (count * 100 / mappings.length));
            }
        }
        CONSOLE.finish(true);

        try
        {
            saveMappings(mappings, MAPPING_FILE);
        }
        catch (IOException ex)
        {
            CONSOLE.finish(false);
        }

        return mappings;
    }


    private void saveMappings(int[] mappings, File file) throws IOException
    {
        CONSOLE.start("Writing 7-card mappings to disk...");

        DataOutputStream outputStream = null;
        try
        {
            outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            for (int i = 0; i < mappings.length; i++)
            {
                outputStream.writeInt(mappings[i]);
                if (i % FIVE_PERCENT == 0)
                {
                    CONSOLE.update((int) ((long) i * 100 / mappings.length));
                }
            }
            outputStream.flush();
        }
        finally
        {
            if (outputStream != null)
            {
                outputStream.close();
            }
        }

        CONSOLE.finish(true);
    }


    private int[] loadMappings(File file) throws IOException
    {
        CONSOLE.start("Loading 7-card mappings from disk...");

        DataInputStream inputStream = null;
        try
        {
            inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            int[] mappings = new int[SEVEN_CARD_COMBINATIONS];
            for (int i = 0; i < mappings.length; i++)
            {
                mappings[i] = inputStream.readInt();
                if (i % FIVE_PERCENT == 0)
                {
                    CONSOLE.update((int) ((long) i * 100 / mappings.length));
                }
            }

            CONSOLE.finish(true);

            return mappings;
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }


    /**
     * {@inheritDoc}
     * @param cards Seven cards, sorted in descending order of rank.
     * @return A ranked 5-card hand.
     */
    public RankedHand evaluate(List<PlayingCard> cards)
    {
        int sevenCardHash = CardUtils.sevenCardHash(cards);
        int fiveCardIndex = sevenCardMappings[sevenCardHash];
        return fiveCardLookupTable[fiveCardIndex];
    }
}
