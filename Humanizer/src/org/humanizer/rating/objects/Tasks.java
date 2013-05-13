/**
 * 
 */
package org.humanizer.rating.objects;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * @author sonhv
 *
 */
public class Tasks {
	Hashtable itemList = new Hashtable();
	List data = new ArrayList();
	
	public void initTasksList(String input){
		//itemList.put(key, value);
		Gson gson = new Gson();
		data.clear();
		//gson.fromJson(json, typeOfT)
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(input).getAsJsonObject();
		JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
		for (int i = 0; i < arr.size(); i ++){
			ArrayList elem = new ArrayList();
			JsonObject obj2 = (JsonObject) arr.get(i);		
			String key = obj2.get("key").getAsString();			
			JsonObject obj3 = obj2.get("value").getAsJsonObject();
			List tmp2 = new ArrayList();
			tmp2.add(obj3.get("_id").getAsString());
			tmp2.add(obj3.get("_rev").getAsString());
			tmp2.add(obj3.get("title").getAsString());
			JsonObject obj4 = obj3.get("params").getAsJsonObject();
			tmp2.add(obj4.get("query").getAsString());
			tmp2.add(obj4.get("engine").getAsString());
			JsonArray arrRaters =  obj3.get("raters").getAsJsonArray();
			List tmp3 = new ArrayList();
			for (int j = 0; j < arrRaters.size(); j ++){				
				tmp3.add(arrRaters.get(j).getAsString());				
			}
			tmp2.add(tmp3);
			data.add(tmp2);			
		}
		
		//obj.
		//return this;		
		
	}	
	public Hashtable getItemList(){
		return this.itemList;
	}
	
	public List getData(){
		return this.data;
	}	
}
