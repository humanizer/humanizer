/**
 * 
 */
package org.humanizer.rating.objects;

/**
 * @author sonhv
 *
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author sonhv
 *
 * Rating result object
 */
public class RatingResult {
	public String time_stamp;
	public String rater;
	public String relevance;
	public String note;
	public String item_id;
	public String task_id;
	public String _id;
	public String _rev;
	
	public String getURL(Hashtable hsItem){
		String ret = "";
		try{
			if (hsItem.containsKey(item_id)){
				Hashtable hs = (Hashtable) hsItem.get(item_id);
				ret = (String) hs.get("url");
			}
		}catch (Exception ex){
			
		}
		
		return ret;
		
	}
	
	public List init(String input){
		List ret = new ArrayList();
		try{
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(input).getAsJsonObject();
			JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
			for (int i = 0; i < arr.size(); i ++){
				ArrayList elem = new ArrayList();
				JsonObject obj2 = (JsonObject) arr.get(i);
				//String task = obj2.get("id").getAsString();
				//elem.add(keyword);
				JsonObject doc = parser.parse(obj2.get("value").toString()).getAsJsonObject();
				String relevance = doc.get("relevance").getAsString();//
				String note = doc.get("note").getAsString();//
				String _rev = doc.get("_rev").getAsString();
				String _id = doc.get("_id").getAsString();
				String rater = doc.get("rater").getAsString();
				String time_stamp = doc.get("time_stamp").getAsString();
				String item_id = doc.get("item_id").getAsString();
				String task_id = doc.get("task_id").getAsString();
				elem.add(rater);
				elem.add(relevance);
				elem.add(note);				
				//Long.g
				//Date date = new Date(Long.parseLong(time_stamp)*1000);
				//elem.add(date.toString());
				elem.add(time_stamp);
				ret.add(elem);
				
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return ret;
	}
	
	public boolean init(String input, String item_id_check, String rater_check){
		boolean ret = false;
		try{
			Gson gson = new Gson();
			//gson.fromJson(json, typeOfT)
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(input).getAsJsonObject();
			JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
			boolean bIsFound = false;
			for (int i = 0; i < arr.size(); i ++){
				ArrayList elem = new ArrayList();
				JsonObject obj2 = (JsonObject) arr.get(i);
				//String task = obj2.get("id").getAsString();
				//elem.add(keyword);
				JsonObject doc = parser.parse(obj2.get("value").toString()).getAsJsonObject();
				String url = "";//doc.get("url").getAsString();
				String rater = doc.get("rater").getAsString();//
				String task = "";//doc.get("task_id").getAsString();
				String query = "";//doc.get("query").getAsString();
				String relevance = doc.get("relevance").getAsString();//
				String note = doc.get("note").getAsString();//
				String _rev = doc.get("_rev").getAsString();
				String _id = doc.get("_id").getAsString();
				String item_id = doc.get("item_id").getAsString();
				String task_id = doc.get("task_id").getAsString();
				if ( (rater.equals(rater_check)) && 
						(item_id.equals(item_id_check)) ){
					bIsFound = true;
					//set data
					this.rater = rater;
					this.relevance = relevance;
					this.note = note;
					this.item_id = item_id;
					this._rev = _rev;
					this._id = _id;				
					this.task_id = task_id;
					return true;
				}
				//data.add(elem);
			}
			
			//ret = true;
			return ret;
		}catch (Exception ex){
			
		}
		return ret;
		
	}
}


