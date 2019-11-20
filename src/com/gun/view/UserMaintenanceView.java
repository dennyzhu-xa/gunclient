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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
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

import com.gun.dto.UserInfoFormDTO;
import com.gun.utility.GUNConstants;
import com.gun.utility.HttpUtility;
import com.gun.utility.JsonResponse;

public class UserMaintenanceView extends JPanel{
	private static final Log LOGGER = LogFactory.getLog(UserMaintenanceView.class);
	/** The table view. */
    JTable  tableView;
    private JScrollPane resultTable;
    JScrollPane scrollpane;
    JTextField userId;
    JTextField userName;
	JButton queryBtn;
	JButton addBtn;
	JButton updateBtn;
	JButton delBtn;
	final int INITIAL_ROWHEIGHT = 25;
	int selectRow;
	
	// 查询区域
	final String[] columnNames = {"ID","序号","用户帐号","姓名","E-mail","角色"," " };
	
    Object[][] data = null;
    DefaultTableModel tableModel;
	public UserMaintenanceView(int x,int y){
		System.out.println("x="+x+" y="+y);
		this.setBounds(0, 0, x-500, y);
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		
		JPanel queryPanel = new JPanel();
		queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.X_AXIS));
		queryPanel.setBorder(new TitledBorder("查询条件"));
		JPanel queryConditionPanel = new JPanel(new GridLayout(2, 1));
		
		JPanel queryDetailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		queryDetailPanel.add(new JLabel("员工帐号"));
		userId = new JTextField(8);
		userId.setToolTipText("请输入查询员工帐号");;
		queryDetailPanel.add(userId);
		queryDetailPanel.add(new JLabel("员工姓名"));
		userName = new JTextField(8);
		userName.setToolTipText("不记得全名？输入部分查询试试");
		queryDetailPanel.add(userName);
		
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
		
		queryConditionPanel.add(queryDetailPanel);
		queryConditionPanel.add(btnPanel);
		
		queryPanel.add(queryConditionPanel);
		
		this.add(queryPanel,BorderLayout.NORTH);
		this.setVisible(true);
		
		// Create the table.
        resultTable = createTable();
        // 让表格的4周有多点空白好看点
        JPanel tttp = new JPanel(new BorderLayout());
        tttp.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        tttp.add(resultTable, BorderLayout.CENTER);
        // END
        this.add(tttp, BorderLayout.CENTER);
	}

	private void queryBtn(){
		Map<String, String> params = new HashMap<>();
		params.put(GUNConstants.USER_ACCOUNT, userId.getText());
		params.put(GUNConstants.USER_NAME, userName.getText());
		String result = null;
		try {
			result = HttpUtility.post(GUNConstants.URI + "/Gui/User/queryList.do?" , params);
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
	
	private class BtnActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonName = e.getActionCommand();
			if (buttonName.equals("新增")){
				MaintenanceUserPage maintenanceUserPage = new MaintenanceUserPage(null);
				queryBtn();
			} else if(buttonName.equals("查询")){
				queryBtn();
			} else if(buttonName.equals("修改")){
				selectRow = tableView.getSelectedRow();
				if(selectRow != -1){
					Object id = (String) tableView.getValueAt(selectRow, 0);
					MaintenanceUserPage maintenanceUserPage = new MaintenanceUserPage(id.toString());
				} else {
					JOptionPane.showMessageDialog(null, "请先选定要修改资料!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				queryBtn();
			} else if(buttonName.equals("删除")){
				selectRow = tableView.getSelectedRow();
				if(selectRow != -1){
					int option = JOptionPane.showConfirmDialog(null, "确认删除员工资料?", "提示消息", JOptionPane.WARNING_MESSAGE);
					if(option != 0){
						return;
					}
					Object id = (String) tableView.getValueAt(selectRow, 0);
					
					Map<String, String> params = new HashMap<>();
					params.put(GUNConstants.USER_ID, id.toString());
					String result = null;
					try {
						result = HttpUtility.post(GUNConstants.URI + "/Gui/User/deleteUser.do?" , params);
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
							JOptionPane.showMessageDialog(null, "删除员工资料成功", "提示消息", JOptionPane.INFORMATION_MESSAGE);
							tableView.clearSelection();
							queryBtn();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "请先选定要删除资料!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
			}
			
		}
		
	}
	
	public JScrollPane createTable() {

//		tableModel = new DefaultTableModel(data, columnNames){
//			public int getColumnCount() {
//				return columnNames.length;
//			}
//			public int getRowCount(){
//				return data.length;
//			}
//			public boolean isCellEditable(int row,int col){
//				return false;
//			}
//		};
//		
        // Create a model of the data.
        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() { 
            	return columnNames.length; 
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
//         tableView = new JTable(tableModel);
         // 隐藏ID列
         TableColumnModel columnModel = tableView.getColumnModel();
         TableColumn column = columnModel.getColumn(0);
         column.setMinWidth(0);
         column.setMaxWidth(0);
         column.setWidth(0);
         column.setPreferredWidth(0);
         
         TableColumn column1 = columnModel.getColumn(6);
         column1.setMinWidth(0);
         column1.setMaxWidth(0);
         column1.setWidth(0);
         column1.setPreferredWidth(0);
//		 JCheckBox checkbox = new JCheckBox();
//		 checkbox.add(new JCheckBoxMenuItem("管理员"));
//		 checkbox.add(new JCheckBoxMenuItem("入库人员"));
//		 checkbox.add(new JCheckBoxMenuItem("一般员工"));
//		 TableColumn roleColumn = tableView.getColumn("角色");
//		 roleColumn.setCellEditor(new DefaultCellEditor(checkbox));
//		 roleColumn.

//		 colorRenderer.setHorizontalAlignment(JLabel.CENTER);
//		 roleColumn.setCellRenderer(colorRenderer);
		 
         
		 tableView.setRowHeight(INITIAL_ROWHEIGHT);

		 scrollpane = new JScrollPane(tableView);	
		 return scrollpane;
	}
	
	public String[][] toRowData(List<Object> userInfoFormDTOList){
		  if(userInfoFormDTOList == null || userInfoFormDTOList.size()  <1){
			  return null;
		  }
		  Object[] lists = (Object[]) userInfoFormDTOList.toArray();
		  String[][] results = new String[userInfoFormDTOList.size()][7];
		  int i=0;
		  for(Object result : lists){
			  JSONArray jsonArr = JSONArray.fromObject("["+result+"]");
			  results[i][0] = String.valueOf( jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.ID.getValue()));
			  results[i][1] = String.valueOf(i + 1);
			  results[i][2] = (String) jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.USER_ID.getValue());
			  results[i][3] = (String) jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.USER_NAME.getValue());
			  results[i][4] = (String) jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.EMAIL.getValue()) == null ? "" : (String) jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.EMAIL.getValue());
			  String roleIds = (String) jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.ROLE_ID.getValue());
			  String roleValue = "";
			  if(roleIds != null && roleIds.length() > 0){
				  String[] roles = roleIds.split(",");
				  for(String r : roles){
					  if(r != null && r.equals("1")){
						  roleValue += "系统管理员,";
					  } else if(r!= null && r.equals("2")){
						  roleValue += "入库人员,";
					  } else if(r!= null && r.equals("3")){
						  roleValue += "一般员工,";
					  }
					  
				  }
			  }
			  if(roleValue != null && roleValue.length() > 2){
				  roleValue = roleValue.substring(0, roleValue.length() -1);
			  }
			  results[i][5] = roleValue;
			  results[i][6] = (String) jsonArr.getJSONObject(0).get(UserInfoFormDTO.ATTRIBUTE.ROLE_ID.getValue());
			  i++;
		  }
		  return results;
	  }
	
	public static void main(String args[]){
		UserMaintenanceView user = new UserMaintenanceView(1280,758);
		
	}
	
}
