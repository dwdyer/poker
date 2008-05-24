package org.uncommons.poker.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import org.uncommons.poker.user.User;

/**
 * @author Daniel Dyer
 */
public class Hand
{
    private final SortedSet<Seat> activePlayers = new TreeSet<Seat>();
    private final Map<User, Seat> playerSeats = new HashMap<User, Seat>(10);
    private final Stack<Pot> pots = new Stack<Pot>();
    private final List<DealtCard> communityCards = new ArrayList<DealtCard>(5);
    private final Map<Seat, List<DealtCard>> playerCards = new HashMap<Seat, List<DealtCard>>(10);
    private final Map<Seat, Long> currentBets = new HashMap<Seat, Long>(10);

    public void fold(User player)
    {
        Seat seat = playerSeats.get(player);
        activePlayers.remove(seat);
        playerCards.remove(seat);
    }
}
