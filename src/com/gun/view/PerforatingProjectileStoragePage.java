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
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.gun.dto.EquipmentLogFormDTO;
import com.gun.dto.PerforatingProjectileLogFormDTO;
import com.gun.utility.GBC;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;

public class PerforatingProjectileStoragePage extends JDialog {

	private static final Log LOGGER = LogFactory.getLog(PerforatingProjectileStoragePage.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JRadioButton storageTpIn = new JRadioButton("入库");
	JRadioButton storageTpOut = new JRadioButton("出库");
	
	JTextField quantility;
	
	JTextArea purpose;
	
	JTextArea logRemark;
	
	JScrollPane scrollpane;
	private JScrollPane resultTable;
	String[] columnNames = {"","","",""};
	
	Object[][] data = null;
	DefaultTableModel tableModel;

	JTable  tableView;
	
	public PerforatingProjectileStoragePage (String id){
		// 创建及设置窗口
		this.setTitle("入库/出库");
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

        JLabel storageTypeLabel = new JLabel("类型");
        storageTypeLabel.setBorder(cellBorder);
        detailPanel.add(storageTypeLabel,new GBC(0,0).setAnchor(GridBagConstraints.EAST));
        
        ButtonGroup storageType = new ButtonGroup();
        storageType.add(storageTpIn);
        storageType.add(storageTpOut);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(storageTpIn);
        radioPanel.add(storageTpOut);
        
        detailPanel.add(radioPanel,new GBC(1,0).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
 
        JLabel quantilityLabel = new JLabel("数量");
        quantilityLabel.setBorder(cellBorder);
        detailPanel.add(quantilityLabel,new GBC(0,1).setAnchor(GridBagConstraints.EAST));
        quantility = new JTextField(30);
        quantility.setToolTipText("请输入入库/出库数量");
        detailPanel.add(quantility,new GBC(1,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        
        JLabel remarkLabel = new JLabel("备注");
        remarkLabel.setBorder(cellBorder);
        detailPanel.add(remarkLabel,new GBC(0,2).setAnchor(GridBagConstraints.EAST));
        logRemark = new JTextArea();
        logRemark.setToolTipText("请输入备注");
        detailPanel.add(logRemark,new GBC(1,2).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        
        
        
        // button part
        JPanel btnPanel =  new JPanel();
        btnPanel.setLayout(new FlowLayout());
        JButton saveBtn = new JButton("保存");
        saveBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean flagIn = storageTpIn.isSelected();
				boolean flagOut = storageTpOut.isSelected();
				if(!(flagIn ||flagOut)){
					JOptionPane.showMessageDialog(pane, "请选择出入库类型!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String quantilityStr = quantility.getText();
				if(quantilityStr == null || quantilityStr.trim().length() <= 0){
					JOptionPane.showMessageDialog(pane, "请填写出入库数量!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String remarkStr = logRemark.getText();
				String storageType = "";
				if(flagIn){
					storageType = GUNConstants.EQUIPMENT_LOG_STORAGE_TPIN;
				}
				if(flagOut){
					storageType = GUNConstants.EQUIPMENT_LOG_STORAGE_TPOUT;
				}
				// 保存资料
				Map<String, String> params = new HashMap<>();
				params.put(PerforatingProjectileLogFormDTO.ATTRIBUTE.PERFORATING_ID.getValue(), id);
				params.put(PerforatingProjectileLogFormDTO.ATTRIBUTE.LOG_TYPE.getValue(), storageType);
				params.put(PerforatingProjectileLogFormDTO.ATTRIBUTE.UPDATE_QUANTITY.getValue(), quantilityStr);
				params.put(PerforatingProjectileLogFormDTO.ATTRIBUTE.LOG_REMARK.getValue(), remarkStr);
				
				String result = null;
				try {
					result = HttpUtility.post(GUNConstants.URI + "/Gui/Perf/saveEquipLog.do?" , params);
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
				quantility.setText("");
				purpose.setText("");
				logRemark.setText("");
			}
        });
        btnPanel.add(clearBtn);
        
        detailPanel.add(btnPanel,new GBC(0,3,2,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(inset));
        
        detailPanel.setVisible(true);
        pane.add(detailPanel, BorderLayout.NORTH);
        
        // 查询异动记录
        Map<String, String> paramsList = new HashMap<>();
        paramsList.put(GUNConstants.EQUIPMENT_LOG_EQP_ID, id);
        String result = null;
		try {
			result = HttpUtility.post(GUNConstants.URI + "/Gui/Perf/queryEquipLog.do?" , paramsList);
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
				List<Object> results =  (List<Object>) jr.getResult();
				data = toRowData(results);
			}
		}
		
		scrollpane = createTableV();
		scrollpane.setVisible(true);
		
        pane.add(scrollpane,BorderLayout.CENTER);
		pane.setVisible(true);
        this.add(pane);
	}
	
	public JScrollPane createTableV(){
		columnNames[0] = "序号";
		columnNames[1] = "类型";
		columnNames[2] = "变动数量";
		columnNames[3] = "备注";
			
		TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() { 
            	int length = 0 ;
            	for(String c : columnNames){
            		if(c != null && !c.equals("")){
            			length ++;
            		}
            	}
            	return length; 
            }
            public int getRowCount() { 
            	if(data != null)
            		return data.length;
            	else return 0;
            }
            public Object getValueAt(int row, int col) {
            	return data[row][col];
            }
            public String getColumnName(int column) {
            	return columnNames[column];
            }
            public Class getColumnClass(int c) {
            	return getValueAt(0, c).getClass();
            }
		    public boolean isCellEditable(int row, int col) {
		    	return false;
	    	}
            public void setValueAt(Object aValue, int row, int column) { 
            	data[row][column] = aValue; 
        	}
        };
        // Create the table
        tableView = new JTable(dataModel);
        
		scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}
	
	public String[][] toRowData(List<Object> equipmentDTOList){
		  if(equipmentDTOList == null || equipmentDTOList.size()  <1){
			  return null;
		  }
		  Object[] lists = (Object[]) equipmentDTOList.toArray();
		  String[][] results = new String[equipmentDTOList.size()][4];
		  int i=0;
		  for(Object result : lists){
			  JSONArray jsonArr = JSONArray.fromObject("["+result+"]");
			  results[i][0] = String.valueOf(i + 1);;
			  String logType = (String) jsonArr.getJSONObject(0).get(PerforatingProjectileLogFormDTO.ATTRIBUTE.LOG_TYPE.getValue());
			  String logTypeStr = "";
			  if(logType.equals(GUNConstants.EQUIPMENT_LOG_STORAGE_TPIN)){
				  logTypeStr = "入库";
			  } else if(logType.equals(GUNConstants.EQUIPMENT_LOG_STORAGE_TPOUT)){
				  logTypeStr = "出库";
			  }
			  results[i][1] = logTypeStr;
			  results[i][2] = String.valueOf(jsonArr.getJSONObject(0).get(PerforatingProjectileLogFormDTO.ATTRIBUTE.UPDATE_QUANTITY.getValue()));
			  results[i][3] = jsonArr.getJSONObject(0).get(PerforatingProjectileLogFormDTO.ATTRIBUTE.LOG_REMARK.getValue()) == null ? "" : String.valueOf(jsonArr.getJSONObject(0).get(EquipmentLogFormDTO.ATTRIBUTE.LOG_REMARK.getValue()));
			  i++;
		  }
		  return results;
	  }
}
