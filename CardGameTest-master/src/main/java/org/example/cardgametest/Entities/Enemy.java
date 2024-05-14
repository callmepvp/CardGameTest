package org.example.cardgametest.Entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.GridPane;
import org.example.cardgametest.Card.Card;
import org.example.cardgametest.Card.attackCard;
import org.example.cardgametest.Card.defenseCard;

import java.util.*;

public class Enemy {
    private IntegerProperty hp;
    private int energy;
    private List<Card> deck;
    private List<Card> playedCards;

    public Enemy(int hp, int energy) {
        this.hp = new SimpleIntegerProperty(hp);
        this.energy = energy;
        this.deck = new ArrayList<>();
        this.playedCards = new ArrayList<>();
    }

    public IntegerProperty hpProperty() {
        return hp;
    }

    public int getHp() {
        return hp.get();
    }

    public void setHp(int hp) {
        this.hp.set(hp);
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public void generateRandomDeck(List<Card> allCards) {
        deck.addAll(allCards); // Add all cards to the deck
        Collections.shuffle(deck); // Shuffle the deck
    }

    public List<Card> generateEnemyCards(List<Card> originalCards, GridPane enemyGrid) {
        List<Card> enemyCards = new ArrayList<>();
        for (Card originalCard : originalCards) {
            if (originalCard instanceof attackCard) {
                enemyCards.add(new attackCard(
                        originalCard.getName(),
                        originalCard.getDescription(),
                        originalCard.getEffect(),
                        originalCard.getEnergyCost(),
                        originalCard.getStat(),
                        originalCard.getStatText(),
                        originalCard.getColor(),
                        enemyGrid)
                );
            } else if (originalCard instanceof defenseCard) {
                enemyCards.add(new defenseCard(
                        originalCard.getName(),
                        originalCard.getDescription(),
                        originalCard.getEffect(),
                        originalCard.getEnergyCost(),
                        originalCard.getStat(),
                        originalCard.getStatText(),
                        originalCard.getColor(),
                        enemyGrid)
                );
            }
        }
        return enemyCards;
    }

    //TODO Implement AI logic, higher weight means its more likely to be picked
    public Card getRandomCardFromPlayedCards(Player player) {
        // Get the list of played cards
        List<Card> playedCards = getPlayedCards();
        Map<Card, Double> cardWeights = new HashMap<>();
        System.out.println("[BOT] Searching for the best move...");

        int healthDifference = getHp() - player.getHp();
        for (Card card : playedCards) {
            double weight = 0.0;

            if (card instanceof attackCard) {
                if (healthDifference >= 0) {
                    weight = 1.0;
                } else {
                    weight = 0.5;
                }
            } else if (card instanceof defenseCard) {
                if (healthDifference < 0) {
                    weight = 1.0;
                } else {
                    weight = 0.5;
                }
            }

            // Check if the card already has a weight in the map
            if (cardWeights.containsKey(card)) {
                // Update the existing weight by adding the new weight to it
                weight += cardWeights.get(card);
            }

            // Store the weight for the card
            cardWeights.put(card, weight);
        }

        //Play the highest weighted card
        Card bestCard = null;
        double highestWeight = Double.NEGATIVE_INFINITY;

        // Iterate through the card weights map to find the card with the highest weight
        for (Map.Entry<Card, Double> entry : cardWeights.entrySet()) {
            Card card = entry.getKey();
            double weight = entry.getValue();

            // Check if the current card has a higher weight than the current highest weight
            if (weight > highestWeight) {
                // Update the best card and highest weight
                bestCard = card;
                highestWeight = weight;
            }
        }

        // Return the card with the highest weight
        System.out.println("[BOT] Found card " + Objects.requireNonNull(bestCard).getName() + " with a weight of " + highestWeight);
        return bestCard;
    }
}