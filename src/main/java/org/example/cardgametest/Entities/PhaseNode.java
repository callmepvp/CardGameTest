package org.example.cardgametest.Entities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.cardgametest.Card.Card;

import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PhaseNode extends VBox {
	public PhaseNode(int aiNum, boolean aiIsAttacking, int playerNum, boolean playerIsAttacking) {
		Font font1 = new Font(72);
		Font font2 = new Font(16);

		Image image = new Image(Paths.get("swordClash.gif").toUri().toString());
		ImageView imageView = new ImageView(image);
		Text leftText = new Text(String.valueOf(aiNum));
		leftText.setFont(font1);
		leftText.setFill(aiIsAttacking ? Color.RED : Color.BLUE);
		Text rightText = new Text(String.valueOf(playerNum));
		rightText.setFont(font1);
		rightText.setFill(playerIsAttacking ? Color.RED : Color.BLUE);
		HBox level1 = new HBox(leftText, imageView, rightText);
		level1.setSpacing(20);
		Text aiText = new Text("(Left) AI IS " + (aiIsAttacking ? "ATTACKING" : "DEFENDING"));
		Text playerText = new Text("(Right) PLAYER IS " + (playerIsAttacking ? "ATTACKING" : "DEFENDING"));

		level1.setAlignment(Pos.CENTER);

		aiText.setFont(font2);
		playerText.setFont(font2);
		aiText.setFill(aiIsAttacking ? Color.RED : Color.BLUE);
		playerText.setFill(playerIsAttacking ? Color.RED : Color.BLUE);

		this.getChildren().addAll(level1, aiText, playerText);

		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: #FFFFFF;");

		this.setMaxWidth(500);
		this.setMaxHeight(200);
	}

	public void startTimer(Group group, Player player, Scene scene, List<Card> cards) {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
			group.getChildren().remove(PhaseNode.this);
			//player.giveCardFunctionality(group, scene, player);
			for (Card c : cards) {
				c.setAnimationPlaying(false);
			}
		}));
		timeline.play();
	}
}
