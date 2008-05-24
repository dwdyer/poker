package org.uncommons.poker.game;

/**
 * @author Daniel Dyer
 */
public interface Action
{
    void apply(Game game, Table table, Hand hand);
}
