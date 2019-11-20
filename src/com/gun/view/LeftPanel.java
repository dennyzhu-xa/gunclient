package com.gun.view;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.gun.dto.ArrGunFormDTO;
import com.gun.dto.EquipmentFormDTO;
import com.gun.dto.JointFormDTO;
import com.gun.dto.SpacerFormDTO;
import com.gun.utility.GBC;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;
import com.gun.utility.KeyValComboBox;
import com.gun.utility.Phase;

public class LeftPanel extends JPanel {

	private static final Log LOGGER = LogFactory.getLog(LeftPanel.class);
	DecimalFormat df = new DecimalFormat("0.00");
	private JTextField textField1; // 甲方
	private JTextField textField11; //井号
	private JTextField textField20; // 地区
	private JTextField textField21;// 人工井底
//	private JComboBox combox32;// 相位
	private KeyValComboBox combox32;
	private final JComboBox combox;//射孔枪类型
	private List<String> gunList = new ArrayList<String>();
	JCheckBox[] equipBox;
	private JComboBox spaceCombox;// 夹层枪类型
	private List<String> spacerGunList = new ArrayList<String>();
	JCheckBox[] spaceBox;
	private JComboBox mCombox;// 弹型
	 // 中接头
	private String connectorValue;
	private ButtonGroup btnGroup;
	private JRadioButton[] connectButton = null;
	private JCheckBox checkbox; // 使用安全枪
	private JTextField mTextFld; // *米安全枪
	private JTextField textField3;// 底部零长
	
	final JPanel equipPanel = new JPanel();//第四行第二列
	JPanel spacerPanel = new JPanel();
	
	private final double PER_MODIFY_LENGTH			= 0.01;//单位 米
	private String wellId;
	
	public void refreshLeftPanel(ArrGunFormDTO formDTO){
		if(formDTO == null) return;
		wellId = formDTO.getWellId();
		textField1.setText(formDTO.getFirstParty());
		textField11.setText(formDTO.getWellNo());
		textField20.setText(formDTO.getArea());
		textField21.setText(formDTO.getManualWell());
		// 相位 combo32
		combox32.setSelectedValue(formDTO.getPhase());
		combox.setSelectedItem(formDTO.getGunModel());
		// 射孔枪数据重新整理 ***************************start**********************************
		equipPanel.removeAll();
		if(formDTO.getGunModel() != null){
			Map<String, String> params = new HashMap<>();
			params.put(GUNConstants.QUERY_EQUIPMENT_MODEL, formDTO.getGunModel());
			String equipList = null;
			try {
				equipList = HttpUtility.post(GUNConstants.URI + "/Gui/Equip/listEquipByModel.do?" , params);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				LOGGER.error("ClientProtocolException error -- ",e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				LOGGER.error("connect POST error -- ",e1);
			}
			List<EquipmentFormDTO> rs = null;
			if(equipList != null){
				JsonResponse jr = new JsonResponse(equipList);
				if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
					List<Object> result = (List<Object>) jr.getResult();
					rs = toRowData(result);
				}
			}
			
			
			
			ItemListener itemListenerChkBox = new ItemListener(){
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					JCheckBox jchbox = (JCheckBox) e.getItem();
					if(jchbox.isSelected()){
						// 将选中的内容放入数组中
						gunList.add(jchbox.getText());
					} else {
						gunList.remove(jchbox.getText());
					}
				}
				
			};
			if(rs != null && rs.size() >0){
				int equipSize = rs.size();
				equipBox = new JCheckBox[equipSize];
				for(int i =0;i<equipSize;i++){
					equipBox[i] = new JCheckBox((String) rs.get(i).getEuipModelName());
					equipBox[i].addItemListener(itemListenerChkBox);
					equipPanel.add(equipBox[i]);
				}
				equipPanel.updateUI();
			}
		}
		// 射孔枪数据重新整理 *************************** end **********************************
		
