package com.gun.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import com.gun.dto.ArrGunFormDTO;
import com.gun.dto.OilLevelDTO;

public class IntegPanel extends JPanel{

	public IntegPanel(int w,int h,ArrGunFormDTO formDTO){
	
		super(new GridLayout(1,3));
		
		this.setPreferredSize(new Dimension(w,h));
		
//		PrePanel pre = new PrePanel(w,h,formDTO);
//		pre.setBounds(0,0,w/3,h);
//		pre.setVisible(true);
//		this.add(pre);
		
	}
	
}
