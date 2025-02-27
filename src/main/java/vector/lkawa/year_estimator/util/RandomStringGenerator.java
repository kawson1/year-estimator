package vector.lkawa.year_estimator.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomStringGenerator {

    private static final Integer MAX_LENGTH = 20;

    private static final Random random = new Random();

    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Integer randomLength = random.nextInt(MAX_LENGTH) + 1;
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < randomLength; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

}
