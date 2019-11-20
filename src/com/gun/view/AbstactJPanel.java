package com.gun.view;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class AbstactJPanel extends JPanel {
	
	final int INITIAL_ROWHEIGHT = 25;
	// 查询区域
	String[] columnNames = {"","","","","","","","","","","","","","","","","","","",""};
		
	Object[][] data = null;
	DefaultTableModel tableModel;

	JTable  tableView;
	
	public JTable createTable() {
        // Create a model of the data.
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
         // 隐藏ID列
         TableColumnModel columnModel = tableView.getColumnModel();
         TableColumn column = columnModel.getColumn(0);
         column.setMinWidth(0);
         column.setMaxWidth(0);
         column.setWidth(0);
         column.setPreferredWidth(0);
         
		 tableView.setRowHeight(INITIAL_ROWHEIGHT);

		 	
		 return tableView;
	}
	
}
