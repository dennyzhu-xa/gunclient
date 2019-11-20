package com.gun.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.gun.dto.ArrGun;
import com.gun.dto.ArrGunFormDTO;
import com.gun.dto.BestChoiceDTO;
import com.gun.dto.EquipmentFormDTO;
import com.gun.dto.JointFormDTO;
import com.gun.dto.OilLevelDTO;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;

/**
 * 居中的JPanel中左边的Panel，占用整体宽度的1/2
 * @author 
 */
public class PrePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Log LOGGER = LogFactory.getLog(PrePanel.class);
	List<OilLevelDTO> oilLevelDTOList = null;
	
	private JointFormDTO jointFormDto;
	
	private List<EquipmentFormDTO> equipDTOList;
	
	Map<Integer, BestChoiceDTO> bestChoice = new HashMap<Integer,BestChoiceDTO>();
	
	List<OilLevelDTO> totalLevels;
	// 是否需要安全枪
	private boolean needSafetyGun;
	// 安全枪长度
	private int safetyGunLength;
	
	private final double PER_MODIFY_LENGTH			= 0.01;//单位 米
	
	List<ArrGun> arrGunList = new ArrayList<ArrGun>();
	Map<Integer,ArrGun> arrGunMap = new HashMap<Integer,ArrGun>(); // 仅记录射孔枪
	// 中接头不能为油层边界
	// 多个Equipment待选的情况，选择最优的排枪;此处的oilLevelDTOList要为由深到浅排列
	public void prepareBest(){
		this.orderEquipmentLists();
		for(int i=0;i<oilLevelDTOList.size();i++){
			double deep = oilLevelDTOList.get(i).deep;
			boolean flag = false;//判断是否处理完毕
			BestChoiceDTO bstChoice = null;
			double equipLongest = equipDTOList.get(0).getLength();
			if(deep >= equipLongest){
				int f = (int) ((deep+2*PER_MODIFY_LENGTH+jointFormDto.getJointLength())/(equipDTOList.get(0).getLength() + jointFormDto.getJointLength()));
				double t = (deep+2*PER_MODIFY_LENGTH+jointFormDto.getJointLength())%(equipDTOList.get(0).getLength() + jointFormDto.getJointLength());
				if(t == 0){
//					刚好f根
					bstChoice = new BestChoiceDTO(equipDTOList.get(0), f, equipDTOList.get(0), deep,0,0);
				} else {
					double reminder = deep-(f*equipLongest + f* jointFormDto.getJointLength() - PER_MODIFY_LENGTH);
					for(int j=equipDTOList.size()-1;j>=0;j--){
						// 此处的equipDTOList.get(j).getSingleBlindArea()为多出的部分盲区
						if(equipDTOList.get(j).getLength()-equipDTOList.get(j).getSingleBlindArea() > reminder){
							bstChoice = new BestChoiceDTO(equipDTOList.get(0), f, equipDTOList.get(j), reminder,equipDTOList.get(j).getLength()-reminder,equipDTOList.get(0).getLength()-reminder);
							flag = true;
							break;
						}
					}
					if(!flag){
						bstChoice = new BestChoiceDTO(equipDTOList.get(0), f, equipDTOList.get(0), reminder,equipDTOList.get(0).getLength() - reminder,equipDTOList.get(0).getLength() - reminder);
					} else {
						flag = false;
					}
				}
			} else {
				for(int j=equipDTOList.size()-1;j>=0;j--){
					if(equipDTOList.get(j).getLength() - 2*equipDTOList.get(j).getSingleBlindArea() >= deep){
						bstChoice = new BestChoiceDTO(equipDTOList.get(j), 1, equipDTOList.get(j), deep,equipDTOList.get(j).getLength()-deep,equipDTOList.get(0).getLength()-deep);
						flag = true;
						break;
					}
				}
				if(!flag){
					bstChoice = new BestChoiceDTO(equipDTOList.get(0), 1, equipDTOList.get(0), deep,equipDTOList.get(0).getLength()-deep,equipDTOList.get(0).getLength()-deep);
				} else {
					flag = false;
				}
			}
			bestChoice.put(i, bstChoice);
		}
	}
	
	// 射孔枪长度由大到小排序
	public void orderEquipmentLists(){
		if(equipDTOList == null || equipDTOList.size() <= 1) return;
		Collections.sort(equipDTOList,new Comparator<EquipmentFormDTO>(){

			@Override
			public int compare(EquipmentFormDTO o1, EquipmentFormDTO o2) {
				double diff = o1.getLength() - o2.getLength();
				if(diff > 0){
					return -1;
				} else if(diff < 0){
					return 1;
				}
				return 0;
			}
			
		});
	}
	
	// 浅到深排序
	public void orderOilLeveles(){
		if(oilLevelDTOList == null || oilLevelDTOList.size() <= 1) return;
		Collections.sort(oilLevelDTOList,new Comparator<OilLevelDTO>() {

			@Override
			public int compare(OilLevelDTO o1, OilLevelDTO o2) {
				double diff = o1.getStartLevel()-o2.getStartLevel();
				if (diff > 0){
					return 1;
				} else if(diff < 0){
					return -1;
				}
				return 0;
			}
		});
	}
	
	// 添加夹层到油层中
	public void getAllList(){
		if(oilLevelDTOList == null) return;
		if(oilLevelDTOList.size() <= 1){
			totalLevels = oilLevelDTOList;
			return;
		}
		this.orderOilLeveles();
		totalLevels = new ArrayList<OilLevelDTO>();
		for(int i=0;i<oilLevelDTOList.size();i++){
			oilLevelDTOList.get(i).setType(GUNConstants.LEVEL_TYPE_OIL);
			totalLevels.add(oilLevelDTOList.get(i));
			if(i < oilLevelDTOList.size() -1){
				// 增加夹层
				double s = oilLevelDTOList.get(i).getEndLevel();
				double e = oilLevelDTOList.get(i+1).getStartLevel();
				double d = e-s;
				totalLevels.add(new OilLevelDTO(s,e,d,GUNConstants.LEVEL_TYPE_SPACER));
			}
		}
	}
	
	public double getMax(){
		this.orderOilLeveles();
		// 井深
		double deep = 0.0;
		// oilLevels全部内容排序，抓出最深位置
		if(oilLevelDTOList != null && oilLevelDTOList.size() > 0){
			deep = oilLevelDTOList.get(oilLevelDTOList.size()-1).getEndLevel();
		}
		return deep;
	}
	
	public double getMin(){
		this.orderOilLeveles();
		// 井深
		double deep = 0.0;
		// oilLevels全部内容排序，抓出最深位置
		if(oilLevelDTOList != null && oilLevelDTOList.size() > 0){
			deep = oilLevelDTOList.get(0).getStartLevel();
		}
		return deep;
	}
	
	Image image = null;
	Image image2 = null;
	public PrePanel(int w,int h,ArrGunFormDTO formDTO) {
//		super();
		
	    this.setLayout(new BorderLayout());
//	    Border board = BorderFactory.createLineBorder(Color.GRAY,3);
	    this.setBorder(BorderFactory.createLineBorder(Color.WHITE,3));//设置面板边框颜色
	    this.oilLevelDTOList = formDTO.getOilLevelDTOList();
	    this.getAllList();
	    this.needSafetyGun = formDTO.isUseSafety();
	    this.safetyGunLength = formDTO.getSafeLength();
	    String connectorText = formDTO.getConnector();
	    jointFormDto = new JointFormDTO();
	    Map<String, String> jointParams = new HashMap<>();
	    jointParams.put(GUNConstants.QUERY_EQUIPMENT_NAME, connectorText);
	    String connectList = null;
		try {
			connectList = HttpUtility.post(GUNConstants.URI + "/Gui/Joint/queryByName.do?" , jointParams);
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
				Object result = jr.getResult();
				List<Object> temp = new ArrayList<Object>();
				temp.add(result);
				jointList = jointFormDto.toJointFormRowData(temp);
			}
		}
		if(jointFormDto != null){
			jointFormDto = jointList.get(0);
		} else {
			return;
		}
		
		String[] gunArray = formDTO.getGun();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<gunArray.length;i++){
			sb.append("'");
			sb.append(gunArray[i]);
			sb.append("'");
			if(i < gunArray.length-1){
				sb.append(",");
			}
		}
		Map<String, String> namesParam = new HashMap<>();
		namesParam.put(GUNConstants.QUERY_EQUIPMENT_NAME, sb.toString());
	    String equipmentsList = null;
		try {
			equipmentsList = HttpUtility.post(GUNConstants.URI + "/Gui/Equip/queryByNameArray.do?" , namesParam);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
		}
		if(equipmentsList != null){
			JsonResponse jr = new JsonResponse(equipmentsList);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				@SuppressWarnings("unchecked")
				List<Object> result = (List<Object>) jr.getResult();
				EquipmentFormDTO f = new EquipmentFormDTO();
				equipDTOList = f.toRowData(result);
			}
		}
	    
