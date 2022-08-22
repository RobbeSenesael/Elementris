package domain.verification;

import org.junit.Before;
import org.junit.Test;
import utils.StringLiterals;

import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void beforeAnyTest() {
        this.user = new User(StringLiterals.TEMP_USERNAME_1, StringLiterals.TEMP_PASSWORD, 30);
    }

    @Test
    public void testUsername() {
        assertEquals(StringLiterals.TEMP_USERNAME_1, user.getUsername());
    }

    @Test
    public void testPassword() {
        assertEquals(StringLiterals.TEMP_PASSWORD, user.getPassword());
    }

    @Test
    public void testWins() {
        assertEquals(30, user.getWins());
    }

    @Test
    public void compareTo() {
        assertEquals(20, user.compareTo(new User(StringLiterals.TEMP_USERNAME_2, StringLiterals.TEMP_PASSWORD, 50)));
    }
}
