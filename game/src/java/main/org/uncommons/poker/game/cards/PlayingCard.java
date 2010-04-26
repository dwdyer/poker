package org.uncommons.poker.game.cards;

/**
 * @author Daniel Dyer
 */
public enum PlayingCard
{
    TWO_OF_CLUBS(Suit.CLUBS, FaceValue.TWO),
    TWO_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.TWO),
    TWO_OF_HEARTS(Suit.HEARTS, FaceValue.TWO),
    TWO_OF_SPADES(Suit.SPADES, FaceValue.TWO),

    THREE_OF_CLUBS(Suit.CLUBS, FaceValue.THREE),
    THREE_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.THREE),
    THREE_OF_HEARTS(Suit.HEARTS, FaceValue.THREE),
    THREE_OF_SPADES(Suit.SPADES, FaceValue.THREE),

    FOUR_OF_CLUBS(Suit.CLUBS, FaceValue.FOUR),
    FOUR_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.FOUR),
    FOUR_OF_HEARTS(Suit.HEARTS, FaceValue.FOUR),
    FOUR_OF_SPADES(Suit.SPADES, FaceValue.FOUR),

    FIVE_OF_CLUBS(Suit.CLUBS, FaceValue.FIVE),
    FIVE_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.FIVE),
    FIVE_OF_HEARTS(Suit.HEARTS, FaceValue.FIVE),
    FIVE_OF_SPADES(Suit.SPADES, FaceValue.FIVE),

    SIX_OF_CLUBS(Suit.CLUBS, FaceValue.SIX),
    SIX_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.SIX),
    SIX_OF_HEARTS(Suit.HEARTS, FaceValue.SIX),
    SIX_OF_SPADES(Suit.SPADES, FaceValue.SIX),

    SEVEN_OF_CLUBS(Suit.CLUBS, FaceValue.SEVEN),
    SEVEN_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.SEVEN),
    SEVEN_OF_HEARTS(Suit.HEARTS, FaceValue.SEVEN),
    SEVEN_OF_SPADES(Suit.SPADES, FaceValue.SEVEN),

    EIGHT_OF_CLUBS(Suit.CLUBS, FaceValue.EIGHT),
    EIGHT_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.EIGHT),
    EIGHT_OF_HEARTS(Suit.HEARTS, FaceValue.EIGHT),
    EIGHT_OF_SPADES(Suit.SPADES, FaceValue.EIGHT),

    NINE_OF_CLUBS(Suit.CLUBS, FaceValue.NINE),
    NINE_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.NINE),
    NINE_OF_HEARTS(Suit.HEARTS, FaceValue.NINE),
    NINE_OF_SPADES(Suit.SPADES, FaceValue.NINE),

    TEN_OF_CLUBS(Suit.CLUBS, FaceValue.TEN),
    TEN_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.TEN),
    TEN_OF_HEARTS(Suit.HEARTS, FaceValue.TEN),
    TEN_OF_SPADES(Suit.SPADES, FaceValue.TEN),

    JACK_OF_CLUBS(Suit.CLUBS, FaceValue.JACK),
    JACK_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.JACK),
    JACK_OF_HEARTS(Suit.HEARTS, FaceValue.JACK),
    JACK_OF_SPADES(Suit.SPADES, FaceValue.JACK),

    QUEEN_OF_CLUBS(Suit.CLUBS, FaceValue.QUEEN),
    QUEEN_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.QUEEN),
    QUEEN_OF_HEARTS(Suit.HEARTS, FaceValue.QUEEN),
    QUEEN_OF_SPADES(Suit.SPADES, FaceValue.QUEEN),

    KING_OF_CLUBS(Suit.CLUBS, FaceValue.KING),
    KING_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.KING),
    KING_OF_HEARTS(Suit.HEARTS, FaceValue.KING),
    KING_OF_SPADES(Suit.SPADES, FaceValue.KING),

    ACE_OF_CLUBS(Suit.CLUBS, FaceValue.ACE),
    ACE_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.ACE),
    ACE_OF_HEARTS(Suit.HEARTS, FaceValue.ACE),                                                
    ACE_OF_SPADES(Suit.SPADES, FaceValue.ACE);

    private final Suit suit;
    private final FaceValue value;

    private final String string;

    PlayingCard(Suit suit, FaceValue value)
    {
        this.suit = suit;
        this.value = value;
        this.string = String.valueOf(value.getSymbol()) + suit.getSymbol();
    }


    public Suit getSuit()
    {
        return suit;
    }

    public FaceValue getValue()
    {
        return value;
    }


    public static PlayingCard[] reverseValues()
    {
        PlayingCard[] values = values();
        for (int i = 0; i < values.length / 2; i++)
        {
            PlayingCard temp = values[i];
            values[i] = values[values.length - i - 1];
            values[values.length - i - 1] = temp;
        }
        return values;
    }


    @Override
    public String toString()
    {
        return string;
    }
}
