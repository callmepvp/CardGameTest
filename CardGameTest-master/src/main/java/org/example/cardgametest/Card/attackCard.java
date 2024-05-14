package org.example.cardgametest.Card;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class attackCard extends Card {
    public attackCard(String name, String description, String effect, int energyCost, int stat, String StatText, Color colour, GridPane gameBoard) {
        super(name, description, effect, energyCost, stat, StatText, colour, gameBoard);
    }
}

