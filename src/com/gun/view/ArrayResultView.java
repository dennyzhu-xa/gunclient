package com.gun.view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.gun.dto.ArrGunFormDTO;

public class ArrayResultView extends JFrame {

	
	
	public ArrayResultView (int width,int height,ArrGunFormDTO formDTO){
		
		JFrame frame = new JFrame("排枪");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(300, 200, 593, 483);

        JPanel panel = new JPanel();
        
        GraphScrollPanel lable = new GraphScrollPanel(593, 483, formDTO);
//        lable.setBounds(300, 200, 593, 483);
        panel.add(lable);
        // 添加面板
        frame.add(panel);
        
        frame.setVisible(true);
		
		
	}
	
	public static void main(String args[]){
		// 标题栏右上方按钮左边的设置按钮
		UIManager.put("RootPane.setupButtonVisible", false);
		try
	    {
			// 添加样式
	        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	    }
	    catch(Exception e)
	    {
	    }
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	MainView mv = new MainView();
            }
        });
	}
	
	public ArrayResultView (){
		
		JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(300, 200, 593, 300);

        JPanel panel = new JPanel();
        
        JScrollPane scp = new JScrollPane();
        scp.setPreferredSize(new Dimension(593,300));
        TestPanel scPanel = new TestPanel();
        scp.setViewportView(scPanel);
        scp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scp);
        // 添加面板
        frame.add(panel);
        
        frame.setVisible(true);
		
		
	}
	
}