package com.gun.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gun.dto.ArrGunFormDTO;

public class ListWellPage extends JDialog {

	private static final Log LOGGER = LogFactory.getLog(ListWellPage.class);
	JScrollPane scrollpane;
	private JScrollPane resultTable;
	final int INITIAL_ROWHEIGHT = 25;
	// 查询区域
	String[] columnNames = {"","","",""};
		
	Object[][] data = null;
	DefaultTableModel tableModel;

	JTable  tableView;
	
	int selectRow;
	
	public ListWellPage (List<Object> results,PrepareMainView mainPanel){
		this.setTitle("");
		setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setBounds(300, 200, 593, 483);

        placeComponents(results,mainPanel,this);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);    // 设置模式类型。
     // 参数 APPLICATION_MODAL：阻塞同一 Java 应用程序中的所有顶层窗口（它自己的子层次结构中的顶层窗口除外）。
        this.setVisible(true);
	}
	
	public void placeComponents(List<Object> results,final PrepareMainView mainPanel,final JDialog dialog) {
		data = toRowData(results);
		// Create the table.
        resultTable = createTableV();
        // 让表格的4周有多点空白好看点
        JPanel tttp = new JPanel(new BorderLayout());
        tttp.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        tttp.add(resultTable, BorderLayout.CENTER);
        // END
        this.add(tttp, BorderLayout.CENTER);
        
        // 
        final JPanel btnPanel = new JPanel();
        JButton btn1 = new JButton("确定");
        btn1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				selectRow = tableView.getSelectedRow();
				if(selectRow != -1){
					Object id = (String) tableView.getValueAt(selectRow, 3);
					dialog.dispose();
					mainPanel.refresh(id.toString());
				} else {
					JOptionPane.showMessageDialog(btnPanel, "请先选定资料!", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
        });
        btnPanel.add(btn1);
        this.add(btnPanel,BorderLayout.SOUTH);
	}
	
	public JScrollPane createTableV(){
		columnNames[0] = "序号";
		columnNames[1] = "井号";
		columnNames[2] = "地区";
		columnNames[3] = "ID";
				
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
        TableColumn column = columnModel.getColumn(3);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);
        
		scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}
	
	public String[][] toRowData(List<Object> data){
		  if(data == null || data.size()  <1){
			  return null;
		  }
		  String[][] results = new String[data.size()][4];
		  for(int i=0;i<data.size();i++){
			  JSONArray jsonArr = JSONArray.fromObject("["+data.get(i)+"]");
			  results[i][0] = String.valueOf(i + 1);
			  Object wellNo = jsonArr.getJSONObject(0).get(ArrGunFormDTO.ATTRIBUTE.WELL_NO.getValue());
			  Object area = jsonArr.getJSONObject(0).get(ArrGunFormDTO.ATTRIBUTE.AREA.getValue());
			  results[i][1] = wellNo == null ? "" : wellNo.toString();
			  results[i][2] = area == null ? "" : area.toString();
			  Object wellId = jsonArr.getJSONObject(0).get(ArrGunFormDTO.ATTRIBUTE.WELL_ID.getValue());
			  results[i][3] = wellId == null ? "" : wellId.toString();
		  }
		  return results;
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
			Object gun = jo.get(ArrGunFormDTO.ATTRIBUTE.GUN.getValue());
			f.setGun(gun == null ? null : (String[])gun);
			Object spaceModel = jo.get(ArrGunFormDTO.ATTRIBUTE.SPACE_MODEL.getValue());
			f.setSpaceModel(spaceModel == null ? "" : spaceModel.toString());
			Object spaceGun = jo.get(ArrGunFormDTO.ATTRIBUTE.SPACE_GUN.getValue());
			f.setSpaceGun(spaceGun == null ? null : (String[])spaceGun);
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
			formDTO.add(f);
		}
		return formDTO;
	}
}
