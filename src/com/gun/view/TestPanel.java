package com.gun.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.gun.utility.GUNConstants;

public class TestPanel extends JPanel {

	
	public TestPanel(){
		this.setPreferredSize(new Dimension(300,300));
	}
	
	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
		int x=0,y=0;
		boolean flag = true;
		for(int i=0;i<20;i++){
			if(i/2 == 0){
				flag = false;
			} else {
				flag = true;
			}
			drawRect(g,x,y,30,60,flag);
			y += 60;
		}
		x += 60;
		y=0;
		for (int j=0;j<20;j++){
			drawImage(g,"",x,y,30,60);
			y+=60;
		}
	}
	private void drawRect(Graphics g,int x,int y,int width,int height,boolean isFill){
		Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.GRAY);

        if(isFill){
        	 // 2. 填充一个矩形
            g2d.fillRect(x, y, width, height);
        } else {
        	// 1. 绘制一个矩形: 起点(30, 20), 宽80, 高100
            g2d.drawRect(x, y, width, height);
        }
        g2d.dispose();
	}
	
	/**
     * 5. 图片
     */
    private void drawImage(Graphics g,String sourceCode,int x,int y,int width,int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        // 从本地读取一张图片
        String filepath = "";
        if(sourceCode.equals(GUNConstants.ARR_GUN_CONNECTOR)){
        	filepath = "./img/joint.jpg";
        } else if(sourceCode.equals(GUNConstants.ARR_GUN_DOWN_EMPTY)){
        	filepath = "./img/dempty.jpeg";
        } else if(sourceCode.equals(GUNConstants.ARR_GUN_FULL)){
        	filepath = "./img/full.jpeg";
        } else if(sourceCode.equals(GUNConstants.ARR_GUN_UP_EMPTY)){
        	filepath = "./img/uempty.jpeg";
        }  else if(sourceCode.equals(GUNConstants.ARR_GUN_SPACER)){
        	filepath = "./img/youguan.jpg";
        }  else if(sourceCode.equals(GUNConstants.ARR_GUN_HALF_FULL)){
        	filepath = "./img/dempty.jpeg";
        } else {
        	filepath = "./img/empty.jpeg";
        }
        
        Image image = Toolkit.getDefaultToolkit().getImage(filepath);
        // 绘制图片（如果宽高传的不是图片原本的宽高, 则图片将会适当缩放绘制）image.getWidth(this);image.getHeight(this);
        g2d.drawImage(image, x,y,width,height, this);
        g2d.dispose();
    }
}
