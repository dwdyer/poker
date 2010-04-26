package org.uncommons.poker.game.cards;

/**
 * @author Daniel Dyer
 */
public enum Suit
{
    CLUBS('c'),
    DIAMONDS('d'),
    HEARTS('h'),
    SPADES('s');

    private final char symbol;

    Suit(char symbol)
    {
        this.symbol = symbol;
    }

    public char getSymbol()
    {
        return symbol;
    }
}
