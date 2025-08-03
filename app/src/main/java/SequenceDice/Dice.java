package SequenceDice;

import java.util.Random;

public class Dice {
    private final Random rand = new Random();

    public int[] roll() {
        int d1 = rand.nextInt(1, 7);
        int d2 = rand.nextInt(1, 7);
        return new int[] { d1, d2 };
    }
}
