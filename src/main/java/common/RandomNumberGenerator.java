package common;

import java.util.Random;

public class RandomNumberGenerator {

    private static final Random RAND = new Random();

    public static int generateRandomInt(int max) {
        return RAND.nextInt(max);
    }

    public static float generateRandomFloat(float max) {
        return RAND.nextFloat() * max;
    }

}
