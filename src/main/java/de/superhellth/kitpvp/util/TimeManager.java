package de.superhellth.kitpvp.util;

public class TimeManager {

    /**
     *
     * @param seconds time in seconds
     * @return Returns a string in style: "<minutes> minutes and <seconds> seconds"
     */
    public static String getSecondsAsString(int seconds) {
        String message = "";
        if (seconds >= 60) {
            message += seconds / 60;
            message += seconds >= 120 ? " minutes" : " minute";
        }
        if (seconds % 60 != 0) {
            if (seconds > 60) {
                message += " and ";
            }
            message += seconds % 60;
            message += seconds % 60 == 1 ? " second" : " seconds";
        }
        return message;
    }

}
