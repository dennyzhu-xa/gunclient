package com.gun.dto;

import java.sql.Timestamp;

public class PerforatingProjectileFormDTO {

	
	public static enum ATTRIBUTE {
	    ID("id"),
	    PERFORATING_NAME("perforatingName"),
	    MODEL("model"),
	    STOCK_UPPER_LIMIT("stockUpperLimit"),
	    STOCK_LOWER_LIMIT("stockLowerLimit"),
	    REMARK("remark"),
	    TOTAL_QUANTITY("totalQuantity"),
	    DELETE_FLAG("deleteFlag"),
	    CREATE_BY_ID("createById"),
	    CREATED_BY_NAME("createdByName"),
	    CREATED_DATE("createdDate"),
	    UPDATED_BY_ID("updatedById"),
	    UPDATED_BY_NAME("updatedByName"),
	    UPDATED_DATE("updatedDate");
	    
	    private String value;
	    ATTRIBUTE(String value) {
	      this.value = value;
	    };
	    public String getValue() {
	      return this.value;
	    }
	};
	private String perforatingName;
	private String model;
	private Integer stockUpperLimit;
	private Integer stockLowerLimit;
	private String remark;
	private int totalQuantity;
	private char deleteFlag;
	private String createById;
	private String createdByName;
	private Timestamp createdDate;
	private String updatedById;
	private String updatedByName;
	private Timestamp updatedDate;
	
	public String getPerforatingName() {
		return perforatingName;
	}
	public void setPerforatingName(String perforatingName) {
		this.perforatingName = perforatingName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public Integer getStockUpperLimit() {
		return stockUpperLimit;
	}
	public void setStockUpperLimit(Integer stockUpperLimit) {
		this.stockUpperLimit = stockUpperLimit;
	}
	public Integer getStockLowerLimit() {
		return stockLowerLimit;
	}
	public void setStockLowerLimit(Integer stockLowerLimit) {
		this.stockLowerLimit = stockLowerLimit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public char getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(char deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getCreateById() {
		return createById;
	}
	public void setCreateById(String createById) {
		this.createById = createById;
	}
	public String getCreatedByName() {
		return createdByName;
	}
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedById() {
		return updatedById;
	}
	public void setUpdatedById(String updatedById) {
		this.updatedById = updatedById;
	}
	public String getUpdatedByName() {
		return updatedByName;
	}
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
