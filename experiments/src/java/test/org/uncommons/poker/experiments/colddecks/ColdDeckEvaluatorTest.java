package org.uncommons.poker.experiments.colddecks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.SevenCardHandEvaluator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

/**
 * @author Daniel Dyer
 */
public class ColdDeckEvaluatorTest
{
    /**
     * Sanity-check using the 2-player deck from http://www.benjoffe.com/holdem.
     */
    @Test
    public void testKnownColdDeck()
    {
        List<PlayingCard> deck = Arrays.asList(PlayingCard.TEN_OF_DIAMONDS,
                                               PlayingCard.SEVEN_OF_HEARTS,
                                               PlayingCard.ACE_OF_DIAMONDS,
                                               PlayingCard.SIX_OF_DIAMONDS,
                                               PlayingCard.ACE_OF_CLUBS,
                                               PlayingCard.FOUR_OF_SPADES,
                                               PlayingCard.JACK_OF_HEARTS,
                                               PlayingCard.KING_OF_SPADES,
                                               PlayingCard.ACE_OF_HEARTS,
                                               PlayingCard.THREE_OF_HEARTS,
                                               PlayingCard.KING_OF_DIAMONDS,
                                               PlayingCard.SEVEN_OF_DIAMONDS,
                                               PlayingCard.SIX_OF_HEARTS,
                                               PlayingCard.FIVE_OF_HEARTS,
                                               PlayingCard.KING_OF_HEARTS,
                                               PlayingCard.THREE_OF_DIAMONDS,
                                               PlayingCard.QUEEN_OF_HEARTS,
                                               PlayingCard.THREE_OF_CLUBS,
                                               PlayingCard.TWO_OF_HEARTS,
                                               PlayingCard.QUEEN_OF_SPADES,
                                               PlayingCard.JACK_OF_CLUBS,
                                               PlayingCard.SEVEN_OF_SPADES,
                                               PlayingCard.JACK_OF_DIAMONDS,
                                               PlayingCard.THREE_OF_SPADES,
                                               PlayingCard.SIX_OF_SPADES,
                                               PlayingCard.SIX_OF_CLUBS,
                                               PlayingCard.SEVEN_OF_CLUBS,
                                               PlayingCard.FOUR_OF_HEARTS,
                                               PlayingCard.TWO_OF_SPADES,
                                               PlayingCard.FIVE_OF_SPADES,
                                               PlayingCard.JACK_OF_SPADES,
                                               PlayingCard.QUEEN_OF_CLUBS,
                                               PlayingCard.TWO_OF_DIAMONDS,
                                               PlayingCard.ACE_OF_SPADES,
                                               PlayingCard.TEN_OF_SPADES,
                                               PlayingCard.FOUR_OF_CLUBS,
                                               PlayingCard.TWO_OF_CLUBS,
                                               PlayingCard.KING_OF_CLUBS,
                                               PlayingCard.NINE_OF_HEARTS,
                                               PlayingCard.EIGHT_OF_HEARTS,
                                               PlayingCard.NINE_OF_SPADES,
                                               PlayingCard.EIGHT_OF_CLUBS,
                                               PlayingCard.TEN_OF_CLUBS,
                                               PlayingCard.EIGHT_OF_DIAMONDS,
                                               PlayingCard.NINE_OF_CLUBS,
                                               PlayingCard.EIGHT_OF_SPADES,
                                               PlayingCard.NINE_OF_DIAMONDS,
                                               PlayingCard.FOUR_OF_DIAMONDS,
                                               PlayingCard.QUEEN_OF_DIAMONDS,
                                               PlayingCard.FIVE_OF_DIAMONDS,
                                               PlayingCard.TEN_OF_HEARTS,
                                               PlayingCard.FIVE_OF_CLUBS);
        FitnessEvaluator<List<PlayingCard>> evaluator = new ColdDeckEvaluator(new SevenCardHandEvaluator(), 2);
        double fitness = evaluator.getFitness(deck, Collections.<List<PlayingCard>>emptyList());
        assert fitness == 0 : "Wrong fitness: " + fitness;
    }

}
