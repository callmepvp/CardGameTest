package org.example.cardgametest;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelloApplication extends Application {
    private static final int GRID_SIZE = 5;
    private static final double CARD_WIDTH = 150;
    private static final double CARD_HEIGHT = 200;
    private static final double SPACING = 5;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Group grupp = new Group();
        Scene scene = new Scene(grupp, 1280, 960);

        Image pakk = new Image(new FileInputStream("pakk.png"));
        Image pakk2 = new Image(new FileInputStream("pakkraam.png"));
        ImageView kaardipakkview = new ImageView(pakk);

        GridPane gameBoard = new GridPane();
        gameBoard.setHgap(SPACING);
        gameBoard.setVgap(SPACING);
        gameBoard.setLayoutX(50);
        gameBoard.setLayoutY(150);

        // Create a player object
        Player player = new Player(100, 4);
        Enemy enemy = new Enemy(100, 4);

        // Create a list to hold all available cards
        List<Card> allCards = new ArrayList<>();
        allCards.add(new skillCard("Skill Card 1", "Description of Skill Card 1", 1, gameBoard));
        allCards.add(new defenseCard("Defense Card 1", "Description of Defense Card 1", 2, gameBoard));
        allCards.add(new summonCard("Summon Card 1", "Description of Summon Card 1", 3, gameBoard));
        allCards.add(new attackCard("Attack Card 1", "Description of Attack Card 1", 4, gameBoard));
        allCards.add(new attackCard("Attack Card 2", "Description of Attack Card 2", 4, gameBoard));
        allCards.add(new attackCard("Attack Card 3", "Description of Attack Card 3", 4, gameBoard));
        allCards.add(new attackCard("Attack Card 4", "Description of Attack Card 4", 4, gameBoard));
        allCards.add(new attackCard("Attack Card 5", "Description of Attack Card 5", 4, gameBoard));

        player.generateRandomDeck(allCards);
        player.generateRandomHand(allCards);

        //Setup player and enemy stats
        Text playerHpText = new Text("Player HP: " + player.getHp());
        playerHpText.setFont(Font.font("Arial", 18));
        playerHpText.setFill(Color.WHITE);
        playerHpText.setX(200);
        playerHpText.setY(20);

        Text enemyHpText = new Text("Enemy HP: " + enemy.getHp());
        enemyHpText.setFont(Font.font("Arial", 18));
        enemyHpText.setFill(Color.WHITE);
        enemyHpText.setX(20);
        enemyHpText.setY(20);

        // Update player's HP text
        playerHpText.textProperty().bind(Bindings.concat("Player HP: ").concat(player.hpProperty()));

        // Update enemy's HP text
        enemyHpText.textProperty().bind(Bindings.concat("Enemy HP: ").concat(enemy.hpProperty()));

        //Setup the cards
        List<Card> hand = player.getHand();
        double spacing = (scene.getWidth() - (CARD_WIDTH * hand.size())) / (hand.size() + 1);
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            double x = spacing * (i + 1) + CARD_WIDTH * i;
            double y = scene.getHeight() - 250;
            card.setPosition(x, y);
            card.addToGroup(grupp);

            card.handleMouseHover();
            card.getGroup().setOnMouseClicked(e -> card.handleMouseClicked(e, SPACING, CARD_HEIGHT, CARD_WIDTH, GRID_SIZE, player));
        }

        //Press F to display cards
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F) {
                System.out.println("Player's Deck:");
                for (Card card : player.getDeck()) {
                    System.out.println(card.getName());
                }
                System.out.println("\nPlayer's Hand:");
                for (Card card : player.getHand()) {
                    System.out.println(card.getName());
                }
            }
        });

        kaardipakkview.setX(950);
        kaardipakkview.setY(250);
        kaardipakkview.setFitHeight(350);
        kaardipakkview.setFitWidth(300);

        kaardipakkview.setOnMouseEntered(t -> kaardipakkview.setImage(pakk2));
        kaardipakkview.setOnMouseExited(t -> kaardipakkview.setImage(pakk));

        kaardipakkview.setOnMouseClicked(event -> {
            if (player.getHand().isEmpty() && !player.getDeck().isEmpty()) {
                player.generateRandomHand(allCards);
                List<Card> newHand = player.getHand();
                double spacing2 = (scene.getWidth() - (CARD_WIDTH * newHand.size())) / (newHand.size() + 1);
                for (int i = 0; i < newHand.size(); i++) {
                    Card card = newHand.get(i);
                    double x = spacing2 * (i + 1) + CARD_WIDTH * i;
                    double y = scene.getHeight() - 250;
                    card.setPosition(x, y);
                    card.addToGroup(grupp);

                    card.handleMouseHover();
                    card.getGroup().setOnMouseClicked(e -> card.handleMouseClicked(e, SPACING, CARD_HEIGHT, CARD_WIDTH, GRID_SIZE, player));
                }
            }
        });

        grupp.getChildren().addAll(kaardipakkview, gameBoard, playerHpText, enemyHpText);

        scene.setFill(Color.DEEPSKYBLUE);
        stage.setTitle("Card Game");
        stage.setScene(scene);
        stage.show();
    }
}

abstract class Card {
    private final String name;
    private final String description;
    private final int energyCost;

    private final Group group;
    private final GridPane gameBoard;

    private Popup popup;

    public Card(String name, String description, int energyCost, GridPane gameBoard) {
        this.gameBoard = gameBoard;
        this.name = name;
        this.description = description;
        this.energyCost = energyCost;

        //Setup the card
        Rectangle rectangle = new Rectangle(0, 0, 150, 200);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);

