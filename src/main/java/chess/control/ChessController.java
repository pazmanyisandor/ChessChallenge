package chess.control;

import chess.model.ChessState;
import javafx.fxml.FXML;
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

    private final Pane[][] squares = new Pane[8][8];
    private final Image kingImage = new Image(getClass().getResourceAsStream("/king.png"));
    private final Image knightImage = new Image(getClass().getResourceAsStream("/knight.png"));
    private final Image goalImage = new Image(getClass().getResourceAsStream("/goal.png"));

    @FXML
    private void initialize() {
        initializeBoard();
        buttonStartGame.setOnAction(e -> startGame());
        buttonLoadGame.setOnAction(e -> handleLoadGame());
        buttonSaveGame.setOnAction(e -> handleSaveGame());
        buttonLeaderboard.setOnAction(e -> showLeaderboard());
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
    }

    private void startGame() {
        chessState = new ChessState(2, 1, 2, 2);
        updateView();
        labelMessage.setText("Game Started!");
    }

    private void handleLoadGame() {
    }

    private void handleSaveGame() {
    }

    private void showLeaderboard() {
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
}