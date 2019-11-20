package com.gun.utility;

import net.sf.json.JSONArray;

public class JsonResponse {

    private int code;

    private String message;

    private Object result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean success() {
        return this.code == 200;
    }

    public boolean authorizationError() {
        return this.code == 401;
    }

    public JsonResponse(String result){
    	if(result == null || result.length()<2){
    		this.code = 0;
    		this.message = "No data return!";
    		return;
    	}
    	JSONArray jsonArr = JSONArray.fromObject("["+result+"]");
    	
    	this.code = (int) jsonArr.getJSONObject(0).get("code");
    	this.message =  (String) jsonArr.getJSONObject(0).get("message");
    	this.result = jsonArr.getJSONObject(0).get("result");
//    	result = result.substring(1, result.length() - 1);
//    	String values[] = result.split(",");
//    	for(String data : values){
//    		String[] datas = data.split(":");
//    		if(datas[0].equals("\"code\"")){
//    			this.code = Integer.valueOf(datas[1]);
//    		}else if(datas[0].equals("\"message\"")){
//    			this.message = datas[1];
//    		}else {
//    			this.result = datas[1];
//    		}
//    	}
    }
    
    
}
