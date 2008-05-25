package org.uncommons.poker.game.cards;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
import org.uncommons.poker.game.Suit;

/**
 * @author Daniel Dyer
 */
public class DefaultHandEvaluator implements HandEvaluator
{
    public RankedHand evaluate(List<PlayingCard> cards)
    {
        if (cards.size() > RankedHand.HAND_SIZE)
        {
            throw new IllegalArgumentException("Hand must contain no more than " + RankedHand.HAND_SIZE + " cards.");
        }
        Collections.sort(cards);        
        return new RankedHand(cards, rankHand(cards));
    }


    private HandRanking rankHand(List<PlayingCard> cards)
    {
        int pairs = countPairs(cards);
        switch (pairs)
        {
            case 1: return HandRanking.PAIR;
            case 2: return HandRanking.TWO_PAIR;
            case 3: return HandRanking.THREE_OF_A_KIND;
            case 4: return HandRanking.FULL_HOUSE;
            case 6: return HandRanking.FOUR_OF_A_KIND;
            default:
            {
                assert pairs == 0 : "Unexpected pair count: " + pairs;
                boolean flush = isFlush(cards);
                boolean straight = isStraight(cards);
                if (flush && straight)
                {
                    // A royal flush is a straight flush from 10 to Ace.
                    FaceValue lowest = cards.get(0).getValue();
                    return lowest == FaceValue.TEN ? HandRanking.ROYAL_FLUSH : HandRanking.STRAIGHT_FLUSH;
                }
                else if (flush)
                {
                    return HandRanking.FLUSH;
                }
                else if (straight)
                {
                    return HandRanking.STRAIGHT;
                }
                else
                {
                    return HandRanking.HIGH_CARD;
                }
            }
        }
    }


    /**
     * Counts how many pairs occur within a 5-card hand.  This is with replacement,
     * so one card can appear in multiple pairs (these are pairs in the Cribbage sense
     * rather than the poker sense).  This number of pairs maps to a particular poker
     * hand ranking (1 = PAIR, 2 = TWO_PAIR, 3 = THREE_OF_A_KIND, 4 = FULL_HOUSE,
     * 6 = FOUR_OF_A_KIND).  If the number of pairs is zero, additional checks for a
     * straight or flush must be made.
     *
     * This pair-counting algorithm for ranking poker hands was invented by David Brunger.
     * @param cards The five cards that make up the poker hand.
     * @return The total number of times any individual card's rank matches that of another
     * card in the hand.
     */
    private int countPairs(List<PlayingCard> cards)
    {
        assert cards instanceof RandomAccess : "This is going to be slow.";

        int pairs = 0;
        // For each card, check how many of the subsequent cards its value matches.
        for (int i = 0; i < cards.size() - 1; i++)
        {
            FaceValue firstCard = cards.get(i).getValue();
            for (int j = i + 1; j < cards.size(); j++)
            {
                if (firstCard == cards.get(j).getValue())
                {
                    ++pairs;
                }
            }
        }
        return pairs;
    }


    /**
     * @return True if the hand is a flush (or straight-flush), false otherwise.
     */
    private boolean isFlush(List<PlayingCard> cards)
    {
        if (cards.size() != RankedHand.HAND_SIZE)
        {
            return false;
        }
        Suit lastSuit = null;
        for (PlayingCard card : cards)
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


    /**
     * Assumes that there are no pairs in the hand.
     * @return True if the hand is a straight (or straight-flush), false otherwise.
     */
    private boolean isStraight(List<PlayingCard> cards)
    {
        if (cards.size() != RankedHand.HAND_SIZE)
        {
            return false;
        }
        
        FaceValue lowest = cards.get(0).getValue();
        FaceValue highest = cards.get(RankedHand.HAND_SIZE - 1).getValue();
        if (highest.ordinal() - lowest.ordinal() == RankedHand.HAND_SIZE - 1)
        {
            return true;
        }
        // Check for wheel (A, 2, 3, 4, 5).
        else if (highest == FaceValue.ACE && lowest == FaceValue.TWO)
        {
            FaceValue highestExcludingAce = cards.get(RankedHand.HAND_SIZE - 2).getValue();
            return highestExcludingAce == FaceValue.FIVE;
        }
        else
        {
            return false;
        }
    }
}
