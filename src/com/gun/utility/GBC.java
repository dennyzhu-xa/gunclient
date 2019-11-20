package com.gun.utility;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	//初始化左上角位置  行对应的是gridy,列对应的是gridx 如面板在1行0列，则gridy=0,gridx=1。
	public GBC(int gridx, int gridy) {  
		this.gridx = gridx;  
		this.gridy = gridy;
	}  
	  
	   //初始化位置和所占行数和列数   组件占据的行数和列数，如面板占了1行2列，则gridwidth=2,gridheight=1
	   public GBC(int gridx, int gridy, int gridwidth, int gridheight)  
	   {  
	      this.gridx = gridx;  
	      this.gridy = gridy;  
	      this.gridwidth = gridwidth;  
	      this.gridheight = gridheight;  
	   }  
	  
	   //对齐方式  组件在所处格子内的对其方式，取anchor=EAST就是指右对齐
	   public GBC setAnchor(int anchor)  
	   {  
	      this.anchor = anchor;  
	      return this;  
	   }  
	  
	   //是否拉伸及拉伸方向  
	   /**
	    * 组件在所处格子(分配区域)内的填充方式 如fill= HORIZONTAL,组件就只在水平方向上填充满单元格，取fill= BOTH则会填满整个格子。
	    * @param fill
	    * @return
	    */
	   public GBC setFill(int fill)  
	   {  
	      this.fill = fill;  
	      return this;  
	   }  
	  
	   //x和y方向上的增量  可以简单理解为组件大小变化的增量值，如设置weightx=100，组件会随着单元格而变化，设置weightx=0时，组件大小不会发生变化
	   public GBC setWeight(double weightx, double weighty)  
	   {  
	      this.weightx = weightx;  
	      this.weighty = weighty;  
	      return this;  
	   }  
	  
	   // 外部填充  
	   public GBC setInsets(int distance)  
	   {  
	      this.insets = new Insets(distance, distance, distance, distance);  
	      return this;  
	   }  
	  
	   // 外部填充，填充的区域是组件与所处格子边框之间的部分,有left,top,right,bottom四个参数，
	   // 不过当组件的fill=NONE时，指定insects值是无意义的
	   public GBC setInsets(int top, int left, int bottom, int right)  
	   {  
	      this.insets = new Insets(top, left, bottom, right);  
	      return this;  
	   }  
	  
	   // 内部填充，是指在组件首选大小的基础上x方向上加上ipadx，y方向上加上ipady,这样做就可以保证组件不会收缩到ipadx,ipady所确定的大小以下，
	   // 因此我们可以用ipadx,ipady的值来指定组件的大小，而不必指定组件的大小否则会有意想不到的效果
	   public GBC setIpad(int ipadx, int ipady)  
	   {  
	      this.ipadx = ipadx;  
	      this.ipady = ipady;  
	      return this;  
	   }
	
}
