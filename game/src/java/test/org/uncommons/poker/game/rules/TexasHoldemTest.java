package org.uncommons.poker.game.rules;

import org.testng.annotations.Test;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.RankedHand;
import org.uncommons.poker.game.cards.HandRanking;
import java.util.List;
import java.util.Arrays;

/**
 * Unit test for the {@link TexasHoldem} rules implementation.
 * @author Daniel Dyer
 */
public class TexasHoldemTest
{
    private final PokerRules holdem = new TexasHoldem();

    /**
     * Test that hand evaluation works correctly when the best hand
     * would include both hole cards.
     */
    @Test
    public void testEvaluationBothHoleCardsUsed()
    {
        List<PlayingCard> holeCards = Arrays.asList(PlayingCard.ACE_OF_CLUBS,
                                                    PlayingCard.ACE_OF_DIAMONDS);
        List<PlayingCard> communityCards = Arrays.asList(PlayingCard.TEN_OF_HEARTS,
                                                         PlayingCard.SIX_OF_DIAMONDS,
                                                         PlayingCard.SEVEN_OF_SPADES,
                                                         PlayingCard.ACE_OF_SPADES,
                                                         PlayingCard.TWO_OF_CLUBS);
        // Best hand is 3 aces.
        RankedHand hand = holdem.rankHand(holeCards, communityCards);
        assert hand.getRanking().equals(HandRanking.THREE_OF_A_KIND) : "Wrong hand ranking: " + hand.getRanking();
        assert hand.contains(PlayingCard.ACE_OF_CLUBS) : "Hand should contain Ace of Clubs.";
        assert hand.contains(PlayingCard.ACE_OF_CLUBS) : "Hand should contain Ace of Diamonds.";
        assert hand.contains(PlayingCard.ACE_OF_CLUBS) : "Hand should contain Ace of Spades.";
    }

    /**
     * Test that hand evaluation works correctly when the best hand
     * would include only one of the hole cards..
     */
    @Test
    public void testEvaluationOneHoleCardUsed()
    {
        List<PlayingCard> holeCards = Arrays.asList(PlayingCard.ACE_OF_CLUBS,
                                                    PlayingCard.KING_OF_SPADES);
        List<PlayingCard> communityCards = Arrays.asList(PlayingCard.TEN_OF_SPADES,
                                                         PlayingCard.SIX_OF_DIAMONDS,
                                                         PlayingCard.SEVEN_OF_SPADES,
                                                         PlayingCard.TWO_OF_SPADES,
                                                         PlayingCard.JACK_OF_SPADES);
        // Best hand is a flush.
        RankedHand hand = holdem.rankHand(holeCards, communityCards);
        assert hand.getRanking().equals(HandRanking.FLUSH) : "Wrong hand ranking: " + hand.getRanking();
        assert !hand.contains(PlayingCard.ACE_OF_CLUBS) : "Hand should not contain Ace of Clubs.";
        assert hand.contains(PlayingCard.KING_OF_SPADES) : "Hand should contain King of Spades.";
    }


    /**
     * Test that hand evaluation works correctly when the best hand
     * would include neither of the hole cards.
     */
    @Test
    public void testEvaluationCommunityCardsOnly()
    {
        List<PlayingCard> holeCards = Arrays.asList(PlayingCard.ACE_OF_CLUBS,
                                                    PlayingCard.ACE_OF_DIAMONDS);
        List<PlayingCard> communityCards = Arrays.asList(PlayingCard.TEN_OF_HEARTS,
                                                         PlayingCard.SIX_OF_DIAMONDS,
                                                         PlayingCard.SEVEN_OF_SPADES,
                                                         PlayingCard.NINE_OF_SPADES,
                                                         PlayingCard.EIGHT_OF_CLUBS);
        // Best hand is a straight (using the community cards).
        RankedHand hand = holdem.rankHand(holeCards, communityCards);
        assert hand.getRanking().equals(HandRanking.STRAIGHT) : "Wrong hand ranking: " + hand.getRanking();
        assert !hand.contains(PlayingCard.ACE_OF_CLUBS) : "Hand should not contain Ace of Clubs.";
        assert !hand.contains(PlayingCard.ACE_OF_DIAMONDS) : "Hand should not contain Ace of Diamonds.";
    }
}
