package org.uncommons.poker.game.cards;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniel Dyer
 */
public class SevenCardPrecomputedHandEvaluator
{
    private static final File DATA_FILE = new File("7cardlookup.dat");

    private static final byte[] LOOKUP_TABLE;
    static
    {
        try
        {
            LookupTableGenerator generator = new LookupTableGenerator();
            if (DATA_FILE.exists())
            {
                LOOKUP_TABLE = generator.loadTable(DATA_FILE);
            }
            else
            {
                LOOKUP_TABLE = generator.generateTable();
                generator.saveTable(LOOKUP_TABLE, DATA_FILE);
            }
        }
        catch (IOException e)
        {
            throw new ExceptionInInitializerError(e);
        }            
    }
}
