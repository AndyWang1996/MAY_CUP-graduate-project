package Constructer;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Vector_module {
	Map<String, Vector<Double>> map = new HashMap<>();
	String key;
	String pic;
	String url;
	String pri;
	
	public Vector_module(String KEY, Vector<Double> vector, String pic, String url, String price){
		this.map.put(KEY, vector);
		this.key = KEY;
		this.pic = pic;
		this.url = url;
		this.pri = price;
	}
	
	public Vector<Double> getItem(){
		return this.map.get(key);
	}
	
	public String getKey(){
		return key;
	}
	
	public String get_pic(){
		return pic;
	}
	
	public String get_url(){
		return url;
	}

	public String get_pri(){ return pri;}

}
