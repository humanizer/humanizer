/**
 * 
 */
package org.humanizer.rating.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

import org.humanizer.rating.objects.Rater;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author sonhv
 *   
 * Contains object definition for Rater
 */
public class Rater {
	public String _id;
	public String _rev;
	public String email;
	public String password;
	public boolean verified;
	
	//List data = new ArrayList();
	
	/**
	 * @author sonhv
	 *
	 * Init an Rater object from json string
	 */	
	public Rater init(String input){
		try{
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(input).getAsJsonObject();
			JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
			JsonObject obj2 = (JsonObject) arr.get(0);
			_id = obj2.get("id").getAsString();
			_rev = "";
			email = obj2.get("key").getAsString();
			password = obj2.get("value").getAsString();
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return this;
		
	}
	
	public List initData(String input, List currentData){
		List data = new ArrayList();
		Hashtable hsCurrent = new Hashtable();
		for (int i = 0; i < currentData.size(); i++){
			String email = (String) currentData.get(i);
			hsCurrent.put(email, "1");
		}
		try{
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(input).getAsJsonObject();
			JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
			for (int i = 0; i < arr.size(); i ++){
				JsonObject obj2 = (JsonObject) arr.get(i);
				obj2 = (JsonObject) obj2.get("value");
				_id = obj2.get("_id").getAsString();
				_rev = obj2.get("_rev").getAsString();
				email = obj2.get("email").getAsString();
				password = obj2.get("password").getAsString();
				ArrayList tmp = new ArrayList();
				tmp.add(_id);
				tmp.add(_rev);
				tmp.add(email);
				tmp.add(password);
				if (!hsCurrent.containsKey(email)){
					tmp.add("1");
				}else{
					tmp.add("0");
				}
				data.add(tmp);
			}

			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return data;
		
	}	
	
	public String getEmail(){
		return email;
	}
	
	public String getPassword(){
		return password;
	}
	
	
	
}

