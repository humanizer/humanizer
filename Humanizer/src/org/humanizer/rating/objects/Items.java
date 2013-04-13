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
public class Items {
	Hashtable itemList = new Hashtable();
	List data = new ArrayList();
	public void initItemData(String input){
		
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
			//tmp2.add(obj3.get("title").getAsString());
			JsonObject obj4 = obj3.get("value").getAsJsonObject();
			tmp2.add(obj4.get("url").getAsString());
			tmp2.add(obj4.get("title").getAsString());
			tmp2.add(obj4.get("snippet").getAsString());
			tmp2.add(obj4.get("position").getAsString());
			data.add(tmp2);			
		}
		
	}
	public void initItemList(String input){
		//itemList.put(key, value);
		Gson gson = new Gson();
		//gson.fromJson(json, typeOfT)
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(input).getAsJsonObject();
		JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
		for (int i = 0; i < arr.size(); i ++){
			ArrayList elem = new ArrayList();
			JsonObject obj2 = (JsonObject) arr.get(i);		
			String key = obj2.get("key").getAsString();			
			JsonArray obj3 = obj2.get("value").getAsJsonArray();
			JsonPrimitive obj3_0 = (JsonPrimitive) obj3.get(0);
			JsonObject obj3_1 = (JsonObject) obj3.get(1);
			Hashtable tmp = new Hashtable();
			tmp.put("item_id", obj2.get("id").getAsString());			
			tmp.put("name", obj3_0.getAsString());
			tmp.put("position", obj3_1.get("position").getAsInt());
			tmp.put("url", obj3_1.get("url").getAsString());
			tmp.put("title", obj3_1.get("title").getAsString());
			tmp.put("snippet", obj3_1.get("snippet").getAsString());
			itemList.put(key, tmp);		
			
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
