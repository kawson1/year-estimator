package vector.lkawa.year_estimator.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomStringGeneratorTest {

    @Test
    void testGenerateRandomStringLength() {
        String randomString = RandomStringGenerator.generateRandomString();
        assertFalse(randomString.isEmpty());
        assertTrue(randomString.length() >= 1 && randomString.length() <= 20,
                "Generated string length should be between 1 and 20");
    }

    @Test
    void testGenerateRandomStringCharacters() {
        String randomString = RandomStringGenerator.generateRandomString();
        assertFalse(randomString.isEmpty());
        assertTrue(randomString.matches("[A-Za-z]+"), "Generated string should only contain alphabetic characters");
    }

    @Test
    void testGenerateRandomStringRandomness() {
        String randomString1 = RandomStringGenerator.generateRandomString();
        String randomString2 = RandomStringGenerator.generateRandomString();
        assertFalse(randomString1.isEmpty());
        assertFalse(randomString2.isEmpty());
        assertNotEquals(randomString1, randomString2, "Generated strings should not be the same");
    }
}