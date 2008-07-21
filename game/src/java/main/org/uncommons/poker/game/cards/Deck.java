package org.uncommons.poker.game.cards;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Daniel Dyer
 */
public class Deck
{
    private final List<PlayingCard> cards = new ArrayList<PlayingCard>(52);

    private Deck(PlayingCard[] cards, Random rng)
    {
        for (PlayingCard card : cards)
        {
            this.cards.add(card);
        }
        Collections.shuffle(this.cards, rng);
    }

    
    public PlayingCard dealCard()
    {
        return cards.remove(cards.size() - 1);
    }


    public int getRemainingCardCount()
    {
        return cards.size();
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
