package com.gun.dto;

public class OilLevelDTO {
	
	public static enum ATTRIBUTE {
		START_LEVEL("startLevel"),
		END_LEVEL("endLevel"),
		DEEP("deep"),
		TYPE("type"),
		BACKIN_DEPTH("backinDepth"),
		DEEP_SUM("deepSum"),
		BACKFIN_SUM("backfinSum");
	    
	    private String value;
	    ATTRIBUTE(String value) {
	      this.value = value;
	    };
	    public String getValue() {
	      return this.value;
	    }
	}
	
	public double startLevel;
	
	public double endLevel;
	
	public double deep;
	
	// 用于分辨夹层或是油层，空或是O为油层，S为夹层
	public String type;

	public double backinDepth;
	
	public double deepSum;
	
	public double backfinSum;
	
	public OilLevelDTO() {
	}
	
	public OilLevelDTO(double startLevel, double endLevel,double deep) {
		super();
		this.startLevel = startLevel;
		this.endLevel = endLevel;
		this.deep = deep;
	}

	
	
	public OilLevelDTO(double startLevel, double endLevel, double deep,
			String type) {
		super();
		this.startLevel = startLevel;
		this.endLevel = endLevel;
		this.deep = deep;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getStartLevel() {
		return startLevel;
	}

	public void setStartLevel(double startLevel) {
		this.startLevel = startLevel;
	}

	public double getEndLevel() {
		return endLevel;
	}

	public void setEndLevel(double endLevel) {
		this.endLevel = endLevel;
	}

	public double getDeep() {
		return deep;
	}

	public void setDeep(double deep) {
		this.deep = deep;
	}

	public double getBackinDepth() {
		return backinDepth;
	}

	public void setBackinDepth(double backinDepth) {
		this.backinDepth = backinDepth;
	}

	public double getDeepSum() {
		return deepSum;
	}

	public void setDeepSum(double deepSum) {
		this.deepSum = deepSum;
	}

	public double getBackfinSum() {
		return backfinSum;
	}

	public void setBackfinSum(double backfinSum) {
		this.backfinSum = backfinSum;
	}
	
	public static void main(String args[]){
		java.lang.reflect.Field[] f  = OilLevelDTO.class.getDeclaredFields();
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