//	    this.orderOilLeveles();
//	    this.setBounds(0, 0, w/3, h+20);
	    if(oilLevelDTOList == null || oilLevelDTOList.size() < 1) return;
	    /*int y = 0;
	    double start = 0;
	    double end = 0;
	    int height = 0;
	    JLabel[] labels = new JLabel[oilLevelDTOList.size() * 2-1];

	    for(int i=0;i<oilLevelDTOList.size();i++){
	    	start = oilLevelDTOList.get(i).getStartLevel();
	    	end = oilLevelDTOList.get(i).getEndLevel();
	    	height = (int) (end - start);
	    	labels[i*2] = new JLabel(String.valueOf(height));
	    	labels[i*2].setBackground(Color.BLACK);
	    	labels[i*2].setForeground(Color.WHITE);
	    	labels[i*2].setBounds(0, y, w/3, height);
	    	labels[i*2].setBorder(board);
	    	labels[i*2].setOpaque(true);
	    	this.add(labels[i*2]);
	    	labels[i*2].setVisible(true);
	    	
	    	if(i < oilLevelDTOList.size()-1){
	    		y += height;
	    		height = (int) (oilLevelDTOList.get(i+1).getStartLevel() - oilLevelDTOList.get(i).getEndLevel());
	    		labels[i*2 + 1] = new JLabel(String.valueOf(height));
	    		labels[i*2 + 1].setBackground(Color.WHITE);
	    		labels[i*2 + 1].setForeground(Color.BLACK);
	    		labels[i*2 + 1].setBounds(0,y,w/3,height);
	    		labels[i*2 + 1].setBorder(board);
	    		labels[i*2 + 1].setOpaque(true);
	    		this.add(labels[i*2 + 1]);
	    		labels[i*2 + 1].setVisible(true);
	    		y += height;
	    	}
	    }
		JLabel label = new JLabel("test");
		label.setBackground(Color.DARK_GRAY);
		label.setBounds(0, 1550, w/3, 20);
		this.add(label);*/
//	    this.updateUI();
	   /* try {
	    	File file = new File("//Users//apple//Desktop//new.png");
	    	image = ImageIO.read(file);
	    	image2 = ImageIO.read(new File("//Users//apple//Desktop//bck.jpg"));
	    } catch (Exception ex) {
	      ex.printStackTrace(System.err);
	    }
	    JLabel jl1 = new JLabel(new ImageIcon(image.getScaledInstance(w/2, h, Image.SCALE_SMOOTH)));
	    jl1.setLayout(null);
	    jl1.setBounds(0, 0, w/2, h);
	    this.add(jl1);
	    JLabel jl2 = new JLabel(new ImageIcon(image2.getScaledInstance(w/2, h, Image.SCALE_SMOOTH)));
	    jl2.setLayout(null);
	    jl2.setBounds(0,h,w/2,h);
	    this.add(jl2);*/
	    arrData();
	    
