package com.fifteenpuzzle.Presentation;

import com.fifteenpuzzle.Application.GameService;
import com.fifteenpuzzle.Application.IGameService;
import com.fifteenpuzzle.Domain.Board;
import com.fifteenpuzzle.Domain.ValueObjects.Position;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;

public class GameController {
    @FXML private GridPane grid;
    @FXML private TextField sizeField;
    @FXML private Label statusLabel;

    private IGameService game;

    @FXML
    public void initialize() {
        this.game = new GameService();
        sizeField.setText("4");
        buildBoard(4);
    }

    @FXML
    private void onNewGame() {
        int n = parseSize();
        buildBoard(n);
        statusLabel.setText("Создано поле " + n + "x" + n);
    }

    @FXML
    private void onShuffle() {
        game.shuffleUntilSolvable();
        render();
        statusLabel.setText("Перемешано");
    }

    private int parseSize() {
        try { return Math.max(3, Integer.parseInt(sizeField.getText().trim())); }
        catch (Exception e) { return 4; }
    }

    private void buildBoard(int n) {
        game.newGame(n); // домен: последовательность 1..N*N-1 и 0
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();

        for (int c = 0; c < n; c++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / n);
            grid.getColumnConstraints().add(cc);
        }
        for (int r = 0; r < n; r++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / n);
            grid.getRowConstraints().add(rc);
        }
        createCells();
        render();
    }

    // Создаём N×N TextField-ы
    private void createCells() {
        int n = game.getBoard().getSize();
        grid.getChildren().clear();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                TextField tf = new TextField();
                tf.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setHgrow(tf, Priority.ALWAYS);
                GridPane.setVgrow(tf, Priority.ALWAYS);
                tf.setEditable(false);
                tf.setFocusTraversable(false);
                tf.setPrefColumnCount(2);
                tf.setStyle("-fx-font-size: 18px; -fx-alignment: center; -fx-background-radius: 8; -fx-padding: 8;");
                final int fr = r, fc = c;
                tf.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        boolean moved = game.tryMove(new Position(fr, fc));
                        if (moved) {
                            render();
                            if (game.isSolved()) {
                                statusLabel.setText("Собрано!");
                            }
                        }
                    }
                });
                grid.add(tf, c, r);
            }
        }
    }

    private void render() {
        Board board = game.getBoard();
        int n = board.getSize();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int value = board.getValue(r, c);
                TextField tf = (TextField) getNodeAt(r, c);
                if (value == 0) {
                    tf.setText("");
                    tf.setStyle("-fx-background-color: #dddddd; -fx-font-size: 18px; -fx-alignment: center; -fx-background-radius: 8; -fx-padding: 8;");
                } else {
                    tf.setText(String.valueOf(value));
                    tf.setStyle("-fx-background-color: #ffffff; -fx-font-size: 18px; -fx-alignment: center; -fx-background-radius: 8; -fx-padding: 8; -fx-border-color: #cccccc; -fx-border-radius: 8;");
                }
            }
        }
    }

    private javafx.scene.Node getNodeAt(int row, int col) {
        for (javafx.scene.Node n : grid.getChildren()) {
            Integer r = GridPane.getRowIndex(n);
            Integer c = GridPane.getColumnIndex(n);
            if ((r == null ? 0 : r) == row && (c == null ? 0 : c) == col) return n;
        }
        return null;
    }
} 