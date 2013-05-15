/**
 * 
 */
package org.humanizer.rating.objects;

import org.humanizer.rating.objects.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author sonhv
 *   
 * Contains object definition for Rater
 */
public class User {
	public String _id;
	public String _rev;
	public String username;
	public String password;
	
	/**
	 * @author sonhv
	 *
	 * Init an Rater object from json string
	 */	
	public User init(String input){
		try{
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(input).getAsJsonObject();
			JsonArray arr = parser.parse(obj.get("rows").toString()).getAsJsonArray();
			if (arr.size() == 0){
				username = null;
				password = null;
				return null;
			}
			JsonObject obj2 = (JsonObject) arr.get(0);
			_id = obj2.get("id").getAsString();
			JsonObject obj3 = obj2.get("value").getAsJsonObject();
			_rev = obj3.get("_rev").getAsString();;
			username = obj3.get("username").getAsString();
			password = obj3.get("password").getAsString();
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return this;
		
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
}

