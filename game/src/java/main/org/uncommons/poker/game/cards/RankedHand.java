package org.uncommons.poker.game.cards;

import java.util.List;

/**
 * Immutable 5-card poker hand.  Cards are arranged in descending order of
 * importance with relation to the hand's ranking.
 * @author Daniel Dyer
 */
public final class RankedHand implements Comparable<RankedHand>
{
    public static final int HAND_SIZE = 5;

    private final PlayingCard[] cards = new PlayingCard[HAND_SIZE];
    private final HandRanking ranking;

    /**
     * @param cards The cards that make up the hand.  Must be ordered in
     * descending order of significance.
     * @param ranking The value of the hand (e.g. TWO_PAIR or FULL_HOUSE).
     */
    public RankedHand(List<PlayingCard> cards, HandRanking ranking)
    {
        cards.toArray(this.cards);
        this.ranking = ranking;
    }


    public RankedHand(PlayingCard card1,
                      PlayingCard card2,
                      PlayingCard card3,
                      PlayingCard card4,
                      PlayingCard card5,
                      HandRanking ranking)
    {
        cards[0] = card1;
        cards[1] = card2;
        cards[2] = card3;
        cards[3] = card4;
        cards[4] = card5;
        this.ranking = ranking;
    }


    public HandRanking getRanking()
    {
        return ranking;
    }


    public PlayingCard getCard(int index)
    {
        return cards[index];
    }


    public int compareTo(RankedHand otherHand)
    {
        int compare = this.ranking.ordinal() - otherHand.getRanking().ordinal();
        // If the hands have the same ranking, check the actual cards.  For example,
        // both may be ranked as PAIR, but one may be a pair of threes and the other
        // a pair of kings.
        if (compare == 0)
        {
            PlayingCard[] otherCards = otherHand.cards;
            for (int i = 0; i < cards.length; i++)
            {
                compare = cards[i].ordinal() - otherCards[i].ordinal();
                if (compare != 0)
                {
                    break;
                }
            }
        }
        return compare;
    }


    /**
     * Test whether this hand contains a given card.  Used mostly for validation by unit tests.
     * @return True if the card is present in this hand, false otherwise.
     * @param card The card to search for.
     */
    public boolean contains(PlayingCard card)
    {
        for (PlayingCard c : cards)
        {
            if (c == card)
            {
                return true;
            }
        }
        return false;
    }


    public PlayingCard[] getCards()
    {
        return cards.clone();
    }


    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        for (PlayingCard card : cards)
        {
            buffer.append(card.toString());
            buffer.append(' ');
        }
        return buffer.toString();
    }
}
