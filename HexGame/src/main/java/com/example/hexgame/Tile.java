package com.example.hexgame;

import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class Tile extends Polygon {

    private boolean isColored = false; // altigenin oynanıp oynanmadıgına bakıyor
    private Player player = Player.NONE; // oyun sırası kimde
    private final int xIndex, yIndex;

    // altigenin olustugu yer
    public Tile(int xIndex, int yIndex, double x, double y, Board board) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;

        // altigenin koselerinin koordinatlari burada
        getPoints().addAll(
                x, y,
                x + Utils.n, y - Utils.r * 0.5,
                x + Utils.TILE_GENISLIGI, y,
                x + Utils.TILE_GENISLIGI, y + Utils.r,
                x + Utils.n, y + Utils.r * 1.5,
                x, y + Utils.r
        );

        setFill(Color.TRANSPARENT);
        setStrokeWidth(1);
        setStroke(Color.BLACK);
        //ilk basta altıgenler seffaf olacak siyah hatlı olacak


        setOnMouseClicked(event -> {
            board.onTileClicked(this);
            playClickAnimation();
        });
        //altigene tıklanmayı isle ve animasyonu oynat
    }


    public boolean isColored() {
        return isColored;
    }
    // tasın oynanma durumu

    public void setColored(boolean colored) {
        isColored = colored;
    }


    public Player getPlayer() {
        return player;
    }
    // sıra kimde


    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }
    //tasın x y koordinatlerı ve rengini ayarlıyor

    public void setStrokeColor(Color color) {
        setStroke(color);
    }


    private void playClickAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        st.setByX(0.2f);
        st.setByY(0.2f);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }
    //// Tıklandıgında animasyonun calıstıgı yer
}