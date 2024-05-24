package chess.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Data model class for entries in the leaderboard.
 * This class represents a single entry in the leaderboard with username, score, and datetime properties.
 */
public class LeaderboardEntry {
    private StringProperty username;
    private IntegerProperty score;
    private StringProperty datetime;

    /**
     * Constructs a new leaderboard entry with specified username, score, and datetime.
     * @param username the username of the player
     * @param score the score of the player
     * @param datetime the datetime when the score was recorded
     */
    public LeaderboardEntry(String username, int score, String datetime) {
        this.username = new SimpleStringProperty(username);
        this.score = new SimpleIntegerProperty(score);
        this.datetime = new SimpleStringProperty(datetime);
    }

    /**
     * Returns the username of the player.
     * @return the username as a String
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Sets the username of the player.
     * @param username the new username as a String
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * Returns the score of the player.
     * @return the score as an int
     */
    public int getScore() {
        return score.get();
    }

    /**
     * Sets the score of the player.
     * @param score the new score as an int
     */
    public void setScore(int score) {
        this.score.set(score);
    }

    /**
     * Returns the datetime when the score was recorded.
     * @return the datetime as a String
     */
    public String getDatetime() {
        return datetime.get();
    }

    /**
     * Sets the datetime when the score was recorded.
     * @param datetime the new datetime as a String
     */
    public void setDatetime(String datetime) {
        this.datetime.set(datetime);
    }

    /**
     * Returns the StringProperty for the username.
     * This property allows the username to be bound to UI components, making it observable.
     * @return the observable property for username
     */
    public StringProperty usernameProperty() {
        return username;
    }

    /**
     * Returns the IntegerProperty for the score.
     * This property allows the score to be bound to UI components and be observed for changes.
     * @return the observable property for score
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Returns the StringProperty for the datetime.
     * This property allows the datetime to be bound to UI components, enabling observation and updates in the UI.
     * @return the observable property for datetime
     */
    public StringProperty datetimeProperty() {
        return datetime;
    }
}