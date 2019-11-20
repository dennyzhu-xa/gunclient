package com.gun.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.gun.dto.ArrGunFormDTO;
import com.gun.dto.OilLevelDTO;

public class LeftTwoPanel extends JPanel {

	DecimalFormat df = new DecimalFormat("0.0");
	private JTextField upTextField;
    private JTextField downTextField;

	int selectedRow;

	DefaultTableModel tableModel;

	public List<OilLevelDTO> getOilLevelDTOList(){
		List<OilLevelDTO> listDTO = null;
		int size = tableModel.getRowCount();
		Vector result = tableModel.getDataVector();
		if(result != null){
			OilLevelDTO oilLevel = null;
			listDTO = new ArrayList<OilLevelDTO>();
			for(int i=0;i<size ;i++){
				
				String oa = (String) tableModel.getValueAt(i, 0); // 上限
				String ob = (String) tableModel.getValueAt(i, 1); // 下限
				String deep = (String) tableModel.getValueAt(i, 2); // 厚度
				oilLevel = new OilLevelDTO(Double.valueOf(oa),Double.valueOf(ob),Double.valueOf(deep));
				listDTO.add(oilLevel);
			}
			this.orderOilLeveles(listDTO);
			// 排序后将夹层厚添加到List中
			this.refreshOilLevels(listDTO);
		}
		return listDTO;
	}
	
	/**
	 * 添加夹层厚与累计厚度到List中
	 * 最后一个 没有夹层厚且累计厚度只能累计到油层
	 * @param oilLevelDTOList
	 */
	private void refreshOilLevels(List<OilLevelDTO> oilLevelDTOList){
		if(oilLevelDTOList == null || oilLevelDTOList.size() < 1) return;
		if(oilLevelDTOList.size() == 1){
			oilLevelDTOList.get(0).setDeepSum(oilLevelDTOList.get(0).getDeep());
		} else {
			for(int i=0;i<oilLevelDTOList.size();i++){
				double s = oilLevelDTOList.get(i).getEndLevel();
				double e = 0;
				if(i<oilLevelDTOList.size()-1){
					e = oilLevelDTOList.get(i+1).getStartLevel();
					double d = e-s;// 夹层厚
					oilLevelDTOList.get(i).setBackinDepth(Double.valueOf(df.format(d)));
					if( i>0 ){
						double preDeep = oilLevelDTOList.get(i-1).getBackfinSum();
						oilLevelDTOList.get(i).setDeepSum(Double.valueOf(df.format(preDeep + oilLevelDTOList.get(i).getDeep())));
						oilLevelDTOList.get(i).setBackfinSum(Double.valueOf(df.format(preDeep + oilLevelDTOList.get(i).getDeep() + d)));
					} else {
						//第一个
						oilLevelDTOList.get(i).setDeepSum(oilLevelDTOList.get(i).getDeep());
						oilLevelDTOList.get(i).setBackfinSum(Double.valueOf(df.format(oilLevelDTOList.get(i).getDeep() + d)));
					}
				} else {//最后一个
					oilLevelDTOList.get(i).setDeepSum(Double.valueOf(df.format(oilLevelDTOList.get(i-1).getBackfinSum() + oilLevelDTOList.get(i).getDeep())));
				}
			}
		}
	}
	
	public void orderOilLeveles(List<OilLevelDTO> oilLevelDTOList){
		if(oilLevelDTOList == null || oilLevelDTOList.size() < 1) return;
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
	
	public void refreshLeftTwoPanel(ArrGunFormDTO formDTO){
		if(formDTO == null) return;
		List<OilLevelDTO> result = formDTO.getOilLevelDTOList();
		if(result != null && result.size() > 0){
			// 先清空table中的内容
			// 射孔井段为空
			int count = tableModel.getRowCount();
			for(int i=count-1;i>=0;i--){
				tableModel.removeRow(0);
			}
			for(int i=0;i<result.size();i++){
				//设置要添加到DefaultTableModel中的---行数据内容
		        String[] rowValues = {String.valueOf(result.get(i).getStartLevel()),String.valueOf(result.get(i).getEndLevel()),df.format(result.get(i).getDeep())};
		        tableModel.addRow(rowValues);
			}
		} else {
			// 射孔井段为空
			int count = tableModel.getRowCount();
			for(int i=count-1;i>=0;i--){
				tableModel.removeRow(0);
			}
		}
	}
	
	public LeftTwoPanel(){
		this.setLayout(new BorderLayout());
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBorder(new TitledBorder("射孔井段"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(0));
        panel.add(new JLabel("油层顶界: "));
        upTextField = new JTextField("", 8); 
        upTextField.addKeyListener(new limitNumberKeyListener());
        panel.add(upTextField);
        
//        JPanel buttonPanel = new JPanel();
        panel.setLayout(new FlowLayout(0));
        panel.add(new JLabel("底界: "));
        downTextField = new JTextField("", 8);
        downTextField.addKeyListener(new limitNumberKeyListener());
        panel.add(downTextField);
        JButton insertButton = new JButton("添加");
        panel.add(insertButton);
        JButton deleteButton = new JButton("删除");
        panel.add(deleteButton);
        panel.setVisible(true);
//        panel.add(buttonPanel);
//        panel.setVisible(true);
        
		titlePanel.add(panel);
		
		Vector<String> titles = new Vector();
		titles.add("油层顶界");
		titles.add("油层底界");
		titles.add("厚度");
		
		final JScrollPane scrollPane = new JScrollPane();
		titlePanel.add(scrollPane, BorderLayout.WEST);

		// 设置表格数据
		Vector<Double> data = new Vector();
		
		tableModel = new DefaultTableModel(data,titles){
			public boolean isCellEditable(int row,int column){
				return false;
			}
		};
		final JTable table = new JTable(tableModel);
		//设置表格的选择模式---为单选模式
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
		scrollPane.setViewportView(table);
		
        this.add(titlePanel);
        /**
         * 点击JScrollPane滚动面板，取消选中的行
         */
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //取消掉选中行，但是选中行的索引值还是在的，没有取消掉
                table.clearSelection();
                //设置JTextField的默认值
                upTextField.setText("");
                downTextField.setText("");
            }
        });
         
         
        /**
         * 将选中的JTable行的的信息传递给-upTextField、downTextField、cTextField
         */
        table.addMouseListener(new MouseAdapter() {
 
            @Override
            public void mouseClicked(MouseEvent e) {
                //获取到JTable中选定行的---索引值
                selectedRow = table.getSelectedRow();
                //获取到指定单元格的值
                Object oa = tableModel.getValueAt(selectedRow, 0);
                Object ob = tableModel.getValueAt(selectedRow, 1);
                //将获取去到的DefaultTableModel中指定单元格的值，传递给JTextField
                upTextField.setText(oa.toString());
                downTextField.setText(ob.toString());
            }
            
        });
         
