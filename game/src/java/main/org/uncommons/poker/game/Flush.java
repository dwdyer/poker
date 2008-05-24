package org.uncommons.poker.game;

import java.util.List;

/**
 * @author Daniel Dyer
 */
public class Flush implements HandRanking
{
    public boolean matches(List<PlayingCard> hand)
    {
        if (hand.size() != 5)
        {
            return false;
        }
        Suit lastSuit = null;
        for (PlayingCard card : hand)
        {
            if (lastSuit == null || card.getSuit().equals(lastSuit))
            {
                lastSuit = card.getSuit();
            }
            else
            {
                return false;
            }

        }
        return true;
    }
}
