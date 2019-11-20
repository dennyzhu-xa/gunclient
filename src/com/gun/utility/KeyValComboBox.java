package com.gun.utility;

import javax.swing.JComboBox;

import java.awt.Component;
import java.util.Vector;
 
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
 
public class KeyValComboBox<E> extends JComboBox<E>{
 
    public KeyValComboBox(E[] values){
          super(values);
          rendererData(); //渲染数据
    }
    
     public void rendererData(){
    	 ListCellRenderer render = new DefaultListCellRenderer(){
    		 public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus){
    			 super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                 if (value instanceof KeyValue){
              	   KeyValue po = (KeyValue) value;
                     this.setText(po.getKey());
                 }
                 return this;
    		 }
    	 };
          this.setRenderer(render);
     }
 
    //修改Combox中的数据
     public void updateData(Vector values){
          setModel(new DefaultComboBoxModel(values));
          rendererData();
     }
    
     @Override
      public void setSelectedItem(Object anObject){ //选中text与传入的参数相同的项
          if (anObject != null){
               if (anObject instanceof KeyValue){
                     super.setSelectedItem(anObject);
               }
               if(anObject instanceof String){
                     for (int index = 0; index < getItemCount(); index++){
                            KeyValue po = (KeyValue) getItemAt(index);
                            if (po.getKey().equals(anObject.toString())){
                                   super.setSelectedIndex(index);
                            }
                     }
               }
          }else{
                 super.setSelectedItem(anObject);
          }
      }
      
      public void setSelectedValue(Object anObject){ //选中value与传入的参数相同的项
          if(anObject != null){
              if(anObject instanceof KeyValue){
                    super.setSelectedItem(anObject);
              }
              if(anObject instanceof String){
                    for(int index = 0; index < getItemCount(); index++){
                         KeyValue po = (KeyValue) getItemAt(index);
                         if(po.getValue().equals(anObject.toString())){
                             super.setSelectedIndex(index);
                         }
                    }
              }
          }else{
               super.setSelectedItem(anObject);
          }
      }
 
      //获得选中项的键值
    public String getSelectedValue(){
           if(getSelectedItem() instanceof KeyValue){
                KeyValue po = (KeyValue)getSelectedItem();
                return po.getValue();
           }
           return (getSelectedItem() != null) ? getSelectedItem().toString() : null;
      }
 
      //获得选中项的显示文本
    public String getSelectedText(){
             if(getSelectedItem() instanceof KeyValue){
            	 KeyValue po = (KeyValue)getSelectedItem();
                return po.getKey();
           }
           return (getSelectedItem() != null) ? getSelectedItem().toString() : null;
      }
}

