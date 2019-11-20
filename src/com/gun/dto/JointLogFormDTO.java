package com.gun.dto;

import java.sql.Timestamp;

public class JointLogFormDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static enum ATTRIBUTE {
	    ID("id"),
	    JOINT_ID("jointId"),
	    LOG_TYPE("logType"),
	    OLD_QUANTITY("oldQuantity"),
	    UPDATE_QUANTITY("updateQuantity"),
	    LOG_REMARK("logRemark"),
	    MODIFY_ID("modifyId"),
	    MODIFY_NAME("modifyName"),
	    MODIFY_DATE("modifyDate")
	    ;
	    
	    private String value;
	    ATTRIBUTE(String value) {
	      this.value = value;
	    };
	    public String getValue() {
	      return this.value;
	    }
	  };
	
	private int jointId;
	private char logType;
	private int oldQuantity;
	private int updateQuantity;
	private String logRemark;
	private String modifyId;
	private String modifyName;
	private Timestamp modifyDate;
	public int getJointId() {
		return jointId;
	}
	public void setJointId(int jointId) {
		this.jointId = jointId;
	}
	public char getLogType() {
		return logType;
	}
	public void setLogType(char logType) {
		this.logType = logType;
	}
	public int getOldQuantity() {
		return oldQuantity;
	}
	public void setOldQuantity(int oldQuantity) {
		this.oldQuantity = oldQuantity;
	}
	public int getUpdateQuantity() {
		return updateQuantity;
	}
	public void setUpdateQuantity(int updateQuantity) {
		this.updateQuantity = updateQuantity;
	}
	public String getLogRemark() {
		return logRemark;
	}
	public void setLogRemark(String logRemark) {
		this.logRemark = logRemark;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}


	public static void main(String args[]){
		java.lang.reflect.Field[] f  = JointLogFormDTO.class.getDeclaredFields();
		String sub = "";
		for(int i=0;i<f.length;i++){
			// USER_ID("userId"),
			String name = f[i].getName();
			char[] c = name.toCharArray();
			for(char ci : c){
				// <91为大写，在其前方加_
				if((int)ci < 91){
					sub += "_";
				}
				sub += String.valueOf(ci).toUpperCase();
			}
			sub += "(\"" + name + "\"),";
			sub += "\n";
		}
		System.out.println(sub);
	}
}
