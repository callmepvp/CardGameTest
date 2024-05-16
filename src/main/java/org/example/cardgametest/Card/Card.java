package org.example.cardgametest.Card;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.example.cardgametest.HelloApplication;
import org.example.cardgametest.Entities.Player;

public abstract class Card {
    private final String name;
    private final String description;
    private final int energyCost;
    private final int stat;
    private final String StatText;
    private final String effect;
    private final Color colour; //all Caps
    private final Group group;
    private final GridPane gameBoard;

    private Popup popup;

    private boolean animationPlaying = false;

    public Card(String name, String description, String effect, int energyCost, int stat, String StatText, Color colour, GridPane gameBoard) {
        this.gameBoard = gameBoard;
        this.name = name;
        this.description = description;
        this.energyCost = energyCost;
        this.stat = stat;
        this.StatText = StatText;
        this.effect= effect;
        this.colour= colour;
        //Setup the card
        Rectangle rectangle = new Rectangle(0, 0, 150, 200);
        rectangle.setFill(this.colour);
        rectangle.setStroke(Color.BLACK);

        Text nameText = new Text(name);
        nameText.setFont(Font.font("Arial", 14));
        nameText.setLayoutX(10);
        nameText.setLayoutY(20);

        Text effectText = new Text(effect);
        effectText.setFont(Font.font("Arial", 14));
        effectText.setLayoutX(10);
        effectText.setLayoutY(50);

        Text energyCostText = new Text("Cost: " + energyCost);
        energyCostText.setFont(Font.font("Arial", 12));
        energyCostText.setLayoutX(10);
        energyCostText.setLayoutY(180);

        Text statText = new Text(StatText);
        statText.setFont(Font.font("Arial", 12));
        statText.setLayoutX(10);
        statText.setLayoutY(160);

        group = new Group(rectangle, nameText, effectText, statText, energyCostText);
    }

    public String getName() {
        return name;
    }

    public String getStatText() {return StatText;}

    public String getDescription() {
        return description;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public Group getGroup() {
        return group;
    }

    public String getEffect() {
        return effect;
    }
    public Color getColor() { return colour;}

    public int getStat() { return stat;}

    public void setPosition(double x, double y) {
        group.setLayoutX(x);
        group.setLayoutY(y);
    }

    public boolean isAnimationPlaying() {
        return animationPlaying;
    }

    public void setAnimationPlaying(boolean clicksEnabled) {
        this.animationPlaying = clicksEnabled;
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

        Text effectText = new Text(this.effect);
        effectText.setFont(Font.font("Arial", 14));

        Text descriptionText = new Text(this.description);
        descriptionText.setFont(Font.font("Arial", 12));

        Text energyCostText = new Text("Cost: " + this.energyCost);
        energyCostText.setFont(Font.font("Arial", 12));

        Text statText = new Text(StatText);
        statText.setFont(Font.font("Arial", 12));

        popUpContent.getChildren().addAll(nameText, effectText, descriptionText, energyCostText, statText);

        popup.getContent().addAll(popUpContent);
        popup.setAutoHide(true);

        double screenWidth = group.getScene().getWindow().getWidth();
        double popupWidth = 200;
        double x = screenWidth - popupWidth + 50;
        double y = 100;

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
            highlight(Color.YELLOW);
            timelineEntered.play();
        });
    }

    public void handleMouseClicked(MouseEvent event, double spacing, double cardHeight, double cardWidth, int gridSize, Player player) {
        int maxCards = 4;
        if (!HelloApplication.isAttackPhase && player.getNumberOfCardsOnGrid() < maxCards && !isAnimationPlaying()) { //Check if the player has played less than 4 cards
            if (player.getEnergy() >= getEnergyCost()) { //Player has enough mana
                player.setEnergy(player.getEnergy() - getEnergyCost()); //Remove the mana amount
                int columnIndex = (int) (event.getX() / (cardWidth + spacing));
                int rowIndex = (int) (event.getY() / (cardHeight + spacing));

                Node existingNode = getNodeByRowColumnIndex(rowIndex, columnIndex);
                if (existingNode == null) {
                    // Remove the card from the player's hand and add it to played cards
                    player.getPlayedCards().add(this);
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
                    player.getPlayedCards().add(this);
                    player.getHand().remove(this);
                }

                group.setOnMouseClicked(null);
            }
        }
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

    public void highlight(Color cl) {
        for (Node node : group.getChildren()) {
            if (node instanceof Rectangle rectangle) {
                rectangle.setStroke(cl);
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