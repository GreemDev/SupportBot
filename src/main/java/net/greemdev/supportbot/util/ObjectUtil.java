package net.greemdev.supportbot.util;

public class ObjectUtil {

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static String combineStrings(String... strings) {
        StringBuilder returnValue = new StringBuilder();
        for (var s : strings) returnValue.append(s);
        return returnValue.toString();
    }
}
