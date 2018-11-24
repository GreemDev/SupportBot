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

    public static String parseBoolean(String args) {
        switch (args.toLowerCase()) {
            case "yes":
            case "enabled":
            case "yeah":
            case "true":
                return "true";
            case "no":
            case "disabled":
            case "nah":
            case "false":
                return "false";
            default:
                return null;
        }
    }
}
