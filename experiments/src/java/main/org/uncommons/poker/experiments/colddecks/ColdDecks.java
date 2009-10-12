package org.uncommons.poker.experiments.colddecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.random.DiscreteUniformGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.maths.random.XORShiftRNG;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.SevenCardHandEvaluator;
import org.uncommons.watchmaker.framework.CachingFitnessEvaluator;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.ConcurrentEvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.factories.ListPermutationFactory;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.ListOrderCrossover;
import org.uncommons.watchmaker.framework.operators.ListOrderMutation;
import org.uncommons.watchmaker.framework.operators.Replacement;
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

/**
 * @author Daniel Dyer
 */
public class ColdDecks
{
    public static void main(String[] args)
    {
        int playerCount = Integer.parseInt(args[0]);
        XORShiftRNG rng = new XORShiftRNG();
        CandidateFactory<List<PlayingCard>> factory = new ListPermutationFactory<PlayingCard>(Arrays.asList(PlayingCard.values()));
        FitnessEvaluator<List<PlayingCard>> fitnessEvaluator = new ColdDeckEvaluator(new SevenCardHandEvaluator(), playerCount);
        List<EvolutionaryOperator<List<PlayingCard>>> operators = new ArrayList<EvolutionaryOperator<List<PlayingCard>>>(2);
        operators.add(new Replacement<List<PlayingCard>>(factory, new Probability(0.005)));
        operators.add(new ListOrderCrossover<PlayingCard>());
        //operators.add(new ListInversion<PlayingCard>(new Probability(0.1)));
        operators.add(new ListOrderMutation<PlayingCard>(new ConstantGenerator<Integer>(1),
                                                         new DiscreteUniformGenerator(1, 51, rng)));
        EvolutionaryOperator<List<PlayingCard>> pipeline = new EvolutionPipeline<List<PlayingCard>>(operators);

        EvolutionEngine<List<PlayingCard>> engine = new ConcurrentEvolutionEngine<List<PlayingCard>>(factory,
                                                                                                     pipeline,
                                                                                                     new CachingFitnessEvaluator<List<PlayingCard>>(fitnessEvaluator),
                                                                                                     new SigmaScaling(),
                                                                                                     rng);
        engine.addEvolutionObserver(new EvolutionObserver<List<PlayingCard>>()
        {
            private double bestFitness = Double.POSITIVE_INFINITY;

            public void populationUpdate(PopulationData<? extends List<PlayingCard>> populationData)
            {
                if (populationData.getBestCandidateFitness() < bestFitness)
                {
                    bestFitness = populationData.getBestCandidateFitness();
                    System.out.println("Fitness: " + bestFitness
                                       + " (generation: " + populationData.getGenerationNumber() + ")");
                }
            }
        });
//        EvolutionMonitor<List<PlayingCard>> monitor = new EvolutionMonitor<List<PlayingCard>>();
//        engine.addEvolutionObserver(monitor);
//        monitor.showInFrame("Cold Decks", true);
        List<PlayingCard> deck = engine.evolve(500, 10, new TargetFitness(0, false));
        for (PlayingCard card : deck)
        {
            System.out.print(card.toString() + ",");
        }
        System.out.println();
    }
}
