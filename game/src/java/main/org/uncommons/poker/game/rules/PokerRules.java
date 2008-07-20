package org.uncommons.poker.game.rules;

import java.util.List;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.RankedHand;

/**
 * @author Daniel Dyer
 */
public interface PokerRules
{
    /**
     * Ranks a player's hand according to the rules of the game.  Different
     * poker variants have different rules about how hole cards can be
     * combined with community cards to make a 5-card hand. 
     * @param playerCards The player's hole cards (and/or face-up cards).
     * @param communityCards The community cards for use by all players.
     * @return A ranked hand.
     */
    RankedHand rankHand(List<PlayingCard> playerCards,
                        List<PlayingCard> communityCards);
}
