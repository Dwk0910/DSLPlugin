package org.dslofficial.util;

public class CompareType {
    public static boolean isInt(String s) {
        try {
            if (s.length() >= Integer.MAX_VALUE) throw new NumberFormatException();
            int dummy = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