        Text nameText = new Text(name);
        nameText.setFont(Font.font("Arial", 14));
        nameText.setLayoutX(10);
        nameText.setLayoutY(20);

        Text energyCostText = new Text("Cost: " + energyCost);
        energyCostText.setFont(Font.font("Arial", 12));
        energyCostText.setLayoutX(10);
        energyCostText.setLayoutY(180);

        group = new Group(rectangle, nameText, energyCostText);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public Group getGroup() {
        return group;
    }

    public void setPosition(double x, double y) {
        group.setLayoutX(x);
        group.setLayoutY(y);
    }

    public void addToGroup(Group parentGroup) {
        parentGroup.getChildren().add(group);
    }

    private void showPopUpWindow() {
        popup = new Popup();
        VBox popUpContent = new VBox();
        popUpContent.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10px;");
        popUpContent.setAlignment(Pos.CENTER);

        Text nameText = new Text(this.name);
        nameText.setFont(Font.font("Arial", 14));

        Text descriptionText = new Text(this.description);
        descriptionText.setFont(Font.font("Arial", 12));

        Text energyCostText = new Text("Cost: " + this.energyCost);
        energyCostText.setFont(Font.font("Arial", 12));

        popUpContent.getChildren().addAll(nameText, descriptionText, energyCostText);

        popup.getContent().addAll(popUpContent);
        popup.setAutoHide(true);

        double screenWidth = group.getScene().getWindow().getWidth();
        double popupWidth = 200;
        double x = screenWidth - popupWidth + 100;
        double y = 20;

        popup.show(group.getScene().getWindow(), x, y);
    }

    public void handleMouseHover() {
        Timeline timelineEntered = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> showPopUpWindow()));
        timelineEntered.setCycleCount(1);

        group.setOnMouseExited(e -> {
            unhighlight();
            timelineEntered.stop();
        });

        group.setOnMouseEntered(e -> {
            highlight();
            timelineEntered.play();
        });
    }

    public void handleMouseClicked(MouseEvent event, double spacing, double cardHeight, double cardWidth, int gridSize, Player player) {
        int columnIndex = (int) (event.getX() / (cardWidth + spacing));
        int rowIndex = (int) (event.getY() / (cardHeight + spacing));

        Node existingNode = getNodeByRowColumnIndex(rowIndex, columnIndex);
        if (existingNode == null) {
            // Remove the card from the player's hand
            player.getHand().remove(this);

            // Check if the card is already present in the gameBoard
            if (!gameBoard.getChildren().contains(group)) {
                gameBoard.getChildren().add(group);
                GridPane.setColumnIndex(group, columnIndex);
                GridPane.setRowIndex(group, rowIndex);
            }
        } else {
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (getNodeByRowColumnIndex(i, j) == null) {
                        // Remove the card from the player's hand
                        player.getHand().remove(this);

                        // Check if the card is already present in the gameBoard
                        if (!gameBoard.getChildren().contains(group)) {
                            gameBoard.getChildren().add(group);
                            GridPane.setColumnIndex(group, j);
                            GridPane.setRowIndex(group, i);
                        }
                        break;
                    }
                }
            }
        }

        // Disable further clicks on the card
        group.setOnMouseClicked(null);
    }


    private Node getNodeByRowColumnIndex(final int row, final int column) {
        Node result = null;
        ObservableList<Node> children = gameBoard.getChildren();

        for (Node node : children) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);

            if (rowIndex != null && columnIndex != null && rowIndex == row && columnIndex == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    public void highlight() {
        for (Node node : group.getChildren()) {
            if (node instanceof Rectangle rectangle) {
                rectangle.setStroke(Color.YELLOW);
                break;
            }
        }
    }

    public void unhighlight() {
        for (Node node : group.getChildren()) {
            if (node instanceof Rectangle rectangle) {
                rectangle.setStroke(Color.BLACK);
                break;
            }
        }
        if (popup != null && popup.isShowing()) {
            popup.hide();
        }
    }
}

class skillCard extends Card {
    public skillCard(String name, String description, int energyCost, GridPane gameBoard) {
        super(name, description, energyCost, gameBoard);
    }
}

class defenseCard extends Card {
    public defenseCard(String name, String description, int energyCost, GridPane gameBoard) {
        super(name, description, energyCost, gameBoard);
    }
}

class attackCard extends Card {
    public attackCard(String name, String description, int energyCost, GridPane gameBoard) {
        super(name, description, energyCost, gameBoard);
    }
}

class summonCard extends Card {
    public summonCard(String name, String description, int energyCost, GridPane gameBoard) {
        super(name, description, energyCost, gameBoard);
    }
}

class Player {
    private int energy;
    private List<Card> deck;
    private List<Card> hand;

    private IntegerProperty hp;

    public Player(int hp, int energy) {
        this.hp = new SimpleIntegerProperty(hp);
        this.energy = energy;
        this.deck = new ArrayList<>();
        this.hand = new ArrayList<>();
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

    public List<Card> getHand() {
        return hand;
    }

    public void generateRandomHand(List<Card> allCards) {
        Collections.shuffle(deck); // Shuffle the list of all cards
        hand.addAll(deck.subList(0, 4)); // Add the first 4 cards to the hand
        deck.removeAll(hand); // Remove the drawn cards from the deck
    }

    public void generateRandomDeck(List<Card> allCards) {
        deck.addAll(allCards); // Add all cards to the deck
        Collections.shuffle(deck); // Shuffle the deck
    }
}

class Enemy {
    private IntegerProperty hp;
    private int energy;

    public Enemy(int hp, int energy) {
        this.hp = new SimpleIntegerProperty(hp);
        this.energy = energy;
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
}