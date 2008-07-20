package org.uncommons.poker.game.rules;

import org.uncommons.poker.game.cards.RankedHand;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.HandEvaluator;
import org.uncommons.poker.game.cards.DefaultHandEvaluator;
import org.uncommons.maths.combinatorics.CombinationGenerator;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Daniel Dyer
 */
public class TexasHoldem implements PokerRules
{
    private static final HandEvaluator HAND_EVALUATOR = new DefaultHandEvaluator();

    public RankedHand rankHand(List<PlayingCard> playerCards,
                               List<PlayingCard> communityCards)
    {
        // There are no restrictions on the use of hole cards in Texas Hold'em.  A hand
        // can be made from any 5 of the 7 cards (2 hole cards and 5 community cards)
        // available to the player.
        List<PlayingCard> allCards = new ArrayList<PlayingCard>(playerCards.size() + communityCards.size());
        allCards.addAll(playerCards);
        allCards.addAll(communityCards);

        CombinationGenerator<PlayingCard> generator = new CombinationGenerator<PlayingCard>(allCards,
                                                                                            RankedHand.HAND_SIZE);

        RankedHand bestHand = null;
        while (generator.hasMore())
        {
            RankedHand next = HAND_EVALUATOR.evaluate(generator.nextCombinationAsList());
            if (bestHand == null || next.compareTo(bestHand) > 0)
            {
                bestHand = next;
            }
        }
        return bestHand;
    }
}
