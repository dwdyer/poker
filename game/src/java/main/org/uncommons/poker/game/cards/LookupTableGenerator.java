package org.uncommons.poker.game.cards;

import java.util.List;
import org.uncommons.maths.combinatorics.CombinationGenerator;

/**
 * @author Daniel Dyer
 */
class LookupTableGenerator
{
    private static final int FIVE_CARD_COMBINATIONS = 2598960;
    private static final int SEVEN_CARD_COMBINATIONS = 133784560;

    private static final HandEvaluator FIVE_CARD_EVALUATOR = new FiveCardHandEvaluator();
    private static final HandEvaluator SEVEN_CARD_EVALUATOR = new SevenCardHandEvaluator();

    
    public RankedHand[] generateFiveCardLookupTable()
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
     */
    public int[] generateSevenCardMappings()
    {
        final int[] lookupTable = new int[SEVEN_CARD_COMBINATIONS];

        System.out.println("Generating 7-card to 5-card mappings, this may take a few minutes...");
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
                System.out.println(count * 100 / lookupTable.length + "% complete");
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("7-card look-up table generated in " + (elapsed / 1000) + " seconds.");

        return lookupTable;
    }

}
