package chess.model;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderboardManagerTest {
    @TempDir
    Path tempDir;
    private LeaderboardManager leaderboardManager;
    private Path filePath;

    @BeforeEach
    void setup() {
        filePath = tempDir.resolve("leaderboard.json");
        LeaderboardManager.FILE_PATH = filePath.toString();
        leaderboardManager = new LeaderboardManager();
    }

    @Test
    public void testEnsureFileExists() {
        assertTrue(Files.exists(filePath));
        try {
            String content = new String(Files.readAllBytes(filePath));
            assertNotNull(content);
            assertEquals("{}", content.trim());
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadLeaderboard() {
        JsonObject leaderboard = leaderboardManager.readLeaderboard();
        assertNotNull(leaderboard);
        assertTrue(leaderboard.entrySet().isEmpty());
    }

    @Test
    public void testWriteLeaderboard() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("testUser", "testValue");

        leaderboardManager.writeLeaderboard(jsonObject);

        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        assertTrue(content.contains("\"testUser\":\"testValue\""));
    }

    @Test
    public void testUpdateLeaderboardNewUser() {
        leaderboardManager.updateLeaderboard("newUser", 15);

        JsonObject leaderboard = leaderboardManager.readLeaderboard();
        JsonObject userDetails = leaderboard.getAsJsonObject("newUser");

        assertNotNull(userDetails);
        assertEquals(15, userDetails.get("moveCount").getAsInt());
        assertNotNull(userDetails.get("dateTime").getAsString());
    }

    @Test
    public void testUpdateLeaderboardExistingUserBetterScore() throws IOException {
        String json = "{\"existingUser\":{\"moveCount\":20,\"dateTime\":\"2024-05-25 12:00:00\"}}";
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        }

        leaderboardManager.updateLeaderboard("existingUser", 10);

        JsonObject leaderboard = leaderboardManager.readLeaderboard();
        JsonObject userDetails = leaderboard.getAsJsonObject("existingUser");

        assertNotNull(userDetails);
        assertEquals(10, userDetails.get("moveCount").getAsInt());
        assertNotNull(userDetails.get("dateTime").getAsString());
    }

    @Test
    public void testUpdateLeaderboardExistingUserWorseScore() throws IOException {
        String json = "{\"existingUser\":{\"moveCount\":10,\"dateTime\":\"2024-05-25 12:00:00\"}}";
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        }

        leaderboardManager.updateLeaderboard("existingUser", 20);

        JsonObject leaderboard = leaderboardManager.readLeaderboard();
        JsonObject userDetails = leaderboard.getAsJsonObject("existingUser");

        assertNotNull(userDetails);
        assertEquals(10, userDetails.get("moveCount").getAsInt());
        assertEquals("2024-05-25 12:00:00", userDetails.get("dateTime").getAsString());
    }
}