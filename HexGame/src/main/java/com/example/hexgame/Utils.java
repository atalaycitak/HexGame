package com.example.hexgame;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import java.util.Arrays;
import java.util.List;

public class Utils {


    public static final double r = 17;

    public static final double n = Math.sqrt(r * r * 0.75);

    public static final double TILE_YUKSEKLIGI = 2 * r;

    public static final double TILE_GENISLIGI = 2 * n;
    //altigenleri boyutlarini yukardaki islemler veriyor


    public static List<Color> getAvailableColors() {
        return Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE, Color.ORANGE);
    }
    // Secilebilen renklerin listesi kirmizi mavi yesil sarı mor turuncu

    public static Color getPlayerColor(String playerName, List<Color> availableColors) {
        Stage colorStage = new Stage();
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Label prompt = new Label(playerName + " için bir renk seçin:");
        vbox.getChildren().add(prompt);

        Color[] selectedColor = new Color[1];
        // Oyuncu hangi rengi seccegini belirliyor


        for (Color color : availableColors) {
            Button colorButton = new Button(colorToName(color));
            colorButton.setStyle("-fx-background-color: " + colorToHex(color) + "; -fx-text-fill: white;");
            colorButton.setOnAction(event -> {
                selectedColor[0] = color;
                colorStage.close();
            });
            vbox.getChildren().add(colorButton);
        }
        // Kullanılabilir renklerin butonları

        Scene colorScene = new Scene(vbox, 200, 300);
        colorStage.setScene(colorScene);
        colorStage.setTitle("Renk Seçimi Yapınız.");
        colorStage.showAndWait();

        return selectedColor[0];
    }

    public static String colorToName(Color color) {
        if (color.equals(Color.RED)) return "Kırmızı";
        if (color.equals(Color.BLUE)) return "Mavi";
        if (color.equals(Color.GREEN)) return "Yeşil";
        if (color.equals(Color.YELLOW)) return "Sarı";
        if (color.equals(Color.PURPLE)) return "Mor";
        if (color.equals(Color.ORANGE)) return "Turuncu";
        return "Lutfen yazan renklerden birini seciniz.";
    }
    //ahmet burdan altı sende rengi hexe donusturme ve animasyon eklencek


    public static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }



    public static void showWinnerAnimation(AnchorPane tileMap, String winnerName, Color winnerColor) {
        Label winnerLabel = new Label(winnerName + " Oyunu Kazandı!");
        winnerLabel.setStyle("-fx-font-size: 27; -fx-text-fill: " + colorToHex(winnerColor) + ";");
        winnerLabel.setLayoutX(tileMap.getWidth() / 2 - 100);
        winnerLabel.setLayoutY(tileMap.getHeight() / 2 - 20);
        tileMap.getChildren().add(winnerLabel);
        // Kazanananin kim oldug
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(winnerLabel.scaleXProperty(), 1),
                        new KeyValue(winnerLabel.scaleYProperty(), 1),
                        new KeyValue(winnerLabel.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(1), new KeyValue(winnerLabel.scaleXProperty(), 2),
                        new KeyValue(winnerLabel.scaleYProperty(), 2),
                        new KeyValue(winnerLabel.opacityProperty(), 0))
                // Kazananın animasyonunuu caslistran kod
        );
        timeline.play();
    }
}