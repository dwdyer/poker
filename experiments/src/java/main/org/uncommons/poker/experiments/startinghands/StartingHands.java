package org.uncommons.poker.experiments.startinghands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.uncommons.maths.random.XORShiftRNG;
import org.uncommons.poker.game.cards.Deck;
import org.uncommons.poker.game.cards.LookupHandEvaluator;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.RankedHand;
import org.uncommons.poker.game.rules.PokerRules;
import org.uncommons.poker.game.rules.TexasHoldem;

/**
 * Statistical anaylsis of different starting hands.
 * @author Daniel Dyer
 */
public class StartingHands
{
    public static void main(String[] args)
    {
        int seats = Integer.parseInt(args[0]);
        int iterations = Integer.parseInt(args[1]);
        
        TexasHoldem rules = new TexasHoldem(new LookupHandEvaluator());
        
        long start = System.currentTimeMillis();
        StartingHands startingHands = new StartingHands();
        Map<String, StartingHandInfo> info = startingHands.simulate(seats,
                                                                    iterations,
                                                                    rules,
                                                                    new XORShiftRNG());
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Completed " + iterations + " " + seats + "-player hands in " + elapsed/1000 + " seconds.");
        tabulate(info);
    }


    public Map<String, StartingHandInfo> simulate(final int seats,
                                                  final int iterations,
                                                  final PokerRules rules,
                                                  final Random rng)
    {
        final ConcurrentMap<String, StartingHandInfo> startingHands = new ConcurrentHashMap<String, StartingHandInfo>();

        // Divide the work across available processors.
        int threadCount = Runtime.getRuntime().availableProcessors();
        final int iterationsPerThread = iterations / threadCount;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++)
        {
            executor.submit(new Runnable()
            {
                public void run()
                {
                    for (int i = 0; i < iterationsPerThread; i++)
                    {
                        playHand(seats, rules, rng, startingHands);
                    }
                    latch.countDown();
                }
            });
        }
        try
        {
            latch.await();
            executor.shutdown();
        }
        catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        return startingHands;
    }


    public static void tabulate(Map<String, StartingHandInfo> startingHands)
    {
        List<StartingHandInfo> info = new ArrayList<StartingHandInfo>(startingHands.values());
        Collections.sort(info, new Comparator<StartingHandInfo>()
        {
            public int compare(StartingHandInfo info1,
                               StartingHandInfo info2)
            {
                return Double.compare(info2.getWinRate(), info1.getWinRate());
            }
        });

        for (StartingHandInfo hand : info)
        {
            System.out.println(hand.getId() + "\t" + hand.getWinRate());
        }
    }


    private void playHand(int seats,
                          PokerRules rules,
                          Random rng,
                          ConcurrentMap<String, StartingHandInfo> startingHands)
    {
        Deck deck = Deck.createFullDeck(rng);

        // Deal the community cards (for the purposes of the experiment, it doesn't
        // matter that we do this before the hole cards).
        List<PlayingCard> communityCards = new ArrayList<PlayingCard>(5);
        for (int i = 0; i < 5; i++)
        {
            communityCards.add(deck.dealCard());
        }


        RankedHand bestHand = null;
        // Maybe more than one winning hand (split pots).
        List<StartingHandInfo> winningStartingHands = new ArrayList<StartingHandInfo>(seats);

        // Deal hole cards and determine the winning hand(s).
        for (int i = 0; i < seats; i++)
        {
            PlayingCard holeCard1 = deck.dealCard();
            PlayingCard holeCard2 = deck.dealCard();

            String startingHand = getStartingHandClassification(holeCard1, holeCard2);
            StartingHandInfo info = getStartingHandInfo(startingHand, startingHands);
            info.incrementDealt();

            RankedHand hand = rules.rankHand(Arrays.asList(holeCard1, holeCard2), communityCards);
            if (bestHand == null || hand.compareTo(bestHand) > 0)
            {
                bestHand = hand;
                winningStartingHands.clear();
                winningStartingHands.add(info);
            }
            else if (hand.compareTo(bestHand) == 0)
            {
                // Potential split pot.
                winningStartingHands.add(info);
            }
        }

        for (StartingHandInfo info : winningStartingHands)
        {
            info.incrementWon();
        }
    }

    
    private StartingHandInfo getStartingHandInfo(String startingHand,
                                                 ConcurrentMap<String, StartingHandInfo> startingHands)
    {
        StartingHandInfo info = startingHands.get(startingHand);
        if (info == null)
        {
            info = new StartingHandInfo(startingHand);
            startingHands.putIfAbsent(startingHand, info);
        }
        return info;
    }


    private String getStartingHandClassification(PlayingCard card1, PlayingCard card2)
    {
        StringBuilder buffer = new StringBuilder(3);
        // Ensure that the first card is the highest ranked.
        if (card2.compareTo(card1) > 0)
        {
            buffer.append(card2.getValue().getSymbol());
            buffer.append(card1.getValue().getSymbol());
        }
        else
        {
            buffer.append(card1.getValue().getSymbol());
            buffer.append(card2.getValue().getSymbol());
        }

        // If not a pair, is it suited or off-suit?
        if (card1.getValue() != card2.getValue())
        {
            buffer.append(card1.getSuit() == card2.getSuit() ? 's' : 'o');
        }
        
        return buffer.toString();
    }


    private static final class StartingHandInfo
    {
        private final String id;
        private final AtomicInteger dealtCount = new AtomicInteger(0);
        private final AtomicInteger wonCount = new AtomicInteger(0);

        StartingHandInfo(String id)
        {
            this.id = id;
        }

        public void incrementDealt()
        {
            dealtCount.incrementAndGet();
        }

        public void incrementWon()
        {
            wonCount.incrementAndGet();
        }

        public String getId()
        {
            return id;
        }

        public int getDealtCount()
        {
            return dealtCount.get();
        }

        public int getWonCount()
        {
            return wonCount.get();
        }

        public double getWinRate()
        {
            return ((double) wonCount.get()) / dealtCount.get();
        }
    }
}
