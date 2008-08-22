package org.uncommons.poker.game.cards;

import java.util.List;
import org.uncommons.maths.combinatorics.CombinationGenerator;

/**
 * @author Daniel Dyer
 */
public class FiveCardLookupHandEvaluator extends FiveCardHandEvaluator
{
    private static final int DECK_SIZE = PlayingCard.values().length;
    private final byte[][][][][] lookupTable = new byte[DECK_SIZE][DECK_SIZE][DECK_SIZE][DECK_SIZE][DECK_SIZE];

    public FiveCardLookupHandEvaluator()
    {
        // Build the look-up table.
        long startTime = System.currentTimeMillis();

        CombinationGenerator<PlayingCard> handGenerator = new CombinationGenerator<PlayingCard>(PlayingCard.values(),
                                                                                                RankedHand.HAND_SIZE);
        for (List<PlayingCard> hand : handGenerator)
        {
           lookupTable[hand.get(0).ordinal()]
                      [hand.get(1).ordinal()]
                      [hand.get(2).ordinal()]
                      [hand.get(3).ordinal()]
                      [hand.get(4).ordinal()] = (byte) rankHand(hand).ordinal();
        }
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Initialised hand ranking look-up table in " + elapsed + "ms.");
    }

    
    @Override
    public RankedHand evaluate(List<PlayingCard> cards)
    {
        int rankingOrdinal = lookupTable[cards.get(0).ordinal()]
                                        [cards.get(1).ordinal()]
                                        [cards.get(2).ordinal()]
                                        [cards.get(3).ordinal()]
                                        [cards.get(4).ordinal()];
        HandRanking ranking = HandRanking.values()[rankingOrdinal];
        orderCards(cards, ranking);
        return new RankedHand(cards, ranking);
    }
}