//	    this.setBounds(0, 0, 400, (int)(100* (arrGunList.get(arrGunList.size()-1).getToDeep() - arrGunList.get(0).getFromDeep())));
	    // 不加这一句滚动条不出来
	    this.setPreferredSize(new Dimension(400,(int)(100* (arrGunList.get(arrGunList.size()-1).getToDeep() - arrGunList.get(0).getFromDeep()))));
	}
	
	
	public void arrData(){
		prepareBest();
		OilLevelDTO oilLevelDTO = null;
		double jointLength = jointFormDto.getJointLength();
		boolean flag = false;
		ArrGun arrgun = null;
		ArrGun arrGunConnect = null;
		/*********************************** 安全枪 start *************************************************/
		if(needSafetyGun && safetyGunLength > 0){
			// 如果有安全枪，先给安全枪安排；规则为：先看下一段的排枪安排中首段枪的单边盲区长度为多少，（射孔枪+接头）*N + 选中的首个排枪的单边盲区>safeGunLength
			arrgun = new ArrGun();
			arrgun.setSequence(0);
			arrgun.setEquipmentId(null);
			arrgun.setLength(safetyGunLength);
			arrgun.setType(GUNConstants.ARR_GUN_SAFETY);
			arrGunList.add(arrgun);
			arrGunMap.put(0, arrgun);
		}
		/*********************************** 安全枪 end *************************************************/
		
		/************************第一根枪的排列 equipDTOList为已从大至小排序 start*************************************/
		double startMore = 0.0; // 排枪第一根没算的部分
		double endMore = 0.0;// 一段排枪后多出的部分
		double deep = totalLevels.get(0).getDeep();
		// 记录设备排列序号
		int p = 1;
		int sp= 1;
		// 上一个器材所在高度
		double lastEquipDeep = 0;
		if((equipDTOList.get(0).getLength() - deep) >= 2*PER_MODIFY_LENGTH){
			// 优先选一根枪-两边盲区>Deep
			for(int m=equipDTOList.size()-1;m>=0;m--){
				if(equipDTOList.get(m).getLength() - equipDTOList.get(m).getSingleBlindArea()*2 > deep){
					startMore = equipDTOList.get(m).getSingleBlindArea();
					endMore = equipDTOList.get(m).getLength() - equipDTOList.get(m).getSingleBlindArea() - deep;
					// 选这根射孔枪equipDTOList.get(m)
					arrgun = new ArrGun();
					arrgun.setSequence(sp++);
					arrgun.setEquipmentId(equipDTOList.get(m).getId());
					arrgun.setFromDeep(totalLevels.get(0).getStartLevel()-startMore);
					arrgun.setLength(equipDTOList.get(m).getLength());
					arrgun.setToDeep(arrgun.getFromDeep() + arrgun.getLength());
					if(deep >= equipDTOList.get(m).getLength()){
						arrgun.setType(GUNConstants.ARR_GUN_FULL);
					} else {
						arrgun.setType(GUNConstants.ARR_GUN_HALF_FULL);
					}
					arrGunList.add(arrgun);
					arrGunMap.put(p++, arrgun);
					// 加上中间接头
					arrGunConnect = new ArrGun();
					arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
					arrGunConnect.setSequence(sp++);
					arrGunConnect.setEquipmentId(jointFormDto.getId());
					arrGunConnect.setFromDeep(arrgun.getToDeep());
					arrGunConnect.setLength(jointLength);
					arrGunConnect.setToDeep(arrgun.getToDeep() + jointLength);
					arrGunList.add(arrGunConnect);
					lastEquipDeep = arrgun.getToDeep() + jointLength;
					flag = true;
					break;
				}
			}
			if(!flag){
				// 没有合适的就选最长的那根equipDTOList.get(0)
				double external = equipDTOList.get(0).getLength() - deep;
				external = (double)Math.round(external*100)/100;
				startMore = external;
				endMore = equipDTOList.get(0).getLength() - deep - external;
				arrgun = new ArrGun();
				arrgun.setSequence(sp++);
				arrgun.setEquipmentId(equipDTOList.get(0).getId());
				arrgun.setFromDeep(totalLevels.get(0).getStartLevel()-startMore);
				arrgun.setLength(equipDTOList.get(0).getLength());
				arrgun.setToDeep(arrgun.getFromDeep() + arrgun.getLength());
				arrgun.setType(GUNConstants.ARR_GUN_FULL);
				arrGunList.add(arrgun);
				arrGunMap.put(p++, arrgun);
				// 加上中间接头
				arrGunConnect = new ArrGun();
				arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
				arrGunConnect.setSequence(sp++);
				arrGunConnect.setEquipmentId(jointFormDto.getId());
				arrGunConnect.setFromDeep(arrgun.getToDeep());
				arrGunConnect.setLength(jointLength);
				lastEquipDeep = arrgun.getToDeep() + jointLength;
				arrGunConnect.setToDeep(lastEquipDeep);
				arrGunList.add(arrGunConnect);
			} else {
				flag = false; //flag归位
			}
		} else { // 一根枪无法解决问题，需要多根来解决
			int t = (int) ((deep+PER_MODIFY_LENGTH) / (equipDTOList.get(0).getLength() + jointLength));
			double y = (deep+PER_MODIFY_LENGTH) % (equipDTOList.get(0).getLength() + jointLength);
			if(y == 0){ // 代表最长的刚好被卡住，反正要选两根了，剩余的距离可以再向上抽一个单边盲区,起始距离为gun.length-singleBlindArea
				// 选t-1根equipDTOList.get(0)
				y = equipDTOList.get(0).getLength() + jointLength;
				a:for(int m=equipDTOList.size()-1;m>=0;m--){
					for(int n=m;n>=0;n--){
						double g = equipDTOList.get(m).getLength() + jointLength + equipDTOList.get(n).getLength() 
								- equipDTOList.get(n).getSingleBlindArea();
						if(g >= (y+equipDTOList.get(0).getSingleBlindArea()-PER_MODIFY_LENGTH)){ //y的距离向上抽一个单边盲区再还回PER_MODIFY_LENGTH
							//  选m和n两个合适
							startMore = equipDTOList.get(0).getSingleBlindArea();
							endMore = g + equipDTOList.get(n).getSingleBlindArea() - (y+equipDTOList.get(0).getSingleBlindArea()-PER_MODIFY_LENGTH);
							lastEquipDeep = totalLevels.get(0).getStartLevel()-startMore;
							for(int tt = 1;tt<t;tt++){
								arrgun = new ArrGun();
								arrgun.setSequence(sp++);
								arrgun.setEquipmentId(equipDTOList.get(0).getId());
								arrgun.setFromDeep(lastEquipDeep);
								arrgun.setLength(equipDTOList.get(0).getLength());
								lastEquipDeep += arrgun.getLength() + endMore; 
								arrgun.setToDeep(lastEquipDeep);
								arrgun.setType(GUNConstants.ARR_GUN_FULL);
								arrGunList.add(arrgun);
								arrGunMap.put(p++,arrgun);
								
								// 加上中间接头
								arrGunConnect = new ArrGun();
								arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
								arrGunConnect.setSequence(sp++);
								arrGunConnect.setEquipmentId(jointFormDto.getId());
								arrGunConnect.setFromDeep(lastEquipDeep);
								arrGunConnect.setLength(jointLength);
								lastEquipDeep += jointLength;
								arrGunConnect.setToDeep(lastEquipDeep);
								arrGunList.add(arrGunConnect);
							}
							arrgun = new ArrGun();
							arrgun.setSequence(sp++);
							arrgun.setEquipmentId(equipDTOList.get(n).getId());
							arrgun.setFromDeep(lastEquipDeep);
							arrgun.setLength(equipDTOList.get(n).getLength());
							lastEquipDeep += equipDTOList.get(n).getLength();
							arrgun.setToDeep(lastEquipDeep);
							arrgun.setType(GUNConstants.ARR_GUN_FULL);
							arrGunList.add(arrgun);
							arrGunMap.put(p++, arrgun);
							// 加上中间接头
							arrGunConnect = new ArrGun();
							arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
							arrGunConnect.setSequence(sp++);
							arrGunConnect.setEquipmentId(jointFormDto.getId());
							arrGunConnect.setFromDeep(lastEquipDeep);
							arrGunConnect.setLength(jointLength);
							lastEquipDeep += jointLength;
							arrGunConnect.setToDeep(lastEquipDeep);
							arrGunList.add(arrGunConnect);
							
							arrgun = new ArrGun();
							arrgun.setSequence(sp++);
							arrgun.setEquipmentId(equipDTOList.get(m).getId());
							arrgun.setFromDeep(lastEquipDeep);
							arrgun.setLength(equipDTOList.get(m).getLength());
							lastEquipDeep += equipDTOList.get(m).getLength();
							arrgun.setToDeep(lastEquipDeep);
							if(g==(y+equipDTOList.get(0).getSingleBlindArea()-PER_MODIFY_LENGTH)){
								arrgun.setType(GUNConstants.ARR_GUN_FULL);
							} else {
								arrgun.setType(GUNConstants.ARR_GUN_DOWN_EMPTY);
							}
							arrGunList.add(arrgun);
							arrGunMap.put(p++, arrgun);
							// 加上中间接头
							arrGunConnect = new ArrGun();
							arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
							arrGunConnect.setSequence(sp++);
							arrGunConnect.setEquipmentId(jointFormDto.getId());
							arrGunConnect.setFromDeep(lastEquipDeep);
							arrGunConnect.setLength(jointLength);
							lastEquipDeep += jointLength;
							arrGunConnect.setToDeep(lastEquipDeep);
							arrGunList.add(arrGunConnect);
							
							flag = true;
							break a;
						}
					}
				}
			
				if(!flag){
					// 选最长的那根枪，也不要向上提单边盲区的长度了
					startMore = PER_MODIFY_LENGTH;
					endMore = 0;
					lastEquipDeep = startMore;
					for(int tt = 0;tt<t;tt++){
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(equipDTOList.get(0).getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(equipDTOList.get(0).getLength());
						lastEquipDeep += arrgun.getLength() + endMore; 
						arrgun.setToDeep(lastEquipDeep);
						arrgun.setType(GUNConstants.ARR_GUN_FULL);
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						// 加上中间接头
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
					}
				} else {
					flag = false; //flag归位
				}
			} else {
				// t > 0
				startMore = equipDTOList.get(0).getSingleBlindArea();
				lastEquipDeep = totalLevels.get(0).getStartLevel()-startMore;
				for(int tt = 0;tt<t;tt++){
					arrgun = new ArrGun();
					arrgun.setSequence(sp++);
					arrgun.setEquipmentId(equipDTOList.get(0).getId());
					arrgun.setFromDeep(lastEquipDeep);
					arrgun.setLength(equipDTOList.get(0).getLength());
					lastEquipDeep += arrgun.getLength() + endMore; 
					arrgun.setToDeep(lastEquipDeep);
					arrgun.setType(GUNConstants.ARR_GUN_FULL);
					arrGunList.add(arrgun);
					arrGunMap.put(p++,arrgun);
					
					// 加上中间接头
					arrGunConnect = new ArrGun();
					arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
					arrGunConnect.setSequence(sp++);
					arrGunConnect.setEquipmentId(jointFormDto.getId());
					arrGunConnect.setFromDeep(lastEquipDeep);
					arrGunConnect.setLength(jointLength);
					lastEquipDeep += jointLength;
					arrGunConnect.setToDeep(lastEquipDeep);
					arrGunList.add(arrGunConnect);
				}
				// y>0代表选t根equipDTOList.get(0)，剩余长度选一根排除两边单边盲区最合适的，若没有再选排除一边单边盲区最合适的，若没有再用y
				for(int m=equipDTOList.size()-1;m>=0;m--){
					double g = equipDTOList.get(m).getLength() - equipDTOList.get(m).getSingleBlindArea();
					if(g >= (y+equipDTOList.get(0).getSingleBlindArea()-PER_MODIFY_LENGTH)){
						//  选合适的那根
						endMore = equipDTOList.get(m).getLength() - (y+equipDTOList.get(0).getSingleBlindArea()-PER_MODIFY_LENGTH);
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(equipDTOList.get(m).getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(equipDTOList.get(m).getLength());
						lastEquipDeep += arrgun.getLength(); 
						arrgun.setToDeep(lastEquipDeep);
						if(g==(y+equipDTOList.get(0).getSingleBlindArea()-PER_MODIFY_LENGTH)){
							arrgun.setType(GUNConstants.ARR_GUN_FULL);
						} else {
							arrgun.setType(GUNConstants.ARR_GUN_DOWN_EMPTY);
						}
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						// 加上中间接头
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
						
						flag = true;
						break;
					}
				}
				if(!flag){
					// 直接选equipDTOList.get(0)
					startMore = PER_MODIFY_LENGTH;
					endMore = equipDTOList.get(0).getLength() - y;
					
					arrgun = new ArrGun();
					arrgun.setSequence(sp++);
					arrgun.setEquipmentId(equipDTOList.get(0).getId());
					arrgun.setFromDeep(lastEquipDeep);
					arrgun.setLength(equipDTOList.get(0).getLength());
					lastEquipDeep += arrgun.getLength(); 
					arrgun.setToDeep(lastEquipDeep);
					if(endMore <= equipDTOList.get(0).getSingleBlindArea()){
						arrgun.setType(GUNConstants.ARR_GUN_FULL);
					} else {
						arrgun.setType(GUNConstants.ARR_GUN_DOWN_EMPTY);
					}
					arrGunList.add(arrgun);
					arrGunMap.put(p++,arrgun);
					
					// 加上中间接头
					arrGunConnect = new ArrGun();
					arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
					arrGunConnect.setSequence(sp++);
					arrGunConnect.setEquipmentId(jointFormDto.getId());
					arrGunConnect.setFromDeep(lastEquipDeep);
					arrGunConnect.setLength(jointLength);
					lastEquipDeep += jointLength;
					arrGunConnect.setToDeep(lastEquipDeep);
					arrGunList.add(arrGunConnect);
				} else {
					flag = false; // flag 归位
				}
			}
				
		}
		/************************第一根枪的排列 equipDTOList为已从大至小排序 end*************************************/
		boolean getFlag = false;
		boolean leftLgtFlag = false;
		boolean maxLeftlgtFlag = false;
		/*********************************** 排枪（排除第一段井深） start *************************************************/
		for(int i=1;i<totalLevels.size();i++){ // totalLevels为从顶到底排序
			oilLevelDTO = totalLevels.get(i);
			// 依序选择当前层级所需的射孔枪，油层需要考虑如果有多个射孔枪选尽量少的枪为最优；夹层需要考虑为下个油层做合适的剩余准备
			if(oilLevelDTO.getType() != null && oilLevelDTO.getType().equals(GUNConstants.LEVEL_TYPE_SPACER)){
				getFlag = false;
				double sDeep = oilLevelDTO.getDeep();
				double upMore = lastEquipDeep - oilLevelDTO.getStartLevel();
				double needArr = sDeep-upMore;//-PER_MODIFY_LENGTH;
				// i为夹层，i-1为油层，i/2为仅有油层的bestlist所对应的值
				BestChoiceDTO bstChoice = (BestChoiceDTO) bestChoice.get((i+1)/2);
				
				double leftLength = bstChoice.getLeftLength();
				leftLgtFlag = false;
				maxLeftlgtFlag = false;
				double maxLeftLength = bstChoice.getMaxLeftLength();
				// needArr剩余长度要放置合适的射孔枪+中接头后剩余的长度<=leftLength
//				double spaceLength = oilLevelDTO.getDeep();
				// 最长枪所需数量
				int lgq = (int) (needArr/(equipDTOList.get(0).getLength() + jointLength));
				// 最长枪后剩的余数
				double lgqLeft = needArr%(equipDTOList.get(0).getLength() + jointLength);
				if(lgqLeft <= leftLength){
					// 太好了，不用再loop了
					leftLgtFlag = true;
					getFlag = true;
					for(int c=0; c<=lgq; c++){
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(equipDTOList.get(0).getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(equipDTOList.get(0).getLength());
						lastEquipDeep += arrgun.getLength(); 
						arrgun.setToDeep(lastEquipDeep);
						arrgun.setType(GUNConstants.ARR_GUN_SPACER);
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
					}
				} else if(lgqLeft <= maxLeftLength){
					maxLeftlgtFlag = true;
					getFlag = true;
					for(int c=0; c<=lgq; c++){
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(equipDTOList.get(0).getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(equipDTOList.get(0).getLength());
						lastEquipDeep += arrgun.getLength(); 
						arrgun.setToDeep(lastEquipDeep);
						arrgun.setType(GUNConstants.ARR_GUN_SPACER);
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
					}
				} else {
					Map record = new HashMap<Integer,int[]>();
					for(int g = 0;g<equipDTOList.size();g++){
						record.put(g, new int[]{g});
					}
					if(lgq > 0){
						int z = 0;
						a:for(int g=0;g<lgq;g++){
							boolean loopFlag = true;
							while(loopFlag){
								loopFlag = false;
								for(int n=0;n<equipDTOList.size();n++){
									for(int m=0;m<record.size();m++){
										int[] temp = (int[]) record.get(m);
										double value = 0;
										for(int t = 0;t<temp.length;t++){
											int tp = temp[t];
											value += equipDTOList.get(tp).getLength() + jointLength;
										}
										value += equipDTOList.get(n).getLength() + jointLength;
										if(value <= lgqLeft){
											int[] tempSave = temp;
											temp = new int[temp.length + 1];
											System.arraycopy(tempSave, 0, temp, 0, tempSave.length);
											temp[temp.length-1] = n;
											record.put(z++, temp); // 重新赋值record为下一次循环遍历做准备
											if(value + leftLength >= lgqLeft){
												//找到了,不用loop了，return吧
												leftLgtFlag = true;
												getFlag = true;
												int l = lgq-g;
												if(l > 0){
													for(int c=0; c<l; c++){
														arrgun = new ArrGun();
														arrgun.setSequence(sp++);
														arrgun.setEquipmentId(equipDTOList.get(0).getId());
														arrgun.setFromDeep(lastEquipDeep);
														arrgun.setLength(equipDTOList.get(0).getLength());
														lastEquipDeep += arrgun.getLength(); 
														arrgun.setToDeep(lastEquipDeep);
														arrgun.setType(GUNConstants.ARR_GUN_SPACER);
														arrGunList.add(arrgun);
														arrGunMap.put(p++,arrgun);
														
														arrGunConnect = new ArrGun();
														arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
														arrGunConnect.setSequence(sp++);
														arrGunConnect.setEquipmentId(jointFormDto.getId());
														arrGunConnect.setFromDeep(lastEquipDeep);
														arrGunConnect.setLength(jointLength);
														lastEquipDeep += jointLength;
														arrGunConnect.setToDeep(lastEquipDeep);
														arrGunList.add(arrGunConnect);
													}
												}
												for(int t = 0;t<temp.length;t++){
													int tp = temp[t];
													arrgun = new ArrGun();
													arrgun.setSequence(sp++);
													arrgun.setEquipmentId(equipDTOList.get(tp).getId());
													arrgun.setFromDeep(lastEquipDeep);
													arrgun.setLength(equipDTOList.get(tp).getLength());
													lastEquipDeep += arrgun.getLength(); 
													arrgun.setToDeep(lastEquipDeep);
													arrgun.setType(GUNConstants.ARR_GUN_SPACER);
													arrGunList.add(arrgun);
													arrGunMap.put(p++,arrgun);
													
													arrGunConnect = new ArrGun();
													arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
													arrGunConnect.setSequence(sp++);
													arrGunConnect.setEquipmentId(jointFormDto.getId());
													arrGunConnect.setFromDeep(lastEquipDeep);
													arrGunConnect.setLength(jointLength);
													lastEquipDeep += jointLength;
													arrGunConnect.setToDeep(lastEquipDeep);
													arrGunList.add(arrGunConnect);
												}
												
												break a;
											} else if(value + maxLeftLength >= lgqLeft){
												// return 吧
												maxLeftlgtFlag = true;
												getFlag = true;
												int l = lgq-g;
												if(l > 0){
													for(int c=0; c<=l; c++){
														arrgun = new ArrGun();
														arrgun.setSequence(sp++);
														arrgun.setEquipmentId(equipDTOList.get(0).getId());
														arrgun.setFromDeep(lastEquipDeep);
														arrgun.setLength(equipDTOList.get(0).getLength());
														lastEquipDeep += arrgun.getLength(); 
														arrgun.setToDeep(lastEquipDeep);
														arrgun.setType(GUNConstants.ARR_GUN_SPACER);
														arrGunList.add(arrgun);
														arrGunMap.put(p++,arrgun);
														
														arrGunConnect = new ArrGun();
														arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
														arrGunConnect.setSequence(sp++);
														arrGunConnect.setEquipmentId(jointFormDto.getId());
														arrGunConnect.setFromDeep(lastEquipDeep);
														arrGunConnect.setLength(jointLength);
														lastEquipDeep += jointLength;
														arrGunConnect.setToDeep(lastEquipDeep);
														arrGunList.add(arrGunConnect);
													}
												}
												for(int t = 0;t<temp.length;t++){
													int tp = temp[t];
													arrgun = new ArrGun();
													arrgun.setSequence(sp++);
													arrgun.setEquipmentId(equipDTOList.get(tp).getId());
													arrgun.setFromDeep(lastEquipDeep);
													arrgun.setLength(equipDTOList.get(tp).getLength());
													lastEquipDeep += arrgun.getLength(); 
													arrgun.setToDeep(lastEquipDeep);
													arrgun.setType(GUNConstants.ARR_GUN_SPACER);
													arrGunList.add(arrgun);
													arrGunMap.put(p++,arrgun);
													
													arrGunConnect = new ArrGun();
													arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
													arrGunConnect.setSequence(sp++);
													arrGunConnect.setEquipmentId(jointFormDto.getId());
													arrGunConnect.setFromDeep(lastEquipDeep);
													arrGunConnect.setLength(jointLength);
													lastEquipDeep += jointLength;
													arrGunConnect.setToDeep(lastEquipDeep);
													arrGunList.add(arrGunConnect);
												}
												
												break a;
											} else {
												// 还不行，继续循环吧,loopFlag不能删除，只要有就要让他可以再做一次循环，要让它循环到最大限制为止
												loopFlag = true;
											}
										} else {
											// 超出底界，这次的值不能再添加
										}
									}
								}
							}
							lgqLeft += equipDTOList.get(0).getLength() + jointLength;
						}
					} else {
						// 夹层长度小于最长枪但大于下一段油层空余的长度，选合适的枪供夹层排枪,排除选最长枪的状况
						// 对于夹层来讲排枪不再遵循最长优先的状况，选择合适的
						boolean loopFlag = true;
						int z = 0;
						while(loopFlag){
							loopFlag = false;
							a:for (int g=1;g<equipDTOList.size();g++){
								for(int m=0;m<record.size();m++){
									int[] temp = (int[]) record.get(m);
									double value = 0;
									for(int t = 0;t<temp.length;t++){
										int tp = temp[t];
										value += equipDTOList.get(tp).getLength() + jointLength;
									}
									value += equipDTOList.get(g).getLength() + jointLength;
									if(value <= lgqLeft){
										temp = new int[temp.length + 1];
										temp[temp.length-1] = g;
										record.put(z++, temp); // 重新赋值record为下一次循环遍历做准备
										if(value + leftLength >= lgqLeft){
											//找到了,不用loop了，return吧
											leftLgtFlag = true;
											getFlag = true;
											for(int t = 0;t<temp.length;t++){
												int tp = temp[t];
												arrgun = new ArrGun();
												arrgun.setSequence(sp++);
												arrgun.setEquipmentId(equipDTOList.get(tp).getId());
												arrgun.setFromDeep(lastEquipDeep);
												arrgun.setLength(equipDTOList.get(tp).getLength());
												lastEquipDeep += arrgun.getLength(); 
												arrgun.setToDeep(lastEquipDeep);
												arrgun.setType(GUNConstants.ARR_GUN_SPACER);
												arrGunList.add(arrgun);
												arrGunMap.put(p++,arrgun);
												
												arrGunConnect = new ArrGun();
												arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
												arrGunConnect.setSequence(sp++);
												arrGunConnect.setEquipmentId(jointFormDto.getId());
												arrGunConnect.setFromDeep(lastEquipDeep);
												arrGunConnect.setLength(jointLength);
												lastEquipDeep += jointLength;
												arrGunConnect.setToDeep(lastEquipDeep);
												arrGunList.add(arrGunConnect);
											}
											
											break a;
										} else if(value + maxLeftLength >= lgqLeft){
											// return 吧
											maxLeftlgtFlag = true;
											getFlag = true;
											for(int t = 0;t<temp.length;t++){
												int tp = temp[t];
												arrgun = new ArrGun();
												arrgun.setSequence(sp++);
												arrgun.setEquipmentId(equipDTOList.get(tp).getId());
												arrgun.setFromDeep(lastEquipDeep);
												arrgun.setLength(equipDTOList.get(tp).getLength());
												lastEquipDeep += arrgun.getLength(); 
												arrgun.setToDeep(lastEquipDeep);
												arrgun.setType(GUNConstants.ARR_GUN_SPACER);
												arrGunList.add(arrgun);
												arrGunMap.put(p++,arrgun);
												
												arrGunConnect = new ArrGun();
												arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
												arrGunConnect.setSequence(sp++);
												arrGunConnect.setEquipmentId(jointFormDto.getId());
												arrGunConnect.setFromDeep(lastEquipDeep);
												arrGunConnect.setLength(jointLength);
												lastEquipDeep += jointLength;
												arrGunConnect.setToDeep(lastEquipDeep);
												arrGunList.add(arrGunConnect);
											}
											
											break a;
										} else {
											// 还不行，继续循环吧
											loopFlag = true;
										}
									}
								}
							}
						}
					}
				}
				
				if(!getFlag){
					// 没有选到合适的,不是算法有问题，就是这个长度太刁钻，只要不让中接头卡在井边界，就随意排吧
					double x = equipDTOList.get(0).getLength() + jointLength;
					int t = (int) (needArr/x);
					double tempN = needArr%x;
					if(equipDTOList.get(0).getLength() > tempN){
						//选x+1根
						for(int c=0; c<=x; c++){
							arrgun = new ArrGun();
							arrgun.setSequence(sp++);
							arrgun.setEquipmentId(equipDTOList.get(0).getId());
							arrgun.setFromDeep(lastEquipDeep);
							arrgun.setLength(equipDTOList.get(0).getLength());
							lastEquipDeep += arrgun.getLength(); 
							arrgun.setToDeep(lastEquipDeep);
							if(c == x){
								arrgun.setType(GUNConstants.ARR_GUN_UP_EMPTY);
							} else {
								arrgun.setType(GUNConstants.ARR_GUN_SPACER);
							}
							arrGunList.add(arrgun);
							arrGunMap.put(p++,arrgun);
							
							arrGunConnect = new ArrGun();
							arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
							arrGunConnect.setSequence(sp++);
							arrGunConnect.setEquipmentId(jointFormDto.getId());
							arrGunConnect.setFromDeep(lastEquipDeep);
							arrGunConnect.setLength(jointLength);
							lastEquipDeep += jointLength;
							arrGunConnect.setToDeep(lastEquipDeep);
							arrGunList.add(arrGunConnect);
						}
					} else {
						Map record = new HashMap<Integer,int[]>();
						for(int g = 0;g<equipDTOList.size();g++){
							record.put(g, new int[]{g});
						}
						boolean sFlag = false;
						a:for(int ii=0;ii<=t;ii++){
							boolean loopFlag = true;
							while(loopFlag){
								loopFlag = false;
								for(int n=0;n<equipDTOList.size();n++){
									for(int m=0;m<record.size();m++){
										int[] temp = (int[]) record.get(m);
										double value = 0;
										for(int t1 = 0;t1<temp.length;t1++){
											int tp = temp[t1];
											value += equipDTOList.get(tp).getLength() + jointLength;
										}
										value += equipDTOList.get(n).getLength() + jointLength;
										if(value > tempN){
											sFlag = true;
											int l = lgq-ii;
											for(int c=0; c<=l; c++){
												arrgun = new ArrGun();
												arrgun.setSequence(sp++);
												arrgun.setEquipmentId(equipDTOList.get(0).getId());
												arrgun.setFromDeep(lastEquipDeep);
												arrgun.setLength(equipDTOList.get(0).getLength());
												lastEquipDeep += arrgun.getLength(); 
												arrgun.setToDeep(lastEquipDeep);
												arrgun.setType(GUNConstants.ARR_GUN_SPACER);
												arrGunList.add(arrgun);
												arrGunMap.put(p++,arrgun);
												
												arrGunConnect = new ArrGun();
												arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
												arrGunConnect.setSequence(sp++);
												arrGunConnect.setEquipmentId(jointFormDto.getId());
												arrGunConnect.setFromDeep(lastEquipDeep);
												arrGunConnect.setLength(jointLength);
												lastEquipDeep += jointLength;
												arrGunConnect.setToDeep(lastEquipDeep);
												arrGunList.add(arrGunConnect);
											}
											for(int t1 = 0;t1<temp.length;t1++){
												int tp = temp[t1];
												arrgun = new ArrGun();
												arrgun.setSequence(sp++);
												arrgun.setEquipmentId(equipDTOList.get(tp).getId());
												arrgun.setFromDeep(lastEquipDeep);
												arrgun.setLength(equipDTOList.get(tp).getLength());
												lastEquipDeep += arrgun.getLength(); 
												arrgun.setToDeep(lastEquipDeep);
												arrgun.setType(GUNConstants.ARR_GUN_SPACER);
												arrGunList.add(arrgun);
												arrGunMap.put(p++,arrgun);
												
												arrGunConnect = new ArrGun();
												arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
												arrGunConnect.setSequence(sp++);
												arrGunConnect.setEquipmentId(jointFormDto.getId());
												arrGunConnect.setFromDeep(lastEquipDeep);
												arrGunConnect.setLength(jointLength);
												lastEquipDeep += jointLength;
												arrGunConnect.setToDeep(lastEquipDeep);
												arrGunList.add(arrGunConnect);
											}
											break a;
										}
									}
								}
							}
							tempN += equipDTOList.get(0).getLength() + jointLength;
						}
						if(!sFlag){
							// 么有办法了，随便排吧，请人工调整吧
							//选x+1根
							for(int c=0; c<=x; c++){
								arrgun = new ArrGun();
								arrgun.setSequence(sp++);
								arrgun.setEquipmentId(equipDTOList.get(0).getId());
								arrgun.setFromDeep(lastEquipDeep);
								arrgun.setLength(equipDTOList.get(0).getLength());
								lastEquipDeep += arrgun.getLength(); 
								arrgun.setToDeep(lastEquipDeep);
								arrgun.setType(GUNConstants.ARR_GUN_SPACER);
								arrGunList.add(arrgun);
								arrGunMap.put(p++,arrgun);
								
								arrGunConnect = new ArrGun();
								arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
								arrGunConnect.setSequence(sp++);
								arrGunConnect.setEquipmentId(jointFormDto.getId());
								arrGunConnect.setFromDeep(lastEquipDeep);
								arrGunConnect.setLength(jointLength);
								lastEquipDeep += jointLength;
								arrGunConnect.setToDeep(lastEquipDeep);
								arrGunList.add(arrGunConnect);
							}
						}
					}
				}
			}
			if(oilLevelDTO.getType() == null || oilLevelDTO.getType().equals(GUNConstants.LEVEL_TYPE_OIL)){
				if(getFlag){
					BestChoiceDTO bstChoice = (BestChoiceDTO) bestChoice.get(i/2);
					EquipmentFormDTO equipmentFormDTO = bstChoice.getFirstGunDTO();
					int qulty = bstChoice.getFirstGunQulty();
					for(int c=0; c<qulty; c++){
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(equipmentFormDTO.getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(equipmentFormDTO.getLength());
						lastEquipDeep += arrgun.getLength(); 
						arrgun.setToDeep(lastEquipDeep);
						arrgun.setType(GUNConstants.ARR_GUN_FULL);
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
					}
					// 可以用最优来排
					if(leftLgtFlag){
						EquipmentFormDTO reminderGun = bstChoice.getReminderGun();
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(reminderGun.getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(reminderGun.getLength());
						lastEquipDeep += arrgun.getLength(); 
						arrgun.setToDeep(lastEquipDeep);
						arrgun.setType(GUNConstants.ARR_GUN_FULL);
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
					} else if(maxLeftlgtFlag){
						EquipmentFormDTO reminderGun = equipDTOList.get(0);
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(reminderGun.getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(reminderGun.getLength());
						lastEquipDeep += arrgun.getLength(); 
						arrgun.setToDeep(lastEquipDeep);
						arrgun.setType(GUNConstants.ARR_GUN_FULL);
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
					}
				} else {
					// 重新排枪 lastEquipDeep
					double arrDeep = oilLevelDTO.getDeep() - (lastEquipDeep - oilLevelDTO.getStartLevel());
					int t1 = (int) (arrDeep/(equipDTOList.get(0).getLength() + jointLength));
					for(int t = 0;t<t1;t++){
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(equipDTOList.get(0).getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(equipDTOList.get(0).getLength());
						lastEquipDeep += arrgun.getLength(); 
						arrgun.setToDeep(lastEquipDeep);
						arrgun.setType(GUNConstants.ARR_GUN_FULL);
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
					}
					double t2 = arrDeep%(equipDTOList.get(0).getLength() + jointLength);
					if(t2 >= equipDTOList.get(0).getLength()){
						// 选最短+合适的
						arrgun = new ArrGun();
						arrgun.setSequence(sp++);
						arrgun.setEquipmentId(equipDTOList.get(equipDTOList.size()-1).getId());
						arrgun.setFromDeep(lastEquipDeep);
						arrgun.setLength(equipDTOList.get(equipDTOList.size()-1).getLength());
						lastEquipDeep += arrgun.getLength(); 
						arrgun.setToDeep(lastEquipDeep);
						arrgun.setType(GUNConstants.ARR_GUN_FULL);
						arrGunList.add(arrgun);
						arrGunMap.put(p++,arrgun);
						
						arrGunConnect = new ArrGun();
						arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
						arrGunConnect.setSequence(sp++);
						arrGunConnect.setEquipmentId(jointFormDto.getId());
						arrGunConnect.setFromDeep(lastEquipDeep);
						arrGunConnect.setLength(jointLength);
						lastEquipDeep += jointLength;
						arrGunConnect.setToDeep(lastEquipDeep);
						arrGunList.add(arrGunConnect);
						
						double s = equipDTOList.get(equipDTOList.size()-1).getLength();
						s = t2 - s - jointLength;
						for(int temp1 = equipDTOList.size()-1;temp1>=0;temp1--){
							double tc = equipDTOList.get(temp1).getLength();
							if(tc > s){
								arrgun = new ArrGun();
								arrgun.setSequence(sp++);
								arrgun.setEquipmentId(equipDTOList.get(temp1).getId());
								arrgun.setFromDeep(lastEquipDeep);
								arrgun.setLength(equipDTOList.get(temp1).getLength());
								lastEquipDeep += arrgun.getLength(); 
								arrgun.setToDeep(lastEquipDeep);
								arrgun.setType(GUNConstants.ARR_GUN_DOWN_EMPTY);
								arrGunList.add(arrgun);
								arrGunMap.put(p++,arrgun);
								
								arrGunConnect = new ArrGun();
								arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
								arrGunConnect.setSequence(sp++);
								arrGunConnect.setEquipmentId(jointFormDto.getId());
								arrGunConnect.setFromDeep(lastEquipDeep);
								arrGunConnect.setLength(jointLength);
								lastEquipDeep += jointLength;
								arrGunConnect.setToDeep(lastEquipDeep);
								arrGunList.add(arrGunConnect);
								break;
							}
						}
					} else {
						// 选择一根合适的
						for(int temp1 = equipDTOList.size()-1;temp1>=0;temp1--){
							double tc = equipDTOList.get(temp1).getLength();
							if(tc > t2){
								arrgun = new ArrGun();
								arrgun.setSequence(sp++);
								arrgun.setEquipmentId(equipDTOList.get(temp1).getId());
								arrgun.setFromDeep(lastEquipDeep);
								arrgun.setLength(equipDTOList.get(temp1).getLength());
								lastEquipDeep += arrgun.getLength(); 
								arrgun.setToDeep(lastEquipDeep);
								arrgun.setType(GUNConstants.ARR_GUN_DOWN_EMPTY);
								arrGunList.add(arrgun);
								arrGunMap.put(p++,arrgun);
								
								arrGunConnect = new ArrGun();
								arrGunConnect.setType(GUNConstants.ARR_GUN_CONNECTOR);
								arrGunConnect.setSequence(sp++);
								arrGunConnect.setEquipmentId(jointFormDto.getId());
								arrGunConnect.setFromDeep(lastEquipDeep);
								arrGunConnect.setLength(jointLength);
								lastEquipDeep += jointLength;
								arrGunConnect.setToDeep(lastEquipDeep);
								arrGunList.add(arrGunConnect);
								break;
							}
						}
					}
				}
			}
		}
		/*********************************** 排枪 end *************************************************/
		for(int f=0;f<arrGunList.size();f++){
			ArrGun ag = arrGunList.get(f);
			if(f%10 == 0){
				System.out.println("Sequence  EquipmentId  FromDeep    Length   ToDeep    Type");
			}
			System.out.print(ag.getSequence() + "         ");
			System.out.print(ag.getEquipmentId() + "          ");
			System.out.print(ag.getFromDeep() + "         ");
			System.out.print(ag.getLength() + "        ");
			System.out.print(ag.getToDeep() + "         ");
			System.out.println(ag.getType());
		}
		
		for(int f=1;f<=arrGunMap.size();f++){
			if(f%10 == 0){
				System.out.println("Sequence  EquipmentId  FromDeep    Length   ToDeep    Type");
			}
			ArrGun ag = arrGunMap.get(f);
			System.out.print(ag.getSequence() + "         ");
			System.out.print(ag.getEquipmentId() + "          ");
			System.out.print(ag.getFromDeep() + "         ");
			System.out.print(ag.getLength() + "        ");
			System.out.print(ag.getToDeep() + "         ");
			System.out.println(ag.getType());
		}
	}
	

	static int n = 5;
	static double bestValue = 0;
	static double cv,cw=0;
	static double C = 6;
	static Double[] X=new Double[100];
	static Double[] cx = new Double[100];
	
	static Double[][] goods = {{1.3,1.3},{2.2,2.2},{3.4,3.4},{4.2,4.2},{5.0,5.0}};
	
	public static double force(int i){
		if(i>n-1){
			if(bestValue < cv && cw <= C){
		        for(int k=0;k<n;k++)
		            X[k] = cx[k];//存储最优路径
		        bestValue = cv;
		    }
		    return bestValue;
		}
		cw = cw + goods[i][0];
		cv = cv + goods[i][1];
		cx[i] = 1.0;//装入背包
		force(i+1);
		cw = cw-goods[i][0];
		cv = cv-goods[i][1];
		cx[i] = 0.0;//不装入背包
		force(i+1);
		return bestValue;
	}
	
	public void paintComponent(Graphics g) {
		final int VARIABLE = 1;
		final int DEFAULT_WIDTH 	= 120;
		final int METER_TRANSFER	= 100;
		// 排列井深
		OilLevelDTO oilLevel = null;
		int x = 0,y=0;
		if(needSafetyGun && safetyGunLength > 0){
			// 如果有安全枪，先给安全枪安排；规则为：先看下一段的排枪安排中首段枪的单边盲区长度为多少，（射孔枪+接头）*N + 选中的首个排枪的单边盲区>safeGunLength
			drawRect(g,x,y,DEFAULT_WIDTH,safetyGunLength*METER_TRANSFER/VARIABLE,false);
			y += safetyGunLength*METER_TRANSFER/VARIABLE;
		}
		for(int i=0;i<totalLevels.size();i++){
			oilLevel = totalLevels.get(i);
			drawRect(g,x,y,DEFAULT_WIDTH,(int)oilLevel.getDeep()*METER_TRANSFER/VARIABLE,oilLevel.getType() == GUNConstants.LEVEL_TYPE_OIL || oilLevel.getType() == null);
			y += oilLevel.getDeep()*METER_TRANSFER/VARIABLE;
		}
		x += DEFAULT_WIDTH;
		x += DEFAULT_WIDTH/4;
		// 排列排枪arrGunList
		y=0;
		for(int i=0;i<arrGunList.size();i++){
			ArrGun arrgun = arrGunList.get(i);
			drawImage(g,arrgun.getType(),x,y,DEFAULT_WIDTH/2,(int)(arrgun.getLength()*METER_TRANSFER));
			y += arrgun.getLength()*METER_TRANSFER;
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
