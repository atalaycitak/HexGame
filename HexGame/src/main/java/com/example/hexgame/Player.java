package com.example.hexgame;

import javafx.scene.paint.Color;

public class Player {


    public static final Player NONE = new Player("NONE", Color.TRANSPARENT);

    private final String name;
    private final Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }


    public String getName() {
        return name;
    }


    public Color getColor() {
        return color;
    }
    //oyuncunun adını ve rengini döndüren metodlar bunlar. yukarida isim ve renk belirleniyor.
}