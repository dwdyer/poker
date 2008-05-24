package org.uncommons.poker.game;

import java.util.List;

/**
 * @author Daniel Dyer
 */
public interface HandRanking
{
    boolean matches(List<PlayingCard> hand);
}
