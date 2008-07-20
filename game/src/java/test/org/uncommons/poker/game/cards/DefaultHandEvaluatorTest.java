package org.uncommons.poker.game.cards;

import java.util.List;
import java.util.ArrayList;
import org.testng.annotations.Test;

/**
 * @author Daniel Dyer
 */
public class DefaultHandEvaluatorTest
{
    private final HandEvaluator handEvaluator = new DefaultHandEvaluator();

    @Test
    public void testRoyalFlush()
    {
        List<PlayingCard> cards = asList(PlayingCard.TEN_OF_SPADES,
                                         PlayingCard.QUEEN_OF_SPADES,
                                         PlayingCard.ACE_OF_SPADES,
                                         PlayingCard.KING_OF_SPADES,
                                         PlayingCard.JACK_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.ROYAL_FLUSH : "Wrong hand ranking: " + hand.getRanking();
        // The ace is the most significant card, the ten the least.
        assert hand.getCards().get(0) == PlayingCard.ACE_OF_SPADES : "Wrong order.";
        assert hand.getCards().get(1) == PlayingCard.KING_OF_SPADES : "Wrong order.";
        assert hand.getCards().get(2) == PlayingCard.QUEEN_OF_SPADES : "Wrong order.";
        assert hand.getCards().get(3) == PlayingCard.JACK_OF_SPADES : "Wrong order.";
        assert hand.getCards().get(4) == PlayingCard.TEN_OF_SPADES : "Wrong order.";
    }


    @Test
    public void testStraightFlush()
    {
        List<PlayingCard> cards = asList(PlayingCard.FOUR_OF_HEARTS,
                                         PlayingCard.FIVE_OF_HEARTS,
                                         PlayingCard.SIX_OF_HEARTS,
                                         PlayingCard.SEVEN_OF_HEARTS,
                                         PlayingCard.EIGHT_OF_HEARTS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.STRAIGHT_FLUSH : "Wrong hand ranking: " + hand.getRanking();
        // The eight is the most significant card, the four the least.
        assert hand.getCards().get(0) == PlayingCard.EIGHT_OF_HEARTS : "Wrong order.";
        assert hand.getCards().get(1) == PlayingCard.SEVEN_OF_HEARTS : "Wrong order.";
        assert hand.getCards().get(2) == PlayingCard.SIX_OF_HEARTS : "Wrong order.";
        assert hand.getCards().get(3) == PlayingCard.FIVE_OF_HEARTS : "Wrong order.";
        assert hand.getCards().get(4) == PlayingCard.FOUR_OF_HEARTS : "Wrong order.";
    }


    /**
     * Ace can be low to make a straight flush (A, 2, 3, 4, 5).
     */
    @Test
    public void testLowStraightFlush()
    {
        List<PlayingCard> cards = asList(PlayingCard.ACE_OF_DIAMONDS,
                                         PlayingCard.TWO_OF_DIAMONDS,
                                         PlayingCard.THREE_OF_DIAMONDS,
                                         PlayingCard.FOUR_OF_DIAMONDS,
                                         PlayingCard.FIVE_OF_DIAMONDS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.STRAIGHT_FLUSH : "Wrong hand ranking: " + hand.getRanking();
        // The five is the most significant card, the ace the least.
        assert hand.getCards().get(0) == PlayingCard.FIVE_OF_DIAMONDS : "Wrong order.";
        assert hand.getCards().get(1) == PlayingCard.FOUR_OF_DIAMONDS : "Wrong order.";
        assert hand.getCards().get(2) == PlayingCard.THREE_OF_DIAMONDS : "Wrong order.";
        assert hand.getCards().get(3) == PlayingCard.TWO_OF_DIAMONDS : "Wrong order.";
        assert hand.getCards().get(4) == PlayingCard.ACE_OF_DIAMONDS : "Wrong order.";
    }


    @Test
    public void testFourOfAKind()
    {
        List<PlayingCard> cards = asList(PlayingCard.KING_OF_CLUBS,
                                         PlayingCard.TWO_OF_DIAMONDS,
                                         PlayingCard.KING_OF_DIAMONDS,
                                         PlayingCard.KING_OF_HEARTS,
                                         PlayingCard.KING_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.FOUR_OF_A_KIND : "Wrong hand ranking: " + hand.getRanking();
        // The kicker should be the least significant card.
        assert hand.getCards().get(4) == PlayingCard.TWO_OF_DIAMONDS : "Wrong order.";
    }


    /**
     * The kicker should be the least significant card even when it is a higher
     * rank than the others.
     */
    @Test
    public void testFourOfAKindHighKicker()
    {
        List<PlayingCard> cards = asList(PlayingCard.FOUR_OF_CLUBS,
                                         PlayingCard.NINE_OF_DIAMONDS,
                                         PlayingCard.FOUR_OF_DIAMONDS,
                                         PlayingCard.FOUR_OF_HEARTS,
                                         PlayingCard.FOUR_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.FOUR_OF_A_KIND : "Wrong hand ranking: " + hand.getRanking();
        // The kicker should be the least significant card.
        assert hand.getCards().get(4) == PlayingCard.NINE_OF_DIAMONDS : "Wrong order.";
    }


    @Test
    public void testFullHouse()
    {
        List<PlayingCard> cards = asList(PlayingCard.SEVEN_OF_CLUBS,
                                         PlayingCard.SEVEN_OF_SPADES,
                                         PlayingCard.JACK_OF_DIAMONDS,
                                         PlayingCard.JACK_OF_HEARTS,
                                         PlayingCard.JACK_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.FULL_HOUSE : "Wrong hand ranking: " + hand.getRanking();
        // Trips are more important than the pair.
        assert hand.getCards().get(0).getValue() == FaceValue.JACK : "Wrong order.";
        assert hand.getCards().get(1).getValue() == FaceValue.JACK : "Wrong order.";
        assert hand.getCards().get(2).getValue() == FaceValue.JACK : "Wrong order.";
        assert hand.getCards().get(3).getValue() == FaceValue.SEVEN : "Wrong order.";
        assert hand.getCards().get(4).getValue() == FaceValue.SEVEN : "Wrong order.";
    }


    @Test
    public void testFullHouseLowTrips()
    {
        List<PlayingCard> cards = asList(PlayingCard.KING_OF_CLUBS,
                                         PlayingCard.KING_OF_SPADES,
                                         PlayingCard.TWO_OF_DIAMONDS,
                                         PlayingCard.TWO_OF_HEARTS,
                                         PlayingCard.TWO_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.FULL_HOUSE : "Wrong hand ranking: " + hand.getRanking();
        // Trips are more important than the pair, even though they are lower.
        assert hand.getCards().get(0).getValue() == FaceValue.TWO : "Wrong order.";
        assert hand.getCards().get(1).getValue() == FaceValue.TWO : "Wrong order.";
        assert hand.getCards().get(2).getValue() == FaceValue.TWO : "Wrong order.";
        assert hand.getCards().get(3).getValue() == FaceValue.KING : "Wrong order.";
        assert hand.getCards().get(4).getValue() == FaceValue.KING : "Wrong order.";
    }


    @Test
    public void testFlush()
    {
        List<PlayingCard> cards = asList(PlayingCard.FOUR_OF_CLUBS,
                                         PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.EIGHT_OF_CLUBS,
                                         PlayingCard.NINE_OF_CLUBS,
                                         PlayingCard.QUEEN_OF_CLUBS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.FLUSH : "Wrong hand ranking: " + hand.getRanking();
        // The queen is the most significant card, the four the least.
        assert hand.getCards().get(0) == PlayingCard.QUEEN_OF_CLUBS : "Wrong order.";
        assert hand.getCards().get(1) == PlayingCard.NINE_OF_CLUBS : "Wrong order.";
        assert hand.getCards().get(2) == PlayingCard.EIGHT_OF_CLUBS : "Wrong order.";
        assert hand.getCards().get(3) == PlayingCard.SIX_OF_CLUBS : "Wrong order.";
        assert hand.getCards().get(4) == PlayingCard.FOUR_OF_CLUBS : "Wrong order.";
    }


    /**
     * If the hand is incomplete (has less than 5 cards), it can't be a flush yet.
     */
    @Test
    public void testFlushDraw()
    {
        List<PlayingCard> cards = asList(PlayingCard.FOUR_OF_CLUBS,
                                         PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.EIGHT_OF_CLUBS,
                                         PlayingCard.NINE_OF_CLUBS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.HIGH_CARD : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testStraight()
    {
        List<PlayingCard> cards = asList(PlayingCard.SEVEN_OF_DIAMONDS,
                                         PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.NINE_OF_HEARTS,
                                         PlayingCard.TEN_OF_HEARTS,
                                         PlayingCard.EIGHT_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.STRAIGHT : "Wrong hand ranking: " + hand.getRanking();
        // The ten is the most significant card, the six the least.
        assert hand.getCards().get(0) == PlayingCard.TEN_OF_HEARTS : "Wrong order.";
        assert hand.getCards().get(1) == PlayingCard.NINE_OF_HEARTS : "Wrong order.";
        assert hand.getCards().get(2) == PlayingCard.EIGHT_OF_SPADES : "Wrong order.";
        assert hand.getCards().get(3) == PlayingCard.SEVEN_OF_DIAMONDS : "Wrong order.";
        assert hand.getCards().get(4) == PlayingCard.SIX_OF_CLUBS : "Wrong order.";
    }


    /**
     * If the hand is incomplete (has less than 5 cards), it can't be a straight yet.
     */
    @Test
    public void testOpenEndedStraightDraw()
    {
        List<PlayingCard> cards = asList(PlayingCard.SEVEN_OF_DIAMONDS,
                                         PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.NINE_OF_HEARTS,
                                         PlayingCard.EIGHT_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.HIGH_CARD : "Wrong hand ranking: " + hand.getRanking();
    }


    /**
     * If the hand is incomplete (has less than 5 cards), it can't be a straight yet.
     */
    @Test
    public void testGutshotStraightDraw()
    {
        List<PlayingCard> cards = asList(PlayingCard.SEVEN_OF_DIAMONDS,
                                         PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.NINE_OF_HEARTS,
                                         PlayingCard.TEN_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.HIGH_CARD : "Wrong hand ranking: " + hand.getRanking();
    }


    /**
     * Ace can be low to make a straight (A, 2, 3, 4, 5).
     */
    @Test
    public void testLowStraight()
    {
        List<PlayingCard> cards = asList(PlayingCard.ACE_OF_DIAMONDS,
                                         PlayingCard.TWO_OF_CLUBS,
                                         PlayingCard.THREE_OF_HEARTS,
                                         PlayingCard.FOUR_OF_HEARTS,
                                         PlayingCard.FIVE_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.STRAIGHT : "Wrong hand ranking: " + hand.getRanking();
        // The five is the most significant card, the ace the least.
        assert hand.getCards().get(0) == PlayingCard.FIVE_OF_SPADES : "Wrong order.";
        assert hand.getCards().get(1) == PlayingCard.FOUR_OF_HEARTS : "Wrong order.";
        assert hand.getCards().get(2) == PlayingCard.THREE_OF_HEARTS : "Wrong order.";
        assert hand.getCards().get(3) == PlayingCard.TWO_OF_CLUBS : "Wrong order.";
        assert hand.getCards().get(4) == PlayingCard.ACE_OF_DIAMONDS : "Wrong order.";
    }


    /**
     * Ace can be high or low in a straight, but the straight cannot wrap-around
     * (i.e. the ace cannot be in the middle of the straight such as Q, K, A, 2, 3).
     */
    @Test
    public void testWrapAround()
    {
        List<PlayingCard> cards = asList(PlayingCard.QUEEN_OF_DIAMONDS,
                                         PlayingCard.KING_OF_SPADES,
                                         PlayingCard.ACE_OF_DIAMONDS,
                                         PlayingCard.TWO_OF_CLUBS,
                                         PlayingCard.THREE_OF_HEARTS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.HIGH_CARD : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testThreeOfAKind()
    {
        List<PlayingCard> cards = asList(PlayingCard.TWO_OF_CLUBS,
                                         PlayingCard.KING_OF_SPADES,
                                         PlayingCard.TWO_OF_DIAMONDS,
                                         PlayingCard.TWO_OF_HEARTS,
                                         PlayingCard.THREE_OF_HEARTS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.THREE_OF_A_KIND : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testTwoPair()
    {
        List<PlayingCard> cards = asList(PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.KING_OF_SPADES,
                                         PlayingCard.SIX_OF_HEARTS,
                                         PlayingCard.TWO_OF_HEARTS,
                                         PlayingCard.KING_OF_DIAMONDS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.TWO_PAIR : "Wrong hand ranking: " + hand.getRanking();        
    }


    @Test
    public void testPair()
    {
        List<PlayingCard> cards = asList(PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.THREE_OF_DIAMONDS,
                                         PlayingCard.SEVEN_OF_CLUBS,
                                         PlayingCard.THREE_OF_SPADES,
                                         PlayingCard.KING_OF_DIAMONDS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.PAIR : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testHighCard()
    {
        List<PlayingCard> cards = asList(PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.THREE_OF_DIAMONDS,
                                         PlayingCard.SEVEN_OF_CLUBS,
                                         PlayingCard.QUEEN_OF_CLUBS,
                                         PlayingCard.KING_OF_DIAMONDS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.HIGH_CARD : "Wrong hand ranking: " + hand.getRanking();
        assert hand.getCards().get(0) == PlayingCard.KING_OF_DIAMONDS : "Wrong high card.";
    }


    /**
     * Ace must be considered high in most circumstances, particularly when there is no
     * pair or better.
     */
    @Test
    public void testAceHigh()
    {
        List<PlayingCard> cards = asList(PlayingCard.SIX_OF_CLUBS,
                                         PlayingCard.THREE_OF_DIAMONDS,
                                         PlayingCard.ACE_OF_CLUBS,
                                         PlayingCard.QUEEN_OF_CLUBS,
                                         PlayingCard.KING_OF_DIAMONDS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.HIGH_CARD : "Wrong hand ranking: " + hand.getRanking();
        assert hand.getCards().get(0) == PlayingCard.ACE_OF_CLUBS : "Wrong high card.";
    }


    /**
     * Like {@link java.util.Arrays#asList} except that the returned list is not fixed-size.
     */
    private static <T> List<T> asList(T... elements)
    {
        List<T> list = new ArrayList<T>(elements.length);
        for (T element : elements)
        {
            list.add(element);
        }
        return list;
    }
}
