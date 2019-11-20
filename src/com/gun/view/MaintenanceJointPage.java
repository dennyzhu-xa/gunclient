package com.gun.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.gun.dto.EquipmentFormDTO;
import com.gun.dto.JointFormDTO;
import com.gun.dto.UserInfoFormDTO;
import com.gun.utility.GBC;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;

public class MaintenanceJointPage extends JDialog {

	private static final Log LOGGER = LogFactory.getLog(MaintenanceJointPage.class);
	
	JTextField jointName;
	JTextField jointExternalDiameter;
	JTextField jointlength;
	JTextField totalQuantity;
	
	
	public MaintenanceJointPage (String id){
		// 创建及设置窗口
		if(id != null && id.length() > 0){
			this.setTitle("修改中接头器材资料");
		} else {
			this.setTitle("新增中接头器材资料");
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
        
        final JPanel detailPanel = new JPanel(new GridBagLayout());
        Border cellBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        int inset = 3;
        JLabel nameLabel = new JLabel("中接头名称");
        nameLabel.setBorder(cellBorder);
        detailPanel.add(nameLabel,new GBC(0,0).setAnchor(GridBagConstraints.EAST));
        jointName = new JTextField(30);
        jointName.setToolTipText("请输入中接头名称");
        detailPanel.add(jointName,new GBC(1,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        
        JLabel externalDiameterLabel = new JLabel("外径(mm)");
        externalDiameterLabel.setBorder(cellBorder);
        detailPanel.add(externalDiameterLabel,new GBC(0,1).setAnchor(GridBagConstraints.EAST));
        jointExternalDiameter = new JTextField(10);
        jointExternalDiameter.setToolTipText("请输入外径");
        detailPanel.add(jointExternalDiameter,new GBC(1,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
       
        JLabel lengthLabel = new JLabel("长度(m)");
        lengthLabel.setBorder(cellBorder);
        detailPanel.add(lengthLabel,new GBC(0,2).setAnchor(GridBagConstraints.EAST));
        jointlength = new JTextField(10);
        jointlength.setToolTipText("请输入长度");
        detailPanel.add(jointlength,new GBC(1,2).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));

        JLabel totalQuantityLabel = new JLabel("数量");
        totalQuantityLabel.setBorder(cellBorder);
        detailPanel.add(totalQuantityLabel,new GBC(0,3).setAnchor(GridBagConstraints.EAST));
        totalQuantity = new JTextField(10);
        totalQuantity.setToolTipText("请输入数量");
        if(id != null && id.length() > 0){
			// 修改页面，数量不能更改
        	totalQuantity.setEditable(false);
		}
        detailPanel.add(totalQuantity,new GBC(1,3).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        
        detailPanel.setVisible(true);
        pane.add(detailPanel, BorderLayout.NORTH);
        // button part
        JPanel btnPanel =  new JPanel();
        btnPanel.setLayout(new FlowLayout());
        JButton saveBtn = new JButton("保存");
        saveBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String equipNameStr = jointName.getText();
				if(equipNameStr == null || equipNameStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写中接头名称!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String externalDiameterStr = jointExternalDiameter.getText();
				if(externalDiameterStr == null || externalDiameterStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写外径!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String lengthStr = jointlength.getText();
				if(lengthStr == null || lengthStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写器材长度!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String totalQuantityStr = totalQuantity.getText();
				if(totalQuantityStr == null || totalQuantityStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写数量!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// 保存资料
				Map<String, String> params = new HashMap<>();
				params.put(JointFormDTO.ATTRIBUTE.ID.getValue(), id);
				params.put(JointFormDTO.ATTRIBUTE.JOINT_NAME.getValue(), equipNameStr);
				params.put(JointFormDTO.ATTRIBUTE.JOINT_EXTERNAL_DIAMETER.getValue(), externalDiameterStr);
				params.put(JointFormDTO.ATTRIBUTE.JOINT_LENGTH.getValue(),lengthStr );
				params.put(JointFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue(),totalQuantityStr );
				
				String result = null;
				try {
					result = HttpUtility.post(GUNConstants.URI + "/Gui/Joint/editJoint.do?" , params);
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
						JOptionPane.showMessageDialog(pane, "保存成功！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
						dialog.dispose();
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
				jointName.setText("");
				jointExternalDiameter.setText("");
				jointlength.setText("");
				if(id == null || id.trim().equals("")){
					totalQuantity.setText("");
				}
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
		params.put(GUNConstants.EQUIPMENT_ID, id);
		
		String result = null;
		try {
			result = HttpUtility.post(GUNConstants.URI + "/Gui/Joint/getJoint.do?" , params);
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
				String equipNameV = jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_NAME.getValue()).toString();
				jointName.setText(equipNameV);
				String externalDiameterV = jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_EXTERNAL_DIAMETER.getValue()).toString();
				jointExternalDiameter.setText(externalDiameterV);
				String lengthV = jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_LENGTH.getValue()).toString();
				jointlength.setText(lengthV);
				String totalQuantityV = jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue()).toString();
				totalQuantity.setText(totalQuantityV);
			}
		}
        
	}
}
