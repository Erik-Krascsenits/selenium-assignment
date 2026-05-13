package utils;

public final class InvalidPasswordGenerator {

    private InvalidPasswordGenerator() {
    }

    public static String from(String validPassword) {
        return validPassword + "_invalid";
    }
}