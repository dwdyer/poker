package org.uncommons.poker.game.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;
import org.uncommons.poker.game.cards.HandEvaluator;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.RankedHand;
import org.uncommons.poker.game.cards.SevenCardHandEvaluator;

/**
 * @author Daniel Dyer
 */
public class TexasHoldem implements PokerRules
{
    private static final HandEvaluator HAND_EVALUATOR = new SevenCardHandEvaluator();


    public RankedHand rankHand(List<PlayingCard> playerCards,
                               List<PlayingCard> communityCards)
    {
        // There are no restrictions on the use of hole cards in Texas Hold'em.  A hand
        // can be made from any 5 of the 7 cards (2 hole cards and 5 community cards)
        // available to the player.
        List<PlayingCard> allCards = new ArrayList<PlayingCard>(playerCards.size() + communityCards.size());
        allCards.addAll(playerCards);
        allCards.addAll(communityCards);

        // The seven-card evaluator expects the cards to be sorted.
        sort(allCards);

        return HAND_EVALUATOR.evaluate(allCards);
    }


    /**
     * Custom sort routine for 7-card lists.  This is a 7 element sorting
     * network (http://en.wikipedia.org/wiki/Sorting_network).  It is more
     * efficient than general purpose sorting algorithms but it only works
     * with lists containing exactly 7 items.
     *
     * Cards are sorting in descending order.
     * 
     * @param cards A list of 7 cards.
     */
    private void sort(List<PlayingCard> cards)
    {
        assert cards.size() == 7 : "Sorting network only works with 7 cards.";
        // Dumping to an array and sorting that is quicker than sorting the list
        // in place (even a random access list) because of the overhead of get/set
        // operations on a list.
        PlayingCard[] array = cards.toArray(new PlayingCard[cards.size()]);
        compareAndSwap(array, 1, 2);
        compareAndSwap(array, 0, 2);
        compareAndSwap(array, 0, 1);
        compareAndSwap(array, 3, 4);
        compareAndSwap(array, 5, 6);
        compareAndSwap(array, 3, 5);
        compareAndSwap(array, 4, 6);
        compareAndSwap(array, 4, 5);
        compareAndSwap(array, 0, 4);
        compareAndSwap(array, 0, 3);
        compareAndSwap(array, 1, 5);
        compareAndSwap(array, 2, 6);
        compareAndSwap(array, 2, 5);
        compareAndSwap(array, 1, 3);
        compareAndSwap(array, 2, 4);
        compareAndSwap(array, 2, 3);

        // Copy the array back into the list.
        assert cards instanceof RandomAccess : "Performance problem.";
        for (int i = 0; i < array.length; i++)
        {
            cards.set(i, array[i]);
        }
    }


    private void compareAndSwap(PlayingCard[] cards, int p1, int p2)
    {
        if (cards[p2].ordinal() > cards[p1].ordinal())
        {
            PlayingCard temp = cards[p1];
            cards[p1] = cards[p2];
            cards[p2] = temp;
        }
    }
}
