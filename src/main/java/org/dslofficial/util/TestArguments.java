package org.dslofficial.util;

public class TestArguments {
    public static boolean test(int count, String[] target) {
        for (int i = 1; i <= count; i++) {
            try {
                String dummy = target[i - 1];
            } catch (ArrayIndexOutOfBoundsException e) {
                return true;
            }
        }
        return false;
    }
}
