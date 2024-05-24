package chess.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameSaverLoaderTest {
    @TempDir
    Path tempDir;
    private GameSaverLoader gameSaverLoader;
    private String filePath;

    @BeforeEach
    void setup() {
        filePath = tempDir.resolve("game.json").toString();
        gameSaverLoader = new GameSaverLoader(filePath);
    }

    @Test
    public void testSaveGame() throws IOException {
        int[] kingPosition = {0, 0};
        int[] knightPosition = {1, 1};
        int[] goalPosition = {7, 7};
        int moveCount = 10;

        gameSaverLoader.saveGame(kingPosition, knightPosition, goalPosition, moveCount);

        assertTrue(Files.exists(Paths.get(filePath)));
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        assertNotNull(content);
        assertTrue(content.contains("\"moveCount\":10"));
    }

    @Test
    public void testLoadGame() throws IOException {
        String json = "{\"kingPosition\":[0,0],\"knightPosition\":[1,1],\"goalPosition\":[7,7],\"moveCount\":10}";
        Files.write(Paths.get(filePath), json.getBytes());

        GameSaverLoader.GameState loadedGame = gameSaverLoader.loadGame();

        assertNotNull(loadedGame);
        assertArrayEquals(new int[]{0, 0}, loadedGame.getKingPosition());
        assertArrayEquals(new int[]{1, 1}, loadedGame.getKnightPosition());
        assertArrayEquals(new int[]{7, 7}, loadedGame.getGoalPosition());
        assertEquals(10, loadedGame.getMoveCount());
    }

    @Test
    public void testLoadGameWithIOException() {
        String invalidPath = tempDir.resolve("nonexistent.json").toString();
        GameSaverLoader faultyLoader = new GameSaverLoader(invalidPath);
        assertNull(faultyLoader.loadGame());
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