package org.uncommons.poker.game.cards;

import java.util.List;

/**
 * @author Daniel Dyer
 */
public class SevenCardPrecomputedHandEvaluator implements HandEvaluator
{
    private static final RankedHand[] FIVE_CARD_LOOKUP_TABLE;
    private static final int[] SEVEN_CARD_MAPPINGS;

    static
    {
        LookupTableGenerator generator = new LookupTableGenerator();
        FIVE_CARD_LOOKUP_TABLE = generator.generateFiveCardLookupTable();
        SEVEN_CARD_MAPPINGS = generator.generateSevenCardMappings();
    }


    public RankedHand evaluate(List<PlayingCard> cards)
    {
        int sevenCardHash = CardUtils.sevenCardHash(cards);
        int fiveCardIndex = SEVEN_CARD_MAPPINGS[sevenCardHash];
        return FIVE_CARD_LOOKUP_TABLE[fiveCardIndex];
    }
}
