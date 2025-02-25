package vector.lkawa.year_estimator.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomStringGenerator {

    private static final Integer MAX_LENGTH = 20;

    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();

        Integer randomLength = random.nextInt(MAX_LENGTH) + 1;
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < randomLength; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

}
