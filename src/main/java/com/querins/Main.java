package com.querins;

import java.time.*;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String cityName;
    private static final String DEFAULT_TIMEZONE = "Etc/GMT+0";
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("MessageBundle");

    public static void main(String[] args) {

        switch(args.length) {
            case 0:
                throw new IllegalArgumentException("No arguments provided");
            case 1: {
                // Only city is provided
                String city = args[0];
                ZoneId targetTimeZone = findZoneByCity(city);
                System.out.println(getMessage(targetTimeZone, city));
            }
            break;
            case 2: {
                // Both city and timezone arguments are provided
                String city = args[0];
                String timeZone = args[1];
                if(validateTimeZone(timeZone)) {
                    getMessage(ZoneId.of(timeZone), city);
                } else {
                    throw new IllegalArgumentException("Wrong timezone provided");
                }
            }
            break;
            default:
                System.out.println("You must provide 1 or 2 command line arguments");
        }

    }

    /**
     * Check if a given timezone to exists
     * @param zone timezone to check
     * @return <code>true</code> if given timezone exists, <code>false</code> otherwise.
     * */
    public static boolean validateTimeZone(String zone) {
        try {
            ZoneId.of(zone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method used to create message based on the city and timezone.
     * Message changes depending on time in given timezone.
     * @param zone
     * @param city
     * @return composed message
     */
    public static String getMessage(ZoneId zone, String city) {

        LocalTime targetTime = LocalTime.now(zone);

        int hour = targetTime.getHour();
        String state;

        if( hour >= 6 && hour < 9 ) {
            state = "morning";
        } else {
            if ( hour >= 9 && hour < 19  ) {
                state = "day";
            } else {
                if( hour >= 19 && hour < 23 ) {
                    state = "evening";
                } else {
                    if( hour > 23 || hour < 6) {
                        state = "night";
                    } else throw new IllegalArgumentException("Wrong value of hour: " + hour);
                }
            }
        }

        return resourceBundle.getString("message." + state) + ", " + city;

    }

    /**
     * Attempts to find timezone of the given city.
     * @param city
     * @return Timezone of the city, GMT+0 if failed.
     */
    public static ZoneId findZoneByCity(String city) {

        for(String zone: ZoneId.getAvailableZoneIds()) {

            int slashPos = zone.indexOf('/');

            if(slashPos == -1) slashPos = 0;

            if( zone.substring( slashPos + 1).replace('_', ' ').equalsIgnoreCase(city) ) {

                logger.info("Target timezone has successfully been found: " + zone);
                return ZoneId.of(zone);
            }

        }
        logger.info("Time zone cannot be identified based on the city, falling back to defaults");
        return ZoneId.of(DEFAULT_TIMEZONE);

    }

}
