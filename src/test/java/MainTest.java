import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.ZoneId;

import static org.junit.Assert.*;

import static com.querins.Main.*;

public class MainTest {

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
