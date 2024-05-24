package chess.control;

import chess.model.LeaderboardEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Controller class for the Leaderboard UI.
 * Handles the initialization and data loading for the leaderboard.
 */
public class LeaderboardController {
    @FXML
    private TableView<LeaderboardEntry> tableView;
    @FXML
    private TableColumn<LeaderboardEntry, String> usernameColumn;
    @FXML
    private TableColumn<LeaderboardEntry, Integer> scoreColumn;
    @FXML
    private TableColumn<LeaderboardEntry, String> datetimeColumn;
    @FXML
    private TableColumn<LeaderboardEntry, Integer> rankColumn;

    private static final Gson gson = new Gson();

    /**
     * Initializes the leaderboard UI.
     * Sets up the table columns and loads the leaderboard data.
     */
    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        datetimeColumn.setCellValueFactory(new PropertyValueFactory<>("datetime"));

        rankColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(cellData.getValue()) + 1));
        rankColumn.setSortable(false);

        loadLeaderboardData();

        Logger.info("Leaderboard UI's initialization finished.");
    }

    private void loadLeaderboardData() {
        String path = "./io_files/leaderboard.json";
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            JsonObject jsonObj = gson.fromJson(content, JsonObject.class);
            ObservableList<LeaderboardEntry> data = FXCollections.observableArrayList();

            jsonObj.entrySet().forEach(entry -> {
                JsonObject userObj = entry.getValue().getAsJsonObject();
                String dateTime = userObj.get("dateTime").getAsString();
                int moveCount = userObj.get("moveCount").getAsInt();
                LeaderboardEntry leaderboardEntry = new LeaderboardEntry(entry.getKey(), moveCount, dateTime);
                data.add(leaderboardEntry);
            });

            // Sort data based on moveCount in ascending order
            FXCollections.sort(data, (entry1, entry2) -> Integer.compare(entry1.getScore(), entry2.getScore()));

            tableView.setItems(data);

            Logger.info("Leaderboard data loaded.");
        } catch (IOException e) {
            e.printStackTrace();

            Logger.error("Failed to load Leaderboard data, error: " + e);
        }
    }
}