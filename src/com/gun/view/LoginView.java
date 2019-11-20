package com.gun.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;

public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Log LOGGER = LogFactory.getLog(LoginView.class);
	
	static JTextField textField1 = null;
	
	private static void createAndShowGUI(){
		// 创建及设置窗口
        JFrame frame = new JFrame("登陆");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(300, 200, 593, 483);

        JPanel panel = new JPanel();
        placeComponents(frame,panel);
        // 添加面板
        frame.add(panel);
        
        frame.setVisible(true);
	}
	
	private static void placeComponents(final JFrame frame,JPanel panel){
		panel.setLayout(null);
//    	panel.setBounds(300, 20, 100, 400);
    	
    	JLabel label = new JLabel("帐号");
    	/* 定义组件位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
    	label.setBounds(165, 160, 80, 25);
    	panel.add(label);
    	
    	/**
    	 * 创建文本框
    	 */
    	textField1 = new JTextField(30);
    	
    	textField1.setText("admin");
    	
    	textField1.setBounds(255,160,165,25);
        panel.add(textField1);
        
        JLabel label2 = new JLabel("密码");
        label2.setBounds(165, 195, 80, 25);
    	panel.add(label2);
    	
    	/**
    	 * 创建密码区域
    	 */
    	final JPasswordField jpwf = new JPasswordField(30);
    	jpwf.setBounds(255,195,165,25);
    	
    	jpwf.setText("admin");
    	
        panel.add(jpwf);
        
        // 创建登录按钮
        JButton loginButton = new JButton("登陆");
        loginButton.setBounds(247, 240, 80, 25);
        loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String userAccount = textField1.getText();
				char[] pwd = jpwf.getPassword();
				if(userAccount == null || userAccount.trim().equals("") ){
					JOptionPane.showMessageDialog(null, "请填写帐号!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				if(pwd == null || pwd.length <= 0 ){
					JOptionPane.showMessageDialog(null, "请填写密码!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String password = "";
				for(char c : pwd){
					password += c;
				}
				LOGGER.debug("userName-- "+ userAccount + "pwd --" + pwd);
				Map<String, String> params = new HashMap<>();
				params.put(GUNConstants.USER_ACCOUNT, userAccount);
				params.put(GUNConstants.PASSWORD, password);
				String returnCode = null;
				try {
					returnCode = HttpUtility.post(GUNConstants.URI + "/Gui/User/Login.do?" , params);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
					LOGGER.error("ClientProtocolException error -- ",e1);
					JOptionPane.showMessageDialog(null, "系统异常!", "错误消息", JOptionPane.ERROR_MESSAGE);
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					LOGGER.error("connect POST error -- ",e1);
					JOptionPane.showMessageDialog(null, "无法连接服务器!", "错误消息", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(returnCode != null){
					JsonResponse jr = new JsonResponse(returnCode);
					if(GUNConstants.LOGIN_STATUS_USER_NOT_EXIST == jr.getCode()){
						JOptionPane.showMessageDialog(null, "用户不存在!", "提示消息", JOptionPane.ERROR_MESSAGE);
						return;
					} else if(GUNConstants.LOGIN_STATUS_USER_PASSWORD_ERROR == jr.getCode()){
						JOptionPane.showMessageDialog(null, "密码错误!", "提示消息", JOptionPane.ERROR_MESSAGE);
						return;
					}else if(GUNConstants.STATUS_SUCCESS == jr.getCode()){
						// 登陆成功跳转至首页
						MainView mainView = new MainView();
						mainView.setVisible(true);
						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(null, "未知的系统错误!", "提示消息", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
		});
        panel.add(loginButton);
	}
	
	
	
	public static void main(String[] args) {
		// 标题栏右上方按钮左边的设置按钮
		UIManager.put("RootPane.setupButtonVisible", false);
		try
	    {
			// 添加样式
	        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	    }
	    catch(Exception e)
	    {
	        LOGGER.error("beautyeye error",e);
	    }
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
	
}
