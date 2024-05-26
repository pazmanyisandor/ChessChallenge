package chess.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaderboardEntryTest {
    private LeaderboardEntry leaderboardEntry;

    @BeforeEach
    void setup() {
        leaderboardEntry = new LeaderboardEntry("user", 10, "2024-05-26T15:30:00");
    }

    @Test
    public void testGetUsername() {
        assertEquals("user", leaderboardEntry.getUsername());
        assertNotEquals("asd", leaderboardEntry.getUsername());
    }

    @Test
    public void testSetUsername() {
        leaderboardEntry.setUsername("newUser");
        assertEquals("newUser", leaderboardEntry.getUsername());
        assertNotEquals("user", leaderboardEntry.getUsername());
    }

    @Test
    public void testGetScore() {
        assertEquals(10, leaderboardEntry.getScore());
        assertNotEquals(5, leaderboardEntry.getScore());
    }

    @Test
    public void testSetScore() {
        leaderboardEntry.setScore(20);
        assertEquals(20, leaderboardEntry.getScore());
        assertNotEquals(10, leaderboardEntry.getScore());
    }

    @Test
    public void testGetDatetime() {
        assertEquals("2024-05-26T15:30:00", leaderboardEntry.getDatetime());
        assertNotEquals("2024-05-25T15:30:00", leaderboardEntry.getDatetime());
    }

    @Test
    public void testSetDatetime() {
        leaderboardEntry.setDatetime("2024-06-01T10:00:00");
        assertEquals("2024-06-01T10:00:00", leaderboardEntry.getDatetime());
        assertNotEquals("2024-05-26T15:30:00", leaderboardEntry.getDatetime());
    }

    @Test
    public void testUsernameProperty() {
        assertEquals("user", leaderboardEntry.usernameProperty().get());
        leaderboardEntry.usernameProperty().set("anotherUser");
        assertEquals("anotherUser", leaderboardEntry.getUsername());
    }

    @Test
    public void testScoreProperty() {
        assertEquals(10, leaderboardEntry.scoreProperty().get());
        leaderboardEntry.scoreProperty().set(30);
        assertEquals(30, leaderboardEntry.getScore());
    }

    @Test
    public void testDatetimeProperty() {
        assertEquals("2024-05-26T15:30:00", leaderboardEntry.datetimeProperty().get());
        leaderboardEntry.datetimeProperty().set("2024-07-01T12:00:00");
        assertEquals("2024-07-01T12:00:00", leaderboardEntry.getDatetime());
    }
}