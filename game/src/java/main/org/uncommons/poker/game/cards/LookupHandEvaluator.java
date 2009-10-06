package org.uncommons.poker.game.cards;

import java.util.List;
import org.uncommons.maths.combinatorics.CombinationGenerator;
import org.uncommons.util.ConsoleProgressDisplay;

/**
 * A {@link HandEvaluator} that uses tables of pre-computed ranked hands to quickly
 * evaluate 7-card hands.  This implementation generates about 550Mb of look-up tables,
 * so requires a significant amount of heap space.
 * @author Daniel Dyer
 */
public class LookupHandEvaluator implements HandEvaluator
{
    private static final int FIVE_CARD_COMBINATIONS = 2598960;
    private static final int SEVEN_CARD_COMBINATIONS = 133784560;
    private static final int FIVE_PERCENT = SEVEN_CARD_COMBINATIONS / 20;

    private static final HandEvaluator FIVE_CARD_EVALUATOR = new FiveCardHandEvaluator();
    private static final HandEvaluator SEVEN_CARD_EVALUATOR = new SevenCardHandEvaluator();
    private static final ConsoleProgressDisplay CONSOLE = new ConsoleProgressDisplay();

    private final RankedHand[] sevenCardLookupTable;

    public LookupHandEvaluator()
    {
        sevenCardLookupTable = generateSevenCardMappings();
    }


    private RankedHand[] generateFiveCardLookupTable()
    {
        CONSOLE.start("Generating 5-card hand evaluation look-up table...");

        final RankedHand[] lookupTable = new RankedHand[FIVE_CARD_COMBINATIONS];
        CombinationGenerator<PlayingCard> generator = new CombinationGenerator<PlayingCard>(PlayingCard.reverseValues(), 5);
        for (List<PlayingCard> hand : generator)
        {
            int hash = CardUtils.fiveCardHash(hand);
            RankedHand rankedHand = FIVE_CARD_EVALUATOR.evaluate(hand);
            lookupTable[hash] = rankedHand;
        }

        CONSOLE.finish(true);

        return lookupTable;
    }


    /**
     * Generate a table of mappings from seven-card hands to entries in a 5-card look-up table.
     * This will return ~134 million integers, which requires about 535Mb of memory.
     * @return An array of indices for mapping
     * {@link CardUtils#sevenCardHash(java.util.List) 7-card hashes} to an entry in the
     * {@link #generateFiveCardLookupTable() 5-card look-up table}.
     */
    private RankedHand[] generateSevenCardMappings()
    {
        // Generate all 5-card hands up front and then refer to these rather than creating
        // duplicate 5-card hands from different 7-card hands.
        RankedHand[] fiveCardLookupTable = generateFiveCardLookupTable();

        CONSOLE.start("Mapping 7-card hands to 5-card hands...");
        CONSOLE.update(0);

        RankedHand[] mappings = new RankedHand[SEVEN_CARD_COMBINATIONS];
        CombinationGenerator<PlayingCard> generator = new CombinationGenerator<PlayingCard>(PlayingCard.reverseValues(), 7);
        long count = 0;
        for (List<PlayingCard> hand : generator)
        {
            int index = CardUtils.sevenCardHash(hand);
            PlayingCard[] cards = SEVEN_CARD_EVALUATOR.evaluate(hand).getCards();
            CardUtils.fiveCardSort(cards);
            int fiveCardIndex = CardUtils.fiveCardHash(cards);
            mappings[index] = fiveCardLookupTable[fiveCardIndex];
            ++count;
            if (count % FIVE_PERCENT == 0)
            {
                CONSOLE.update((int) (count * 100 / mappings.length));
            }
        }
        CONSOLE.finish(true);

        return mappings;
    }


    /**
     * {@inheritDoc}
     * @param cards Seven cards, sorted in descending order of rank.
     * @return A ranked 5-card hand.
     */
    public RankedHand evaluate(List<PlayingCard> cards)
    {
        int sevenCardHash = CardUtils.sevenCardHash(cards);
        return sevenCardLookupTable[sevenCardHash];
    }
}
