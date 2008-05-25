package org.uncommons.poker.game.cards;

import java.util.Arrays;
import java.util.List;
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
        List<PlayingCard> cards = Arrays.asList(PlayingCard.TEN_OF_SPADES,
                                                PlayingCard.JACK_OF_SPADES,
                                                PlayingCard.QUEEN_OF_SPADES,
                                                PlayingCard.KING_OF_SPADES,
                                                PlayingCard.ACE_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.ROYAL_FLUSH : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testStraightFlush()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.FOUR_OF_HEARTS,
                                                PlayingCard.FIVE_OF_HEARTS,
                                                PlayingCard.SIX_OF_HEARTS,
                                                PlayingCard.SEVEN_OF_HEARTS,
                                                PlayingCard.EIGHT_OF_HEARTS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.STRAIGHT_FLUSH : "Wrong hand ranking: " + hand.getRanking();
    }


    /**
     * Ace can be low to make a straight flush (A, 2, 3, 4, 5).
     */
    @Test
    public void testLowStraightFlush()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.ACE_OF_DIAMONDS,
                                                PlayingCard.TWO_OF_DIAMONDS,
                                                PlayingCard.THREE_OF_DIAMONDS,
                                                PlayingCard.FOUR_OF_DIAMONDS,
                                                PlayingCard.FIVE_OF_DIAMONDS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.STRAIGHT_FLUSH : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testFourOfAKind()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.KING_OF_CLUBS,
                                                PlayingCard.TWO_OF_DIAMONDS,
                                                PlayingCard.KING_OF_DIAMONDS,
                                                PlayingCard.KING_OF_HEARTS,
                                                PlayingCard.KING_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.FOUR_OF_A_KIND : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testFullHouse()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.KING_OF_CLUBS,
                                                PlayingCard.KING_OF_SPADES,
                                                PlayingCard.TWO_OF_DIAMONDS,
                                                PlayingCard.TWO_OF_HEARTS,
                                                PlayingCard.TWO_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.FULL_HOUSE : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testFlush()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.FOUR_OF_CLUBS,
                                                PlayingCard.SIX_OF_CLUBS,
                                                PlayingCard.EIGHT_OF_CLUBS,
                                                PlayingCard.NINE_OF_CLUBS,
                                                PlayingCard.QUEEN_OF_CLUBS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.FLUSH : "Wrong hand ranking: " + hand.getRanking();
    }


    /**
     * If the hand is incomplete (has less than 5 cards), it can't be a flush yet.
     */
    @Test
    public void testFlushDraw()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.FOUR_OF_CLUBS,
                                                PlayingCard.SIX_OF_CLUBS,
                                                PlayingCard.EIGHT_OF_CLUBS,
                                                PlayingCard.NINE_OF_CLUBS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.HIGH_CARD : "Wrong hand ranking: " + hand.getRanking();
    }


    @Test
    public void testStraight()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.SEVEN_OF_DIAMONDS,
                                                PlayingCard.SIX_OF_CLUBS,
                                                PlayingCard.NINE_OF_HEARTS,
                                                PlayingCard.TEN_OF_HEARTS,
                                                PlayingCard.EIGHT_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.STRAIGHT : "Wrong hand ranking: " + hand.getRanking();
    }


    /**
     * If the hand is incomplete (has less than 5 cards), it can't be a straight yet.
     */
    @Test
    public void testOpenEndedStraightDraw()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.SEVEN_OF_DIAMONDS,
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
        List<PlayingCard> cards = Arrays.asList(PlayingCard.SEVEN_OF_DIAMONDS,
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
        List<PlayingCard> cards = Arrays.asList(PlayingCard.ACE_OF_DIAMONDS,
                                                PlayingCard.TWO_OF_CLUBS,
                                                PlayingCard.THREE_OF_HEARTS,
                                                PlayingCard.FOUR_OF_HEARTS,
                                                PlayingCard.FIVE_OF_SPADES);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.STRAIGHT : "Wrong hand ranking: " + hand.getRanking();
    }


    /**
     * Ace can be high or low in a straight, but the straight cannot wrap-around
     * (i.e. the ace cannot be in the middle of the straight such as Q, K, A, 2, 3).
     */
    @Test
    public void testWrapAround()
    {
        List<PlayingCard> cards = Arrays.asList(PlayingCard.QUEEN_OF_DIAMONDS,
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
        List<PlayingCard> cards = Arrays.asList(PlayingCard.TWO_OF_CLUBS,
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
        List<PlayingCard> cards = Arrays.asList(PlayingCard.SIX_OF_CLUBS,
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
        List<PlayingCard> cards = Arrays.asList(PlayingCard.SIX_OF_CLUBS,
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
        List<PlayingCard> cards = Arrays.asList(PlayingCard.SIX_OF_CLUBS,
                                                PlayingCard.THREE_OF_DIAMONDS,
                                                PlayingCard.SEVEN_OF_CLUBS,
                                                PlayingCard.QUEEN_OF_CLUBS,
                                                PlayingCard.KING_OF_DIAMONDS);
        RankedHand hand = handEvaluator.evaluate(cards);
        assert hand.getRanking() == HandRanking.HIGH_CARD : "Wrong hand ranking: " + hand.getRanking();        
    }
}
