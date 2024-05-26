package chess.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.tinylog.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manager class for handling operations related to the leaderboard data storage.
 * It provides functionality to read, write, and update leaderboard data in JSON format.
 */
public class LeaderboardManager {
    /**
     * The file path where the leaderboard will be saved to and loaded from.
     * The default path is "leaderboard.json".
     */
    public static String FILE_PATH = "leaderboard.json";
    private static final Gson gson = new Gson();

    /**
     * Constructs a LeaderboardManager that ensures the leaderboard file exists in the resource directory.
     */
    public LeaderboardManager() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            try (OutputStream outputStream = Files.newOutputStream(path)) {
                JsonObject emptyLeaderboard = new JsonObject();
                outputStream.write(gson.toJson(emptyLeaderboard).getBytes(StandardCharsets.UTF_8));
                Logger.info("Created new leaderboard file.");
            } catch (Exception e) {
                e.printStackTrace();
                Logger.error("Failed to ensure that the Leaderboard file exists: " + e);
            }
        }
    }

    /**
     * Reads the leaderboard data from the resource file and returns it as a JsonObject.
     * @return JsonObject containing the leaderboard data, or an empty JsonObject if an error occurs
     */
    public JsonObject readLeaderboard() {
        Path path = Paths.get(FILE_PATH);
        try (InputStream inputStream = Files.newInputStream(path);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Failed to read Leaderboard data: " + e);
            return new JsonObject();
        }
    }

    /**
     * Writes the provided JSON object containing leaderboard data to the resource file.
     * This method serializes the JsonObject to a JSON string and writes it to the disk.
     * If an error occurs during writing, it logs the error message.
     * @param jsonObj the JsonObject to be written to the file
     */
    public void writeLeaderboard(JsonObject jsonObj) {
        Path path = Paths.get(FILE_PATH);
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            outputStream.write(gson.toJson(jsonObj).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Failed to write to Leaderboard: " + e);
        }
    }

    /**
     * Updates the leaderboard with the provided username and score.
     * If the username exists, it updates the score if the new score is better.
     * If the username doesn't exist, it adds a new entry with the score and current datetime.
     * @param username the username of the player
     * @param moveCount the score of the player, typically the number of moves in a game
     */
    public void updateLeaderboard(String username, int moveCount) {
        JsonObject leaderboard = readLeaderboard();
        if (leaderboard == null) {
            leaderboard = new JsonObject();
        }

        JsonObject userDetails = leaderboard.getAsJsonObject(username);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());

        if (userDetails == null) {
            userDetails = new JsonObject();
            userDetails.addProperty("moveCount", moveCount);
            userDetails.addProperty("dateTime", dateTime);
            leaderboard.add(username, userDetails);
        } else {
            int currentBest = userDetails.get("moveCount").getAsInt();
            if (moveCount < currentBest) {
                userDetails.addProperty("moveCount", moveCount);
                userDetails.addProperty("dateTime", dateTime);
            }
        }

        writeLeaderboard(leaderboard);
        Logger.info("Leaderboard updated.");
    }
}