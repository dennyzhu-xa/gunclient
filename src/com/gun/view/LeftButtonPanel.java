package com.gun.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * 无用，待删
 * @author apple
 *
 */
public class LeftButtonPanel extends JPanel {

	public LeftButtonPanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(new TitledBorder("操作命令"));
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(1,4));
		btnPanel.setBackground(Color.WHITE);
		
		JButton btnReset = new JButton("重置");
		JButton btnDel = new JButton("删除器材");
		JButton btnArrange = new JButton("自动排枪");
		JButton btnExport = new JButton("排炮输出");
		
		btnPanel.add(btnReset);
		btnPanel.add(btnDel);
		btnPanel.add(btnArrange);
		btnPanel.add(btnExport);
		
		this.add( btnPanel);
	}
	
}
