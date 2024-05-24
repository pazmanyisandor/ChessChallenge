package chess.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GameSaverLoaderTest {
    @TempDir
    Path tempDir;
    private GameSaverLoader gameSaverLoader;

    @BeforeEach
    void setup() {
        gameSaverLoader = new GameSaverLoader();
    }

    @Test
    public void testSaveGame() throws IOException {
        int[] kingPosition = {0, 0};
        int[] knightPosition = {1, 1};
        int[] goalPosition = {7, 7};
        int moveCount = 10;

        gameSaverLoader.saveGame(kingPosition, knightPosition, goalPosition, moveCount);

        Path filePath = null;
        try {
            filePath = Paths.get(getClass().getResource("/gameState.json").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Files.exists(filePath));
        String content = new String(Files.readAllBytes(filePath));
        assertNotNull(content);
        assertTrue(content.contains("\"moveCount\":10"));
    }

    @Test
    public void testLoadGame() throws IOException {
        String json = "{\"kingPosition\":[0,0],\"knightPosition\":[1,1],\"goalPosition\":[7,7],\"moveCount\":10}";
        Path filePath = null;
        try {
            filePath = Paths.get(getClass().getResource("/gameState.json").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Use OutputStream to write the JSON data to the file
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        }

        // Load the game state from the file
        GameSaverLoader.GameState loadedGame = gameSaverLoader.loadGame();

        // Assertions to verify the loaded game state
        assertNotNull(loadedGame);
        assertArrayEquals(new int[]{0, 0}, loadedGame.getKingPosition());
        assertArrayEquals(new int[]{1, 1}, loadedGame.getKnightPosition());
        assertArrayEquals(new int[]{7, 7}, loadedGame.getGoalPosition());
        assertEquals(10, loadedGame.getMoveCount());
    }

    @Test
    public void testSortGameStatesByMoveCount() {
        List<GameSaverLoader.GameState> gameStates = Arrays.asList(
                new GameSaverLoader.GameState(new int[]{0, 0}, new int[]{1, 1}, new int[]{7, 7}, 10),
                new GameSaverLoader.GameState(new int[]{1, 1}, new int[]{2, 2}, new int[]{6, 6}, 5)
        );
        GameSaverLoader.GameState.sortGameStatesByMoveCount(gameStates);
        assertEquals(5, gameStates.get(0).getMoveCount());
        assertEquals(10, gameStates.get(1).getMoveCount());
    }
}