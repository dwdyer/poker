package org.uncommons.poker.game.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.uncommons.poker.game.Suit;

/**
 * A {@link HandEvaluator} implementation that works on 7-card hands.  It assumes that any
 * 5 of the seven cards can be used to form the best 5-card hand.  This evaluator is suitable
 * for Texas Hold'em and 7-Card Stud but not for Omaha, which uses 9 cards and has more
 * restrictions on how cards may be used.
 *
 * <i>For Omaha, first generate all valid 5-card combinations and then use
 * {@link FiveCardHandEvaluator}.  The same approach could also be used for Hold'em and
 * Stud but this implementation is faster.</i>
 * 
 * @author Daniel Dyer
 */
public class SevenCardHandEvaluator implements HandEvaluator
{
    /**
     * {@inheritDoc}
     */
    public RankedHand evaluate(List<PlayingCard> cards)
    {
        List<List<PlayingCard>> groupedByValue = new ArrayList<List<PlayingCard>>(7);

        List<PlayingCard> group = new ArrayList<PlayingCard>(4);
        group.add(cards.get(0));
        for (int i = 1; i < cards.size(); i++)
        {
            PlayingCard card = cards.get(i);
            if (cards.get(i - 1).getValue() != card.getValue())
            {
                groupedByValue.add(group);
                group = new ArrayList<PlayingCard>(4);
            }
            group.add(card);
        }
        groupedByValue.add(group);
        Collections.sort(groupedByValue, new Comparator<List<PlayingCard>>()
        {
            public int compare(List<PlayingCard> cards1, List<PlayingCard> cards2)
            {
                return cards2.size() - cards1.size(); // Reverse order.
            }
        });

        RankedHand straightOrFlushHand = checkForFlush(cards);
        if (straightOrFlushHand == null)
        {
            straightOrFlushHand = checkForStraight(cards);
        }
        RankedHand nonStraightOrFlushHand = convertGroupsToRankedHand(groupedByValue);

        if (straightOrFlushHand != null && straightOrFlushHand.compareTo(nonStraightOrFlushHand) > 0)
        {
            return straightOrFlushHand;
        }
        else
        {
            return nonStraightOrFlushHand;
        }
    }


    private RankedHand convertGroupsToRankedHand(List<List<PlayingCard>> groupedByValue)
    {
        List<PlayingCard> cards = new ArrayList<PlayingCard>(5);
        outerLoop: for (List<PlayingCard> group : groupedByValue)
        {
            for (PlayingCard card : group)
            {
                cards.add(card);
                if (cards.size() == RankedHand.HAND_SIZE)
                {
                    break outerLoop;
                }
            }
        }
        return new RankedHand(cards, rankGroups(groupedByValue));
    }


    private HandRanking rankGroups(List<List<PlayingCard>> groupedByValue)
    {
        Iterator<List<PlayingCard>> iterator = groupedByValue.iterator();
        switch (iterator.next().size())
        {
            case 4: return HandRanking.FOUR_OF_A_KIND;
            case 3: return iterator.next().size() >= 2 ? HandRanking.FULL_HOUSE
                                                       : HandRanking.THREE_OF_A_KIND;
            case 2: return iterator.next().size() == 2 ? HandRanking.TWO_PAIR
                                                       : HandRanking.PAIR;
            default: return HandRanking.HIGH_CARD;
        }
    }


    /**
     * @return A ranked hand if these cards include a flush, straight flush or royal flush;
     * null otherwise.
     */
    private RankedHand checkForFlush(List<PlayingCard> cards)
    {
        int[] suitCounts = new int[4];

        for (PlayingCard card : cards)
        {
            ++suitCounts[card.getSuit().ordinal()];
        }

        // If we have enough cards of one suit to make a flush, filter those cards into
        // a new list.
        for (Suit suit : Suit.values())
        {
            if (suitCounts[suit.ordinal()] >= RankedHand.HAND_SIZE)
            {
                List<PlayingCard> flush = new ArrayList<PlayingCard>(RankedHand.HAND_SIZE);
                for (PlayingCard card : cards)
                {
                    if (card.getSuit() == suit)
                    {
                        flush.add(card);
                        if (flush.size() == RankedHand.HAND_SIZE)
                        {
                            break;
                        }
                    }
                }

                HandRanking ranking = HandRanking.FLUSH;
                // If the hand is also a straight, then this is more than just a flush...
                if (flush.get(0).getValue().ordinal() - flush.get(RankedHand.HAND_SIZE - 1).ordinal()
                    == RankedHand.HAND_SIZE - 1)
                {
                    ranking = flush.get(0).getValue() == FaceValue.ACE ? HandRanking.ROYAL_FLUSH
                                                                       : HandRanking.STRAIGHT_FLUSH;
                }
                return new RankedHand(flush, ranking);

            }
        }

        return null;
    }


    private RankedHand checkForStraight(List<PlayingCard> cards)
    {
        // Re-jig the list so that we can detect 5, 4, 3, 2, A as a straight too. 
        PlayingCard highestCard = cards.get(0);
        if (highestCard.getValue() == FaceValue.ACE && cards.get(cards.size() - 1).getValue() == FaceValue.TWO)
        {
            cards = new ArrayList<PlayingCard>(cards);
            cards.add(highestCard); // Ace is also low.
        }

        int length = 1;
        for (int i = 1; i < cards.size(); i++)
        {
            if (cards.get(i).getValue().ordinal() == cards.get(i - 1).getValue().ordinal() - 1)
            {
                ++length;
                if (length == RankedHand.HAND_SIZE)
                {
                    List<PlayingCard> straightCards = cards.subList(i - (RankedHand.HAND_SIZE - 1), i + 1);
                    return new RankedHand(straightCards, HandRanking.STRAIGHT);
                }
            }
            else
            {
                length = 1;
            }
        }
        return null;
    }
}
