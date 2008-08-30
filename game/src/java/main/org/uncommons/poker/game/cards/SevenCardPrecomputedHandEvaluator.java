package org.uncommons.poker.game.cards;

import java.util.List;
import org.uncommons.maths.combinatorics.CombinationGenerator;

/**
 * A {@link HandEvaluator} that uses tables of precomputed ranked hands to quickly
 * evaluate 7-card hands.  This implementation generates about 550Mb of look-up tables,
 * so requires a significant amount of heap space.
 * @author Daniel Dyer
 */
public class SevenCardPrecomputedHandEvaluator implements HandEvaluator
{
    private static final HandEvaluator FIVE_CARD_EVALUATOR = new FiveCardHandEvaluator();
    private static final HandEvaluator SEVEN_CARD_EVALUATOR = new SevenCardHandEvaluator();

    private static final int FIVE_CARD_COMBINATIONS = 2598960;
    private static final int SEVEN_CARD_COMBINATIONS = 133784560;

    private final RankedHand[] fiveCardLookupTable;
    private final int[] sevenCardMappings;

    public SevenCardPrecomputedHandEvaluator()
    {
        fiveCardLookupTable = generateFiveCardLookupTable();
        sevenCardMappings = generateSevenCardMappings();
    }


    private RankedHand[] generateFiveCardLookupTable()
    {
        System.out.println("Generating 5-card hand evaluation look-up table...");

        final RankedHand[] lookupTable = new RankedHand[FIVE_CARD_COMBINATIONS];
        CombinationGenerator<PlayingCard> generator = new CombinationGenerator<PlayingCard>(PlayingCard.reverseValues(), 5);
        long start = System.currentTimeMillis();
        for (List<PlayingCard> hand : generator)
        {
            int hash = CardUtils.fiveCardHash(hand);
            RankedHand rankedHand = FIVE_CARD_EVALUATOR.evaluate(hand);
            lookupTable[hash] = rankedHand;
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("5-card look-up table generated in " + (elapsed / 1000) + " seconds.");

        return lookupTable;
    }


    /**
     * Generate a table of mappings from seven-card hands to entries in a 5-card look-up table.
     * This will return ~134 million integers, which requires about 535Mb of memory.
     * @return An array of indices for mapping
     * {@link CardUtils#sevenCardHash(java.util.List) 7-card hashes} to an entry in the
     * {@link #generateFiveCardLookupTable() 5-card look-up table}.
     */
    private int[] generateSevenCardMappings()
    {
        System.out.println("Generating 7-card to 5-card mappings, this may take a few minutes...");

        final int[] lookupTable = new int[SEVEN_CARD_COMBINATIONS];
        CombinationGenerator<PlayingCard> generator = new CombinationGenerator<PlayingCard>(PlayingCard.reverseValues(), 7);
        long start = System.currentTimeMillis();
        long count = 0;
        for (List<PlayingCard> hand : generator)
        {
            int index = CardUtils.sevenCardHash(hand);
            PlayingCard[] cards = SEVEN_CARD_EVALUATOR.evaluate(hand).getCards();
            CardUtils.fiveCardSort(cards);
            int fiveCardIndex = CardUtils.fiveCardHash(cards);
            lookupTable[index] = fiveCardIndex;
            ++count;
            if (count % 13378456 == 0)
            {
                System.out.println("  " + count * 100 / lookupTable.length + "% complete");
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("7-card look-up table generated in " + (elapsed / 1000) + " seconds.");

        return lookupTable;
    }


    /**
     * {@inheritDoc}
     * @param cards Seven cards, sorted in descending order of rank.
     * @return A ranked 5-card hand.
     */
    public RankedHand evaluate(List<PlayingCard> cards)
    {
        int sevenCardHash = CardUtils.sevenCardHash(cards);
        int fiveCardIndex = sevenCardMappings[sevenCardHash];
        return fiveCardLookupTable[fiveCardIndex];
    }
}
