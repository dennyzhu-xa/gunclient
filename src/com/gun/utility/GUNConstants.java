package com.gun.utility;

public interface GUNConstants {

	public static final String URI					= "http://192.168.93.31:8080/gun-web";
	public static final String USER_ID 				= "id";
	public static final String USER_ACCOUNT 		= "userId";
	public static final String USER_NAME 			= "userName";
	public static final String USER_EMAIL			= "email";
	public static final String PASSWORD	 			= "password";
	public static final String USER_ROLE			= "roleId";
	public static final String SPLIT_DATA_DB		= ",";
	public static final String ROLE_ADMIN			= "1";
	public static final String ROLE_INSTORAGE		= "2";
	public static final String ROLE_EMPLOYEE		= "3";
	
	public static final int STATUS_SUCCESS					= 200;
	public static final int LOGIN_STATUS_USER_PASSWORD_ERROR		= 601;
	public static final int LOGIN_STATUS_USER_NOT_EXIST				= 602;
	public static final int USER_IN_ALREADY_IN_USE					= 606;
	
	public static final String QUERY_EQUIPMENT_NAME			= "equipName";
	public static final String QUERY_EQUIPMENT_MODEL		= "equipModel";
	public static final String EQUIPMENT_GUN				= "G"; // 射孔枪
	public static final String EQUIPMENT_ROCK				= "R"; // 弹架
	public static final String EQUIPMENT_ID					= "id";
	public static final String EQUIPMENT_TYPE				= "equipmentType";
	public static final String EQUIPMENT_NAME				= "equipmentName";
	public static final String EQUIPMENT_MODEL				= "model";
	public static final String EQUIPMENT_SPEC				= "spec";
	public static final String EQUIPMENT_UNIT				= "unit";
	public static final String EUIP_MODEL_NAME				= "euipModelName";
	public static final String EQUIPMENT_STOCK_UPPER_LIMIT	= "stockUpperLimit";
	public static final String EQUIPMENT_STOCK_LOWER_LIMIT	= "stockLowerLimit";
	public static final String EQUIPMENT_REMARK				= "remark";
	public static final String EQUIPMENT_EXTERNAL_DIAMETER	= "externalDiameter";
	public static final String EQUIPMENT_LENGTH				= "length";
	public static final String EQUIPMENT_SINGLE_BLIND_AREA	= "singleBlindArea";
	public static final String EQUIPMENT_SHOT_NUMBER		= "shotNumber";
	public static final String EQUIPMENT_SHOT_DENSITY		= "shotDensity";
	public static final String EQUIPMENT_SHOT_SPACE			= "shotSpace";
	public static final String EQUIPMENT_TOTAL_QUANTITY		= "totalQuantity";
	
	public static final String EQUIPMENT_LOG_EQP_ID			= "equipmentId";
	public static final String EQUIPMENT_LOG_STORAGE_TYPE	= "logType";
	public static final String EQUIPMENT_LOG_OLD_QUANLITY	= "oldQuanlity";
	public static final String EQUIPMENT_LOG_QUANLITY		= "updateQuantity";
	public static final String EQUIPMENT_LOG_PURPOSE		= "purpose";
	public static final String EQUIPMENT_LOG_REMARK			= "logRemark";
	
	public static final String EQUIPMENT_LOG_STORAGE_TPIN	= "I";
	public static final String EQUIPMENT_LOG_STORAGE_TPOUT	= "O";
	
	public static final String LEVEL_TYPE_OIL				= "O";
	public static final String LEVEL_TYPE_SPACER			= "S";
	
	public static final String ARR_GUN_SAFETY				= "AS";
	public static final String ARR_GUN_SPACER				= "GS";
	public static final String ARR_GUN_UP_EMPTY				= "UE";
	public static final String ARR_GUN_DOWN_EMPTY			= "DE";
	public static final String ARR_GUN_FULL					= "AF";
	public static final String ARR_GUN_HALF_FULL			= "HF";
	public static final String ARR_GUN_CONNECTOR			= "CN";
	
}
