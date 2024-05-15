package org.example.cardgametest;

import org.example.cardgametest.Entities.Enemy;
import org.example.cardgametest.Entities.Player;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Stat {
	public static void saveStatsToFile(Player player, Enemy enemy) throws Exception{
		try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("stats.txt"), StandardCharsets.UTF_8))){
			bufferedWriter.write("Player");
			bufferedWriter.newLine();
			bufferedWriter.write("\tDamage done: "+player.getDamageDoneByPlayer());
			bufferedWriter.newLine();
			bufferedWriter.write("\tAttack cards played: "+player.getAttackCardsPlayedByPlayer());
			bufferedWriter.newLine();
			bufferedWriter.write("\tDefense cards played: "+player.getDefenseCardsPlayedByPlayer());
			bufferedWriter.newLine();
			bufferedWriter.write("Enemy");
			bufferedWriter.newLine();
			bufferedWriter.write("\tDamage done: "+enemy.getDamageDoneByEnemy());
			bufferedWriter.newLine();
			bufferedWriter.write("\tAttack cards played: "+enemy.getAttackCardsPlayedByEnemy());
			bufferedWriter.newLine();
			bufferedWriter.write("\tDefense cards played: "+enemy.getDefenseCardsPlayedByPlayer());
		}
	}
}