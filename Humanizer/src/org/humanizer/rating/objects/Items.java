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
		try{
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
		}catch (Exception ex){
			ex.printStackTrace();
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
			JsonObject obj3 = obj2.get("value").getAsJsonObject();
			JsonObject obj4 = obj3.get("value").getAsJsonObject();
			//JsonPrimitive obj3_0 = (JsonPrimitive) obj3.get("name");
			//JsonObject obj3_1 = (JsonObject) obj3.get("position");
			Hashtable tmp = new Hashtable();
			tmp.put("item_id", obj2.get("id").getAsString());			
			tmp.put("name", obj3.get("name").getAsString());
			tmp.put("position", obj4.get("position").getAsInt());
			tmp.put("url", obj4.get("url").getAsString());
			tmp.put("title", obj4.get("title").getAsString());
			tmp.put("snippet", obj4.get("snippet").getAsString());
			itemList.put(key, tmp);		
			
		}
		
		//obj.
		//return this;		
		
	}
	
	public void initItemListForTask(String input){
		//itemList.put(key, value);
		Gson gson = new Gson();
		//gson.fromJson(json, typeOfT)
		JsonParser parser = new JsonParser();
		try{
			JsonObject obj = parser.parse(input).getAsJsonObject();
			
			JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
			for (int i = 0; i < arr.size(); i ++){
				ArrayList elem = new ArrayList();
				JsonObject obj2 = (JsonObject) arr.get(i);									
				JsonObject obj3 = obj2.get("doc").getAsJsonObject();
				JsonObject obj4 = obj3.get("value").getAsJsonObject();
				String id = obj3.get("_id").getAsString();
				//JsonPrimitive obj3_0 = (JsonPrimitive) obj3.get("name");
				//JsonObject obj3_1 = (JsonObject) obj3.get("position");
				Hashtable tmp = new Hashtable();
				tmp.put("item_id", obj3.get("_id").getAsString());			
				tmp.put("name", obj3.get("name").getAsString());
				tmp.put("position", obj4.get("position").getAsInt());
				tmp.put("url", obj4.get("url").getAsString());
				tmp.put("title", obj4.get("title").getAsString());
				tmp.put("snippet", obj4.get("snippet").getAsString());
				itemList.put(id, tmp);		
				
				List tmp2 = new ArrayList();
				tmp2.add(obj3.get("_id").getAsString());
				tmp2.add(obj3.get("_rev").getAsString());
				//tmp2.add(obj3.get("title").getAsString());
				tmp2.add(obj4.get("url").getAsString());
				tmp2.add(obj4.get("title").getAsString());
				tmp2.add(obj4.get("snippet").getAsString());
				tmp2.add(obj4.get("position").getAsString());
				data.add(tmp2);			
				
				
			}			
		}catch (Exception ex){
			ex.printStackTrace();
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
