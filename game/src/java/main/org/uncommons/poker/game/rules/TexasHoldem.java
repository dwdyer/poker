package org.uncommons.poker.game.rules;

import java.util.ArrayList;
import java.util.List;
import org.uncommons.poker.game.cards.HandEvaluator;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.RankedHand;
import org.uncommons.poker.game.cards.CardUtils;
import org.uncommons.poker.game.cards.SevenCardHandEvaluator;

/**
 * @author Daniel Dyer
 */
public class TexasHoldem implements PokerRules
{
    private final HandEvaluator handEvaluator;


    public TexasHoldem()
    {
        this(new SevenCardHandEvaluator());
    }


    /**
     * @param handEvaluator A 7-card hand evaluator.
     */
    public TexasHoldem(HandEvaluator handEvaluator)
    {
        this.handEvaluator = handEvaluator;
    }

    
    public RankedHand rankHand(List<PlayingCard> playerCards,
                               List<PlayingCard> communityCards)
    {
        // There are no restrictions on the use of hole cards in Texas Hold'em.  A hand
        // can be made from any 5 of the 7 cards (2 hole cards and 5 community cards)
        // available to the player.
        List<PlayingCard> allCards = new ArrayList<PlayingCard>(playerCards.size() + communityCards.size());
        allCards.addAll(playerCards);
        allCards.addAll(communityCards);

        // The seven-card evaluator expects the cards to be sorted.
        CardUtils.sevenCardSort(allCards);

        return handEvaluator.evaluate(allCards);
    }
}
