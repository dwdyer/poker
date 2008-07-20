package org.uncommons.poker.game.cards;

import org.testng.annotations.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import java.util.Set;
import java.util.HashSet;

/**
 * Unit test for the {@link Deck} class.
 * @author Daniel Dyer
 */
public class DeckTest
{
    /**
     * Make sure that new decks have the right number of cards and contain
     * no duplicates.
     */
    @Test
    public void testCreateDeck()
    {
        Deck deck = Deck.createFullDeck(new MersenneTwisterRNG());
        int size = deck.getRemainingCardCount();
        assert size == 52 : "Full deck should have 52 cards, has " + size;
        // Add each card in the deck to a HashSet to eliminate duplicates.
        Set<PlayingCard> cards = new HashSet<PlayingCard>();
        for (int i = 0; i < size; i++)
        {
            cards.add(deck.dealCard());
            assert deck.getRemainingCardCount() == size - (i + 1) : "Wrong deck size after deal.";
        }
        assert cards.size() == 52 : "Deck should have 52 unique cards, has only " + cards.size();
    }
}
