package org.uncommons.poker.game;

/**
 * @author Daniel Dyer
 */
public enum PlayingCard
{
    TWO_OF_CLUBS(Suit.CLUBS, FaceValue.TWO),
    THREE_OF_CLUBS(Suit.CLUBS, FaceValue.THREE),
    FOUR_OF_CLUBS(Suit.CLUBS, FaceValue.FOUR),
    FIVE_OF_CLUBS(Suit.CLUBS, FaceValue.FIVE),
    SIX_OF_CLUBS(Suit.CLUBS, FaceValue.SIX),
    SEVEN_OF_CLUBS(Suit.CLUBS, FaceValue.SEVEN),
    EIGHT_OF_CLUBS(Suit.CLUBS, FaceValue.EIGHT),
    NINE_OF_CLUBS(Suit.CLUBS, FaceValue.NINE),
    TEN_OF_CLUBS(Suit.CLUBS, FaceValue.TEN),
    JACK_OF_CLUBS(Suit.CLUBS, FaceValue.JACK),
    QUEEN_OF_CLUBS(Suit.CLUBS, FaceValue.QUEEN),
    KING_OF_CLUBS(Suit.CLUBS, FaceValue.KING),
    ACE_OF_CLUBS(Suit.CLUBS, FaceValue.ACE),

    TWO_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.TWO),
    THREE_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.THREE),
    FOUR_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.FOUR),
    FIVE_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.FIVE),
    SIX_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.SIX),
    SEVEN_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.SEVEN),
    EIGHT_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.EIGHT),
    NINE_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.NINE),
    TEN_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.TEN),
    JACK_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.JACK),
    QUEEN_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.QUEEN),
    KING_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.KING),
    ACE_OF_DIAMONDS(Suit.DIAMONDS, FaceValue.ACE),

    TWO_OF_HEARTS(Suit.HEARTS, FaceValue.TWO),
    THREE_OF_HEARTS(Suit.HEARTS, FaceValue.THREE),
    FOUR_OF_HEARTS(Suit.HEARTS, FaceValue.FOUR),
    FIVE_OF_HEARTS(Suit.HEARTS, FaceValue.FIVE),
    SIX_OF_HEARTS(Suit.HEARTS, FaceValue.SIX),
    SEVEN_OF_HEARTS(Suit.HEARTS, FaceValue.SEVEN),
    EIGHT_OF_HEARTS(Suit.HEARTS, FaceValue.EIGHT),
    NINE_OF_HEARTS(Suit.HEARTS, FaceValue.NINE),
    TEN_OF_HEARTS(Suit.HEARTS, FaceValue.TEN),
    JACK_OF_HEARTS(Suit.HEARTS, FaceValue.JACK),
    QUEEN_OF_HEARTS(Suit.HEARTS, FaceValue.QUEEN),
    KING_OF_HEARTS(Suit.HEARTS, FaceValue.KING),
    ACE_OF_HEARTS(Suit.HEARTS, FaceValue.ACE),

    TWO_OF_SPADES(Suit.SPADES, FaceValue.TWO),
    THREE_OF_SPADES(Suit.SPADES, FaceValue.THREE),
    FOUR_OF_SPADES(Suit.SPADES, FaceValue.FOUR),
    FIVE_OF_SPADES(Suit.SPADES, FaceValue.FIVE),
    SIX_OF_SPADES(Suit.SPADES, FaceValue.SIX),
    SEVEN_OF_SPADES(Suit.SPADES, FaceValue.SEVEN),
    EIGHT_OF_SPADES(Suit.SPADES, FaceValue.EIGHT),
    NINE_OF_SPADES(Suit.SPADES, FaceValue.NINE),
    TEN_OF_SPADES(Suit.SPADES, FaceValue.TEN),
    JACK_OF_SPADES(Suit.SPADES, FaceValue.JACK),
    QUEEN_OF_SPADES(Suit.SPADES, FaceValue.QUEEN),
    KING_OF_SPADES(Suit.SPADES, FaceValue.KING),
    ACE_OF_SPADES(Suit.SPADES, FaceValue.ACE);

    private final Suit suit;
    private final FaceValue value;

    private PlayingCard(Suit suit, FaceValue value)
    {
        this.suit = suit;
        this.value = value;
    }


    public Suit getSuit()
    {
        return suit;
    }

    public FaceValue getValue()
    {
        return value;
    }
}
