package org.uncommons.poker.game.cards;

import java.util.List;

/**
 * @author Daniel Dyer
 */
public interface HandEvaluator
{
    /**
     * @param cards A list of no more than five cards.
     * @return The highest possible ranking for this hand.
     */
    RankedHand evaluate(List<PlayingCard> cards);
}
