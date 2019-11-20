package com.gun.dto;

public class WellLevelFormDTO {
	public static enum ATTRIBUTE {
		ID("id"),
		WELL_ID("wellId"),
		OIL_START("oilStart"),
		OIL_END("oilEnd"),
		LEVEL_DEPTH("levelDepth"),
		BACKIN_DEPTH("backinDepth");
	    
	    private String value;
	    ATTRIBUTE(String value) {
	      this.value = value;
	    };
	    public String getValue() {
	      return this.value;
	    }
	}
	private int wellId;
	private double oilStart;
	private double oilEnd;
	private double levelDepth;
	private double backinDepth;
	
	
	public int getWellId() {
		return wellId;
	}


	public void setWellId(int wellId) {
		this.wellId = wellId;
	}


	public double getOilStart() {
		return oilStart;
	}


	public void setOilStart(double oilStart) {
		this.oilStart = oilStart;
	}


	public double getOilEnd() {
		return oilEnd;
	}


	public void setOilEnd(double oilEnd) {
		this.oilEnd = oilEnd;
	}


	public double getLevelDepth() {
		return levelDepth;
	}


	public void setLevelDepth(double levelDepth) {
		this.levelDepth = levelDepth;
	}


	public double getBackinDepth() {
		return backinDepth;
	}


	public void setBackinDepth(double backinDepth) {
		this.backinDepth = backinDepth;
	}
}
