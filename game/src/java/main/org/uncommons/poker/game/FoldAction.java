package org.uncommons.poker.game;

import org.uncommons.poker.user.User;

/**
 * @author Daniel Dyer
 */
public class FoldAction implements Action
{
    private final User user;

    
    public FoldAction(User user)
    {
        this.user = user;
    }


    public void apply(Game game,
                      Table table,
                      Hand hand)
    {
        hand.fold(user);
    }
}
