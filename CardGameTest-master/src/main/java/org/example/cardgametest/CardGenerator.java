package org.example.cardgametest;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import org.example.cardgametest.Card.Card;
import org.example.cardgametest.Card.attackCard;
import org.example.cardgametest.Card.defenseCard;
import org.example.cardgametest.Effects.effect;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CardGenerator {
    private GridPane gameBoard;
    private List<effect> AttackEffects;
    //AttackEffects.add()
    private List<effect> DefenseEffects;
    private List<String> ründenimed;
    private List<String> vingedründenimed;
    private List<String> kaitsenimed;
    private List<String> vingedkaitsenimed;

    public CardGenerator(GridPane gameBoard, List<effect> attackEffects, List<effect> defenseEffects, List<String> ründenimed, List<String> vingedründenimed, List<String> kaitsenimed, List<String> vingedkaitsenimed) {
        this.AttackEffects = attackEffects;
        this.DefenseEffects = defenseEffects;
        this.ründenimed = ründenimed;
        this.vingedründenimed = vingedründenimed;
        this.kaitsenimed = kaitsenimed;
        this.vingedkaitsenimed = vingedkaitsenimed;
        this.gameBoard = gameBoard;
    }
    public List<Card> Generate(){
        List<Card> allcards = new ArrayList<>();
        int attack;
        int defense;
        int attackenergy;
        int defenseenergy;
        double kaseffekt;

        for (int i = 0; i < 32; i++) {
            attack = (int) (Math.random() *10) +1;
            defense = (int) (Math.random() *10) +1;
            kaseffekt = Math.random()*10;
            Collections.shuffle(ründenimed);
            Collections.shuffle(vingedründenimed);
            Collections.shuffle(kaitsenimed);
            Collections.shuffle(vingedkaitsenimed);
            attackCard a;
            defenseCard b;
            attackenergy = (int) (attack/2);
            defenseenergy = (int) (defense/2);
            Collections.shuffle(AttackEffects);
            Collections.shuffle(DefenseEffects);
            if(attack <= 5){
                if (kaseffekt <= 2){
                    a = new attackCard(ründenimed.get(0), "Yap Yap Yap", AttackEffects.get(0).getName(), attackenergy, attack, "Attack: " + attack, Color.LIGHTCORAL, gameBoard);
                } else {
                    a = new attackCard(ründenimed.get(0), "Yap Yap Yap", "No effect", attackenergy, attack, "Attack: " + attack, Color.LIGHTCORAL, gameBoard);
                }
            } else{
                if (kaseffekt <= 5){
                    a = new attackCard(vingedründenimed.get(0),"Yap Yap Yap", AttackEffects.get(0).getName(), attackenergy, attack,"Attack: " + attack, Color.LIGHTCORAL, gameBoard);
                } else {
                    a = new attackCard(vingedründenimed.get(0), "Yap Yap Yap", "No effect", attackenergy, attack, "Attack: " + attack, Color.LIGHTCORAL, gameBoard);
                }
            } if(defense <= 5){
                if (kaseffekt <= 2) {
                    b = new defenseCard(kaitsenimed.get(0), "Yap Yap Yap", DefenseEffects.get(0).getName(), defenseenergy, defense, "Defense: " + defense, Color.LIGHTBLUE, gameBoard);
                } else {
                    b = new defenseCard(kaitsenimed.get(0), "Yap Yap Yap", "No effect", defenseenergy, defense, "Defense: " + defense, Color.LIGHTBLUE, gameBoard);
                }
            } else{
                if (kaseffekt <= 5) {
                    b = new defenseCard(vingedkaitsenimed.get(0), "Yap Yap Yap", DefenseEffects.get(0).getName(), defenseenergy, defense, "Defense: " + defense, Color.LIGHTBLUE, gameBoard);
                } else {
                    b = new defenseCard(vingedkaitsenimed.get(0), "Yap Yap Yap", "No effect", defenseenergy, defense, "Defense: " + defense, Color.LIGHTBLUE, gameBoard);
                }
            }
            allcards.add(a);
            allcards.add(b);

        }
        return allcards;
    }
}

