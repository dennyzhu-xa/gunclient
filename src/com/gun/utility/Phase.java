package com.gun.utility;

import java.util.HashMap;
import java.util.Map;

public class Phase{

	public final Map PHASES = new HashMap<String,String>(){
		{
			
			put("单相位","360");
			put("两相位","180");
			put("三相位","120");
			put("四相位","90");
			put("六相位","60");
			put("八相位","45");
			put("十二相位","30");
		}
	};
	
	public KeyValue[] getPhases(){
		 KeyValue[] result = new KeyValue[7];
		 KeyValue t = null;
		 int i = 0;
		 for(Object s : PHASES.keySet()){
			String key = s.toString();
			String value = (String) PHASES.get(key);
			t = new KeyValue(key,value);
			result[i] = t;
			i++;
		}
		 return result;
	}
	
	public String[] getKeysSortByValues(){
		String[] result = new String[7];
		String[][] rs = new String[7][2];
		int i=0;
		for(Object s : PHASES.keySet()){
			String key = s.toString();
			String value = (String) PHASES.get(key);
			rs[i][0] = key;
			rs[i][1] = value;
			i++;
		}
		for(int k=0;k<rs.length;k++ ){
			for(int j=k+1;j<rs.length;j++){
				int pre = Integer.valueOf(rs[k][1]);
				int after = Integer.valueOf(rs[j][1]);
				if(pre < after){
					String[] temp = rs[k];
					rs[k] = rs[j];
					rs[j] = temp;
				}
			}
			result[k] = rs[k][0];
		}
		return result;
	}
}
