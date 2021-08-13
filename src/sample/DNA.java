package sample;

import java.util.Random;

public class DNA {
    public static final int DNA_LENGTH = 50;
    public static final double MUTATION_CHANCE = 5; // 5%
    private int[] rotation;
    private double fitness;

    private double[][] trace;

    private Random rnd;

    public int[] getRotation() {
        return rotation;
    }

    public double getFitness() {
        return fitness;
    }

    public double[][] getTrace() {
        return trace;
    }

    public DNA()
    {
        trace = new double[DNA_LENGTH][2];
        rotation = new int[DNA_LENGTH];
        rnd = new Random();
        generateRandomDNA();
    }

    public void generateRandomDNA()
    {
        rotation[0] = 150 - rnd.nextInt(121);

        for(int i = 1; i < DNA_LENGTH; i++)
        {
            rotation[i] = rotation[i-1] + 45 - rnd.nextInt(91);
        }
    }

    public double calculateFitness()
    {
        int traceIter = 0;

        double currentX = Main.STARTING_X;
        double currentY = Main.STARTING_Y;

        boolean won = false;
        int steps = 0;

        for(int i = 0; i < DNA_LENGTH;i++)
        {
            currentX = currentX - Math.cos(Math.toRadians(rotation[i]))*Main.velocity;
            currentY = currentY - Math.sin(Math.toRadians(rotation[i]))*Main.velocity;

            if(crashed(currentX,currentY))
            {
                break;
            }
            trace[traceIter] = new double[2];
            trace[traceIter][0] = currentX;
            trace[traceIter][1] = currentY;
            traceIter++;

            if(winCheck(currentX, currentY))
            {
                won = true;
                steps = i;

                try{
                    trace[traceIter] = null;
                }
                catch (Exception e)
                {
                    break;
                }
                break;
            }
        }

        double distance = Math.sqrt(
                        (currentX-Main.TARGET_X)*(currentX-Main.TARGET_X) +
                        (currentY-Main.TARGET_Y)*(currentY-Main.TARGET_Y));

        if(won)
        {
            if(steps > 40) fitness = 10;
            else if(steps > 30) fitness = 20;
            else if(steps > 25) fitness = 50;
            else if(steps > 22) fitness = 100;
            else if(steps > 20) fitness = 1000;
            else fitness = 5000;
        }
        else fitness = 1000/distance/2;

        return fitness;
    }

    private boolean winCheck(double x, double y)
    {
        return Math.sqrt(
                (x-Main.TARGET_X)*(x-Main.TARGET_X) +
                        (y-Main.TARGET_Y)*(y-Main.TARGET_Y))< 10;
    }

    private boolean crashed(double x, double y)
    {
        // Out of the screen
        if(!(x >= 0 && y >= 0 && x <= Main.SCENE_WIDTH && y <= Main.SCENE_HEIGHT)) return true;

        // Obstacle
        if(Math.abs(y - 350) < 20 && x >= 190 && x <= 610) return true;

        if(Math.abs(x - 300) < 20 && y >= 0 && y <= 250) return true;

        if(Math.abs(x - 450) < 20 && y >= 0 && y <= 250) return true;

        return false;
    }

    public DNA mutate()
    {
        int randomInt;
        for(int i = 0; i < DNA_LENGTH; i++)
        {
            randomInt = rnd.nextInt(100) + 1;
            if(randomInt <= MUTATION_CHANCE)
            {
                rotation[i] = rotation[i] + 60 - rnd.nextInt(121);
            }
        }

        return this;
    }

    public void copy(DNA other)
    {
        this.fitness = other.fitness;

        System.arraycopy(other.rotation, 0, this.rotation, 0, DNA_LENGTH);
    }
}
