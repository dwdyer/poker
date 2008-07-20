package org.uncommons.poker.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.user.User;

/**
 * @author Daniel Dyer
 */
public class Hand
{
    private final SortedSet<Seat> activePlayers = new TreeSet<Seat>();
    private final Map<User, Seat> playerSeats = new HashMap<User, Seat>(10);
    private final Stack<Pot> pots = new Stack<Pot>();
    private final List<PlayingCard> communityCards = new ArrayList<PlayingCard>(5);
    // Each player's face-down cards.
    private final Map<Seat, List<PlayingCard>> holeCards = new HashMap<Seat, List<PlayingCard>>(10);
    // Each player's face-up cards.
    private final Map<Seat, List<PlayingCard>> boardCards = new HashMap<Seat, List<PlayingCard>>(10);
    private final Map<Seat, Long> currentBets = new HashMap<Seat, Long>(10);

    public void fold(User player)
    {
        Seat seat = playerSeats.get(player);
        activePlayers.remove(seat);
        holeCards.remove(seat);
        boardCards.remove(seat);
    }
}
