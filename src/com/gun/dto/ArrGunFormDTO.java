package com.gun.dto;

import java.util.List;

public class ArrGunFormDTO {

	public static enum ATTRIBUTE {
		WELL_ID("wellId"),
		FIRST_PARTY("firstParty"),
		WELL_NO("wellNo"),
		AREA("area"),
		MANUAL_WELL("manualWell"),
		PHASE("phase"),
		GUN_MODEL("gunModel"),
		GUN("gun"),
		SPACE_MODEL("spaceModel"),
		SPACE_GUN("spaceGun"),
		MAGAZINE("magazine"),
		CONNECTOR("connector"),
		USE_SAFETY("useSafety"),
		SAFE_LENGTH("safeLength"),
		EXTRA_LENGTH("extraLength"),
		OIL_LEVEL_D_T_O_LIST("oilLevelDTOList"),
		REMARK("remark");
		
	    private String value;
	    ATTRIBUTE(String value) {
	      this.value = value;
	    };
	    public String getValue() {
	      return this.value;
	    }
	};
	// OilWell id
	private String wellId;
	// 甲方
	private String firstParty;
	// 井号
	private String wellNo;
	// 地区
	private String area;
	// 人工井底
	private String manualWell;
	// 相位
	private String phase;
	//射孔枪类型
	private String gunModel;
	//射孔枪
	private String[] gun;
	//夹层枪类型
	private String spaceModel;
	// 夹层枪
	private String[] spaceGun;
	// 弹型
	private String magazine;
	// 中接头
	private String connector;
	// 是否使用安全枪
	private boolean useSafety;
	// 安全枪长度
	private int safeLength;
	// 底部零长
	private double extraLength;
	// 油层
	private List<OilLevelDTO> oilLevelDTOList;
	// 技术说明
	private String remark;
	
	public ArrGunFormDTO() {
		super();
	}
	
	public String getWellId() {
		return wellId;
	}

	public void setWellId(String wellId) {
		this.wellId = wellId;
	}

	public String getFirstParty() {
		return firstParty;
	}
	public void setFirstParty(String firstParty) {
		this.firstParty = firstParty;
	}
	public String getWellNo() {
		return wellNo;
	}
	public void setWellNo(String wellNo) {
		this.wellNo = wellNo;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getManualWell() {
		return manualWell;
	}
	public void setManualWell(String manualWell) {
		this.manualWell = manualWell;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getGunModel() {
		return gunModel;
	}
	public void setGunModel(String gunModel) {
		this.gunModel = gunModel;
	}
	public String[] getGun() {
		return gun;
	}
	public void setGun(String[] gun) {
		this.gun = gun;
	}
	public String getSpaceModel() {
		return spaceModel;
	}
	public void setSpaceModel(String spaceModel) {
		this.spaceModel = spaceModel;
	}
	public String[] getSpaceGun() {
		return spaceGun;
	}
	public void setSpaceGun(String[] spaceGun) {
		this.spaceGun = spaceGun;
	}
	public String getMagazine() {
		return magazine;
	}
	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}
	public String getConnector() {
		return connector;
	}
	public void setConnector(String connector) {
		this.connector = connector;
	}
	public boolean isUseSafety() {
		return useSafety;
	}
	public void setUseSafety(boolean useSafety) {
		this.useSafety = useSafety;
	}
	public int getSafeLength() {
		return safeLength;
	}
	public void setSafeLength(int safeLength) {
		this.safeLength = safeLength;
	}
	public double getExtraLength() {
		return extraLength;
	}
	public void setExtraLength(double extraLength) {
		this.extraLength = extraLength;
	}
	public List<OilLevelDTO> getOilLevelDTOList() {
		return oilLevelDTOList;
	}
	public void setOilLevelDTOList(List<OilLevelDTO> oilLevelDTOList) {
		this.oilLevelDTOList = oilLevelDTOList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
