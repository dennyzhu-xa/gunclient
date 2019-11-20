package com.gun.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

public class JointFormDTO{

	public static enum ATTRIBUTE {
	    ID("id"),
	    JOINT_NAME("jointName"),
	    JOINT_EXTERNAL_DIAMETER("jointExternalDiameter"),
	    JOINT_LENGTH("jointLength"),
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
	private String jointName;
	private double jointExternalDiameter;
	private double jointLength;
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
	public String getJointName() {
		return jointName;
	}
	public void setJointName(String jointName) {
		this.jointName = jointName;
	}
	public double getJointExternalDiameter() {
		return jointExternalDiameter;
	}
	public void setJointExternalDiameter(double jointExternalDiameter) {
		this.jointExternalDiameter = jointExternalDiameter;
	}
	public double getJointLength() {
		return jointLength;
	}
	public void setJointLength(double jointLength) {
		this.jointLength = jointLength;
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
	
	public List<JointFormDTO> toJointFormRowData(List<Object> jointDTOList) {
		if(jointDTOList == null || jointDTOList.size() <1){
			return null;
		}
		List<JointFormDTO> results = new ArrayList<JointFormDTO>();
		Object[] lists = (Object[]) jointDTOList.toArray();
		JointFormDTO jointFormDTO = null;
		for(Object result : lists){
			JSONArray jsonArr = JSONArray.fromObject("["+result+"]");
			jointFormDTO = new JointFormDTO();
			Integer id = (Integer) jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.ID.getValue());
			jointFormDTO.setId(id);
			String jointName = jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_NAME.getValue()) == null ? "" : jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_NAME.getValue()).toString();
			jointFormDTO.setJointName(jointName);
			double jointExternalDiameter = Double.valueOf(jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_EXTERNAL_DIAMETER.getValue()) == null ? "0" : jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_EXTERNAL_DIAMETER.getValue()).toString());
			jointFormDTO.setJointExternalDiameter(jointExternalDiameter);
			double jointLength =  Double.valueOf(jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_LENGTH.getValue()) == null ? "0" : jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.JOINT_LENGTH.getValue()).toString());
			jointFormDTO.setJointLength(jointLength);
			int totalQuantity = (int)jsonArr.getJSONObject(0).get(JointFormDTO.ATTRIBUTE.TOTAL_QUANTITY.getValue());
			jointFormDTO.setTotalQuantity(totalQuantity);
			results.add(jointFormDTO);
		}
		return results;
	}
}
