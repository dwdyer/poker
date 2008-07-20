package org.uncommons.poker.game.cards;

import org.testng.annotations.Test;
import java.util.List;
import java.util.Arrays;

/**
 * Unit test for {@link RankedHand} class.
 * @author Daniel Dyer
 */
public class RankedHandTest
{
    /**
     * {@link RankedHand#compareTo(RankedHand)} should return zero for identical
     * hands.
     */
    @Test
    public void testEqualComparison()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.THREE_OF_DIAMONDS,
                                                PlayingCard.THREE_OF_HEARTS,
                                                PlayingCard.KING_OF_SPADES,
                                                PlayingCard.TEN_OF_CLUBS,
                                                PlayingCard.SEVEN_OF_CLUBS);
        RankedHand hand = new RankedHand(cards, HandRanking.PAIR);
        assert hand.compareTo(hand) == 0 : "Hand should be equal to itself.";
    }


    /**
     * {@link RankedHand#compareTo(RankedHand)} should return a positive integer
     * when the first hand has the same ranking as the second but a higher kicker.
     */
    @Test
    public void testSameRankingDifferentKickers()
    {
        List<PlayingCard> cards1 = Arrays.asList(PlayingCard.THREE_OF_DIAMONDS,
                                                 PlayingCard.THREE_OF_HEARTS,
                                                 PlayingCard.KING_OF_SPADES,
                                                 PlayingCard.TEN_OF_CLUBS,
                                                 PlayingCard.SEVEN_OF_CLUBS);
        RankedHand hand1 = new RankedHand(cards1, HandRanking.PAIR);

        List<PlayingCard> cards2 = Arrays.asList(PlayingCard.THREE_OF_CLUBS,
                                                 PlayingCard.THREE_OF_SPADES,
                                                 PlayingCard.QUEEN_OF_SPADES,
                                                 PlayingCard.TEN_OF_DIAMONDS,
                                                 PlayingCard.SEVEN_OF_HEARTS);
        RankedHand hand2 = new RankedHand(cards2, HandRanking.PAIR);

        assert hand1.compareTo(hand2) > 0 : "First hand should beat second hand.";
    }


    /**
     * {@link RankedHand#compareTo(RankedHand)} should return a negative integer
     * when the first hand has a lower ranking than the second.
     */
    @Test
    public void testDifferentRankings()
    {
        List<PlayingCard> cards1 = Arrays.asList(PlayingCard.THREE_OF_CLUBS,
                                                 PlayingCard.THREE_OF_SPADES,
                                                 PlayingCard.QUEEN_OF_SPADES,
                                                 PlayingCard.TEN_OF_DIAMONDS,
                                                 PlayingCard.SEVEN_OF_HEARTS);
        RankedHand hand1 = new RankedHand(cards1, HandRanking.PAIR);

        List<PlayingCard> cards2 = Arrays.asList(PlayingCard.SIX_OF_DIAMONDS,
                                                 PlayingCard.SIX_OF_HEARTS,
                                                 PlayingCard.FOUR_OF_SPADES,
                                                 PlayingCard.FOUR_OF_CLUBS,
                                                 PlayingCard.SEVEN_OF_CLUBS);
        RankedHand hand2 = new RankedHand(cards2, HandRanking.PAIR);


        assert hand1.compareTo(hand2) < 0 : "Second hand should beat first hand.";
    }
}
