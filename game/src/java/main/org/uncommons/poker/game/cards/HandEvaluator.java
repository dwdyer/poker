package org.uncommons.poker.game.cards;

import java.util.List;

/**
 * Strategy interface for evaluating poker hands.
 * @author Daniel Dyer
 */
public interface HandEvaluator
{
    /**
     * @param cards A list of cards that can be used to make the 5-card hand.
     * These must be sorted in descending order of face value.
     * @return The highest possible ranking for this hand.
     */
    RankedHand evaluate(List<PlayingCard> cards);
}
