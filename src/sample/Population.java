package sample;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    public final static int POPULATION_SIZE = 200;
    private DNA[] rockets;

    private Random rnd;

    private double fitnessSum;
    private double fitnessMax;
    private int fitnessMaxIndex;

    public double getFitnessMax() {
        return fitnessMax;
    }

    public double getFitnessSum() {
        return fitnessSum;
    }

    public int getFitnessMaxIndex() {
        return fitnessMaxIndex;
    }

    public DNA[] getDNA()
    {
        return rockets;
    }

    public Population()
    {
        rnd = new Random();

        rockets = new DNA[POPULATION_SIZE];
        for(int i = 0; i < POPULATION_SIZE; i++)
        {
            rockets[i] = new DNA();
        }
    }

    public void breed()
    {
        getFitnessStat();
        // Populating mating pool
        ArrayList<DNA> matingPool = new ArrayList<>();
        for(int i = 0; i < POPULATION_SIZE; i++)
        {
            for(int j = 0; j < rockets[i].getFitness(); j++)
            {
                matingPool.add(rockets[i]);
            }
        }

        // Creating the new population
        DNA[] newRockets = new DNA[POPULATION_SIZE];
        int randomInt;
        for(int i = 0; i < POPULATION_SIZE; i++)
        {
            newRockets[i] = new DNA();
            randomInt = rnd.nextInt(matingPool.size());
            newRockets[i].copy(matingPool.get(randomInt));
            newRockets[i].mutate();
        }

        System.arraycopy(newRockets, 0, rockets, 0, POPULATION_SIZE);
    }

    public void getFitnessStat()
    {
        double currentMax = -1;
        double rocketFitness;
        double currentSum = 0;

        for(int i = 0; i < POPULATION_SIZE; i++)
        {
            rocketFitness = rockets[i].calculateFitness();
            currentSum += rocketFitness;

            if(rocketFitness > currentMax)
            {
                currentMax = rocketFitness;
                fitnessMaxIndex = i;
            }
        }
        fitnessMax = currentMax;
        fitnessSum = currentSum;
    }
}
