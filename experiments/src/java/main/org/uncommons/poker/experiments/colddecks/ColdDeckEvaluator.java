package org.uncommons.poker.experiments.colddecks;

import java.util.ArrayList;
import java.util.List;
import org.uncommons.poker.game.cards.CardUtils;
import org.uncommons.poker.game.cards.HandEvaluator;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.RankedHand;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

/**
 * @author Daniel Dyer
 */
public class ColdDeckEvaluator implements FitnessEvaluator<List<PlayingCard>>
{
    private final HandEvaluator handEvaluator;
    private final int playerCount;

    public ColdDeckEvaluator(HandEvaluator handEvaluator, int playerCount)
    {
        this.handEvaluator = handEvaluator;
        this.playerCount = playerCount;
    }


    public double getFitness(List<PlayingCard> candidate, List<? extends List<PlayingCard>> population)
    {
        int losses = 0;
        for (int i = 0; i < 52; i++)
        {
            boolean win = playHand(candidate, i);
            if (!win)
            {
                ++losses;
                if (losses >= 5 && i <= 10)
                {
                    return losses + (52 - i); // This deck is rubbish, give up.
                }
            }
        }
        return losses;
    }


    /**
     * @return True if the dealer wins, false otherwise.
     */
    private boolean playHand(List<PlayingCard> deck, int cutIndex)
    {
        int topOfDeck = cutIndex - 1;

        // Create player hands.  Last hand is the dealer's.
        List<List<PlayingCard>> players = new ArrayList<List<PlayingCard>>(playerCount);
        for (int i = 0; i < playerCount; i++)
        {
            players.add(new ArrayList<PlayingCard>(7));
        }

        // Deal hole cards.
        for (int i = 0; i < 2; i++) // 2 hole cards each.
        {
            for (List<PlayingCard> hand : players)
            {
                hand.add(deck.get(++topOfDeck % 52));
            }
        }

        // Deal community cards.
        ++topOfDeck; // Burn.
        PlayingCard flop1 = deck.get(++topOfDeck % 52);
        PlayingCard flop2 = deck.get(++topOfDeck % 52);
        PlayingCard flop3 = deck.get(++topOfDeck % 52);
        ++topOfDeck; // Burn.
        PlayingCard turn = deck.get(++topOfDeck % 52);
        ++topOfDeck; // Burn.
        PlayingCard river = deck.get(++topOfDeck % 52);

        // Community cards are used by all players, so added to each hand before evaluating.
        for (List<PlayingCard> hand : players)
        {
            hand.add(flop1);
            hand.add(flop2);
            hand.add(flop3);
            hand.add(turn);
            hand.add(river);
            CardUtils.sevenCardSort(hand);
        }

        RankedHand dealerHand = handEvaluator.evaluate(players.get(playerCount - 1));
        for (int i = 0; i < playerCount - 1; i++)
        {
            RankedHand playerHand = handEvaluator.evaluate(players.get(i));
            if (dealerHand.compareTo(playerHand) <= 0)
            {
                return false;
            }
        }                        
        return true;
    }


    public boolean isNatural()
    {
        return false;
    }
}
