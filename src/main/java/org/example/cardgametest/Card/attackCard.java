package org.example.cardgametest.Card;

import javafx.scene.layout.GridPane;

public class attackCard extends Card {
    public attackCard(String name, String description, String effect, int energyCost, int stat, String StatText, GridPane gameBoard) {
        super(name, description, effect, energyCost, stat, StatText, gameBoard);
    }
}
