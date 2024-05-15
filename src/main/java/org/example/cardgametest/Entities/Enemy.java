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
                        enemyGrid)
                );
            }
        }
        return enemyCards;
    }

    //TODO Implement AI logic, higher weight means its more likely to be picked
    public Card getRandomCardFromPlayedCards(Player player) {
        List<Card> playedCards = getPlayedCards();

        // Check if there are any played cards
        if (playedCards.isEmpty()) {
            System.out.println("[BOT] No cards have been played.");
            return null;
        }

        // Pick a random index within the range of played cards list
        int randomIndex = new Random().nextInt(playedCards.size());

        // Return the card at the randomly selected index
        Card randomCard = playedCards.get(randomIndex);
        System.out.println("[BOT] Selected card " + randomCard.getName() + " randomly.");
        return randomCard;
    }
}