		String[] guns = formDTO.getGun();
		if(guns != null && guns.length > 0){
			List<String> gunList = new ArrayList<String>();
			for(int j=0;j<guns.length;j++){
				gunList.add(guns[j]);
			}
			for(int i=0;i<equipBox.length;i++){
				JCheckBox jt = equipBox[i];
				String name = jt.getText();
				if(name == null || name.trim().equals(""))continue;
				if(gunList.contains(name)){
					jt.setSelected(true);
				}
			}
		}
		// 夹层枪
		spaceCombox.setSelectedItem(formDTO.getSpaceModel());
		// 根据夹层枪型号内容重新整理夹层枪内容 *********************start*****************************
		spacerPanel.removeAll();
		if(formDTO.getSpaceModel() != null){
			Map<String, String> spacerParams = new HashMap<>();
			spacerParams.put(GUNConstants.QUERY_EQUIPMENT_MODEL,formDTO.getSpaceModel());
			String spList = null;
			try {
				spList = HttpUtility.post(GUNConstants.URI + "/Gui/Spc/queryList.do?" , spacerParams);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				LOGGER.error("ClientProtocolException error -- ",e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				LOGGER.error("connect POST error -- ",e1);
			}
			List<SpacerFormDTO> spacerList = null;
			if(spList != null){
				JsonResponse jr = new JsonResponse(spList);
				if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
					List<Object> result = (List<Object>) jr.getResult();
					spacerList = toSpacerData(result);
				}
			}
			
			if(spacerList != null && spacerList.size() >0){
				
				int equipSize = spacerList.size();
				spaceBox = new JCheckBox[equipSize];
				for(int i =0;i<equipSize;i++){
					spaceBox[i] = new JCheckBox((String) spacerList.get(i).getSpacerName());
					spaceBox[i].addItemListener(new ItemListener(){
						
						@Override
						public void itemStateChanged(ItemEvent e) {
							JCheckBox jchbox = (JCheckBox) e.getItem();
							if(jchbox.isSelected()){
								spacerGunList.add(jchbox.getText());
							} else {
								spacerGunList.remove(jchbox.getText());
							}
						}
						
					});
					spacerPanel.add(spaceBox[i]);
				}
				spacerPanel.updateUI();
			}
		}
		// 根据夹层枪型号内容重新整理夹层枪内容 ********************* end *****************************
		
