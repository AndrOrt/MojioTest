package com.example.mojiotest.tools;

import java.util.concurrent.TimeUnit;

public class ToolsTime {

    public static String intervalToString(long interval) {
        String intervalStr = null;
        if (interval <= 0)
            return intervalStr;
        long days = TimeUnit.MILLISECONDS.toDays(interval);
        interval -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(interval);
        interval -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(interval);
        interval -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(interval);
        if (days > 0) {
            intervalStr = String.format("%02dd %02dh %02dmin %02dsec", days, hours, minutes, seconds);
        } else if (hours > 0) {
            intervalStr = String.format("%02dh %02dmin %02dsec", hours, minutes, seconds);
        } else if (minutes > 0) {
            intervalStr = String.format("%02dmin %02dsec", minutes, seconds);
        } else {
            intervalStr = String.format("%02dsec", seconds);
        }
        return intervalStr;
    }
}
