package com.gun.view;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;

import com.gun.dto.ArrGunFormDTO;

public class GraphScrollPanel extends JScrollPane implements MouseListener{

	/**
	 * 
	 * @param w 窗体宽
	 * @param h 窗体高
	 */
	public GraphScrollPanel(int w,int h,ArrGunFormDTO formDTO){
		if(formDTO == null) return;
		
		this.setPreferredSize(new Dimension(w,h));
		
		PrePanel jpanel = new PrePanel(w,h,formDTO);
		setViewportView(jpanel);
		
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
