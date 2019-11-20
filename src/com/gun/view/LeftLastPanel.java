package com.gun.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class LeftLastPanel extends JPanel {

	public LeftLastPanel(){
		
		this.setLayout(null);
		Border board = BorderFactory.createLineBorder(Color.gray,1);
		this.setBorder(board);
		this.setBackground(Color.WHITE);
		
		JLabel jlabel = new JLabel("待选射孔器材");
		jlabel.setBounds(10, 10, 200, 30);
		jlabel.setFont(new Font(null,Font.BOLD,15));
		jlabel.setForeground(Color.BLACK);
		jlabel.setVisible(true);
		this.add(jlabel);
	}
}
