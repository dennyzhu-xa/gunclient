package com.gun.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSON;
import com.gun.dto.ArrGunFormDTO;
import com.gun.dto.OilLevelDTO;
import com.gun.dto.OilWellFormDTO;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;

public class PrepareMainView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log LOGGER = LogFactory.getLog(PrepareMainView.class);
	LeftPanel leftPanelView;
	LeftTwoPanel leftTwoPanel; // 油层panel
	GraphScrollPanel prePanel; // 排枪panel
	JPanel resultPanel;
	JTextArea techRemark;
	List<OilLevelDTO> oilLevelDTOList = null;
	int width ;
	int height ;
	ArrGunFormDTO formDTO = null;
	
	private PrepareMainView mainView = null;
	
	public void refresh(String wellId){
		// 初始化变量值
		if(wellId != null && !wellId.equals("") && !wellId.equals("0")){
			
			String result = null;
			Map<String,String> params = new HashMap();
			params.put("id", wellId);
			try {
				result = HttpUtility.post(GUNConstants.URI + "/Gui/Well/getOilWellById.do?" , params);
			} catch (ClientProtocolException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "查询失败！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
				LOGGER.error(this.getClass().getName() + "refresh ClientProtocolException error -- ", e2);
			} catch (IOException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "查询失败！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
				LOGGER.error(this.getClass().getName() + "refresh ClientProtocolException error -- ", e2);
			}
			if(result != null){
				JsonResponse jr = new JsonResponse(result);
				if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
					Object results =   jr.getResult();
					List<Object> temps = new ArrayList<Object>();
					temps.add(results);
					List<ArrGunFormDTO> temp = toList(temps);
					if(temp != null){
						formDTO = temp.get(0);
					}
				} else {
					JOptionPane.showMessageDialog(null, "查询失败！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "无资料！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		formDTO.setWellId(wellId);
		//  更新井参数信息
		leftPanelView.refreshLeftPanel(formDTO);
		// 更新油层信息
		leftTwoPanel.refreshLeftTwoPanel(formDTO);
		// 更新技术说明
		techRemark.setText(formDTO.getRemark());
	}
	
	// 重置
	public void resetValues(){
		formDTO = new ArrGunFormDTO();
		leftPanelView.refreshLeftPanel(formDTO);
		leftTwoPanel.refreshLeftTwoPanel(formDTO);
		techRemark.setText("");
		
	}
	
	public PrepareMainView(int x,int y){
		mainView = this;
		width = x;
		height = y;
//		this.setBounds(0, 0, x-500, y);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		Border cellBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		
		// 井参数
		leftPanelView = new LeftPanel();
		this.add(leftPanelView,BorderLayout.NORTH);
		
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new BorderLayout());
		leftTwoPanel = new LeftTwoPanel();
		downPanel.add(leftTwoPanel,BorderLayout.WEST);
		
		resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.setBorder(new TitledBorder("排枪"));
		
		downPanel.add(resultPanel,BorderLayout.CENTER);
		
		JPanel rightD = new JPanel();
//		rightD.setLayout(new BorderLayout());
		rightD.setLayout(new GridLayout(2, 1));
		// 技术说明
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBorder(new TitledBorder("技术说明"));
		techRemark = new JTextArea(5,30);
		techRemark.setLineWrap(true);
		titlePanel.add(techRemark);
//		rightD.add(titlePanel,BorderLayout.WEST);
		rightD.add(titlePanel);
		
		JPanel leftButtonPnl = new JPanel();
		leftButtonPnl.setLayout(new BoxLayout(leftButtonPnl, BoxLayout.X_AXIS));
		leftButtonPnl.setBorder(new TitledBorder("操作命令"));
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(2,4));
		btnPanel.setBackground(Color.WHITE);
		btnPanel.setBorder(cellBorder);
		JButton tempSaveBtn = new JButton("本地暂存");
		tempSaveBtn.addActionListener(new BtnActionListener());
		JButton openeTempBtn = new JButton("本地打开");
		openeTempBtn.addActionListener(new BtnActionListener());
		JButton saveBtn = new JButton("存入数据库");
		saveBtn.addActionListener(new BtnActionListener());
		JButton openBtn = new JButton("打开数据库");
		openBtn.addActionListener(new BtnActionListener());
		JButton btnReset = new JButton("重置");
		btnReset.addActionListener(new BtnActionListener());
		JButton btnDel = new JButton("删除器材");
		btnDel.addActionListener(new BtnActionListener());
		JButton btnArrange = new JButton("自动排枪");
		btnArrange.addActionListener(new BtnActionListener());
		JButton btnExport = new JButton("排炮输出");
		btnExport.addActionListener(new BtnActionListener());
		btnPanel.add(tempSaveBtn);
		btnPanel.add(openeTempBtn);
		btnPanel.add(saveBtn);
		btnPanel.add(openBtn);
		btnPanel.add(btnReset);
		btnPanel.add(btnDel);
		btnPanel.add(btnArrange);
		btnPanel.add(btnExport);
		
		leftButtonPnl.add(btnPanel);
//		rightD.add(leftButtonPnl,BorderLayout.CENTER);
		rightD.add(leftButtonPnl);
		downPanel.add(rightD,BorderLayout.EAST);
		this.add(downPanel,BorderLayout.CENTER);
	}
	
	public class BtnActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String btnName = e.getActionCommand();
			if(btnName.equals("重置")){
				resultPanel.removeAll();
				resetValues();
				updateUI();
			} else if(btnName.equals("删除器材")){
				
			} else if(btnName.equals("自动排枪")){
				resultPanel.removeAll();
				ArrGunFormDTO formDTO = leftPanelView.getArrGunFormDTO();
				if(formDTO == null){
					JOptionPane.showMessageDialog(null, "异常，请联络系统管理员！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				if(formDTO.getGun() == null || formDTO.getGun().length < 1){
					JOptionPane.showMessageDialog(null, "射孔枪必须选择！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				if(formDTO.getSpaceGun() == null || formDTO.getSpaceGun().length < 1){
					JOptionPane.showMessageDialog(null, "夹层枪必须选择！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				if(formDTO.getConnector() == null || formDTO.getConnector().equals("")){
					JOptionPane.showMessageDialog(null, "中接头必须选择！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				oilLevelDTOList = leftTwoPanel.getOilLevelDTOList();
				if(oilLevelDTOList == null || oilLevelDTOList.size() < 1){
					JOptionPane.showMessageDialog(null, "射孔井段必须有数据！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				formDTO.setOilLevelDTOList(oilLevelDTOList);
				JPanel panel = new JPanel();
		        GraphScrollPanel lable = new GraphScrollPanel(width/3-20, 483, formDTO);
		        panel.add(lable);
		        resultPanel.add(panel);
		        updateUI();
			} else if(btnName.equals("排炮输出")){
				
			} else if(btnName.equals("打开数据库")){
				String result = null;
				Map<String,String> params = new HashMap();
				params.put("user", "");
				try {
					result = HttpUtility.post(GUNConstants.URI + "/Gui/Well/listWell.do?" , params);
				} catch (ClientProtocolException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(result != null){
					JsonResponse jr = new JsonResponse(result);
					if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
						List<Object> results =  (List<Object>) jr.getResult();
						ListWellPage lw = new ListWellPage(results,mainView);
//						lw.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "查询失败！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "无资料！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
				}
			}else if(btnName.equals("存入数据库")){
				ArrGunFormDTO formDTO = leftPanelView.getArrGunFormDTO();
				oilLevelDTOList = leftTwoPanel.getOilLevelDTOList();
				formDTO.setOilLevelDTOList(oilLevelDTOList);
				formDTO.setRemark(techRemark.getText());
				if(formDTO.getWellNo() == null || formDTO.getWellNo().equals("")){
					JOptionPane.showMessageDialog(null, "井号必填！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(formDTO.getArea() == null || formDTO.getArea().equals("")){
					JOptionPane.showMessageDialog(null, "区域必填！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				Map<String, String> params = new HashMap<>();
				params.put(OilWellFormDTO.ATTRIBUTE.OWN_NAME.getValue(), formDTO.getFirstParty());
				params.put(OilWellFormDTO.ATTRIBUTE.OIL_NO.getValue(),formDTO.getWellNo());
				params.put(OilWellFormDTO.ATTRIBUTE.AREA.getValue(), formDTO.getArea());
				params.put(OilWellFormDTO.ATTRIBUTE.PHASE.getValue(), formDTO.getArea());
				params.put(OilWellFormDTO.ATTRIBUTE.EQUIPMENT_MODEL_ID.getValue(),formDTO.getGunModel());
				params.put(OilWellFormDTO.ATTRIBUTE.EQUIPMENTS.getValue(), arrayToString(formDTO.getGun()));
				params.put(OilWellFormDTO.ATTRIBUTE.SPACER_MODEL_ID.getValue(), formDTO.getSpaceModel());
				params.put(OilWellFormDTO.ATTRIBUTE.SPACERS.getValue(), arrayToString(formDTO.getSpaceGun()));
				params.put(OilWellFormDTO.ATTRIBUTE.PERFORATING_ID.getValue(), formDTO.getMagazine());
				params.put(OilWellFormDTO.ATTRIBUTE.CONNECT_ID.getValue(), formDTO.getConnector());
				params.put(OilWellFormDTO.ATTRIBUTE.EXTRA_LENGTH.getValue(), String.valueOf(formDTO.getExtraLength()));
				params.put(OilWellFormDTO.ATTRIBUTE.IS_3_M.getValue(), formDTO.isUseSafety() ? "Y" : "N");
				params.put(OilWellFormDTO.ATTRIBUTE.SAFETY_LENGTH.getValue(), String.valueOf(formDTO.getSafeLength()));
				params.put("wellId", formDTO.getWellId());
				String results = null;
				try {
					results = HttpUtility.postOfObject(GUNConstants.URI + "/Gui/Well/saveWell.do?" , formDTO);
				} catch (ClientProtocolException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				if(results != null){
					JsonResponse jr = new JsonResponse(results);
					if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
						JOptionPane.showMessageDialog(null, "保存成功！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
						return;
					}  else {
						JOptionPane.showMessageDialog(null, "保存失败！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "保存失败！", "提示消息", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		}
		
	}
	
	private String arrayToString(String[] value){
		if(value == null || value.length <= 0) return "";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<value.length;i++){
			sb.append(value[i]);
			if(i < value.length -1) sb.append(",");
		}
		return sb.toString();
	}
	
	public List<ArrGunFormDTO> toList(List<Object> results){
		if(results == null || results.size() <= 0) return null;
		List<ArrGunFormDTO> formDTO = new ArrayList();
		ArrGunFormDTO f = null;
		for(int i=0;i<results.size();i++){
			Object r = results.get(i);
			JSONArray jsonArr = JSONArray.fromObject("["+r+"]");
			f = new ArrGunFormDTO();
			JSONObject jo = jsonArr.getJSONObject(0);
			Object wellId = jo.get(ArrGunFormDTO.ATTRIBUTE.WELL_ID.getValue());
			String wellIdValue = wellId == null ? "" : String.valueOf(wellId.toString());
			f.setWellId(wellIdValue);
			Object firstParty = jo.get(ArrGunFormDTO.ATTRIBUTE.FIRST_PARTY.getValue());
			f.setFirstParty(firstParty == null ? "" : firstParty.toString());
			Object wellNo = jo.get(ArrGunFormDTO.ATTRIBUTE.WELL_NO.getValue());
			f.setWellNo(wellNo == null ? "" : wellNo.toString());
			Object area = jo.get(ArrGunFormDTO.ATTRIBUTE.AREA.getValue());
			f.setArea(area == null ? "" : area.toString());
			Object manualWell = jo.get(ArrGunFormDTO.ATTRIBUTE.MANUAL_WELL.getValue());
			f.setManualWell(manualWell == null ? "" : manualWell.toString());
			Object phase = jo.get(ArrGunFormDTO.ATTRIBUTE.PHASE.getValue());
			f.setPhase(phase == null ? "" : phase.toString());
			Object gunModel = jo.get(ArrGunFormDTO.ATTRIBUTE.GUN_MODEL.getValue());
			f.setGunModel(gunModel == null ? "" :gunModel.toString());
			JSONArray gun = (JSONArray) jo.get(ArrGunFormDTO.ATTRIBUTE.GUN.getValue());
			if(gun != null){
				Object[] guns = gun.toArray();
				if(guns != null && guns.length > 0){
					String[] gunArray = new String[guns.length];
					int k=0;
					for(Object g : guns){
						gunArray[k] = g == null ? "" : g.toString();
						k++;
					}
					f.setGun(gunArray);
				}
			}
			Object spaceModel = jo.get(ArrGunFormDTO.ATTRIBUTE.SPACE_MODEL.getValue());
			f.setSpaceModel(spaceModel == null ? "" : spaceModel.toString());
			JSONArray spaceGun = (JSONArray) jo.get(ArrGunFormDTO.ATTRIBUTE.SPACE_GUN.getValue());
			if(spaceGun != null){
				Object[] spaceGuns = spaceGun.toArray();
				if(spaceGuns != null && spaceGuns.length > 0){
					String[] gunArray = new String[spaceGuns.length];
					int k=0;
					for(Object g : spaceGuns){
						gunArray[k] = g == null ? "" : g.toString();
						k++;
					}
					f.setSpaceGun(gunArray);
				}
			}
			Object magazine = jo.get(ArrGunFormDTO.ATTRIBUTE.MAGAZINE.getValue());
			f.setMagazine(magazine == null ? "" : magazine.toString());
			Object connector = jo.get(ArrGunFormDTO.ATTRIBUTE.CONNECTOR.getValue());
			f.setConnector(connector == null ? "" : connector.toString());
			Object useSafety = jo.get(ArrGunFormDTO.ATTRIBUTE.USE_SAFETY.getValue());
			f.setUseSafety(useSafety == null ? false : (useSafety.toString().equals("true") ? true : false));
			Object safeLength = jo.get(ArrGunFormDTO.ATTRIBUTE.SAFE_LENGTH.getValue());
			f.setSafeLength(safeLength == null ? 0 : Integer.valueOf(safeLength.toString()));
			Object extraLength = jo.get(ArrGunFormDTO.ATTRIBUTE.EXTRA_LENGTH.getValue());
			f.setExtraLength(extraLength == null ? 0 : Double.valueOf(extraLength.toString()));
			Object remark = jo.get(ArrGunFormDTO.ATTRIBUTE.REMARK.getValue());
			f.setRemark(remark == null ? "" : remark.toString());
			JSONArray oilLevelDTOList = (JSONArray) jo.get(ArrGunFormDTO.ATTRIBUTE.OIL_LEVEL_D_T_O_LIST.getValue());
			if(oilLevelDTOList != null){
				int size = oilLevelDTOList.size();
				List<OilLevelDTO> oilLevelDTOListRs = new ArrayList<OilLevelDTO>();
				OilLevelDTO old = null;
				for(int k=0;k<size;k++){
					JSONObject jsonObject = oilLevelDTOList.getJSONObject(k);
					old = new OilLevelDTO();
					Object startLevel = jsonObject.get(OilLevelDTO.ATTRIBUTE.START_LEVEL.getValue());
					old.setStartLevel(startLevel == null ? 0 : Double.valueOf(startLevel.toString()));
					Object endLevel = jsonObject.get(OilLevelDTO.ATTRIBUTE.END_LEVEL.getValue());
					old.setEndLevel(endLevel == null ? 0 : Double.valueOf(endLevel.toString()));
					Object deep = jsonObject.get(OilLevelDTO.ATTRIBUTE.DEEP.getValue());
					old.setDeep(deep == null ? 0 : Double.valueOf(deep.toString()));
					Object type = jsonObject.get(OilLevelDTO.ATTRIBUTE.TYPE.getValue());
					old.setType(type == null ? "" : type.toString());
					Object backinDepth = jsonObject.get(OilLevelDTO.ATTRIBUTE.BACKIN_DEPTH.getValue());
					old.setBackinDepth(backinDepth == null ? 0 : Double.valueOf(backinDepth.toString()));
					Object deepSum = jsonObject.get(OilLevelDTO.ATTRIBUTE.DEEP_SUM.getValue());
					old.setDeepSum(deepSum == null ? 0 : Double.valueOf(deepSum.toString()));
					Object backfinSum = jsonObject.get(OilLevelDTO.ATTRIBUTE.BACKFIN_SUM.getValue());
					old.setBackfinSum(backfinSum == null ? 0 : Double.valueOf(backfinSum.toString()));
					oilLevelDTOListRs.add(old);
				}
				f.setOilLevelDTOList(oilLevelDTOListRs);
			}
			formDTO.add(f);
		}
		return formDTO;
	}
	
}