		String[] spacers = formDTO.getSpaceGun();
		if(spacers != null && spacers.length > 0){
			List<String> spacerLists = new ArrayList<String>();
			for(int j=0;j<spacers.length;j++){
				spacerLists.add(spacers[j]);
			}
			for(int i=0;i<spaceBox.length;i++){
				JCheckBox jt = spaceBox[i];
				String name = jt.getText();
				if(name == null || name.trim().equals(""))continue;
				if(spacerLists.contains(name)){
					jt.setSelected(true);
				}
			}
		}
		// 弹型
		mCombox.setSelectedItem(formDTO.getMagazine());
		btnGroup.clearSelection();
		// 中接头
		if(connectButton != null){
			for(int i=0;i<connectButton.length;i++){
				if(connectButton[i].getText().equals(formDTO.getConnector())){
					connectButton[i].setSelected(true);
					break;
				} else {
					connectButton[i].setSelected(false);
				}
			}
		}
		// 使用安全枪
		if(formDTO.getSafeLength() > 0){
			checkbox.setSelected(true);
			mTextFld.setText(String.valueOf(formDTO.getSafeLength()));
		} else {
			checkbox.setSelected(false);
			mTextFld.setText("");
		}
		// 底部零长
		textField3.setText(String.valueOf(formDTO.getExtraLength()));
	}
	
	public LeftPanel(){
		this.setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		mainPanel.setBorder(new TitledBorder("井参数"));
		
		JPanel oilNmPanel = new JPanel(new GridBagLayout());
		Border cellBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        int inset = 3;
        
		JLabel label1 = new JLabel("甲方:");
		label1.setBorder(cellBorder);
		oilNmPanel.add(label1,new GBC(0,0).setAnchor(GridBagConstraints.EAST));
		
		textField1 = new JTextField("",10);
		oilNmPanel.add(textField1,new GBC(1,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));

		JLabel label11 = new JLabel("井号:");
		label11.setBorder(cellBorder);
		oilNmPanel.add(label11,new GBC(2,0).setAnchor(GridBagConstraints.EAST));
		
		textField11 = new JTextField("",10);
		oilNmPanel.add(textField11,new GBC(3,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
		
		JLabel label20 = new JLabel("地区:");
		label20.setBorder(cellBorder);
		oilNmPanel.add(label20,new GBC(4,0).setAnchor(GridBagConstraints.EAST));
		
		textField20 = new JTextField("",10);
		oilNmPanel.add(textField20,new GBC(5,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
		JLabel label21 = new JLabel("人工井底:");
		label21.setBorder(cellBorder);
		oilNmPanel.add(label21,new GBC(6,0).setAnchor(GridBagConstraints.EAST));
		
		textField21= new JTextField("",10); // 第二行第四列
		oilNmPanel.add(textField21,new GBC(7,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
		JLabel label32 = new JLabel("相位:");
		label32.setBorder(cellBorder);
		oilNmPanel.add(label32,new GBC(8,0).setAnchor(GridBagConstraints.EAST));
		Phase phase = new Phase();
//		combox32 = new JComboBox(phase.getPhases());
		combox32 = new KeyValComboBox(phase.getPhases());
		oilNmPanel.add(combox32,new GBC(9,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
		
		
		JLabel label2 = new JLabel("射孔枪类型:"); //第三行第一列 型号
		label2.setBorder(cellBorder);
		oilNmPanel.add(label2,new GBC(0,1).setAnchor(GridBagConstraints.EAST));
		
		// 获取器材型号
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		String equipModels = null ;
		try {
			equipModels = HttpUtility.post(GUNConstants.URI + "/Gui/Equip/listEquipModel.do?" , null);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
		}
		if(equipModels != null){
			JsonResponse jr = new JsonResponse(equipModels);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				List<Object> result = (List<Object>) jr.getResult();
				Object[] rs = result.toArray();
				for(Object r : rs){
					model.addElement(r);
				}
			}
		}
		
		combox = new JComboBox(model);
		
		oilNmPanel.add(combox,new GBC(1,1,2,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
		
		
		
		JLabel label3 = new JLabel("射孔枪:"); //第四行第一列
		label3.setBorder(cellBorder);
		oilNmPanel.add(label3,new GBC(4,1).setAnchor(GridBagConstraints.EAST));
		
		equipPanel.setLayout(new FlowLayout());
		
		Map<String, String> params = new HashMap<>();
		params.put(GUNConstants.QUERY_EQUIPMENT_MODEL, combox.getSelectedItem() == null ? "": combox.getSelectedItem().toString());
		String equipList = null;
		try {
			equipList = HttpUtility.post(GUNConstants.URI + "/Gui/Equip/listEquipByModel.do?" , params);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
		}
		List<EquipmentFormDTO> rs = null;
		if(equipList != null){
			JsonResponse jr = new JsonResponse(equipList);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				List<Object> result = (List<Object>) jr.getResult();
				rs = toRowData(result);
			}
		}
		ItemListener itemListenerChkBox = new ItemListener(){
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox jchbox = (JCheckBox) e.getItem();
				if(jchbox.isSelected()){
					// 将选中的内容放入数组中
					gunList.add(jchbox.getText());
				} else {
					gunList.remove(jchbox.getText());
				}
			}
			
		};
		if(rs != null && rs.size() >0){
			int equipSize = rs.size();
			equipBox = new JCheckBox[equipSize];
			for(int i =0;i<equipSize;i++){
				equipBox[i] = new JCheckBox((String) rs.get(i).getEuipModelName());
				equipBox[i].addItemListener(itemListenerChkBox);
				equipPanel.add(equipBox[i]);
			}
		}
		oilNmPanel.add(equipPanel,new GBC(5,1,5,1).setAnchor(GridBagConstraints.WEST));
		
		
		
		combox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent event){
				equipPanel.removeAll();
				String value = combox.getSelectedItem() == null ? "" : combox.getSelectedItem().toString();
				if(value.equals("")){
					return;
				}
				Map<String, String> params = new HashMap<>();
				params.put(GUNConstants.QUERY_EQUIPMENT_MODEL, value);
				String equipList = null;
				try {
					equipList = HttpUtility.post(GUNConstants.URI + "/Gui/Equip/listEquipByModel.do?" , params);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
					LOGGER.error("ClientProtocolException error -- ",e1);
				} catch (IOException e1) {
					e1.printStackTrace();
					LOGGER.error("connect POST error -- ",e1);
				}
				List<EquipmentFormDTO> rs = null;
				if(equipList != null){
					JsonResponse jr = new JsonResponse(equipList);
					if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
						List<Object> result = (List<Object>) jr.getResult();
						rs = toRowData(result);
					}
				}
				
				if(rs != null && rs.size() >0){
					Checkbox[] equipBox;
					int equipSize = rs.size();
					equipBox = new Checkbox[equipSize];
					for(int i =0;i<equipSize;i++){
						equipBox[i] = new Checkbox((String) rs.get(i).getEuipModelName());
						equipPanel.add(equipBox[i]);
						equipPanel.updateUI();
					}
				}
				
			}
		});
		
		
		
		JLabel label22 = new JLabel("夹层枪型号:");// 第六行第一列
		label22.setBorder(cellBorder);
		oilNmPanel.add(label22,new GBC(0,2).setAnchor(GridBagConstraints.EAST));
		
		String spaceList = null;
		try {
			spaceList = HttpUtility.post(GUNConstants.URI + "/Gui/Spc/listEquipModel.do?" , null);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
		}
		// 获取夹层枪型号
		DefaultComboBoxModel spaceModel = new DefaultComboBoxModel();
		if(spaceList != null){
			JsonResponse jr = new JsonResponse(spaceList);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				List<Object> result = (List<Object>) jr.getResult();
				Object[] rsz = result.toArray();
				for(Object r : rsz){
					spaceModel.addElement(r);
				}
			}
		}
		
		spaceCombox = new JComboBox(spaceModel);
		oilNmPanel.add(spaceCombox,new GBC(1,2,2,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
		
		JLabel spacerLabel = new JLabel("夹层枪:");// 第7行第一列
		spacerLabel.setBorder(cellBorder);
		oilNmPanel.add(spacerLabel,new GBC(4,2).setAnchor(GridBagConstraints.EAST));
		
		spacerPanel.setLayout(new FlowLayout());
		Map<String, String> spacerParams = new HashMap<>();
		spacerParams.put(GUNConstants.QUERY_EQUIPMENT_MODEL, spaceCombox.getSelectedItem()== null ? "":spaceCombox.getSelectedItem().toString());
		String spList = null;
		try {
			spList = HttpUtility.post(GUNConstants.URI + "/Gui/Spc/queryList.do?" , spacerParams);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
		}
		List<SpacerFormDTO> spacerList = null;
		if(spList != null){
			JsonResponse jr = new JsonResponse(spList);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				List<Object> result = (List<Object>) jr.getResult();
				spacerList = toSpacerData(result);
			}
		}
		
		if(spacerList != null && spacerList.size() >0){
			
			int equipSize = spacerList.size();
			spaceBox = new JCheckBox[equipSize];
			for(int i =0;i<equipSize;i++){
				spaceBox[i] = new JCheckBox((String) spacerList.get(i).getSpacerName());
				spaceBox[i].addItemListener(new ItemListener(){

					@Override
					public void itemStateChanged(ItemEvent e) {
						JCheckBox jchbox = (JCheckBox) e.getItem();
						if(jchbox.isSelected()){
							spacerGunList.add(jchbox.getText());
						} else {
							spacerGunList.remove(jchbox.getText());
						}
					}
					
				});
				spacerPanel.add(spaceBox[i]);
			}
		}
		oilNmPanel.add(spacerPanel,new GBC(5,2,5,1).setAnchor(GridBagConstraints.WEST));
		
		JLabel label6 = new JLabel("弹型:"); // 第8行第一列
		label6.setBorder(cellBorder);
		oilNmPanel.add(label6,new GBC(0,3).setAnchor(GridBagConstraints.EAST));
		
		String mList = null;
		try {
			mList = HttpUtility.post(GUNConstants.URI + "/Gui/Maga/listEquipModel.do?" , null);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
		}
		// 获取夹层枪型号
		DefaultComboBoxModel mModel = new DefaultComboBoxModel();
		if(mList != null){
			JsonResponse jr = new JsonResponse(mList);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				List<Object> result = (List<Object>) jr.getResult();
				Object[] rsz = result.toArray();
				for(Object r : rsz){
					mModel.addElement(r);
				}
			}
		}
		mCombox = new JComboBox(mModel);
		oilNmPanel.add(mCombox,new GBC(1,3,2,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
		
		JLabel label4 = new JLabel("中接头:"); //第五行第一列
		label4.setBorder(cellBorder);
		oilNmPanel.add(label4,new GBC(4,3).setAnchor(GridBagConstraints.EAST));
		
		JPanel connectPanel = new JPanel();
		String connectList = null;
		try {
			connectList = HttpUtility.post(GUNConstants.URI + "/Gui/Joint/queryList.do?" , null);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
		}
		List<JointFormDTO> jointList = null;
		if(connectList != null){
			JsonResponse jr = new JsonResponse(connectList);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				List<Object> result = (List<Object>) jr.getResult();
				jointList = toJointFormRowData(result);
			}
		}
		
		if(jointList != null && jointList.size() >0){
			
			btnGroup = new ButtonGroup();
			int equipSize = jointList.size();
			connectButton = new JRadioButton[equipSize];
			for(int i =0;i<equipSize;i++){
				connectButton[i] = new JRadioButton((String) jointList.get(i).getJointName());
				connectButton[i].addItemListener(new ItemListener(){

					@Override
					public void itemStateChanged(ItemEvent e) {
						JRadioButton jrb = (JRadioButton) e.getItem();
						if(jrb.isSelected()){
							connectorValue = jrb.getText();
						}
					}
				});
				btnGroup.add(connectButton[i]);
				connectPanel.add(connectButton[i]);
			}
		}
		
		oilNmPanel.add(connectPanel,new GBC(5,3,5,1).setAnchor(GridBagConstraints.WEST));
		
		JPanel mPnl = new JPanel(new FlowLayout());
		
		checkbox = new JCheckBox("使用");
		mTextFld = new JTextField("",3);
//		3M安全枪
		JLabel mlable = new JLabel("M安全枪");
		mPnl.add(checkbox);
		mPnl.add(mTextFld);
		mPnl.add(mlable);
		oilNmPanel.add(mPnl,new GBC(0,4,10,1).setAnchor(GridBagConstraints.WEST));
		
		// 射孔枪底部零长
		JLabel label9 = new JLabel("底部零长:");
		label9.setBorder(cellBorder);
		oilNmPanel.add(label9,new GBC(0,5).setAnchor(GridBagConstraints.EAST));
		
		JPanel dPanel = new JPanel(new FlowLayout(0));
		
		textField3 = new JTextField("",8);
		dPanel.add(textField3);
		
		JLabel label42 = new JLabel("m");
		dPanel.add(label42);
		
		JButton button31 = new JButton("+");
		button31.addKeyListener(new limitNumberKeyListener());
		button31.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String t = textField3.getText();
				
				Pattern p = Pattern.compile("^\\d*(\\.?\\d{0,2})?$"); // 将正则表达式编译为Pattern
				Matcher m = p.matcher(t); // 创建匹配器
				if (!m.matches()){
					JOptionPane.showMessageDialog(null, "底部零长值必须为整数或两位小数!");
					return;
				}
				
				double value = 0.00;
				if(t != null && !t.equals("")){
					value = Double.valueOf(t);
				}
				value += PER_MODIFY_LENGTH;
				textField3.setText(df.format(value)  );
			}
			
		});
		dPanel.add(button31);
		
		JButton button32 = new JButton("-");
		button32.addKeyListener(new limitNumberKeyListener());
		button32.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String t = textField3.getText();
				Pattern p = Pattern.compile("^\\d*(\\.?\\d{0,2})?$"); // 将正则表达式编译为Pattern
				Matcher m = p.matcher(t); // 创建匹配器
				if (!m.matches()){
					JOptionPane.showMessageDialog(null, "底部零长值必须为整数或两位小数!");
					return;
				}
				
				double value = 0.00;
				if(t != null && !t.equals("")){
					value = Double.valueOf(t);
				}
				value -= PER_MODIFY_LENGTH;
				if(value < 0){
					value = 0.00;
				}
				textField3.setText(df.format(value));
			}
			
		});
		dPanel.add(button32);
		
		oilNmPanel.add(dPanel,new GBC(1,5,8,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
		
		
		
		mainPanel.add(oilNmPanel);
		
		this.add(mainPanel,BorderLayout.CENTER);
	}
	
	public List<SpacerFormDTO> toSpacerData(List<Object> spList){
		if(spList == null || spList.size() <1){
			return null;
		}
		List<SpacerFormDTO> results = new ArrayList<SpacerFormDTO>();
		Object[] lists = (Object[]) spList.toArray();
		SpacerFormDTO spFormDTO = null;
		for(Object result : lists){
			JSONArray jsonArr = JSONArray.fromObject("["+result+"]");
			spFormDTO = new SpacerFormDTO();
			Integer id = (Integer) jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.ID.getValue());
			spFormDTO.setId(id);
			String spacerName = jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.SPACER_NAME.getValue()).toString();
			spFormDTO.setSpacerName(spacerName);
			String model = jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.MODEL.getValue()).toString();
			spFormDTO.setModel(model);
			double spacerLgt = Double.valueOf(jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.SPACER_LENGTH.getValue()) == null ? "0" : jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.SPACER_LENGTH.getValue()).toString());
			spFormDTO.setSpacerLength(spacerLgt);
			Integer stockUpperLimit = (Integer)jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.STOCK_UPPER_LIMIT.getValue());
			spFormDTO.setStockUpperLimit(stockUpperLimit);
			Integer stockLowerLimit = (Integer)jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.STOCK_LOWER_LIMIT.getValue());
			spFormDTO.setStockLowerLimit(stockLowerLimit);
			String remark = jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.REMARK.getValue()).toString();
			spFormDTO.setRemark(remark);
			int totalQuantity = (int) jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue());
			spFormDTO.setTotalQuantity(totalQuantity);
			results.add(spFormDTO);
		}
		return results;
	}
	
	public ArrGunFormDTO getArrGunFormDTO(){
		ArrGunFormDTO formDTO = new ArrGunFormDTO();
		formDTO.setWellId(wellId);
		formDTO.setFirstParty(textField1.getText());
		formDTO.setWellNo(textField11.getText());
		formDTO.setArea(textField20.getText());
		formDTO.setManualWell(textField21.getText());
		formDTO.setPhase(combox32.getSelectedValue());
		formDTO.setGunModel(combox.getSelectedItem() == null ? null : combox.getSelectedItem().toString());
		if(gunList != null && gunList.size() > 0){
			String[] r = new String[gunList.size()];
			for(int i=0;i<gunList.size();i++){
				r[i] = gunList.get(i);
			}
			formDTO.setGun(r);
		}
		formDTO.setSpaceModel(spaceCombox.getSelectedItem() == null ? null : spaceCombox.getSelectedItem().toString());
		if(spacerGunList != null && spacerGunList.size() > 0){
			String[] r = new String[spacerGunList.size()];
			for(int i=0;i<spacerGunList.size();i++){
				r[i] = spacerGunList.get(i);
			}
			formDTO.setSpaceGun(r);
		}
		formDTO.setMagazine(mCombox.getSelectedItem() == null ? null : mCombox.getSelectedItem().toString());
		formDTO.setConnector(connectorValue);
		formDTO.setUseSafety(checkbox.isSelected());
		if(checkbox.isSelected()){
			formDTO.setSafeLength(mTextFld.getText() == null || mTextFld.getText().equals("") ? 0 : Integer.valueOf(mTextFld.getText()));
		}
		formDTO.setExtraLength(textField3.getText() == null || textField3.getText().equals("") ? 0 : Double.valueOf(textField3.getText()));
		return formDTO;
	}
	
	public List<JointFormDTO> toJointFormRowData(List<Object> jointDTOList) {
		if(jointDTOList == null || jointDTOList.size() <1){
			return null;
		}
		List<JointFormDTO> results = new ArrayList<JointFormDTO>();
		Object[] lists = (Object[]) jointDTOList.toArray();
		JointFormDTO jointFormDTO = null;
		for(Object result : lists){
			JSONArray jsonArr = JSONArray.fromObject("["+result+"]");
			jointFormDTO = new JointFormDTO();
			Integer id = (Integer) jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.ID.getValue());
			jointFormDTO.setId(id);
			String jointName = jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_NAME.getValue()) == null ? "" : jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_NAME.getValue()).toString();
			jointFormDTO.setJointName(jointName);
			double jointExternalDiameter = Double.valueOf(jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_EXTERNAL_DIAMETER.getValue()) == null ? "0" : jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_EXTERNAL_DIAMETER.getValue()).toString());
			jointFormDTO.setJointExternalDiameter(jointExternalDiameter);
			double jointLength =  Double.valueOf(jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_LENGTH.getValue()) == null ? "0" : jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_LENGTH.getValue()).toString());
			jointFormDTO.setJointLength(jointLength);
			int totalQuantity = (int)jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue());
			jointFormDTO.setTotalQuantity(totalQuantity);
			results.add(jointFormDTO);
		}
		return results;
	}
	
	public List<EquipmentFormDTO> toRowData(List<Object> equipmentDTOList){
		  if(equipmentDTOList == null || equipmentDTOList.size()  <1){
			  return null;
		  }
		  List<EquipmentFormDTO> results = new ArrayList<EquipmentFormDTO>();
		  Object[] lists = (Object[]) equipmentDTOList.toArray();
		  EquipmentFormDTO formDTO = null;
		  for(Object result : lists){
			  formDTO = new EquipmentFormDTO();
			  JSONArray jsonArr = JSONArray.fromObject("["+result+"]");
			  Integer id = (Integer) jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.ID.getValue());
			  formDTO.setId(id);
			  String equipModelName = jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.EUIP_MODEL_NAME.getValue()).toString();
			  formDTO.setEuipModelName(equipModelName);
			  String equipName = jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.EQUIPMENT_NAME.getValue()).toString();
			  formDTO.setEquipmentName(equipName);
			  String model = jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.MODEL.getValue()).toString();
			  formDTO.setModel(model);
			  String spec = jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.SPEC.getValue()).toString();
			  formDTO.setSpec(spec);
			  String unit = jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.UNIT.getValue()).toString();
			  formDTO.setUnit(unit);
			  Integer stockUpperLimit = (Integer) jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.STOCK_UPPER_LIMIT.getValue());
			  formDTO.setStockUpperLimit(stockUpperLimit);
			  Integer stockLowerLimit = (Integer) jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.STOCK_LOWER_LIMIT.getValue());
			  formDTO.setStockLowerLimit(stockLowerLimit);
			  double externalDiameter = Double.valueOf(jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.EXTERNAL_DIAMETER.getValue()) == null ? "0": jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.EXTERNAL_DIAMETER.getValue()).toString());
			  formDTO.setExternalDiameter(externalDiameter);
			  double length = Double.valueOf(jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.LENGTH.getValue()) == null ? "0" : jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.LENGTH.getValue()).toString());
			  formDTO.setLength(length);
			  double singleBlindArea = Double.valueOf(jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.SINGLE_BLIND_AREA.getValue()) == null ? "0" : jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.SINGLE_BLIND_AREA.getValue()).toString());
			  formDTO.setSingleBlindArea(singleBlindArea);
			  int shotNumber  = (int) jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.SHOT_NUMBER.getValue());
			  formDTO.setShotNumber(shotNumber);
			  int shotDensity = (int) jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.SHOT_DENSITY.getValue());
			  formDTO.setShotDensity(shotDensity);
			  double shotSpace = Double.valueOf(jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.SHOT_SPACE.getValue()) == null ? "0":jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.SHOT_SPACE.getValue()).toString());
			  formDTO.setShotSpace(shotSpace);
			  int totalQuantity = (int) jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue());
			  formDTO.setTotalQuantity(totalQuantity);
			  String remark = jsonArr.getJSONObject(0).get(EquipmentFormDTO.ATTRIBUTE.REMARK.getValue()).toString();
			  formDTO.setRemark(remark);
			  results.add(formDTO);
		  }
		  return results;
	  }
	
	/**
	 * 验证输入的内容必须为数字
	 */
	private static class limitNumberKeyListener implements KeyListener{
		@Override
		public void keyTyped(KeyEvent e) {
			char c = e.getKeyChar(); // 获取键盘输入的字符
			String str = String.valueOf(c); // 将字符转换为字符串
			Pattern p = Pattern.compile("^\\d*(\\.?\\d{0,1})?$"); // 将正则表达式编译为Pattern
			Matcher m = p.matcher(str); // 创建匹配器
			if (m.matches())
				return;
			e.consume();
				
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar(); // 获取键盘输入的字符
			String str = String.valueOf(c); // 将字符转换为字符串
			Pattern p = Pattern.compile("^\\d*(\\.?\\d{0,1})?$"); // 将正则表达式编译为Pattern
			Matcher m = p.matcher(str); // 创建匹配器
			if (m.matches())
				return;
			e.consume();
		}
		
	}
}
