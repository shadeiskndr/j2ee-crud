package com.example.util;

import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2;

public class PasswordUtil {
    private static final Argon2 argon2 = Argon2Factory.create();

    public static String hash(String password) {
        return argon2.hash(2, 65536, 1, password);
    }

    public static boolean verify(String hash, String password) {
        return argon2.verify(hash, password);
    }
}
