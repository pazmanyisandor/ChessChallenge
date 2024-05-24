package chess.control;

import chess.model.ChessState;
import chess.model.GameSaverLoader;
import chess.model.LeaderboardManager;
import org.tinylog.Logger;
import puzzle.TwoPhaseMoveState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Controller class for the Chess game UI.
 * Handles the user interactions and game logic.
 */
public class ChessController {

    @FXML
    private GridPane board;
    @FXML
    private Button buttonStartGame;
    @FXML
    private Button buttonLeaderboard;
    @FXML
    private Button buttonSaveGame;
    @FXML
    private Button buttonLoadGame;
    @FXML
    private TextField textFieldUserName;
    @FXML
    private Label labelScoreNum;
    @FXML
    private Label labelMessage;

    private ChessState chessState;
    private String selectedPiece = null;

    private final Pane[][] squares = new Pane[8][8];
    private final Image kingImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/king.png")));
    private final Image knightImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/knight.png")));
    private final Image goalImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/goal.png")));

    private final LeaderboardManager leaderboardManager = new LeaderboardManager("./io_files/leaderboard.json");
    private final GameSaverLoader gameSaverLoader = new GameSaverLoader("./io_files/gameState.json");

    @FXML
    private void initialize() {
        initializeBoard();
        labelScoreNum.setText(String.valueOf(0));

        buttonStartGame.setDisable(true);
        buttonSaveGame.setDisable(true);
        buttonLoadGame.setDisable(true);

        textFieldUserName.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isDisabled = newValue.trim().isEmpty();
            buttonStartGame.setDisable(isDisabled);
            buttonSaveGame.setDisable(isDisabled);
            buttonLoadGame.setDisable(isDisabled);
        });

        buttonStartGame.setOnAction(e -> startGame());
        buttonLoadGame.setOnAction(e -> handleLoadGame());
        buttonSaveGame.setOnAction(e -> handleSaveGame());
        buttonLeaderboard.setOnAction(e -> showLeaderboard());

        Logger.info("Chess Game's UI is initialized.");
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pane square = createSquare(i, j);
                board.add(square, j, i);
                squares[i][j] = square;
                addClickHandler(square, i, j);
            }
        }
    }

    private Pane createSquare(int row, int col) {
        Pane square = new Pane();
        square.setBackground(new Background(new BackgroundFill(determineColor(row, col), CornerRadii.EMPTY, Insets.EMPTY)));
        square.setMinSize(50, 50);
        return square;
    }

    private Color determineColor(int row, int col) {
        return (row + col) % 2 == 0 ? Color.WHITE : Color.GRAY;
    }

    private void addClickHandler(Pane square, int row, int col) {
        square.setOnMouseClicked(event -> handleSquareClick(row, col));
    }

    private void handleSquareClick(int row, int col) {
        Logger.info("Detected click at the " + (row + 1) + "th row's " + (col + 1) + "th column.");

        if (selectedPiece == null) {
            if (chessState.isLegalToMoveFrom("King") && row == chessState.getKingX() && col == chessState.getKingY()) {
                selectedPiece = "King";
                highlightMoves(selectedPiece);
            } else if (chessState.isLegalToMoveFrom("Knight") && row == chessState.getKnightX() && col == chessState.getKnightY()) {
                selectedPiece = "Knight";
                highlightMoves(selectedPiece);
            }
        } else {
            if (squares[row][col].getBackground().getFills().get(0).getFill() == Color.GREEN) {
                movePiece(selectedPiece, row, col);
                selectedPiece = null;
                updateView();
                if (chessState.isSolved()) {
                    labelMessage.setText("YOU WON!");
                    Logger.info("Game won by the User.");

                    String username = textFieldUserName.getText();
                    leaderboardManager.updateLeaderboard(username, chessState.getMoveCount());
                }
            } else {
                selectedPiece = null;
                clearHighlights();
            }
        }
    }

    private void startGame() {
        chessState = new ChessState(2, 1, 2, 2, 0);
        updateView();
        labelMessage.setText("Game Started!");
        labelScoreNum.setText(String.valueOf(chessState.getMoveCount()));

        Logger.info("Game started.");
    }

    private void handleLoadGame() {
        GameSaverLoader.GameState gameState = gameSaverLoader.loadGame();
        if (gameState != null) {
            chessState = new ChessState(
                    gameState.getKingPosition()[0], gameState.getKingPosition()[1],
                    gameState.getKnightPosition()[0], gameState.getKnightPosition()[1],
                    gameState.getMoveCount()
            );
            updateView();
            labelMessage.setText("Game Loaded!");
        } else {
            labelMessage.setText("Failed to load game.");
        }

        labelScoreNum.setText(String.valueOf(chessState.getMoveCount()));
        Logger.info("Game loaded.");
    }

    private void handleSaveGame() {
        gameSaverLoader.saveGame(
                new int[]{chessState.getKingX(), chessState.getKingY()},
                new int[]{chessState.getKnightX(), chessState.getKnightY()},
                new int[]{chessState.getGoalX(), chessState.getGoalY()},
                chessState.getMoveCount()
        );
        labelMessage.setText("Game Saved!");
        Logger.info("Game saved.");
    }

    private void showLeaderboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/leaderboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Leaderboard");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Error while showing Leaderboard: " + e);
        }
    }

    private void updateView() {
        clearBoard();
        initializeBoard();
        addPiece(chessState.getKingX(), chessState.getKingY(), kingImage);
        addPiece(chessState.getKnightX(), chessState.getKnightY(), knightImage);
        addPiece(chessState.getGoalX(), chessState.getGoalY(), goalImage);
    }

    private void addPiece(int x, int y, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        squares[x][y].getChildren().add(imageView);
    }

    private void clearBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j].getChildren().clear();
            }
        }
    }

    private void highlightMoves(String piece) {
        clearHighlights();
        Set<TwoPhaseMoveState.TwoPhaseMove<String>> legalMoves = chessState.getLegalMoves();
        for (TwoPhaseMoveState.TwoPhaseMove<String> move : legalMoves) {
            if (move.from().equals(piece)) {
                String[] parts = move.to().split(" ");
                int newX = Integer.parseInt(parts[1]);
                int newY = Integer.parseInt(parts[2]);
                squares[newX][newY].setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    private void clearHighlights() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j].setBackground(new Background(new BackgroundFill(determineColor(i, j), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    private void movePiece(String piece, int newX, int newY) {
        TwoPhaseMoveState.TwoPhaseMove<String> move = new TwoPhaseMoveState.TwoPhaseMove<>(piece, piece + " " + newX + " " + newY);
        chessState.makeMove(move);
        labelScoreNum.setText(String.valueOf(chessState.getMoveCount()));

        Logger.info("Piece moved.");
    }
}