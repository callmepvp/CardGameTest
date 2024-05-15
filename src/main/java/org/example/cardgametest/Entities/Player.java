package org.example.cardgametest.Entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import org.example.cardgametest.Card.Card;
import org.example.cardgametest.HelloApplication;
import org.example.cardgametest.Card.attackCard;
import org.example.cardgametest.Card.defenseCard;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Player {
    private List<Card> deck;
    private List<Card> hand;
    private List<Card> playedCards;

    private IntegerProperty energy;
    private IntegerProperty hp;

    private int addedDamage = 0;
    private int maxMana = 4;

    public Player(int hp, int energy) {
        this.hp = new SimpleIntegerProperty(hp);
        this.energy = new SimpleIntegerProperty(energy);
        this.deck = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.playedCards = new ArrayList<>();
    }

    public IntegerProperty hpProperty() {
        return hp;
    }

    public IntegerProperty energyProperty() {
        return energy;
    }

    public int getNumberOfCardsOnGrid() {
        return 6 - getHand().size();
    }

    public int getHp() {
        return hp.get();
    }

    public void setHp(int hp) {
        this.hp.set(hp);
    }

    public int getEnergy() {
        return energy.get();
    }

    public void setEnergy(int energy) {
        this.energy.set(energy);
    }

    public int getAddedDamage() {
        return addedDamage;
    }

    public void setAddedDamage(int addedDamage) {
        this.addedDamage = addedDamage;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public void replenishAndIncreaseMana(int toAdd) {
        setMaxMana(getMaxMana() + toAdd);
        setEnergy(getMaxMana());
    }

    public void giveCardFunctionality(Group grupp, Scene scene, Player player) {
        double spacing = (scene.getWidth() - (HelloApplication.CARD_WIDTH * getHand().size())) / (getHand().size() + 1);
        // Clear all mouse click handlers for all cards before setting new ones
        for (Node node : grupp.getChildren()) {
            if (node instanceof Group cardGroup) {
                for (Node cardNode : cardGroup.getChildren()) {
                    if (cardNode instanceof Rectangle) {
                        cardNode.setOnMouseClicked(null);
                    }
                }
            }
        }

        for (int i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            double x = spacing * (i + 1) + HelloApplication.CARD_WIDTH * i;
            double y = scene.getHeight() - 250;
            card.setPosition(x, y);

            //Card is new
            if (!grupp.getChildren().contains(card.getGroup())) {
                grupp.getChildren().add(card.getGroup());

                // Add event handlers
                card.handleMouseHover();
            }
            card.getGroup().setOnMouseClicked(e -> card.handleMouseClicked(e, HelloApplication.SPACING, HelloApplication.CARD_HEIGHT, HelloApplication.CARD_WIDTH, HelloApplication.GRID_SIZE, player));
        }
    }

    public void generateRandomHand() {
        if (getHand().isEmpty()) {
            Collections.shuffle(deck); // Shuffle the list of all cards

            int attackCardsCount = 0;
            int defenseCardsCount = 0;
            for (Card card : deck) {
                // Check if the hand already has three attack and three defense cards
                if (attackCardsCount == 3 && defenseCardsCount == 3) {
                    break;
                }

                // Add the card to the hand if there is space and it matches the criteria
                if (card instanceof attackCard && attackCardsCount < 3) {
                    getHand().add(card);
                    attackCardsCount++;
                } else if (card instanceof defenseCard && defenseCardsCount < 3) {
                    getHand().add(card);
                    defenseCardsCount++;
                }
            }

            getHand().forEach(deck::remove);
        } else {
            int missingAttackCards = 3 - countCardsOfTypeInHand(attackCard.class);
            int missingDefenseCards = 3 - countCardsOfTypeInHand(defenseCard.class);

            for (int i = 0; i < missingAttackCards; i++) {
                // Draw missing attack cards
                Card drawnCard = drawCardOfType(attackCard.class);
                if (drawnCard != null) {
                    getHand().add(drawnCard);
                    deck.remove(drawnCard);
                }
            }

            for (int i = 0; i < missingDefenseCards; i++) {
                // Draw missing defense cards
                Card drawnCard = drawCardOfType(defenseCard.class);
                if (drawnCard != null) {
                    getHand().add(drawnCard);
                    deck.remove(drawnCard);
                }
            }
        }
    }

    // Method to count the number of cards of a specific type in the hand
    private int countCardsOfTypeInHand(Class<? extends Card> cardType) {
        int count = 0;
        for (Card card : getHand()) {
            if (cardType.isInstance(card)) {
                count++;
            }
        }
        return count;
    }

    // Method to draw a card of a specific type from the deck
    private Card drawCardOfType(Class<? extends Card> cardType) {
        List<Card> validCards = new ArrayList<>();
        for (Card card : getDeck()) {
            if (cardType.isInstance(card)) {
                validCards.add(card);
            }
        }
        if (!validCards.isEmpty()) {
            return validCards.get(new Random().nextInt(validCards.size()));
        } else {
            return null;
        }
    }

    public void generateRandomDeck(List<Card> allCards) {
        deck.addAll(allCards); // Add all cards to the deck
        Collections.shuffle(deck); // Shuffle the deck
    }
}
