package org.uncommons.poker.game.cards;

/**
 * @author Daniel Dyer
 */
public final class DealtCard
{
    private final PlayingCard card;
    private boolean faceUp;


    public DealtCard(PlayingCard card, boolean faceUp)
    {
        this.card = card;
        this.faceUp = faceUp;
    }


    public PlayingCard getCard()
    {
        return card;
    }


    public boolean isFaceUp()
    {
        return faceUp;
    }


    public void setFaceUp(boolean faceUp)
    {
        this.faceUp = faceUp;
    }
}
