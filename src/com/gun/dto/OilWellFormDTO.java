package com.gun.dto;
import java.sql.Timestamp;

public class OilWellFormDTO {

	public static enum ATTRIBUTE {
	    ID("id"),
	    WELL_TYPE("wellType"),
	    OWN_NAME("ownName"),
	    OIL_NO("oilNo"),
	    AREA("area"),
	    MANUAL_DEEP("manualDeep"),
	    LEVEL_QUANLITY("levelQuanlity"),
	    SURFACE_TEMPERATURE("surfaceTemperature"),
	    OIL_TEMPERATURE("oilTemperature"),
	    EQUIPMENT_MODEL_ID("equipmentModelId"),
	    EQUIPMENTS("equipments"),
	    SPACER_MODEL_ID("spacerModelId"),
	    SPACERS("spacers"),
	    PERFORATING_ID("perforatingId"),
	    CONNECT_ID("connectId"),
	    PHASE("phase"),
	    EXTRA_LENGTH("extraLength"),
	    IS_3_M("is3M"),
	    SAFETY_LENGTH("safetyLength"),
	    TECH_REMARK("techRemark"),
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
  
	private String wellType;
	private String ownName;
	private String oilNo;
	private String area;
	private String manualDeep;
	private int levelQuanlity;
	private String surfaceTemperature;
	private String oilTemperature;
	private String equipmentModelId ;
	private String equipments;
	private String spacerModelId;
	private String spacers;
	private String perforatingId;
	private String connectId;
	private String phase;
	private double extraLength;
	private char is3M;
	private int safetyLength;
	private String techRemark;
	private String createById;
	private String createdByName;
	private Timestamp createdDate;
	private String updatedById;
	private String updatedByName;
	private Timestamp updatedDate;
	public String getWellType() {
		return wellType;
	}
	public void setWellType(String wellType) {
		this.wellType = wellType;
	}
	public String getOwnName() {
		return ownName;
	}
	public void setOwnName(String ownName) {
		this.ownName = ownName;
	}
	public String getOilNo() {
		return oilNo;
	}
	public void setOilNo(String oilNo) {
		this.oilNo = oilNo;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getManualDeep() {
		return manualDeep;
	}
	public void setManualDeep(String manualDeep) {
		this.manualDeep = manualDeep;
	}
	public int getLevelQuanlity() {
		return levelQuanlity;
	}
	public void setLevelQuanlity(int levelQuanlity) {
		this.levelQuanlity = levelQuanlity;
	}
	public String getSurfaceTemperature() {
		return surfaceTemperature;
	}
	public void setSurfaceTemperature(String surfaceTemperature) {
		this.surfaceTemperature = surfaceTemperature;
	}
	public String getOilTemperature() {
		return oilTemperature;
	}
	public void setOilTemperature(String oilTemperature) {
		this.oilTemperature = oilTemperature;
	}
	public String getEquipmentModelId() {
		return equipmentModelId;
	}
	public void setEquipmentModelId(String equipmentModelId) {
		this.equipmentModelId = equipmentModelId;
	}
	public String getEquipments() {
		return equipments;
	}
	public void setEquipments(String equipments) {
		this.equipments = equipments;
	}
	public String getSpacerModelId() {
		return spacerModelId;
	}
	public void setSpacerModelId(String spacerModelId) {
		this.spacerModelId = spacerModelId;
	}
	public String getSpacers() {
		return spacers;
	}
	public void setSpacers(String spacers) {
		this.spacers = spacers;
	}
	public String getPerforatingId() {
		return perforatingId;
	}
	public void setPerforatingId(String perforatingId) {
		this.perforatingId = perforatingId;
	}
	public String getConnectId() {
		return connectId;
	}
	public void setConnectId(String connectId) {
		this.connectId = connectId;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public double getExtraLength() {
		return extraLength;
	}
	public void setExtraLength(double extraLength) {
		this.extraLength = extraLength;
	}
	public char getIs3M() {
		return is3M;
	}
	public void setIs3M(char is3m) {
		is3M = is3m;
	}
	public int getSafetyLength() {
		return safetyLength;
	}
	public void setSafetyLength(int safetyLength) {
		this.safetyLength = safetyLength;
	}
	public String getTechRemark() {
		return techRemark;
	}
	public void setTechRemark(String techRemark) {
		this.techRemark = techRemark;
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
