package com.abohava.data.db;

public class DbErrors {

    public static final class NoCityForIdException extends Exception {
        NoCityForIdException(String message) {
            super(message);
        }
    }

    public static final class NoCityForNameException extends Exception {
        NoCityForNameException(String message) {
            super(message);
        }
    }

    public static final class CityHasDuplicateException extends Exception {
        CityHasDuplicateException(String message) {
            super(message);
        }
    }

    public static final class CityListEmptyException extends Exception {
        CityListEmptyException(String message) {
            super(message);
        }
    }

    public static final class ApiConnectionException extends Exception {
        ApiConnectionException(String message) {
            super(message);
        }
    }
}
