package com.gun.utility;

import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class AutoCompleter implements KeyListener {

	private JComboBox owner = null;  
    private JTextField editor = null;  
  
    private ComboBoxModel model = null;  
  
    public AutoCompleter(JComboBox comboBox) {  
        owner = comboBox;  
        editor = (JTextField) comboBox.getEditor().getEditorComponent();  
        editor.addKeyListener(this);  
        model = comboBox.getModel();  
    }
    
    public AutoCompleter(JComboBox comboBox, ItemListener itemListener) {  
        owner = comboBox;  
        editor = (JTextField) comboBox.getEditor().getEditorComponent();  
        editor.addKeyListener(this);  
        model = comboBox.getModel();  
        owner.addItemListener(itemListener);  
    }
    
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		char ch = e.getKeyChar();  
        if (ch == KeyEvent.VK_ENTER) {  
            return;  
        }  
        editor.setText("");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		char ch = e.getKeyChar();  
        if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch)  
                || ch == KeyEvent.VK_DELETE)  
            return;  
  
        int caretPosition = editor.getCaretPosition();  
        String str = editor.getText();  
        if (str.length() == 0)  
            return;  
        autoComplete(str, caretPosition);
	}

	/** 
     * 自动完成。根据输入的内容，在列表中找到相似的项目. 
     */  
    protected void autoComplete(String strf, int caretPosition) {  
        Object[] opts;  
        opts = getMatchingOptions(strf.substring(0, caretPosition));  
        if (owner != null) {  
            model = new DefaultComboBoxModel(opts);  
            owner.setModel(model);  
        }  
        if (opts.length > 0) {  
            String str = opts[0].toString();  
            // editor.setCaretPosition(caretPosition);  
            if (owner != null) {  
                try {  
                    owner.showPopup();  
                } catch (Exception ex) {  
                    ex.printStackTrace();  
                }  
            }  
        }  
    }  
  
    /** 
     *  
     * 找到相似的项目, 并且将之排列到数组的最前面。 
     *  
     * @param str 
     * @return 返回所有项目的列表。 
     */  
    protected Object[] getMatchingOptions(String str) {  
        List v = new Vector();  
        List v1 = new Vector();  
        model = owner.getModel();  
        for (int k = 0; k < model.getSize(); k++) {  
            Object itemObj = model.getElementAt(k);  
            if (itemObj != null) {  
                String item = itemObj.toString();  
                if (item.startsWith(str))  
                    v.add(model.getElementAt(k));  
                else  
                    v1.add(model.getElementAt(k));  
            } else  
                v1.add(model.getElementAt(k));  
        }  
        for (int i = 0; i < v1.size(); i++) {  
            v.add(v1.get(i));  
        }  
        if (v.isEmpty())  
            v.add(str);  
        return v.toArray();  
    }
	
}
