/*
 	MAIN FILE 
 */
package com.Main;

import com.Interface.Interface;
import java.awt.Color;
import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		Interface Interface = new Interface();
		Interface.setTitle("Game Launcher");
		Interface.setSize(1200,860);
		Interface.setVisible(true);
		Interface.getContentPane().setBackground(Color.WHITE);
		Interface.setResizable(false);
		Interface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
