package com.gun.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MainView extends JFrame implements ActionListener{

	private JPanel mainPanel ;
	
	private JMenuItem userMaintenanceItem;
	
	private JMenuItem equipmentItem;
	
	private JMenuItem jointItem;
	
	private JMenuItem magazineItem;
	
	private JMenuItem spacerItem;
	
	private JMenuItem perforatingItem;
	
	private JMenuItem prepareMainItem;
	
	
	
	private int height;
	
	private int width;
	
	public MainView(){
		this.setTitle("登陆成功");
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 根据屏幕大小设置当前frame在屏幕上的显示位置，在屏幕宽、高1/6的位置开始展示，frame大小为屏幕的2/3大小
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = (int)screenSize.getHeight()-20;
		width = (int)screenSize.getWidth();
		
		setBounds(0,0,(int)width,(int)height);
		setVisible(true);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBounds(0,0,(int)width-50,(int)height-100);
		JLabel label = new JLabel("  欢迎登入！ ");
		label.setBackground(Color.GRAY);
		mainPanel.add(label,BorderLayout.NORTH);
		mainPanel.setVisible(true);
		
		this.add(mainPanel);
		height = height-120;
		initJMenu();
		
	}
	
	public void initJMenu(){
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu jMenu1 = new JMenu("用户");
		JMenu jMenu2 = new JMenu("基础数据");
		JMenu jMenu3 = new JMenu("排炮");
		JMenu jMenu4 = new JMenu("菜单4");
		
		menuBar.add(jMenu1);
		menuBar.add(jMenu2);
		menuBar.add(jMenu3);
//		menuBar.add(jMenu4);
		
		userMaintenanceItem = new JMenuItem("用户资料");
		
		equipmentItem = new JMenuItem("射孔枪维护");
		jointItem = new JMenuItem("接头维护");
		magazineItem = new JMenuItem("弹架维护");
		spacerItem = new JMenuItem("夹层枪维护");
		perforatingItem = new JMenuItem("射孔弹维护");
		
		prepareMainItem = new JMenuItem("排炮设计");
		
		jMenu1.add(userMaintenanceItem);
		jMenu1.addSeparator();
		
		jMenu2.add(equipmentItem);
		jMenu2.addSeparator();
		jMenu2.add(jointItem);
		jMenu2.addSeparator();
		jMenu2.add(magazineItem);
		jMenu2.addSeparator();
		jMenu2.add(spacerItem);
		jMenu2.addSeparator();
		jMenu2.add(perforatingItem);
		
		jMenu3.add(prepareMainItem);
		
		userMaintenanceItem.addActionListener(this);
		equipmentItem.addActionListener(this);
		jointItem.addActionListener(this);
		magazineItem.addActionListener(this);
		prepareMainItem.addActionListener(this);
		spacerItem.addActionListener(this);
		perforatingItem.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() != null && e.getSource() == userMaintenanceItem){
			// 用户维护功能
			mainPanel.removeAll();
			this.setTitle("员工维护");
			mainPanel.add(new UserMaintenanceView(width,height));
			mainPanel.validate();
			repaint();
		} else if(e.getSource() != null && e.getSource() == equipmentItem){
			mainPanel.removeAll();
			this.setTitle("器材维护");
			mainPanel.add(new EquipmentMaintenanceView(width, height));
			mainPanel.validate();
			repaint();
		} else if(e.getSource() != null && e.getSource() == jointItem){
			mainPanel.removeAll();
			this.setTitle("连接头维护");
			mainPanel.add(new JointMaintenanceView(width, height));
			mainPanel.validate();
			repaint();
		} else if(e.getSource() != null && e.getSource() == magazineItem){
			mainPanel.removeAll();
			this.setTitle("弹架维护");
			mainPanel.add(new MagazineMaintenanceView(width, height));
			mainPanel.validate();
			repaint();
		} else if(e.getSource() != null && e.getSource() == spacerItem){
			mainPanel.removeAll();
			this.setTitle("夹层枪维护");
			mainPanel.add(new SpacerMaintenanceView(width, height));
			mainPanel.validate();
			repaint();
		} else if(e.getSource() != null && e.getSource() == perforatingItem){
			mainPanel.removeAll();
			this.setTitle("射孔弹维护");
			mainPanel.add(new PerforatingProjectileMaintenanceView(width, height));
			mainPanel.validate();
			repaint();
		} else if(e.getSource() != null && e.getSource() == prepareMainItem){
			mainPanel.removeAll();
			this.setTitle("排炮设计");
			mainPanel.add(new PrepareMainView(width, height));
			mainPanel.validate();
			repaint();
		}
	}
	
}
