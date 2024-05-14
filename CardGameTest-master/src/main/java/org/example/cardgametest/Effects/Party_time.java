package org.example.cardgametest.Effects;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Party_time extends effect{
    private Scene scene;

    public Party_time(String name, Scene scene){
        super(name);
        this.scene = scene;
    }
    @Override
    public void cardeffect() {
        double a = Math.random();
        double b = Math.random();
        double c = Math.random();
        scene.setFill(Color.color(a, b, c));
    }
}
