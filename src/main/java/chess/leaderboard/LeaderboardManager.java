package chess.leaderboard;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manager class for handling operations related to the leaderboard data storage.
 * It provides functionality to read, write, and update leaderboard data in JSON format.
 */
public class LeaderboardManager {
    private String filePath;
    private static final Gson gson = new Gson();

    /**
     * Constructs a LeaderboardManager with the specified file path for the leaderboard data.
     * Ensures that the leaderboard file exists or creates a new one if it doesn't.
     * @param filePath the file path where leaderboard data is stored
     */
    public LeaderboardManager(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                writeLeaderboard(new JsonObject());
            } catch (IOException e) {
                e.printStackTrace();
                Logger.error("Failed to ensure that the Leaderboard file exists: " + e);
            }
        }
    }

    /**
     * Reads the leaderboard data from the file and returns it as a {link JsonObject}.
     * @return JsonObject containing the leaderboard data
     */
    public JsonObject readLeaderboard() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            return new JsonObject();
        }
    }

    /**
     * Writes the provided JSON object containing leaderboard data to the specified file.
     * This method serializes the JsonObject to a JSON string and writes it to the disk.
     * If an error occurs during writing, it logs the error message.
     * @param jsonObj the JsonObject to be written to the file
     */
    public void writeLeaderboard(JsonObject jsonObj) {
        try {
            Files.write(Paths.get(filePath), gson.toJson(jsonObj).getBytes());
        } catch (IOException e) {
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