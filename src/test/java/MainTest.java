import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;

import static com.querins.Main.*;

public class MainTest {

    private Map<String, String> englishGreetings = new HashMap<>(4);
    private Map<String, String> russianGreetings = new HashMap<>(4);
    private String testCity = "Dnipro";

    @Before
    public void initGreetings() {

        englishGreetings.put("morning", "Good morning");
        englishGreetings.put("day", "Good day");
        englishGreetings.put("evening", "Good evening");
        englishGreetings.put("night", "Good night");

        russianGreetings.put("morning", "Доброе утро");
        russianGreetings.put("day", "Добрый день");
        russianGreetings.put("evening", "Добрый вечер");
        russianGreetings.put("night", "Доброй ночи");
    }

    @Test
    @DisplayName("Test correctness of greeting messages")
    public void testMessages() {

        Locale locale = Locale.getDefault();

        Set<String> timezones = ZoneId.getAvailableZoneIds();

        Map targetGreetings = locale.getLanguage().equals(new Locale("ru").getLanguage()) ?
                russianGreetings : englishGreetings;

        for(String timezone: timezones) {

            LocalTime time = LocalTime.now(ZoneId.of(timezone));
            int hour = time.getHour();
            String state;

            if( hour >= 6 && hour < 9 ) {
                state = "morning";
            } else {
                if ( hour >= 9 && hour < 19  ) {
                    state = "day";
                } else {
                    if( hour >= 19 && hour <= 23 ) {
                        state = "evening";
                    } else {
                        if( hour > 23 || hour < 6) {
                            state = "night";
                        } else {
                            fail("Wrong value of hours: " + hour + " Should be in [0; 23]");
                            return;
                        }
                    }
                }
            }

            assertEquals("Testing message on timezone " + timezone,
                    targetGreetings.get(state) + ", " + testCity + "!",
                    getMessage(ZoneId.of(timezone), testCity));

        }
    }

    @Test(expected = IllegalArgumentException.class)
    @DisplayName("Wrong timezone test")
    public void wrongTimeZone() {
        main(new String[] {"Kiev", "Australia/Kiev"});
    }

    @Test(expected = IllegalArgumentException.class)
    @DisplayName("Test on zero parameters")
    public void wrongParams() {
        main(new String[] {});
    }

    @Test
    @DisplayName("Tests method of finding timezone by city's name")
    public void cityFinder() {
        assertTrue(findZoneByCity("Kiev").equals(ZoneId.of("Europe/Kiev")));
        assertTrue(findZoneByCity("New York").equals(ZoneId.of("America/New_York")));
        assertFalse(findZoneByCity("Dnipro").equals(ZoneId.of("Europe/Kiev")));
    }

    @Test
    @DisplayName("Checks if certain timezone exists")
    public void validateTimeZoneTest() {
        assertFalse(validateTimeZone("Europe/Dnipro"));
        assertTrue(validateTimeZone("Europe/Kiev"));
        assertTrue(validateTimeZone("America/New_York"));
    }

}
