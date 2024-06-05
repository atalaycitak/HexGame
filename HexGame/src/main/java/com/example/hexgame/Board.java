package com.example.hexgame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class Board {

    private final AnchorPane tileMap;
    private final int row, col; // Satır ve sütun sayıları
    private final Player player1, player2;
    private final Label turnLabel;
    private boolean playerTurn = true;
    private boolean gameEnded = false; // Oyunun bitip bitmediğini kontrol eden komut
    private boolean secondPlayerFirstMove = true; // swap icin
    private List<Tile> cornerTiles;
    private Timeline colorChangeTimeline;

    public Board(AnchorPane tileMap, int row, int col, Player player1, Player player2, Label turnLabel) {
        this.tileMap = tileMap;
        this.row = row;
        this.col = col;
        this.player1 = player1;
        this.player2 = player2;
        this.turnLabel = turnLabel;
        createTileMap();
        startCornerTileColorChange();
    }

    private void createTileMap() {
        tileMap.getChildren().clear();
        cornerTiles = new ArrayList<>();


        double xBaslangicKaydirma = 40;
        double yBaslangicKaydirma = 40;
        //altigenlerin baslangıc yeri

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                double xKoord = x * Utils.TILE_GENISLIGI + y * Utils.n + xBaslangicKaydirma;
                double yKoord = y * Utils.TILE_YUKSEKLIGI * 0.75 + yBaslangicKaydirma;
                Tile tile = new Tile(x, y, xKoord, yKoord, this);
                tileMap.getChildren().add(tile);
                if (isCornerTile(x, y)) {
                    cornerTiles.add(tile);
                }
                if (y == 0 || y == row - 1 || x == 0 || x == col - 1) {
                    Color strokeColor = getEdgeColor(x, y);
                    tile.setStrokeColor(strokeColor);
                    // Sadece dış kenarlardaki altıgenleri secilen renklere gore ayarla
                }
            }
        }
    }
    private Color getEdgeColor(int x, int y) {
        if (y == 0 || y == row - 1) {
            return player1.getColor(); // Üst ve alt kenarlar için Player 1'in rengi
        } else if (x == 0 || x == col - 1) {
            return player2.getColor(); // Sağ ve sol kenarlar için Player 2'nin rengi
        }
        return Color.BLACK; // Diğer durumlar için siyah (varsayılan)
    }
    // köşe altıgenlerinin rengini belirle
    private boolean isCornerTile(int x, int y) {
        return (x == 0 && y == 0) || (x == 0 && y == row - 1) || (x == col - 1 && y == 0) || (x == col - 1 && y == row - 1);
    }
    // Köşedeki altıgenlerin yerini belirledi
    private void startCornerTileColorChange() {
        colorChangeTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event -> changeCornerTileColors()));
        colorChangeTimeline.setCycleCount(Timeline.INDEFINITE);
        colorChangeTimeline.play();
    }

    private void changeCornerTileColors() {
        for (Tile tile : cornerTiles) {
            Color currentColor = (Color) tile.getStroke();
            Color newColor = currentColor.equals(player1.getColor()) ? player2.getColor() : player1.getColor();
            tile.setStrokeColor(newColor);
        }
    }
    // en köşedeki altıgenlerın renklerini seçilen renge göre ayarlıyor
    public void onTileClicked(Tile tile) {
        if (tile.isColored()) return;
        makeMove(tile);
    }
    // altigene tıklandıgında metodu cagır tıklandıysa hicbir sey yapma

    private void makeMove(Tile tile) {
        if (gameEnded) {
            return;
        }
        Player currentPlayer = getCurrentPlayer();

        tile.setFill(currentPlayer.getColor());
        tile.setPlayer(currentPlayer);
        tile.setColored(true);
        tile.setDisable(true);

        if (checkWinCondition(currentPlayer)) {
            String winner = currentPlayer.getName() + " Kazandı!";
            turnLabel.setText(winner);
            disableAllTiles();
            Utils.showWinnerAnimation(tileMap, currentPlayer.getName(), currentPlayer.getColor());
            gameEnded = true;
            colorChangeTimeline.stop();
            return;
        }

        if (!playerTurn && secondPlayerFirstMove) {
            swapTiles();
        } else {
            playerTurn = !playerTurn;
            String playerTurnText = "Sıra: " + getCurrentPlayer().getName();
            turnLabel.setText(playerTurnText);
        }
    }
    // Hamlenin gerceklestigi metod

    private void swapTiles() {
        if (!secondPlayerFirstMove) {
            return;
        }

        showSwapDialog();
    }


    private void showSwapDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Takas Yap");
        alert.setHeaderText(null);
        alert.setContentText("İki taraf da ilk hamlesini yaptı. Altıgenleri takaslamak ister misiniz?");

        ButtonType buttonTypeYes = new ButtonType("Takasla");
        ButtonType buttonTypeNo = new ButtonType("Hayır");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        // takas bildirimi ve sonuc

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            swapStones();
        }
        playerTurn = true;
        String playerTurnText = "Sıra: " + getCurrentPlayer().getName();
        turnLabel.setText(playerTurnText);
        //takas kabul edilse de edilmese de sıra 1.e geçmeli

        secondPlayerFirstMove = false;
    }
    private void swapStones() {
        for (var node : tileMap.getChildren()) {
            if (node instanceof Tile) {
                Tile tile = (Tile) node;
                Player currentPlayer = tile.getPlayer();
                if (currentPlayer == player1) {
                    tile.setPlayer(player2);
                    tile.setFill(player2.getColor());
                } else if (currentPlayer == player2) {
                    tile.setPlayer(player1);
                    tile.setFill(player1.getColor());
                }
            }
        }
    }
    //swap kuralı

    // oyun sırası
    private Player getCurrentPlayer() {
        return playerTurn ? player1 : player2;
    }

    // Kazanma koşulunu kontrol eden metod
    private boolean checkWinCondition(Player player) {
        return player == player1 ? checkWinConditionHorizontal(player) : checkWinConditionVertical(player);
    }

    private boolean checkWinConditionHorizontal(Player player) {
        for (int x = 0; x < col; x++) {
            if (dfs(player, new boolean[row][col], x, 0)) {
                return true;
            }
        }
        return false;
    }
    // sagdan sola kazanma kontrolu

    private boolean checkWinConditionVertical(Player player) {
        for (int y = 0; y < row; y++) {
            if (dfs(player, new boolean[row][col], 0, y)) {
                return true;
            }
        }
        return false;
    }
    //yukardan asağı kazanma kontrolu


    private boolean dfs(Player player, boolean[][] visited, int x, int y) {
        if (x < 0 || y < 0 || x >= col || y >= row || visited[y][x]) return false;

        Tile tile = getTile(x, y);
        if (tile == null || tile.getPlayer() != player) return false;

        visited[y][x] = true;

        if ((player == player1 && y == row - 1) || (player == player2 && x == col - 1)) {
            return true;
        }

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}};
        for (int[] dir : directions) {
            if (dfs(player, visited, x + dir[0], y + dir[1])) {
                return true;
            }
        }
        return false;
    }
    //dfs ile kazananı arama

    private Tile getTile(int x, int y) {
        for (var node : tileMap.getChildren()) {
            if (node instanceof Tile) {
                Tile tile = (Tile) node;
                if (tile.getXIndex() == x && tile.getYIndex() == y) {
                    return tile;
                }
            }
        }
        return null;
    }

    private void disableAllTiles() {
        for (var node : tileMap.getChildren()) {
            if (node instanceof Tile) {
                node.setDisable(true);
                //tum tasları durdur
            }
        }
    }
}