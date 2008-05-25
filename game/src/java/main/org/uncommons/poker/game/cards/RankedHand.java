package org.uncommons.poker.game.cards;

import java.util.List;

/**
 * @author Daniel Dyer
 */
public final class RankedHand
{
    public static final int HAND_SIZE = 5;

    private final List<PlayingCard> cards;
    private final HandRanking ranking;

    public RankedHand(List<PlayingCard> cards, HandRanking ranking)
    {
        this.cards = cards;
        this.ranking = ranking;
    }


    public HandRanking getRanking()
    {
        return ranking;
    }
}
