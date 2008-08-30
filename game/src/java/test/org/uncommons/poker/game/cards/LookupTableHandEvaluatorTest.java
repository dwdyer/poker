package org.uncommons.poker.game.cards;

import org.testng.annotations.Test;

/**
 * @author Daniel Dyer
 */
public class LookupTableHandEvaluatorTest
{
    @Test
    public void testGenerator()
    {
        new LookupTableGenerator().generateSevenCardMappings();
    }
}
