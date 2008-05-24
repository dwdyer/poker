package org.uncommons.poker.game;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * @author Daniel Dyer
 */
public class Deck
{
    private final Stack<PlayingCard> cards = new Stack<PlayingCard>();

    private Deck(PlayingCard[] cards)
    {
        for (PlayingCard card : cards)
        {
            this.cards.add(card);
        }
    }

    
    public void shuffle(Random rng)
    {
        Collections.shuffle(cards, rng);
    }


    public DealtCard dealCard(boolean faceUp)
    {
        return new DealtCard(cards.pop(), faceUp);
    }


    public static Deck createFullDeck()
    {
        return new Deck(PlayingCard.values());
    }
}
