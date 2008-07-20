package org.uncommons.poker.game.cards;

/**
 * @author Daniel Dyer
 */
public enum FaceValue
{
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    private final char symbol;

    private FaceValue(char symbol)
    {
        this.symbol = symbol;
    }


    public char getSymbol()
    {
        return symbol;
    }
}
