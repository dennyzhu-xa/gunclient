package com.gun.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.gun.dto.MagazineFormDTO;
import com.gun.dto.SpacerFormDTO;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JAutoCompleteComboBox;
import com.gun.utility.JsonResponse;

public class SpacerMaintenanceView extends AbstactJPanel {

	private static final Log LOGGER = LogFactory.getLog(SpacerMaintenanceView.class);
	// 查询条件 器材名称、型号
	JTextField equipmentNameQuery;
	JComboBox equipmentModelQuery;
	JButton queryBtn;
	JButton addBtn;
	JButton updateBtn;
	JButton delBtn;
	JButton modifyBtn;
	JScrollPane scrollpane;
	private JScrollPane resultTable;
	
	final int INITIAL_ROWHEIGHT = 25;
	// 查询区域
	String[] columnNames = {"","","","","","","","",""};
		
	Object[][] data = null;
	DefaultTableModel tableModel;

	JTable  tableView;
	
	int selectRow;
	
	public SpacerMaintenanceView(int x,int y){
		this.setBounds(0, 0, x-500, y);
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		
		JPanel queryPanel = new JPanel();
		queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.X_AXIS));
		queryPanel.setBorder(new TitledBorder("查询条件"));
		JPanel queryConditionPanel = new JPanel(new GridLayout(2, 1));
		
		// 获取器材型号
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		String equipModels = null ;
		try {
			equipModels = HttpUtility.post(GUNConstants.URI + "/Gui/Spc/listEquipModel.do?" , null);
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
				model.addElement("");
				for(Object r : rs){
					model.addElement(r);
				}
			}
		}
		
		
		JPanel queryDetailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		queryDetailPanel.add(new JLabel("夹层枪名称"));
		equipmentNameQuery = new JTextField(20);
		equipmentNameQuery.setToolTipText("请输入夹层枪名称");;
		queryDetailPanel.add(equipmentNameQuery);
		queryDetailPanel.add(new JLabel("型号"));
		equipmentModelQuery = new JAutoCompleteComboBox(model);
		equipmentModelQuery.setToolTipText("请输入型号");
		queryDetailPanel.add(equipmentModelQuery);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		BtnActionListener btnActionListener = new BtnActionListener();
		
		queryBtn = new JButton("查询");
		btnPanel.add(queryBtn);
		queryBtn.addActionListener(btnActionListener);
		
		addBtn = new JButton("新增");
		btnPanel.add(addBtn);
		addBtn.addActionListener(btnActionListener);
		
		updateBtn = new JButton("修改");
		btnPanel.add(updateBtn);
		updateBtn.addActionListener(btnActionListener);
		
		delBtn = new JButton("删除");
		btnPanel.add(delBtn);
		delBtn.addActionListener(btnActionListener);
		
		modifyBtn = new JButton("入库/出库");
		btnPanel.add(modifyBtn);
		modifyBtn.addActionListener(btnActionListener);
		
		queryConditionPanel.add(queryDetailPanel);
		queryConditionPanel.add(btnPanel);
		
		queryPanel.add(queryConditionPanel);
		
		this.add(queryPanel,BorderLayout.NORTH);
		this.setVisible(true);
		
		// Create the table.
        resultTable = createTableV();
        // 让表格的4周有多点空白好看点
        JPanel tttp = new JPanel(new BorderLayout());
        tttp.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        tttp.add(resultTable, BorderLayout.CENTER);
        // END
        this.add(tttp, BorderLayout.CENTER);
	}
	
	public JScrollPane createTableV(){
		columnNames[0] = "ID";
		columnNames[1] = "序号";
		columnNames[2] = "名称";
		columnNames[3] = "型号";
		columnNames[4] = "长度";
		columnNames[5] = "库存上限";
		columnNames[6] = "库存下限";
		columnNames[7] = "数量";
		columnNames[8] = "备注";
				
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
         tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
		 tableView.setRowHeight(INITIAL_ROWHEIGHT);
		
		TableColumnModel columnModel = tableView.getColumnModel();
        TableColumn column = columnModel.getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);
        
		scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}
	
	
	private class BtnActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String btnName = e.getActionCommand();
			if(btnName.equals("查询")){
				query();
			} else if(btnName.equals("新增")){
				MaintenanceSpacerPage mp = new MaintenanceSpacerPage(null);
				query();
			} else if(btnName.equals("修改")){
				selectRow = tableView.getSelectedRow();
				if(selectRow != -1){
					Object id = (String) tableView.getValueAt(selectRow, 0);
					MaintenanceSpacerPage mp = new MaintenanceSpacerPage(id.toString());
				} else {
					JOptionPane.showMessageDialog(null, "请先选定要修改资料!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				query();
			} else if(btnName.equals("删除")){
				selectRow = tableView.getSelectedRow();
				if(selectRow != -1){
					int option = JOptionPane.showConfirmDialog(null, "确认删除夹层枪资料?", "提示消息", JOptionPane.WARNING_MESSAGE);
					if(option != 0){
						return;
					}
					Object id = (String) tableView.getValueAt(selectRow, 0);
					Map<String, String> params = new HashMap<>();
					params.put(GUNConstants.EQUIPMENT_ID, id.toString());
					String result = null;
					try {
						result = HttpUtility.post(GUNConstants.URI + "/Gui/Spc/delEquip.do?" , params);
					} catch (ClientProtocolException e1) {
						e1.printStackTrace();
						LOGGER.error(this.getClass().getName() + "ClientProtocolException error -- ",e1);
						JOptionPane.showMessageDialog(null, "系统异常!", "错误消息", JOptionPane.ERROR_MESSAGE);
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
						LOGGER.error(this.getClass().getName() + "connect POST error -- ",e1);
						JOptionPane.showMessageDialog(null, "无法连接服务器!", "错误消息", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(result != null){
						JsonResponse jr = new JsonResponse(result);
						if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
							JOptionPane.showMessageDialog(null, "删除夹层枪资料成功", "提示消息", JOptionPane.INFORMATION_MESSAGE);
							tableView.clearSelection();
							query();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "请先选定要删除资料!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				query();
			} else if(btnName.equals("入库/出库")){
				selectRow = tableView.getSelectedRow();
				if(selectRow != -1){
					Object id = (String) tableView.getValueAt(selectRow, 0);
					SpacerStoragePage equPage = new SpacerStoragePage(id.toString());
				} else {
					JOptionPane.showMessageDialog(null, "请先选定要出入库资料!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				query();
			}
			
		}
	}
	
	private void query(){
		Map<String, String> params = new HashMap<>();
		params.put(GUNConstants.QUERY_EQUIPMENT_NAME, equipmentNameQuery.getText());
		params.put(GUNConstants.QUERY_EQUIPMENT_MODEL, equipmentModelQuery.getSelectedItem().toString());
		String result = null;
		try {
			result = HttpUtility.post(GUNConstants.URI + "/Gui/Spc/queryList.do?" , params);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			LOGGER.error("ClientProtocolException error -- ",e1);
			JOptionPane.showMessageDialog(null, "系统异常!", "错误消息", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error("connect POST error -- ",e1);
			JOptionPane.showMessageDialog(null, "无法连接服务器!", "错误消息", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(result != null){
			JsonResponse jr = new JsonResponse(result);
			if(jr.getCode() == GUNConstants.STATUS_SUCCESS){
				List<Object> results =  (List<Object>) jr.getResult();
				data = toRowData(results);
				tableView.validate();
				tableView.updateUI();
			}
		}
	}
	
	public String[][] toRowData(List<Object> equipmentDTOList){
		  if(equipmentDTOList == null || equipmentDTOList.size()  <1){
			  return null;
		  }
		  Object[] lists = (Object[]) equipmentDTOList.toArray();
		  String[][] results = new String[equipmentDTOList.size()][17];
		  int i=0;
		  for(Object result : lists){
			  JSONArray jsonArr = JSONArray.fromObject("["+result+"]");
			  results[i][0] = jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.ID.getValue()).toString();
			  results[i][1] = String.valueOf(i + 1);
			  results[i][2] = jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.SPACER_NAME.getValue()).toString();
			  results[i][3] = jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.MODEL.getValue()).toString();
			  results[i][4] = jsonArr.getJSONObject(0).get(SpacerFormDTO.ATTRIBUTE.SPACER_LENGTH.getValue()).toString();
			  results[i][5] = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.STOCK_UPPER_LIMIT.getValue()).toString();
			  results[i][6] = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.STOCK_LOWER_LIMIT.getValue()).toString();
			  results[i][7] = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue()).toString();
			  results[i][8] = jsonArr.getJSONObject(0).get(MagazineFormDTO.ATTRIBUTE.REMARK.getValue()).toString();
			  i++;
		  }
		  return results;
	  }
}
