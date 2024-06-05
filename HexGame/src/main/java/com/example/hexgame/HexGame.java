package com.example.hexgame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HexGame extends Application {

    private Label turnLabel;
    private Board board;
    private Player player1, player2;
    private boolean playerTurn = true;

    @Override
    public void start(Stage primaryStage) {

        // karsilama ekrani
        Label welcomeLabel = new Label("HexGame'e Hoş Geldiniz!");
        welcomeLabel.setStyle("-fx-font-size: 27; -fx-text-fill: #000;");
        Button startButton = new Button("Başlat");
        startButton.setOnAction(event -> showMainScreen(primaryStage));

        Button howToPlayButton = new Button("Hex Game Nasıl Oynanır?");
        howToPlayButton.setOnAction(event -> showHowToPlayDialog());

        VBox welcomeLayout = new VBox(20, welcomeLabel, startButton, howToPlayButton);
        welcomeLayout.setAlignment(Pos.CENTER);
        Scene welcomeScene = new Scene(welcomeLayout, 800, 600);

        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Hex Game v1.0");
        primaryStage.show();
    }
    private void showMainScreen(Stage primaryStage) {
        // Oyuncu isim ve renk belirledigi yer
        String player1Name = getPlayerName("1. Oyuncu");
        String player2Name = getPlayerName("2. Oyuncu");
        List<javafx.scene.paint.Color> availableColors = new ArrayList<>(Utils.getAvailableColors());
        player1 = new Player(player1Name, Utils.getPlayerColor(player1Name, availableColors));
        availableColors.remove(player1.getColor());
        player2 = new Player(player2Name, Utils.getPlayerColor(player2Name, availableColors));

        AnchorPane tileMap = new AnchorPane();
        VBox root = new VBox();
        Scene scene = new Scene(root, 800, 600);

        // siranin kimde oldugu yazicak
        turnLabel = new Label("Sıra: " + player1.getName());
        turnLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        turnLabel.setAlignment(Pos.CENTER_RIGHT);

        // Butonlara basildiginda tahta boyutları degisiyor
        Button btn5x5 = new Button("5x5");
        Button btn11x11 = new Button("11x11");
        Button btn17x17 = new Button("17x17");
        btn5x5.setOnAction(event -> createBoard(tileMap, 5, 5));
        btn11x11.setOnAction(event -> createBoard(tileMap, 11, 11));
        btn17x17.setOnAction(event -> createBoard(tileMap, 17, 17));

        // "Nasıl Oynanır?" butonunu oluştur ve tıklanınca açıklama göster
        Button howToPlayButton = new Button("Hex Game Oynanışı");
        howToPlayButton.setOnAction(event -> showHowToPlayDialog());

        // hbox olusturup butonlar duzenleniyr
        HBox buttonBox = new HBox(10, btn5x5, btn11x11, btn17x17, howToPlayButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(turnLabel, tileMap, buttonBox);
        VBox.setVgrow(tileMap, javafx.scene.layout.Priority.ALWAYS); // TileMap'in ortada esneklik göstermesini sağla
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hex Game");
        primaryStage.show();
    }

    // Oyuncudan isim iste
    private String getPlayerName(String player) {
        TextInputDialog dialog = new TextInputDialog(player);
        dialog.setTitle("1.Oyuncu Adı");
        dialog.setHeaderText(null);
        dialog.setContentText(player + " adını girin:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse(player);
    }

    private void createBoard(AnchorPane tileMap, int row, int col) {
        board = new Board(tileMap, row, col, player1, player2, turnLabel);
        turnLabel.setText("Sıra: " + player1.getName());
    }

    // uyari ekraniyla oynanısı acıkla
    private void showHowToPlayDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hex Game Oynanış");
        alert.setHeaderText(null);
        alert.setContentText(
                "HexGame, iki kişiyle oynanan bir masa oyunudur. Oyunun amacı, altıgenlerden oluşan bir tahtada" +
                        "karşılıklı iki kenarı birleştirerek bir yol oluşturmak ve diğer oyuncunun yol oluşturmasına engel olmaktır.\n" +
                        "Sıranız geldiğinde bir altıgen seçin ve seçtiğiniz altıgene tıklayarak kendi renginizle doldurun." +
                        "Bir yolu tamamladığınızda, oyunu kazanırsınız.\n" +
                        "Oyunda ikinci hamleyi oynayan kişi ilk hamlesini yaptıktan sonra takas(swap) sorusu sorulur." +
                        "Eğer ikinci oyuncu takas teklifini kabul ederse birinci oyuncunun ilk hamlesi ve ikinci oyuncunun ilk hamlesiyle yer değiştirir ve sıra ilk hamleyi yapan oyuncuya geçerek devam eder\n" +
                        "Bu kural yeni stratejilerin geliştirilmesine yardımcı olarak oyuna derinlik katar!\n" +
                        "İyi eğlenceler!"
        );
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}