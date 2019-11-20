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

import com.gun.dto.MagazineFormDTO;
import com.gun.utility.GBC;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;

public class MaintenanceMagazinePage extends JDialog {

	private static final Log LOGGER = LogFactory.getLog(MaintenanceMagazinePage.class);
	
	JTextField equipName;
	JTextField equipModel;
	JTextField equipSpec;
	JTextField equipUnit;
	JTextField equipModelName;
	JTextField stockLimitUpper;
	JTextField stockLimitLower;
	JTextArea remark;
	JTextField totalQuantity;
	
	
	public MaintenanceMagazinePage (String id){
		// 创建及设置窗口
		if(id != null && id.length() > 0){
			this.setTitle("修改弹架资料");
		} else {
			this.setTitle("新增弹架资料");
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
        JLabel nameLabel = new JLabel("名称");
        nameLabel.setBorder(cellBorder);
        detailPanel.add(nameLabel,new GBC(0,0).setAnchor(GridBagConstraints.EAST));
        equipName = new JTextField(30);
        equipName.setToolTipText("请输入弹架名称");
        detailPanel.add(equipName,new GBC(1,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        JLabel modelLabel= new JLabel("型号");
        modelLabel.setBorder(cellBorder);
        detailPanel.add(modelLabel,new GBC(2,0).setAnchor(GridBagConstraints.EAST));
        equipModel = new JTextField(30);
        equipModel.setToolTipText("请输入型号");
        detailPanel.add(equipModel,new GBC(3,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        JLabel specLabel = new JLabel("规格");
        specLabel.setBorder(cellBorder);
        detailPanel.add(specLabel,new GBC(0,1).setAnchor(GridBagConstraints.EAST));
        equipSpec = new JTextField(30);
        equipSpec.setToolTipText("请输入规格");
        detailPanel.add(equipSpec,new GBC(1,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        JLabel unitLabel = new JLabel("单位");
        unitLabel.setBorder(cellBorder);
        detailPanel.add(unitLabel,new GBC(2,1).setAnchor(GridBagConstraints.EAST));
        // 单位 -- 根
        equipUnit = new JTextField(10);
        equipUnit.setText("根");
        detailPanel.add(equipUnit,new GBC(3,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        JLabel labelModelName = new JLabel("器材名称规格型号");
        detailPanel.add(labelModelName,new GBC(0,2).setAnchor(GridBagConstraints.EAST));
        equipModelName = new JTextField(50);
        equipModelName.setToolTipText("请输入器材名称规格型号");
        detailPanel.add(equipModelName,new GBC(1,2,3,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        JLabel upperLabel = new JLabel("库存上限");
        upperLabel.setBorder(cellBorder);
        detailPanel.add(upperLabel,new GBC(0,3).setAnchor(GridBagConstraints.EAST));
        stockLimitUpper = new JTextField(10);
        stockLimitUpper.setToolTipText("请输入库存上限");
        detailPanel.add(stockLimitUpper,new GBC(1,3).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        JLabel lower = new JLabel("库存下限");
        lower.setBorder(cellBorder);
        detailPanel.add(lower,new GBC(2,3).setAnchor(GridBagConstraints.EAST));
        stockLimitLower = new JTextField(10);
        stockLimitLower.setToolTipText("请输入库存下限");
        detailPanel.add(stockLimitLower,new GBC(3,3).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));

        JLabel totalQuantityLabel = new JLabel("数量（本期结余）");
        totalQuantityLabel.setBorder(cellBorder);
        detailPanel.add(totalQuantityLabel,new GBC(0,4).setAnchor(GridBagConstraints.EAST));
        totalQuantity = new JTextField(10);
        totalQuantity.setToolTipText("请输入数量");
        if(id != null && id.length() > 0){
			// 修改页面，数量不能更改
        	totalQuantity.setEditable(false);
		}
        detailPanel.add(totalQuantity,new GBC(1,4).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        
        JLabel remarkLabel = new JLabel("备注");
        remarkLabel.setBorder(cellBorder);
        detailPanel.add(remarkLabel,new GBC(0,5).setAnchor(GridBagConstraints.EAST));
        remark = new JTextArea(3,3);
        detailPanel.add(remark,new GBC(1,5,3,3).setFill(GridBagConstraints.BOTH).setWeight(100, 0).setInsets(inset));
        
        detailPanel.setVisible(true);
        pane.add(detailPanel, BorderLayout.NORTH);
        // button part
        JPanel btnPanel =  new JPanel();
        btnPanel.setLayout(new FlowLayout());
        JButton saveBtn = new JButton("保存");
        saveBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String equipNameStr = equipName.getText();
				if(equipNameStr == null || equipNameStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写器材名称!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String equipModelStr = equipModel.getText();
				if(equipModelStr == null || equipModelStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写器材型号!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String equipSpecStr = equipSpec.getText();
				if(equipSpecStr == null || equipSpecStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写器材规格!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String equipUnitStr = equipUnit.getText();
				if(equipUnitStr == null || equipUnitStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写器材单位!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String equipModelNameStr = equipModelName.getText();
				if(equipModelNameStr == null || equipModelNameStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写器材型号!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String stockLimitUpperStr = stockLimitUpper.getText();
				if(stockLimitUpperStr == null || stockLimitUpperStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写器材数量上限!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String stockLimitLowerStr = stockLimitLower.getText();
				if(stockLimitLowerStr == null || stockLimitLowerStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写器材数量下限!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String remarkStr = remark.getText();
				String totalQuantityStr = totalQuantity.getText();
				if(totalQuantityStr == null || totalQuantityStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写数量!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// 保存资料
				Map<String, String> params = new HashMap<>();
				params.put(MagazineFormDTO.ATTRIBUTE.ID.getValue(), id);
				params.put(MagazineFormDTO.ATTRIBUTE.MAGAZINE_NAME.getValue(), equipNameStr);
				params.put(MagazineFormDTO.ATTRIBUTE.MODEL.getValue(), equipModelStr);
				params.put(MagazineFormDTO.ATTRIBUTE.REMARK.getValue(),remarkStr );
				params.put(MagazineFormDTO.ATTRIBUTE.SPEC.getValue(),equipSpecStr );
				params.put(MagazineFormDTO.ATTRIBUTE.STOCK_LOWER_LIMIT.getValue(),stockLimitLowerStr );
				params.put(MagazineFormDTO.ATTRIBUTE.STOCK_UPPER_LIMIT.getValue(),stockLimitUpperStr );
				params.put(MagazineFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue(),totalQuantityStr );
				params.put(MagazineFormDTO.ATTRIBUTE.UNIT.getValue(), equipUnitStr);
				params.put(MagazineFormDTO.ATTRIBUTE.EUIP_MODEL_NAME.getValue(), equipModelNameStr);
				
				String result = null;
				try {
					result = HttpUtility.post(GUNConstants.URI + "/Gui/Maga/editEquip.do?" , params);
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
				equipName.setText("");
				equipModel.setText("");
				equipSpec.setText("");
				equipUnit.setText("根");
				equipModelName.setText("");
				stockLimitUpper.setText("");
				stockLimitLower.setText("");
				remark.setText("");
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
			result = HttpUtility.post(GUNConstants.URI + "/Gui/Maga/getEquip.do?" , params);
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
				String equipModelNameV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.EUIP_MODEL_NAME.getValue()).toString();
				equipModelName.setText(equipModelNameV);
				String equipNameV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.MAGAZINE_NAME.getValue()).toString();
				equipName.setText(equipNameV);
				String modelV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.MODEL.getValue()).toString();
				equipModel.setText(modelV);
				String specV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.SPEC.getValue()).toString();
				equipSpec.setText(specV);
				String unitV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.UNIT.getValue()).toString();
				equipUnit.setText(unitV);
				String stockUpperLmtV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.STOCK_UPPER_LIMIT.getValue()).toString();
				stockLimitUpper.setText(stockUpperLmtV);
				String stockLowerLmtV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.STOCK_LOWER_LIMIT.getValue()).toString();
				stockLimitLower.setText(stockLowerLmtV);
				String totalQuantityV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue()).toString();
				totalQuantity.setText(totalQuantityV);
				String remarkV = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.REMARK.getValue()).toString();
//				remark.setText(remarkV);// 修改画面不提供备注
				
			}
		}
        
	}
}
