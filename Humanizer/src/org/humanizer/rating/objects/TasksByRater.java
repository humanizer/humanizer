/**
 * 
 */
package org.humanizer.rating.objects;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * @author sonhv
 *
 * Contains object definition for Tasks loading Rater
 */
public class TasksByRater {
	int total_row;
	int offset;
	//String m_title;
	//String m_status;
	
	String[] rows;
	
	List data = new ArrayList();
	Hashtable itemList = new Hashtable();
	
	/**
	 * @author sonhv
	 *
	 * Init Taks for Rater from json string
	 */	
	
	public TasksByRater init(String input, String check_keyword){
		data.clear();
		Gson gson = new Gson();
		//gson.fromJson(json, typeOfT)
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(input).getAsJsonObject();
		JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
		for (int i = 0; i < arr.size(); i ++){
			ArrayList elem = new ArrayList();
			JsonObject obj2 = (JsonObject) arr.get(i);
			String task = obj2.get("id").getAsString();
			//elem.add(keyword);
			JsonObject doc = parser.parse(obj2.get("doc").toString()).getAsJsonObject();
			String keyword = doc.get("name").toString();
			if (!check_keyword.equals(keyword)){
				continue;
			}			
			//elem.add(keyword);
			System.out.println(keyword);
			JsonObject search = parser.parse(doc.get("search").toString()).getAsJsonObject();
			JsonArray results = parser.parse(search.get("results").toString()).getAsJsonArray();
			for (int j = 0; j < results.size(); j ++){
				String url = results.get(j).toString();
				
				elem.add(url);
				//System.out.println(url);
			}
			data = elem;
			//data.add(elem);
		}
		
		return this;
	
	}
	
	public void setRatingResult(String input){
		data.clear();
		Gson gson = new Gson();
		//gson.fromJson(json, typeOfT)
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(input).getAsJsonObject();
		JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
		for (int i = 0; i < arr.size(); i ++){
			JsonObject obj3 = (JsonObject) arr.get(i);
			JsonObject obj2 = parser.parse(obj3.get("value").toString()).getAsJsonObject();
			JsonPrimitive task = (JsonPrimitive)obj2.get("item_id");
			JsonPrimitive relevance = (JsonPrimitive)obj2.get("relevance");
			JsonPrimitive note = (JsonPrimitive)obj2.get("note");
			if (task != null){
				//add Id
				String item_id = task.getAsString();
				if (itemList.containsKey(item_id)){
					//add
					Hashtable hs = (Hashtable) itemList.get(item_id);
					hs.put("rating", relevance);
					hs.put("note", note);
					itemList.remove(item_id);
					itemList.put(item_id, hs);
				}
			}
		}
	}
	
	/**
	 * @author sonhv
	 *
	 * Init Taks for Rater from json string
	 */	
	public TasksByRater init(String input){
		data.clear();
		Gson gson = new Gson();
		//gson.fromJson(json, typeOfT)
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(input).getAsJsonObject();
		JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
		for (int i = 0; i < arr.size(); i ++){
			ArrayList elem = new ArrayList();
			JsonObject obj2 = (JsonObject) arr.get(i);				
			String task = obj2.get("id").getAsString();
			String user = obj2.get("key").getAsString();
			elem.add(task);
			JsonObject obj3 = obj2.get("value").getAsJsonObject();		
			//JsonObject doc = parser.parse(obj2.get("doc").toString()).getAsJsonObject();			
			String title = obj3.get("title").getAsString();
			JsonObject obj6 = obj3.get("status").getAsJsonObject();
			JsonPrimitive obj7 = (JsonPrimitive) obj6.get(user);
			String status = "";
			if (obj7 != null){
				status = obj7.getAsString();
			}
			
			JsonObject obj4 = obj3.get("params").getAsJsonObject();
			String keyword = obj4.get("query").toString();
			elem.add(keyword);
			System.out.println(keyword);
			elem.add(title);
			elem.add(status);
			JsonArray obj5 = obj3.get("item_ids").getAsJsonArray();
			//JsonObject search = parser.parse(doc.get("search").toString()).getAsJsonObject();
			//JsonArray results = parser.parse(search.get("results").toString()).getAsJsonArray();
			for (int j = 0; j < obj5.size(); j ++){
				//String key = .toString();
				JsonPrimitive tmp3 = (JsonPrimitive) obj5.get(j); 
				String key = String.valueOf(tmp3.getAsString());
				System.out.println((String)key);
				if (itemList.containsKey((String)key)){
					List tmp = new ArrayList();
					Hashtable req = (Hashtable) itemList.get((String)key);
					tmp.add(req.get("item_id").toString());
					tmp.add(req.get("name").toString());
					tmp.add(req.get("position").toString());
					tmp.add(req.get("url").toString());
					tmp.add(req.get("title").toString());
					tmp.add(req.get("snippet").toString());
					if (req.containsKey("rating")){
						tmp.add(req.get("rating").toString());
					}
					if (req.containsKey("note")){
						tmp.add(req.get("note").toString());
					}					
					
					elem.add(tmp);
					//elem.add(req.get("url").toString());
					System.out.println(req.get("url").toString());
				}
				//item_ids.add(url);
				//elem.add(url);
				//System.out.println(url);
			}
			data.add(elem);
		}
		
		//obj.
		return this;
	}
	
	public List getData(){
		return data;
	}
	
	public void setItemList(Hashtable _itemList){
		this.itemList = _itemList;
		
	}
	
	
}


