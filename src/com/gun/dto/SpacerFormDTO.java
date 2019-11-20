package com.gun.dto;

import java.sql.Timestamp;

public class SpacerFormDTO {

	public static enum ATTRIBUTE {
	    ID("id"),
	    SPACER_NAME("spacerName"),
	    MODEL("model"),
	    SPACER_LENGTH("spacerLength"),
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
	    UPDATED_DATE("updatedDate")
	    ;
	    
	    private String value;
	    ATTRIBUTE(String value) {
	      this.value = value;
	    };
	    public String getValue() {
	      return this.value;
	    }
	};
	
	private Integer id;
	private String spacerName;
	private String model;
	private double spacerLength;
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
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSpacerName() {
		return spacerName;
	}
	public void setSpacerName(String spacerName) {
		this.spacerName = spacerName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public double getSpacerLength() {
		return spacerLength;
	}
	public void setSpacerLength(double spacerLength) {
		this.spacerLength = spacerLength;
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
