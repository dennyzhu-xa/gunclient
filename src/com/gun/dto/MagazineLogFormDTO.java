package com.gun.dto;

import java.sql.Timestamp;

public class MagazineLogFormDTO {
	
	public static enum ATTRIBUTE {
	    ID("id"),
	    MAGAZINE_ID("magazineId"),
	    LOG_TYPE("logType"),
	    OLD_QUANTITY("oldQuantity"),
	    UPDATE_QUANTITY("updateQuantity"),
	    PURPOSE("purpose"),
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

	/**
	 * 	ID	id
		设备ID	equipment_id
		入库/出库类型	log_type
		原库存数量	old_quantity
		变更数量	update_quantity
		用途	purpose
		备注	log_remark
		变更人	modify_id
		变更时间	modify_date
	 */
	private int magazineId;
	private char logType;
	private int oldQuantity;
	private int updateQuantity;
	private String logRemark;
	private String modifyId;
	private String modifyName;
	private Timestamp modifyDate;

	public int getMagazineId() {
		return magazineId;
	}
	public void setMagazineId(int magazineId) {
		this.magazineId = magazineId;
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
