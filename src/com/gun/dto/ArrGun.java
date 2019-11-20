package com.gun.dto;

public class ArrGun {

	private Integer equipmentId;
	
	private int sequence;
	
	private double length;
	
	private double fromDeep;
	
	private double toDeep;
	
	private String type;

	public ArrGun() {
	}

	public ArrGun(Integer equipmentId,int sequence, double length, double fromDeep,
			double toDeep,String type) {
		super();
		this.equipmentId = equipmentId;
		this.length = length;
		this.fromDeep = fromDeep;
		this.toDeep = toDeep;
		this.sequence = sequence;
		this.type = type;
	}

	
	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getFromDeep() {
		return fromDeep;
	}

	public void setFromDeep(double fromDeep) {
		this.fromDeep = fromDeep;
	}

	public double getToDeep() {
		return toDeep;
	}

	public void setToDeep(double toDeep) {
		this.toDeep = toDeep;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
