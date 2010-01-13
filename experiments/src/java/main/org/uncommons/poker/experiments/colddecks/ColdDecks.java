package org.uncommons.poker.experiments.colddecks;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.random.AESCounterRNG;
import org.uncommons.maths.random.DiscreteUniformGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.poker.game.cards.PlayingCard;
import org.uncommons.poker.game.cards.SevenCardHandEvaluator;
import org.uncommons.watchmaker.framework.CachingFitnessEvaluator;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.factories.ListPermutationFactory;
import org.uncommons.watchmaker.framework.islands.IslandEvolution;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;
import org.uncommons.watchmaker.framework.islands.Migration;
import org.uncommons.watchmaker.framework.islands.RingMigration;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.ListInversion;
import org.uncommons.watchmaker.framework.operators.ListOrderCrossover;
import org.uncommons.watchmaker.framework.operators.ListOrderMutation;
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.termination.TargetFitness;
import org.uncommons.watchmaker.swing.evolutionmonitor.EvolutionMonitor;

/**
 * @author Daniel Dyer
 */
public class ColdDecks
{
    public static void main(String[] args) throws GeneralSecurityException
    {
        int playerCount = Integer.parseInt(args[0]);
        int populationSize = Integer.parseInt(args[1]);
        int eliteCount = Integer.parseInt(args[2]);
        int islandCount = Integer.parseInt(args[3]);
        int epochLength = Integer.parseInt(args[4]);
        int migrantCount = Integer.parseInt(args[5]);
        
        Random rng = new AESCounterRNG();
        CandidateFactory<List<PlayingCard>> factory = new ListPermutationFactory<PlayingCard>(Arrays.asList(PlayingCard.values()));
        FitnessEvaluator<List<PlayingCard>> fitnessEvaluator = new ColdDeckEvaluator(new SevenCardHandEvaluator(), playerCount);
        List<EvolutionaryOperator<List<PlayingCard>>> operators = new ArrayList<EvolutionaryOperator<List<PlayingCard>>>(2);
        //operators.add(new Replacement<List<PlayingCard>>(factory, new Probability(0.005)));
        operators.add(new ListOrderCrossover<PlayingCard>(new Probability(0.7)));
        operators.add(new ListInversion<PlayingCard>(new Probability(0.05)));
        operators.add(new ListOrderMutation<PlayingCard>(new ConstantGenerator<Integer>(1),
                                                         new DiscreteUniformGenerator(1, 51, rng)));
        EvolutionaryOperator<List<PlayingCard>> pipeline = new EvolutionPipeline<List<PlayingCard>>(operators);

        Migration migration = new RingMigration();
        IslandEvolution<List<PlayingCard>> engine = new IslandEvolution<List<PlayingCard>>(islandCount,
                                                                                           migration,
                                                                                           factory,
                                                                                           pipeline,
                                                                                           new CachingFitnessEvaluator<List<PlayingCard>>(fitnessEvaluator),
                                                                                           new SigmaScaling(),
                                                                                           rng);
        engine.addEvolutionObserver(new IslandEvolutionObserver<List<PlayingCard>>()
        {
            private double bestFitness = Double.POSITIVE_INFINITY;

            public void populationUpdate(PopulationData<? extends List<PlayingCard>> populationData)
            {
                if (populationData.getBestCandidateFitness() < bestFitness)
                {
                    bestFitness = populationData.getBestCandidateFitness();
                    System.out.println("Fitness: " + bestFitness
                                       + " (epoch: " + populationData.getGenerationNumber() + ")");
                }
            }


            public void islandPopulationUpdate(int i, PopulationData<? extends List<PlayingCard>> populationData)
            {
                // Do nothing.
            }
        });
        EvolutionMonitor<List<PlayingCard>> monitor = new EvolutionMonitor<List<PlayingCard>>(true);
        engine.addEvolutionObserver(monitor);
        monitor.showInFrame("Cold Decks", true);
        List<PlayingCard> deck = engine.evolve(populationSize, eliteCount, epochLength, migrantCount, new TargetFitness(0, false));
        for (PlayingCard card : deck)
        {
            System.out.print(card.toString() + ",");
        }
        System.out.println();
    }
}
