package org.uncommons.poker.game.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.uncommons.poker.game.Suit;

/**
 * @author Daniel Dyer
 */
public class SevenCardHandEvaluator implements HandEvaluator
{
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
        Map<Suit, List<PlayingCard>> groupedBySuit = new HashMap<Suit, List<PlayingCard>>();
        groupedBySuit.put(Suit.CLUBS, new ArrayList<PlayingCard>(4));
        groupedBySuit.put(Suit.DIAMONDS, new ArrayList<PlayingCard>(4));
        groupedBySuit.put(Suit.HEARTS, new ArrayList<PlayingCard>(4));
        groupedBySuit.put(Suit.SPADES, new ArrayList<PlayingCard>(4));

        for (PlayingCard card : cards)
        {
            groupedBySuit.get(card.getSuit()).add(card);
        }

        for (List<PlayingCard> flush : groupedBySuit.values())
        {
            if (flush.size() == RankedHand.HAND_SIZE)
            {
                if (flush.get(0).getValue().ordinal() - flush.get(RankedHand.HAND_SIZE - 1).ordinal()
                    == RankedHand.HAND_SIZE - 1)
                {
                    if (flush.get(0).getValue() == FaceValue.ACE)
                    {
                        return new RankedHand(flush, HandRanking.ROYAL_FLUSH);
                    }
                    else
                    {
                        return new RankedHand(flush, HandRanking.STRAIGHT_FLUSH);
                    }
                }
                else
                {
                    return new RankedHand(flush, HandRanking.FLUSH);
                }
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
                    assert straightCards.size() == RankedHand.HAND_SIZE : "Wrong straight length: " + straightCards.size();
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
