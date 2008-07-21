package org.uncommons.poker.game.cards;

import java.util.List;
import java.util.RandomAccess;
import org.uncommons.poker.game.Suit;
import org.uncommons.util.ListUtils;

/**
 * @author Daniel Dyer
 */
public class DefaultHandEvaluator implements HandEvaluator
{
    /**
     * @param cards The cards that make up a hand.  These must be sorted in descending
     * order of face value.
     * @return A ranked hand.
     */
    public RankedHand evaluate(List<PlayingCard> cards)
    {
        if (cards.size() > RankedHand.HAND_SIZE)
        {
            throw new IllegalArgumentException("Hand must contain no more than " + RankedHand.HAND_SIZE + " cards.");
        }

        HandRanking handRanking = rankHand(cards);
        orderCards(cards, handRanking);

        return new RankedHand(cards, handRanking);
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
                    FaceValue lowest = cards.get(RankedHand.HAND_SIZE - 1).getValue();
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
     * Re-orders the cards in a hand so that the most significant cards(s) are
     * first.  For example, the highest ranked card is the most significant in
     * a straight or flush whereas the two matching cards are the most significant
     * in a hand that has one pair.
     *
     * Once the hand is correctly ordered, two hands with the same ranking can
     * easily be compared to see which is better by comparing the most significant
     * card (if these are equivalent then the second most significant cards can
     * be compared and so on).
     */
    private void orderCards(List<PlayingCard> cards, HandRanking ranking)
    {
        // Assumes that the cards are already sorted by rank, so for flushes, high
        // cards and most straights, there is nothing to do.
        switch (ranking)
        {
            case STRAIGHT_FLUSH:
            case STRAIGHT:
            {
                // If this hand is A, 2, 3, 4, 5, make sure ace is low.
                if (cards.get(0).getValue() == FaceValue.ACE && cards.get(1).getValue() == FaceValue.FIVE)
                {
                    cards.add(cards.remove(0));
                }
                break;
            }
            case FOUR_OF_A_KIND:
            {
                // The matching four cards will all be next to each other, so the kicker
                // is either at the beginning or the end.  If it's at the beginning, it
                // needs to be moved to the end.
                if (cards.get(0).getValue() != cards.get(1).getValue())
                {
                    cards.add(cards.remove(0));
                }
                break;
            }
            case FULL_HOUSE:
            {
                // The three-of-a-kind is more important than the pair, so if the pair
                // is first, it needs to be moved to the end.
                if (cards.get(2).getValue() != cards.get(0).getValue()) // 3rd card is not the same as 1st.
                {
                    cards.add(cards.remove(0)); // Shift first card to the end.
                    cards.add(cards.remove(0)); // Shift second (now first) card to the end.
                }
                break;
            }
            case THREE_OF_A_KIND:
            {
                int start = findPair(cards);
                ListUtils.shiftLeft(cards, start, 3, start);
                break;
            }
            case TWO_PAIR:
            {
                // The kicker will either be the first, third or fifth card.  If it is the first or third,
                // it needs to be moved.
                if (cards.get(0).getValue() != cards.get(1).getValue()) // Kicker is first card.
                {
                    cards.add(cards.remove(0));
                }
                else if (cards.get(2).getValue() != cards.get(3).getValue()) // Kicker is third card.
                {
                    cards.add(cards.remove(2));
                }
                break;
            }
            case PAIR:
            {
                int start = findPair(cards);
                ListUtils.shiftLeft(cards, start, 2, start);
                break;
            }
        }
    }


    private int findPair(List<PlayingCard> cards)
    {
        for (int i = 0; i < cards.size() - 1; i++)
        {
            if (cards.get(i).getValue() == cards.get(i + 1).getValue())
            {
                return i;
            }
        }
        return -1;
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
     * Assumes that there are no pairs in the hand and that the cards are sorted.
     * @return True if the hand is a straight (or straight-flush), false otherwise.
     */
    private boolean isStraight(List<PlayingCard> cards)
    {
        if (cards.size() != RankedHand.HAND_SIZE)
        {
            return false;
        }
        
        FaceValue highest = cards.get(0).getValue();
        FaceValue lowest = cards.get(RankedHand.HAND_SIZE - 1).getValue();
        if (highest.ordinal() - lowest.ordinal() == RankedHand.HAND_SIZE - 1)
        {
            return true;
        }
        // Check for wheel (A, 2, 3, 4, 5).
        else if (highest == FaceValue.ACE && lowest == FaceValue.TWO)
        {
            FaceValue highestExcludingAce = cards.get(1).getValue();
            return highestExcludingAce == FaceValue.FIVE;
        }
        else
        {
            return false;
        }
    }
}
