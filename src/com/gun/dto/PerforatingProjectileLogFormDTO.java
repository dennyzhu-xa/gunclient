package com.gun.dto;

import java.sql.Timestamp;

public class PerforatingProjectileLogFormDTO {
	public static enum ATTRIBUTE {
	    ID("id"),
	    PERFORATING_ID("perforatingId"),
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
	private int perforatingId;
	private char logType;
	private int oldQuantity;
	private int updateQuantity;
	private String logRemark;
	private String modifyId;
	private String modifyName;
	private Timestamp modifyDate;

	public int getPerforatingId() {
		return perforatingId;
	}
	public void setPerforatingId(int perforatingId) {
		this.perforatingId = perforatingId;
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
}
