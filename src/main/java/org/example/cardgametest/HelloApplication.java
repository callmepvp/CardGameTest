package org.example.cardgametest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.cardgametest.Card.Card;
import org.example.cardgametest.Card.attackCard;
import org.example.cardgametest.Card.defenseCard;
import org.example.cardgametest.Effects.Hardened;
import org.example.cardgametest.Effects.Sharpened;
import org.example.cardgametest.Effects.effect;
import org.example.cardgametest.Entities.Enemy;
import org.example.cardgametest.Entities.PhaseNode;
import org.example.cardgametest.Entities.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class HelloApplication extends Application {
    public static final int GRID_SIZE = 5;
    public static final double CARD_WIDTH = 150;
    public static final double CARD_HEIGHT = 200;
    public static final double SPACING = 5;

    private boolean playerTurn = true; //Whose turn it is
    public static boolean isAttackPhase = false;

    private boolean playerPlacedCards = false;
    private boolean enemyPlacedCards = false;

    private static final String[] nimedcollec = {"Stab", "Kick", "Slash", "Punch", "Poke", "Hit", "Bash", "Deadly maul", "Ambush", "Backstab"};
    private static final  String[] vnimedcollec = {"Obliterate", "Annihilate", "Disintegration ray", "Collapse", "Marked for death"};
    private static final String[] knimedcollec = {"Defend", "Shield", "Barrier", "Block", "Dodge", "Barricade", "Endure"};
    private static final String[] kvnimedcollec = {"Paladin's favor", "Absolute cancel", "Saving grace", "Grit", "Unbreakable fortress"};

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        //Main variables and things
        Group grupp = new Group();

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(grupp, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
        stage.setMaximized(true);
        stage.initStyle(StageStyle.UNDECORATED);

        Image pakk = new Image(new FileInputStream("pakk.png"));
        ImageView kaardipakkview = new ImageView(pakk);
        Media media = new Media(Paths.get("tavern.mp3").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));

        MediaView mediaView = new MediaView(mediaPlayer);

        GridPane playerGrid = new GridPane();
        GridPane enemyGrid = new GridPane();
        playerGrid.setHgap(SPACING);
        playerGrid.setVgap(SPACING);
        enemyGrid.setHgap(SPACING);
        enemyGrid.setVgap(SPACING);

        playerGrid.setLayoutX(50); // Adjust the X position as needed
        playerGrid.setLayoutY(400); // Adjust the Y position as needed
        enemyGrid.setLayoutX(50); // Adjust the X position as needed
        enemyGrid.setLayoutY(50);

        // Create a player object
        Player player = new Player(100, 5);
        Enemy enemy = new Enemy(100, 4);

        Runnable onExitFunction = () -> {
            System.out.println("Program is exiting. Performing cleanup...");
            try {
                Stat.saveStatsToFile(player, enemy);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        // Add the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(onExitFunction));

        // Create cardstacks for attacking and defending
        List<attackCard> attackCards = new ArrayList<>();
        List<defenseCard> defenseCards = new ArrayList<>();

        List<effect> AttackEffects = new ArrayList<>();
        List<effect> DefenseEffects = new ArrayList<>();
        AttackEffects.add(new Sharpened("Sharpened(+2)"));
        DefenseEffects.add(new Hardened("Hardened(+2)"));

        List<String> ründenimed = new ArrayList<>();
        ründenimed.addAll(Arrays.asList(nimedcollec));

        List<String> vingedründenimed = new ArrayList<>();
        vingedründenimed.addAll(Arrays.asList(vnimedcollec));

        List<String> kaitsenimed = new ArrayList<>();
        kaitsenimed.addAll(Arrays.asList(knimedcollec));

        List<String> kvingedkaitsenimed = new ArrayList<>();
        kvingedkaitsenimed.addAll(Arrays.asList(kvnimedcollec));

        CardGenerator cardGenerator = new CardGenerator(playerGrid, AttackEffects, DefenseEffects, ründenimed, vingedründenimed, kaitsenimed, kvingedkaitsenimed);
        List<Card> allCards = cardGenerator.Generate();

        player.generateRandomDeck(allCards);
        player.generateRandomHand();
        player.giveCardFunctionality(grupp, scene, player);

        enemy.generateRandomDeck(enemy.generateEnemyCards(allCards, enemyGrid));

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

        //Update the player's energy
        Text playerEnergyText = new Text("Player Energy: " + player.getEnergy());
        playerEnergyText.setFont(Font.font("Arial", 18));
        playerEnergyText.setFill(Color.WHITE);
        playerEnergyText.setX(380);
        playerEnergyText.setY(20);

        playerEnergyText.textProperty().bind(Bindings.concat("Player Energy: ").concat(player.energyProperty()));

        // Next Phase Button
        Button nextPhaseButton = new Button("NEXT PHASE");
        nextPhaseButton.setFont(Font.font(18));
        nextPhaseButton.setLayoutX(scene.getWidth() - 150);
        nextPhaseButton.setLayoutY(20);
        nextPhaseButton.setDisable(true); // Initially disable the button

        nextPhaseButton.setOnAction(event -> {
            if (playerTurn) {
                // Player's turn
                nextPhaseButton.setDisable(true);
                if (!isAttackPhase) {
                    // If it's not the attack phase, play the enemy turn
                    nextPhaseButton.setDisable(true);
                    playEnemyTurn(enemyGrid, enemy, player);
                    playerTurn = true;
                    if (playerPlacedCards && enemyPlacedCards) {
                        // If both player and enemy have placed cards, transition to attack phase
                        isAttackPhase = true;
                        enableAttackPhase(player, playerGrid, enemy, enemyGrid, grupp, scene);
                    } else {
                        enablePlayerTurn(player);
                    }
                } else {
                    // If it's the attack phase, enable the player's attack phase
                    nextPhaseButton.setDisable(true);
                    enableAttackPhase(player, playerGrid, enemy, enemyGrid, grupp, scene);
                }
            } else {
                // Enemy's turn
                nextPhaseButton.setDisable(true);
                if (!isAttackPhase) {
                    // If it's not the attack phase, play the enemy turn
                    playerTurn = true;
                    if (playerPlacedCards && enemyPlacedCards) {
                        // If both player and enemy have placed cards, transition to attack phase
                        isAttackPhase = true;
                        nextPhaseButton.setDisable(true);
                        enableAttackPhase(player, playerGrid, enemy, enemyGrid, grupp, scene);
                    } else {
                        enablePlayerTurn(player);
                    }
                } else {
                    // If it's the attack phase, enable the player's attack phase
                    nextPhaseButton.setDisable(true);
                    enableAttackPhase(player, playerGrid, enemy, enemyGrid, grupp, scene);
                }
            }
        });

        enablePlayerTurn(player); //Player goes first

        // Enable the button only if the player has placed exactly 4 cards
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            if (playerTurn && player.getNumberOfCardsOnGrid() == 1 && !isAttackPhase) {
                nextPhaseButton.setDisable(false);
            }
        });

        kaardipakkview.setX(primaryScreenBounds.getWidth()-200);
        kaardipakkview.setY(250);
        kaardipakkview.setFitHeight(250);
        kaardipakkview.setFitWidth(150);

        grupp.getChildren().addAll(nextPhaseButton, kaardipakkview, playerGrid, enemyGrid, playerHpText, enemyHpText, playerEnergyText, mediaView);

        Font font = Font.font(32);
        Image img = new Image(new FileInputStream("nupp.png"));
        Image img2 = new Image(new FileInputStream("tiitel.png"));

        ImageView view2 = new ImageView(img2);
        view2.setPreserveRatio(true);
        view2.setFitHeight(0);

        HBox pilt = new HBox();
        pilt.getChildren().add(view2);
        pilt.setAlignment(Pos.TOP_CENTER);

        ImageView view = new ImageView(img);

        view.setPreserveRatio(true);

        Button start = new Button("");
        start.setFont(font);
        start.setOnAction(event -> {
            Image backgroundImage;
            try {
                backgroundImage = new Image(new FileInputStream("bg1.png"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            ImageView imageView = new ImageView(backgroundImage);

            // Set the dimensions of the ImageView to match the desired background size
            imageView.setFitWidth(scene.getWidth());
            imageView.setFitHeight(scene.getHeight());

            // Create a BackgroundImage
            BackgroundImage background = new BackgroundImage(
                    imageView.snapshot(null, null), // Use snapshot to capture the ImageView with its updated dimensions
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            scene.getWidth(), scene.getHeight(),
                            true, true,
                            true, true
                    )
            );

            // Create a Background
            Background bg = new Background(background);

            // Create a root pane and set the background
            Pane rootPane = new Pane();
            rootPane.setBackground(bg);
            rootPane.getChildren().add(grupp);

            // Update the scene's root with the new rootPane
            scene.setRoot(rootPane);
            stage.setTitle("Card Game");
            stage.setScene(scene);
            stage.show();
            mediaPlayer.play();
        });

        start.setPrefSize(430, 300);
        start.setGraphic(view);
        start.setPadding(Insets.EMPTY);

        VBox vbox = new VBox(50, start);
        vbox.setTranslateX(0);
        vbox.setTranslateY(50);
        vbox.setSpacing(0);
        vbox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();

        stackPane.getChildren().add(pilt);
        stackPane.getChildren().add(vbox);


        stackPane.setStyle("-fx-background-color:BLACK");

        Scene menuscene = new Scene(stackPane, 1000, 700);

        stage.setResizable(false);
        stage.setTitle("Main menu");
        stage.setScene(menuscene);
        stage.show();
    }

    //Other methods
    private void enablePlayerTurn(Player player) {
        System.out.println("Player turn.");
        // Enable the player to play cards
        for (Card card : player.getHand()) {
            card.getGroup().setOnMouseClicked(e -> {
                if (playerTurn) { // Allow playing cards only during player's turn
                    card.handleMouseClicked(e, SPACING, CARD_HEIGHT, CARD_WIDTH, GRID_SIZE, player);
                    // Check if the player has played 4 cards
                    if (player.getNumberOfCardsOnGrid() >= 4) {
                        // Disable further card playing
                        for (Card c : player.getHand()) {
                            c.getGroup().setOnMouseClicked(null);
                        }
                    }
                }
            });
        }
        playerPlacedCards = true;
    }

    private void playEnemyTurn(GridPane enemyGrid, Enemy enemy, Player player) {
        System.out.println("Enemy turn.");
        int cardsToPlay = player.getNumberOfCardsOnGrid();

        for (int i = 0; i < cardsToPlay; i++) {
            // Get the card to play
            Card cardToPlay = enemy.getDeck().get(i);

            // Find the next empty slot in the enemy grid
            int row = 0;
            int column = 0;
            while (getNodeByRowColumnIndex(enemyGrid, row, column) != null) {
                column++;
                if (column >= GRID_SIZE) {
                    column = 0;
                    row++;
                }
                if (row >= GRID_SIZE) {
                    // The grid is full, break out of the loop
                    break;
                }
            }

            // Add the card to the enemy grid
            if (row < GRID_SIZE) {
                enemyGrid.add(cardToPlay.getGroup(), column, row);
                enemy.getPlayedCards().add(cardToPlay);

            }
        }
        enemyPlacedCards = true;
    }

    private Node getNodeByRowColumnIndex(GridPane gridPane, int row, int column) {
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);

            if (rowIndex != null && columnIndex != null && rowIndex == row && columnIndex == column) {
                return node;
            }
        }

        return null;
    }

    private void enableAttackPhase(Player player, GridPane playerGrid, Enemy enemy, GridPane enemyGrid, Group grupp, Scene scene) {
        System.out.println("Attack phase.");

        // Disable player's ability to play cards during attack phase
        disableCardClicks(player.getHand());

        // Iterate over each node in the playerGrid
        for (Node node : playerGrid.getChildren()) {
            if (node instanceof Group group) {
                for (Node cardNode : group.getChildren()) {
                    if (cardNode instanceof Rectangle) {
                        // Access the associated Card instance
                        Card card = getCardFromGroup(group, player.getPlayedCards());
                        if (!player.getHand().contains(card)) {
                            if (card != null) {

                                // Attach event listener to handle mouse clicks for played cards
                                cardNode.setOnMouseClicked(event -> {
                                    disableCardClicks(player.getPlayedCards());
                                    if (!player.getPlayedCards().getFirst().isAnimationPlaying()) {
                                        System.out.println("Played card: " + card.getName());
                                        System.out.println("Queuing animations..."); //Here goes the visual cues of the cards

                                        //Enemy automatically plays a card in return
                                        Card enemyCard = enemy.getRandomCardFromPlayedCards(player);

                                        //Perform both the player and enemy card actions
                                        if (enemyCard != null) {
                                            System.out.println("Enemy played card: " + enemyCard.getName());

                                            //Check the combination of the played cards
                                            //CASE 1 - Attack and attack
                                            if (card instanceof attackCard && enemyCard instanceof attackCard) {
                                                int playerAttack = card.getStat();
                                                int enemyAttack = enemyCard.getStat();

                                                if (!card.getEffect().equals("No effect")) {
                                                    if (card.getEffect().equals("Sharpened(+2)")) {
                                                        playerAttack +=2;
                                                    }
                                                }

                                                player.setHp(player.getHp() - enemyAttack);
                                                enemy.setHp(enemy.getHp() - playerAttack);

                                                // Stats
                                                player.updateDamageStat(playerAttack);
                                                enemy.updateDamageStat(enemyAttack);

                                                System.out.println("Player took " + enemyAttack + " damage!");
                                                System.out.println("Enemy took " + playerAttack + " damage!");

                                                //Animation
                                                PhaseNode pn = new PhaseNode(enemyAttack, true, playerAttack, true);
                                                pn.setLayoutX(scene.getWidth()/2 - pn.getMaxWidth()/3);
                                                pn.setLayoutY(scene.getHeight()/2 - pn.getMaxHeight()/2);
                                                grupp.getChildren().add(pn);

                                                for (Card c : player.getPlayedCards()) {
                                                    c.setAnimationPlaying(true);
                                                }
                                                pn.startTimer(grupp, player, scene, player.getPlayedCards());
                                            }

                                            //CASE 2 - Defense and defense
                                            if (card instanceof defenseCard && enemyCard instanceof defenseCard) {
                                                int playerDefense = card.getStat();
                                                int enemyDefense = enemyCard.getStat();

                                                if (!card.getEffect().equals("No effect")) {
                                                    if (card.getEffect().equals("Hardened(+2)")){
                                                        playerDefense += 2;
                                                    }
                                                }

                                                // Sum up total defense
                                                int totalDefense = playerDefense + enemyDefense;
                                                player.setAddedDamage(totalDefense);

                                                player.updateDefenseCardsPlayedByPlayer();
                                                enemy.updateDefenseCardsPlayedByAI();

                                                System.out.println("Total bonus to next upcoming attack: " + totalDefense);

                                                //Animation
                                                PhaseNode pn = new PhaseNode(enemyDefense, false, playerDefense, false);
                                                pn.setLayoutX(scene.getWidth()/2 - pn.getMaxWidth()/3);
                                                pn.setLayoutY(scene.getHeight()/2 - pn.getMaxHeight()/2);
                                                grupp.getChildren().add(pn);

                                                for (Card c : player.getPlayedCards()) {
                                                    c.setAnimationPlaying(true);
                                                }
                                                pn.startTimer(grupp, player, scene, player.getPlayedCards());
                                            }

                                            //CASE 3 - Attack and defense
                                            if ((card instanceof attackCard && enemyCard instanceof defenseCard) ||
                                                    (card instanceof defenseCard && enemyCard instanceof attackCard)) {
                                                int playerAttack;
                                                int enemyDefense;
                                                PhaseNode pn;

                                                // Determine attacker and defender
                                                Card attacker;
                                                Card defender;
                                                if (card instanceof attackCard) {
                                                    attacker = card;
                                                    defender = enemyCard;
                                                } else {
                                                    attacker = enemyCard;
                                                    defender = card;
                                                }

                                                // Calculate attack and defense stats
                                                playerAttack = attacker.getStat();
                                                enemyDefense = defender.getStat();

                                                // Calculate damage and chip damage
                                                int damage = (playerAttack + player.getAddedDamage() - enemyDefense);
                                                player.setAddedDamage(0);

                                                if (damage > 0) {
                                                    System.out.println("Damage dealt: " + damage);
                                                    if (attacker == card) {
                                                        // Player attacked, so enemy takes damage
                                                        if (enemyCard.getEffect().equals("Hardened(+2)")){
                                                            damage -= 2;
                                                        }
                                                        if (card.getEffect().equals("Sharpened(+2)")) {
                                                            damage += 2;
                                                        }
                                                        enemy.setHp(enemy.getHp() - damage);
                                                        enemy.updateDefenseCardsPlayedByAI();
                                                        player.updateDamageStat(playerAttack+enemyDefense);
                                                        System.out.println("Enemy takes " + damage + " damage.");
                                                    } else {
                                                        // Enemy attacked, so player takes damage
                                                        if (card.getEffect().equals("Hardened(+2)")){
                                                            damage -= 2;
                                                        } if (enemyCard.getEffect().equals("Sharpened(+2)")) {
                                                            damage += 2;
                                                        }
                                                        player.setHp(player.getHp() - damage);
                                                        enemy.updateDamageStat(damage+enemyDefense);
                                                        player.updateDefenseCardsPlayedByPlayer();
                                                        System.out.println("Player takes " + damage + " damage.");
                                                    }
                                                } else {
                                                    System.out.println("No damage dealt.");
                                                }

                                                if (attacker == card) {
                                                    pn = new PhaseNode(enemyDefense, false, playerAttack, true);
                                                } else {
                                                    pn = new PhaseNode(enemyDefense, true, playerAttack, false);
                                                }

                                                //Animation
                                                pn.setLayoutX(scene.getWidth()/2 - pn.getMaxWidth()/3);
                                                pn.setLayoutY(scene.getHeight()/2 - pn.getMaxHeight()/2);
                                                grupp.getChildren().add(pn);

                                                for (Card c : player.getPlayedCards()) {
                                                    c.setAnimationPlaying(true);
                                                }
                                                pn.startTimer(grupp, player, scene, player.getPlayedCards());
											}

                                            //Deal with removing the cards
                                            playerGrid.getChildren().remove(card.getGroup());
                                            player.getPlayedCards().remove(card);
                                            grupp.getChildren().remove(card.getGroup());

                                            enemyGrid.getChildren().remove(enemyCard.getGroup());
                                            enemy.getPlayedCards().remove(enemyCard);
                                            grupp.getChildren().remove(enemyCard.getGroup());

                                            //Check HP
											try {
												checkHealth(player, enemy);
											} catch (Exception ignored) {
											}
										}

                                        //Check if the grid is now empty, attackPhase is over
                                        if (player.getPlayedCards().isEmpty() || enemy.getPlayedCards().isEmpty()) {
                                            isAttackPhase = false;
                                            playerPlacedCards = false;
                                            enemyPlacedCards = false;
                                            System.out.println("Switching back to placement phase.");

                                            //Do the after-attack-phase cleanup (eg. clear lists, give mana, give new cards)
                                            player.getPlayedCards().clear();
                                            enemy.getPlayedCards().clear();

                                            //Give back missing cards
                                            player.generateRandomHand();
                                            player.giveCardFunctionality(grupp, scene, player);

                                            //Give back mana and update max mana
                                            int maxManaRegainAmount = 2;
                                            player.replenishAndIncreaseMana(maxManaRegainAmount);
                                            player.setAddedDamage(0);

                                            enablePlayerTurn(player);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    // Helper method to get the Card object associated with a Group
    private Card getCardFromGroup(Group group, List<Card> hand) {
        for (Card card : hand) {
            if (card.getGroup() == group) {
                return card;
            }
        }
        return null;
    }

    private void disableCardClicks(List<Card> cards) {
        for (Card card : cards) {
            card.getGroup().setOnMouseClicked(null);
        }
    }

    private void checkHealth(Player player, Enemy enemy) {
        if (player.getHp() <= 0) {
            System.out.println("Player has been defeated. Exiting game...");
            Platform.exit();
        } else if (enemy.getHp() <= 0) {
            System.out.println("Enemy has been defeated. Exiting game...");
            Platform.exit();
        }
    }
}