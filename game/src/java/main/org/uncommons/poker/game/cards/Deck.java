package org.uncommons.poker.game.cards;

import java.util.Random;

/**
 * @author Daniel Dyer
 */
public class Deck
{
    private final PlayingCard[] deck;
    private int index = 0;

    private Deck(PlayingCard[] cards, Random rng)
    {
        this.deck = cards.clone();
        // Fisher-Yates shuffle.
        for (int i = deck.length; i > 1; i--)
        {
            swap(deck, i - 1, rng.nextInt(i));
        }
    }


    private <T> void swap(T[] array, int index1, int index2)
    {
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    
    public PlayingCard dealCard()
    {
        if (index >= deck.length)
        {
            throw new IllegalStateException("Deck exhausted.");
        }
        PlayingCard card = deck[index];
        ++index;
        return card;
    }


    public int getRemainingCardCount()
    {
        return deck.length - index;
    }


    /**
     * Creates a shuffled 52-card deck.
     * @param rng The RNG to use for shuffling.
     * @return A shuffled deck.
     */
    public static Deck createFullDeck(Random rng)
    {
        return new Deck(PlayingCard.values(), rng);
    }
}
