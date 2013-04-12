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
public class Project {
	Hashtable itemList = new Hashtable();
	List data = new ArrayList();
	
	public void initItemList(String input){
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
			Hashtable tmp = new Hashtable();
			tmp.put("_id", obj3.get("_id").getAsString());
			tmp.put("_rev", obj3.get("_rev").getAsString());
			tmp.put("name", obj3.get("name").getAsString());
			tmp.put("rater_per_task", obj3.get("rater_per_task").getAsString());					
			itemList.put(key, tmp);
			List tmp2 = new ArrayList();
			tmp2.add(obj3.get("_id").getAsString());
			tmp2.add(obj3.get("_rev").getAsString());
			tmp2.add(obj3.get("name").getAsString());
			tmp2.add(obj3.get("rater_per_task").getAsString());
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
