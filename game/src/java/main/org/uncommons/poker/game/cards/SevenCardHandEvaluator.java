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
 * for Texas Hold'em and 7-Card Stud but not for Omaha, which uses 4 hole cards and 5 community
 * cards (9 cards in total) and has restrictions on how cards may be combined to make a 5-card
 * hand.
 *
 * <i>For Omaha, first generate all valid 5-card combinations and then use
 * {@link FiveCardHandEvaluator}.  The same approach could also be used for Hold'em and
 * Stud but a 7-card evaluator is faster.</i>
 * 
 * @author Daniel Dyer
 */
public class SevenCardHandEvaluator implements HandEvaluator
{
    private static final Comparator<List<PlayingCard>> LIST_SIZE_COMPARATOR = new Comparator<List<PlayingCard>>()
    {
        public int compare(List<PlayingCard> cards1, List<PlayingCard> cards2)
        {
            return cards2.size() - cards1.size(); // Reverse order.
        }
    };


    /**
     * {@inheritDoc}
     */
    public RankedHand evaluate(List<PlayingCard> cards)
    {
        // First check for straight, flush or straight flush.
        RankedHand rankedHand = rankStraightOrFlush(cards);

        List<List<PlayingCard>> groupedByValue = groupByValue(cards);

        List<PlayingCard> hand = new ArrayList<PlayingCard>(5);
        Iterator<List<PlayingCard>> groupIterator = groupedByValue.iterator();
        while (hand.size() < RankedHand.HAND_SIZE)
        {
            // If we have four cards in the hand already (FOUR_OF_A_KIND or TWO_PAIR),
            // we don't want to add the next group, we want to add the highest remaining
            // card as a kicker.
            if (hand.size() == RankedHand.HAND_SIZE - 1)
            {
                hand.add(findHighestCard(groupIterator));
            }
            else
            {
                for (PlayingCard card : groupIterator.next())
                {
                    hand.add(card);
                    if (hand.size() == RankedHand.HAND_SIZE)
                    {
                        break;
                    }
                }
            }
        }

        HandRanking handRanking = rankGroups(groupedByValue);
        if (rankedHand == null || handRanking.compareTo(rankedHand.getRanking()) > 0)
        {
            rankedHand = new RankedHand(hand, handRanking);
        }

        return rankedHand;
    }


    /**
     * Helper method to find the highest ranked individual card in one or more groups
     * of cards.
     * @param groupIterator Iterator for cards grouped by rank.
     * @return The highest ranked card found.
     */
    private PlayingCard findHighestCard(Iterator<List<PlayingCard>> groupIterator)
    {
        PlayingCard kicker = null;
        while (groupIterator.hasNext())
        {
            // Don't need to iterate over all cards in the group since they are the
            // same rank.  Just take the first one.
            PlayingCard card = groupIterator.next().get(0);
            if (kicker == null || card.compareTo(kicker) > 0)
            {
                kicker = card;
            }
        }
        return kicker;
    }


    /**
     * Group the cards by rank.  If there are no pairs, trips or quads, there will be 7 groups
     * each with one member.
     */
    private List<List<PlayingCard>> groupByValue(List<PlayingCard> cards)
    {
        List<List<PlayingCard>> groupedByValue = new ArrayList<List<PlayingCard>>(7);

        int start = 0;
        int end = 1;
        for (int i = 1; i < cards.size(); i++)
        {
            if (cards.get(i - 1).getValue() != cards.get(i).getValue())
            {
                groupedByValue.add(cards.subList(start, end));
                start = i;
                end = i;
            }
            ++end;
        }
        groupedByValue.add(cards.subList(start, end));
        Collections.sort(groupedByValue, LIST_SIZE_COMPARATOR);
        return groupedByValue;
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
    private RankedHand rankStraightOrFlush(List<PlayingCard> cards)
    {
        List<PlayingCard> flushCards = filterFlushCards(cards);
        List<PlayingCard> straightCards = filterStraightCards(flushCards == null ? cards : flushCards);
        if (flushCards != null)
        {
            HandRanking ranking = HandRanking.FLUSH;
            // If the hand is also a straight, then this is more than just a flush...
            if (straightCards != null)
            {
                flushCards = straightCards;
                ranking = flushCards.get(0).getValue() == FaceValue.ACE ? HandRanking.ROYAL_FLUSH
                                                                        : HandRanking.STRAIGHT_FLUSH;
            }
            // We only need 5 cards to make a flush.
            while (flushCards.size() > RankedHand.HAND_SIZE)
            {
                flushCards.remove(flushCards.size() - 1);
            }
            
            return new RankedHand(flushCards, ranking);
        }
        else if (straightCards != null)
        {
            return new RankedHand(straightCards, HandRanking.STRAIGHT);
        }
        return null;
    }


    /**
     * Takes a list of playing cards and if 5 or more of them have the same suit, they are returned.
     * The list returned will contain 5 or more cards or it will be null if there is no flush to be
     * made from the speficied cards.  The reason for potentially returning more than 5 cards is that,
     * because we haven't yet checked for a straight flush, we don't know whether we might still need
     * the lower ranked cards.
     */
    private List<PlayingCard> filterFlushCards(List<PlayingCard> cards)
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
                    }
                }
                return flush;
            }
        }
        return null;
    }


    private List<PlayingCard> filterStraightCards(List<PlayingCard> cards)
    {
        // Re-jig the list so that we can detect 5, 4, 3, 2, A as a straight too. 
        PlayingCard highestCard = cards.get(0);
        if (highestCard.getValue() == FaceValue.ACE && cards.get(cards.size() - 1).getValue() == FaceValue.TWO)
        {
            cards = new ArrayList<PlayingCard>(cards);
            cards.add(highestCard); // Ace is also low.
        }

        List<PlayingCard> straightCards = new ArrayList<PlayingCard>(RankedHand.HAND_SIZE);
        straightCards.add(cards.get(0));
        for (int i = 1; i < cards.size(); i++)
        {
            if (assertConsecutiveRanks(cards.get(i), cards.get(i - 1)))
            {
                straightCards.add(cards.get(i));
                if (straightCards.size() == RankedHand.HAND_SIZE)
                {
                    return straightCards;
                }
            }
            // If there are two consecutive cards of the same rank, skip over the second one.
            else if (cards.get(i).getValue() != cards.get(i - 1).getValue())
            {
                // If we get to here, the card we're looking at is not part of a straight.
                straightCards.clear();
                straightCards.add(cards.get(i));
            }
        }
        return null;
    }


    /**
     * Returns true if the two cards would be next to each other in a straight, false otherwise.
     * The first card has to be one rank lower than the second in order for this method to return
     * true.  Also returns true when the first card is an ace and the second a two to account for
     * a low straight: 5, 4, 3, 2, A.
     */
    private boolean assertConsecutiveRanks(PlayingCard card1, PlayingCard card2)
    {
        return card1.getValue().ordinal() == card2.getValue().ordinal() - 1
               || (card1.getValue() == FaceValue.ACE && card2.getValue() == FaceValue.TWO);
    }
}