        /**
         * 添加
         */
        insertButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String upText = upTextField.getText();
				if(upText == null || upText.length() <= 0){
					JOptionPane.showMessageDialog(null, "油层顶界值不能为空!");
					return;
				}
				Pattern p = Pattern.compile("^\\d*(\\.?\\d{0,1})?$"); // 将正则表达式编译为Pattern
				Matcher m = p.matcher(upText); // 创建匹配器
				if (!m.matches()){
					JOptionPane.showMessageDialog(null, "油层顶界值必须为整数或一位小数!");
					return;
				}
				Double upDouble = Double.valueOf(upText);
				if(upDouble <= 0){
					JOptionPane.showMessageDialog(null, "油层顶界值应大于0!");
					return;
				}
				String downText = downTextField.getText();
				if(downText == null || downText.length() <= 0){
					JOptionPane.showMessageDialog(null, "油层底界值不能为空!");
					return;
				}
				m = p.matcher(downText); // 创建匹配器
				if (!m.matches()){
					JOptionPane.showMessageDialog(null, "油层底界值必须为整数或一位小数!");
					return;
				}
				Double downDouble = Double.valueOf(downText);
				if(downDouble <= 0){
					JOptionPane.showMessageDialog(null, "油层底界值应大于0!");
					return;
				}
				Double deep = downDouble - upDouble;
				if(deep <= 0){
					JOptionPane.showMessageDialog(null, "油层底界值应大于油层顶界值!");
					return;
				}
				//设置要添加到DefaultTableModel中的---行数据内容
		        String[] rowValues = {upTextField.getText(), downTextField.getText(),df.format(deep)};
		        tableModel.addRow(rowValues);
		        //设置JTextField的默认值
		        upTextField.setText("");
		        downTextField.setText("");
		        
		     // TODO Auto-generated method stub
		        // 新输入的值不能和table中的值有重叠
			}
        	
        });
         
        /**
         * 删除
         */
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取到JTable中选定行的---索引值
                selectedRow = table.getSelectedRow();
                //JTable中行的索引值是从0,1,2.3...开始的，
                //只有选中行时，才能修改内容
                if(selectedRow != -1){
                    //删除DefaultTableModel中---指定索引值得行
                    tableModel.removeRow(selectedRow);
                    //设置JTextField的默认值
  		          	upTextField.setText("");
  		          	downTextField.setText("");
                }
                
            }
        });
         
         
//        /**
//         * 修改
//         */
//        updateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //获取到JTable中选定行的---索引值
//                selectedRow = table.getSelectedRow();
//                //JTable中行的索引值是从0,1,2.3...开始的，
//                //只有选中行时，才能修改内容
//                if(selectedRow != -1){
//                    //通过获取到JTextField的中的值，来修改指定单元格的内容
//                    defaultTableModel.setValueAt(upTextField.getText(), selectedRow, 0);
//                    defaultTableModel.setValueAt(downTextField.getText(), selectedRow, 1);
//                    defaultTableModel.setValueAt(cTextField.getText(), selectedRow, 2);
//                }
//            }
//        });

        
	}

	private static class limitNumberKeyListener implements KeyListener{
		@Override
		public void keyTyped(KeyEvent e) {
//			int keyChar=e.getKeyChar();
//			if (keyChar>=KeyEvent.VK_0 && keyChar<=KeyEvent.VK_9) {
//			} else {
//				e.consume();
//			}
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
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
