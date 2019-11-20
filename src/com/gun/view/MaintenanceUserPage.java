package com.gun.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.gun.dto.UserInfoFormDTO;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;

public class MaintenanceUserPage extends JDialog {

	private static final Log LOGGER = LogFactory.getLog(MaintenanceUserPage.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField userAccount;
	JTextField userName;
	JTextField email;
	JCheckBox adminRole;
	JCheckBox employRole;
	JCheckBox dataRole;
	
	public MaintenanceUserPage (String id){
		// 创建及设置窗口
		if(id != null && id.length() > 0){
			this.setTitle("修改用户资料");
		} else {
			this.setTitle("新增用户资料");
		}
		setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setBounds(300, 200, 593, 483);

        placeComponents(id,this);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);    // 设置模式类型。
     // 参数 APPLICATION_MODAL：阻塞同一 Java 应用程序中的所有顶层窗口（它自己的子层次结构中的顶层窗口除外）。
        this.setVisible(true);
	}
	
	public void placeComponents(final String id,final JDialog dialog){
		final JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
        pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        
        final JPanel detailPanel = new JPanel(new GridLayout(9, 1, 0, 5));
        detailPanel.add(new JLabel("用户帐号"));
        userAccount = new JTextField(30);
        userAccount.setToolTipText("请输入用户帐号，以便用户登陆系统使用。用户帐号只能为英文+数字格式！");
        if(id != null ){
        	// 修改，userAccount不能修改
        	userAccount.setEditable(false);
        }
        detailPanel.add(userAccount);
        detailPanel.add(new JLabel("用户姓名"));
        userName = new JTextField(30);
        userName.setToolTipText("请输入用户姓名");
        detailPanel.add(userName);
        detailPanel.add(new JLabel("E-mail"));
        email = new JTextField(30);
        email.setToolTipText("请输入");
        detailPanel.add(email);
        detailPanel.add(new JLabel("角色"));
        adminRole = new JCheckBox("系统管理员"); //1
        dataRole = new JCheckBox("入库人员"); //2
        employRole = new JCheckBox("一般员工"); //3
        JPanel roles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roles.add(adminRole);
        roles.add(employRole);
        roles.add(dataRole);
        detailPanel.add(roles);
        
        detailPanel.setVisible(true);
        pane.add(detailPanel, BorderLayout.NORTH);
        // button part
        JPanel btnPanel =  new JPanel();
        btnPanel.setLayout(new FlowLayout());
        JButton saveBtn = new JButton("保存");
        saveBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String userAccountStr = userAccount.getText();
				if(userAccountStr == null || userAccountStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写用户帐号!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String userNameStr = userName.getText();
				if(userNameStr == null || userNameStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写用户姓名!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(!adminRole.isSelected() && !employRole.isSelected() && !dataRole.isSelected()){
					JOptionPane.showMessageDialog(pane, "请选择用户角色!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String emailStr = email.getText();
				String roles = "";
				if(adminRole.isSelected()){
					roles = GUNConstants.ROLE_ADMIN + GUNConstants.SPLIT_DATA_DB;
				}
				if(dataRole.isSelected()){
					roles += GUNConstants.ROLE_INSTORAGE + GUNConstants.SPLIT_DATA_DB;
				}
				if(employRole.isSelected()){
					roles += GUNConstants.ROLE_EMPLOYEE + GUNConstants.SPLIT_DATA_DB;
				}
				// 保存资料
				Map<String, String> params = new HashMap<>();
				params.put(GUNConstants.USER_ID, id);
				params.put(GUNConstants.USER_ACCOUNT, userAccountStr);
				params.put(GUNConstants.USER_NAME, userNameStr);
				params.put(GUNConstants.USER_EMAIL, emailStr);
				params.put(GUNConstants.USER_ROLE, roles);
				
				String result = null;
				try {
					result = HttpUtility.post(GUNConstants.URI + "/Gui/User/EditUserInfo.do?" , params);
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					LOGGER.error("ClientProtocolException error -- ",e1);
					JOptionPane.showMessageDialog(pane, "系统异常!", "错误消息", JOptionPane.ERROR_MESSAGE);
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					LOGGER.error("connect POST error -- ",e1);
					JOptionPane.showMessageDialog(pane, "无法连接服务器!", "错误消息", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(result != null){
					JsonResponse jr = new JsonResponse(result);
					if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
						JOptionPane.showMessageDialog(pane, "保存成功！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
						dialog.dispose();
						return;
					} else if(jr.getCode() == GUNConstants.USER_IN_ALREADY_IN_USE){
						JOptionPane.showMessageDialog(pane, "用户帐号已存在!", "错误消息", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
        	
        });
        btnPanel.add(saveBtn);
        JButton clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(id == null || id.trim().equals("")) userAccount.setText(""); // 新增功能的清除才能清除account
				userName.setText("");
				email.setText("");
				adminRole.setSelected(false);
				employRole.setSelected(false);
				dataRole.setSelected(false);
			}
        });
        btnPanel.add(clearBtn);
        
        pane.add(btnPanel,BorderLayout.SOUTH);
        
        pane.setVisible(true);
        this.add(pane);
        if(id == null || id.trim().equals("")){
        	return;
        }
        Map<String, String> params = new HashMap<>();
		params.put(GUNConstants.USER_ID, id);
		String result = null;
		try {
			result = HttpUtility.post(GUNConstants.URI + "/Gui/User/getUser.do?" , params);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
			JOptionPane.showMessageDialog(pane, "系统异常!", "错误消息", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
			JOptionPane.showMessageDialog(pane, "无法连接服务器!", "错误消息", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(result != null){
			JsonResponse jr = new JsonResponse(result);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				Object r = jr.getResult();
				JSONArray jsonArr = JSONArray.fromObject("["+r+"]");
				String account = jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.USER_ID.getValue()) == null ? "" : (String)jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.USER_ID.getValue());
				String name = jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.USER_NAME.getValue()) == null ? "" : (String)jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.USER_NAME.getValue());
				String mail = (String) (jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.EMAIL.getValue()) == null ? "" : jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.EMAIL.getValue()));
				String roleIds = (String) (jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.ROLE_ID.getValue()) == null ? "" : jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.ROLE_ID.getValue()));
				userAccount.setText(account);
				userName.setText(name);
				email.setText(mail);
				if(roleIds != null && roleIds.length() > 0){
					String[] roleArr = roleIds.split(",");
					for(String rl : roleArr){
						if(rl != null ){
							if(rl.trim().equals("1")){
								adminRole.setSelected(true);
							} else if(rl.trim().equals("2")){
								dataRole.setSelected(true);
							} else if (rl.trim().equals("3")){
								employRole.setSelected(true);
							}
						}
					}
				}
				
			}
		}
        
	}
	
	public static void main(String args[]){
		UIManager.put("RootPane.setupButtonVisible", false);
		try
	    {
			// 添加样式
	        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	    }
	    catch(Exception e)
	    {
//	        LOGGER.error("beautyeye error",e);
	    }
		MaintenanceUserPage page = new MaintenanceUserPage("");
	}
	
}
