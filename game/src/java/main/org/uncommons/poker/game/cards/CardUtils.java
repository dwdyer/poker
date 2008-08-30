package org.uncommons.poker.game.cards;

import java.math.BigInteger;
import java.util.List;
import java.util.RandomAccess;
import org.uncommons.maths.Maths;

/**
 * @author Daniel Dyer
 */
public final class CardUtils
{
    // [n][k] = Number of combinations of size k that can be choosen from a set of size n.
    private static final int[][] CHOICES = new int[52][8];

    static
    {
        for (int n = 0; n < CHOICES.length; n++)
        {
            BigInteger nFactorial = Maths.bigFactorial(n);
            for (int k = 0; k < CHOICES[n].length; k++)
            {
                if (n < k)
                {
                    CHOICES[n][k] = 0;
                }
                else if (n == k)
                {
                    CHOICES[n][k] = 1;
                }
                else
                {
                    BigInteger kFactorial = Maths.bigFactorial(k);
                    BigInteger nMinusKFactorial = Maths.bigFactorial(n - k);
                    BigInteger value = nFactorial.divide(kFactorial.multiply(nMinusKFactorial));
                    CHOICES[n][k] = value.intValue();
                }
            }
        }
    }

    
    private CardUtils()
    {
        // Prevents instantiation of utility class.
    }


    /**
     * Custom sort routine for 5-card lists.  This is a 5 element sorting
     * network (http://en.wikipedia.org/wiki/Sorting_network).  It is more
     * efficient than general purpose sorting algorithms but it only works
     * with lists containing exactly 5 items.
     *
     * Cards are sorting in descending order.
     *
     * @param cards A list of 5 cards.
     */
    public static void fiveCardSort(PlayingCard[] cards)
    {
        assert cards.length == 5 : "Sorting network only works with 7 cards.";
        compareAndSwap(cards, 0, 1);
        compareAndSwap(cards, 3, 4);
        compareAndSwap(cards, 2, 4);
        compareAndSwap(cards, 2, 3);
        compareAndSwap(cards, 0, 3);
        compareAndSwap(cards, 0, 2);
        compareAndSwap(cards, 1, 4);
        compareAndSwap(cards, 1, 3);
        compareAndSwap(cards, 1, 2);
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
    public static void sevenCardSort(List<PlayingCard> cards)
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



    private static void compareAndSwap(PlayingCard[] cards, int p1, int p2)
    {
        if (cards[p2].ordinal() > cards[p1].ordinal())
        {
            PlayingCard temp = cards[p1];
            cards[p1] = cards[p2];
            cards[p2] = temp;
        }
    }


    /**
     * Minimal perfect hash for an ordered combination of 7 cards.
     * @param hand 7 cards, descending order of rank, no duplicates.
     * @return Minimal perfect hash value.
     */
    public static int sevenCardHash(List<PlayingCard> hand)
    {
        return CHOICES[hand.get(0).ordinal()][7]
               + CHOICES[hand.get(1).ordinal()][6]
               + CHOICES[hand.get(2).ordinal()][5]
               + CHOICES[hand.get(3).ordinal()][4]
               + CHOICES[hand.get(4).ordinal()][3]
               + CHOICES[hand.get(5).ordinal()][2]
               + CHOICES[hand.get(6).ordinal()][1];
    }


    /**
     * Minimal perfect hash for an ordered combination of 5 cards.
     * @param hand 5 cards, descending order of rank, no duplicates.
     * @return Minimal perfect hash value.
     */
    public static int fiveCardHash(PlayingCard[] hand)
    {
        return CHOICES[hand[0].ordinal()][5]
               + CHOICES[hand[1].ordinal()][4]
               + CHOICES[hand[2].ordinal()][3]
               + CHOICES[hand[3].ordinal()][2]
               + CHOICES[hand[4].ordinal()][1];
    }


    /**
     * Minimal perfect hash for an ordered combination of 5 cards.
     * @param hand 5 cards, descending order of rank, no duplicates.
     * @return Minimal perfect hash value.
     */
    public static int fiveCardHash(List<PlayingCard> hand)
    {
        return CHOICES[hand.get(0).ordinal()][5]
               + CHOICES[hand.get(1).ordinal()][4]
               + CHOICES[hand.get(2).ordinal()][3]
               + CHOICES[hand.get(3).ordinal()][2]
               + CHOICES[hand.get(4).ordinal()][1];
    }

}
