package Algorithm;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import Constructer.Product_Constructer;
import Constructer.Queue_module;
import Constructer.Vector_module;
import Constructer.raw_item;

public class VectorBuilder {
	
	private static Map<String, Vector_module> products = new HashMap<>();
	private static LinkedList<Queue_module> final_list = new LinkedList<>();
	private static List<raw_item> ranked_result = new ArrayList<>();

	
	//维度1
	private static final Map<String, double[]> tag_state;
	static{
		tag_state = new HashMap<>();
		
		double[] value_default = {0,0};
		double[] value_0 = {32,1};
		double[] value_1 = {16,1};
		double[] value_2 = {8,1};
		double[] value_3 = {4,1};
		double[] value_4 = {2,1};
		double[] value_5 = {-2,1};
		double[] value_6 = {-4,1};
		double[] value_7 = {-8,1};
		double[] value_8 = {-16,1};
		double[] value_9 = {-32,1};
		
		tag_state.put("default", value_default);
		tag_state.put("水", value_0);
		tag_state.put("液", value_1);
		tag_state.put("露", value_2);
		tag_state.put("啫喱", value_3);
		tag_state.put("凝胶", value_4);
		tag_state.put("霜", value_5);
		tag_state.put("乳", value_6);
		tag_state.put("膏", value_7);
		tag_state.put("粉", value_8);
		tag_state.put("油", value_9);
		
	}

	//维度2
	private static final Map<String, double[]> tag_function1;
	static{
		tag_function1 = new HashMap<>();
		
		double[] value_default = {0,0};
		double[] value_0 = {32,1};
		double[] value_1 = {16,1};
		double[] value_2 = {8,1};
		double[] value_3 = {4,1};
		double[] value_4 = {-8,1};
		double[] value_5 = {-16,1};
		double[] value_6 = {-32,1};
		
		tag_function1.put("default", value_default);
		tag_function1.put("修", value_0);
		tag_function1.put("护肤", value_1);
		tag_function1.put("护理", value_1);
		tag_function1.put("舒缓", value_2);
		tag_function1.put("活", value_3);
		tag_function1.put("焕肤", value_4);
		tag_function1.put("亮肤", value_4);
		tag_function1.put("美白", value_5);
		tag_function1.put("祛斑", value_6);
		
	}

	//维度3
	private static final Map<String, double[]> tag_function2;
	static{
		tag_function2 = new HashMap<>();
		
		double[] value_default = {0,0};
		double[] value_0 = {32,1};
		double[] value_1 = {16,1};
		double[] value_2 = {8,1};
		double[] value_3 = {-12,1};
		double[] value_4 = {-32,1};
		
		tag_function2.put("default", value_default);
		tag_function2.put("底妆", value_0);
		tag_function2.put("化妆", value_0);
		tag_function2.put("补妆", value_0);
		tag_function2.put("遮瑕", value_1);
		tag_function2.put("隔离", value_2);
		tag_function2.put("洁面", value_3);
		tag_function2.put("卸妆", value_4);
		
	}

	//维度4
	private static final Map<String, double[]> tag_function3;
	static{
		tag_function3 = new HashMap<>();
		
		double[] value_default = {0,0};
		double[] value_0 = {16,1};
		double[] value_1 = {8,1};
		double[] value_2 = {3,1};
		double[] value_3 = {-10,1};
		double[] value_4 = {-20,1};
		
		tag_function3.put("default", value_default);
		tag_function3.put("抗皱", value_0);
		tag_function3.put("提拉", value_1);
		tag_function3.put("平衡", value_2);
		tag_function3.put("紧致", value_3);
		tag_function3.put("塑颜", value_4);
		
	}

	//维度5
	private static final Map<String, double[]> tag_function4;
	static{
		tag_function4 = new HashMap<>();
		
		double[] value_default = {0,0};
		double[] value_0 = {16,1};
		double[] value_1 = {12,1};
		double[] value_2 = {8,1};
		double[] value_3 = {-16,1};
		
		tag_function4.put("default", value_default);
		tag_function4.put("补水", value_0);
		tag_function4.put("润", value_1);
		tag_function4.put("湿", value_2);
		tag_function4.put("控油", value_3);	
		
	}

	//维度6
	private static final Map<String, double[]> tag_type1;
	static{
		tag_type1 = new HashMap<>();
		
		double[] value_default = {0,0};
		double[] value_0 = {16,1};
		double[] value_1 = {14,1};
		double[] value_2 = {8,1};
		double[] value_3 = {4,1};
		double[] value_4 = {-32,1};
		
		tag_type1.put("default", value_default);
		tag_type1.put("刷", value_0);
		tag_type1.put("笔", value_1);
		tag_type1.put("套装", value_2);
		tag_type1.put("彩妆盒", value_3);	
		tag_type1.put("腮红", value_3);	
		tag_type1.put("仪", value_4);	
		
	}

	//维度7
	private static final Map<String, double[]> tag_type2;
	static{
		tag_type2 = new HashMap<>();
		
		double[] value_default = {0,0,0,0,0};
		double[] value_1 = {10,0,0,0,1};
		double[] value_2 = {0,10,0,0,1};
		double[] value_3 = {0,0,10,0,1};
		double[] value_4 = {0,0,0,10,1};
		double[] value_5 = {0,0,0,-10,1};
		
		tag_type2.put("default", value_default);
		tag_type2.put("面膜", value_1);
		tag_type2.put("喷雾", value_2);
		tag_type2.put("香水", value_3);
		tag_type2.put("香氛", value_3);
		tag_type2.put("古龙", value_3);
		tag_type2.put("口红", value_4);
		tag_type2.put("唇", value_5);
		
	}

	//维度8
	private static final Map<String, double[]> tag_ingredients;
	static{
		tag_ingredients = new HashMap<>();
		
		double[] value_default = {0,0,0,0};
		double[] value_0 = {16,0,0,1};
		double[] value_1 = {12,4,0,1};
		double[] value_2 = {16,2,2,1};
		double[] value_3 = {8,12,2,1};
		double[] value_4 = {4,8,12,1};
		double[] value_5 = {0,16,0,1};
		double[] value_6 = {0,12,2,1};
		double[] value_7 = {0,10,4,1};
		double[] value_8 = {0,20,0,1};
		double[] value_9 = {0,0,16,1};
		double[] value_10 = {0,10,10,1};
		double[] value_11 = {6,6,6,1};
		
		tag_ingredients.put("default", value_default);
		tag_ingredients.put("蛋白", value_0);
		tag_ingredients.put("胶原", value_1);
		tag_ingredients.put("氨基酸", value_2);
		tag_ingredients.put("蜂蜜", value_3);
		tag_ingredients.put("酵素", value_4);
		tag_ingredients.put("植物", value_5);
		tag_ingredients.put("草", value_5);
		tag_ingredients.put("果酸", value_6);
		tag_ingredients.put("茶树", value_7);
		tag_ingredients.put("天然", value_8);
		tag_ingredients.put("维生素", value_9);
		tag_ingredients.put("水杨酸", value_10);
		tag_ingredients.put("精华", value_11);
	}
	
	private int ctr = 0;

	public Map<String, Vector_module> getProducts(){
		return products;
	}

	public LinkedList<Queue_module> getFinal_list(){
		return final_list;
	}

	public List<raw_item> getRanked_result(){
		Iterator<Queue_module> iterator = final_list.iterator();
		while (iterator.hasNext()){
			Vector_module temp = products.get(iterator.next().getname());
			ranked_result.add(new raw_item(temp.getKey(),temp.get_url(),temp.get_pic(),temp.get_pri()));
		}
		return ranked_result;
	}

	//主程序
	public void JsonReader(Vector<Double> user) throws Exception{

		//检测向量
		if (user == null) {user = creat_random_user();}
				
		try {
			FileReader fReader = null;
			Log.i("ALGORITHM OK","ok");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(new File("mnt/ext_sdcard/Download/data.json")), "gbk");
			BufferedReader bufferedReader = new BufferedReader(isr);
			String json_data = null;
			try {
				json_data = bufferedReader.readLine();
				StringTokenizer stringTokenizer = new StringTokenizer(json_data, "},");
				while (stringTokenizer.hasMoreTokens()){
					JSONObject jsonObject = new JSONObject(stringTokenizer.nextElement().toString()+"}");
					products.put(jsonObject.getString("name"),
								new Vector_module(
									jsonObject.getString("name"), 
									get_Product_vector(jsonObject,null),
									jsonObject.getString("imagelink"), 
									jsonObject.getString("productlink"),
									jsonObject.getString("price")));
					ctr++;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println(user);
		Result_producer(user);
	}
	
	//输出排序
	private void Result_producer(Vector<Double> user_vector){
		
		for(Entry<String, Vector_module> item : products.entrySet()){
			Queue_module q = new Queue_module(item.getKey(),COSIN(user_vector, item.getValue().getItem()));
			if (final_list.isEmpty()) {
				final_list.addFirst(q);
			}else{
				int i = 0;
				Iterator<Queue_module> it = final_list.iterator();
				while (it.hasNext()) {
					if (Math.abs(it.next().getvalue()) <= Math.abs(q.getvalue())) {
						final_list.add(i,q);
						i = 0;
						break;
					}else if (!it.hasNext()) {
						final_list.add(i,q);
						break;
					}else {
						i++;
					}
				}
			}
		}
		
		Iterator<Queue_module> iterator = final_list.iterator();
//		for(int i = 0; i < 100; i ++){
//			Queue_module module = iterator.next();
//			System.out.println(module.getname() + " cos值：" + module.getvalue());
//		}
	}
	
	//获取向量
	public Vector<Double> get_Product_vector(JSONObject jObject, String name) throws JSONException{
		String p_Name = "";
		if (jObject != null){
			p_Name = jObject.getString("name");
		}else{
			p_Name = name;
		}

//		System.out.println(p_Name);
		
		Vector<Double> p_Vector = new Vector<>();
		int observer = 0;
		
		//遍历state表
		for (String key : tag_state.keySet()){
			if (p_Name.contains(key)) {
				for (int i = 0; i < tag_state.get(key).length; i++){
					p_Vector.add(tag_state.get(key)[i]);
				}
				break;
			}
		}
		
		if (p_Vector.size() == observer) {
			for (int i = 0; i < tag_state.get("default").length; i++){
				p_Vector.add(tag_state.get("default")[i]);
			}
			observer = p_Vector.size();
		}else{
			observer = p_Vector.size();
		}
		
		
		//遍历function1表
		for (String key : tag_function1.keySet()){
			if (p_Name.contains(key)) {
				for (int i = 0; i < tag_function1.get(key).length; i++){
					p_Vector.add(tag_function1.get(key)[i]);
				}
				break;
			}
		}
		
		if (p_Vector.size() == observer) {
			for (int i = 0; i < tag_function1.get("default").length; i++){
				p_Vector.add(tag_function1.get("default")[i]);
			}
			observer = p_Vector.size();
		}else{
			observer = p_Vector.size();
		}
		
		//遍历function2表
		for (String key : tag_function2.keySet()){
			if (p_Name.contains(key)) {
				for (int i = 0; i < tag_function2.get(key).length; i++){
					p_Vector.add(tag_function2.get(key)[i]);
				}
				break;
			}
		}
				
		if (p_Vector.size() == observer) {
			for (int i = 0; i < tag_function2.get("default").length; i++){
				p_Vector.add(tag_function2.get("default")[i]);
			}
			observer = p_Vector.size();
		}else{
			observer = p_Vector.size();
		}
		
		//遍历function3表
		for (String key : tag_function3.keySet()){
			if (p_Name.contains(key)) {
				for (int i = 0; i < tag_function3.get(key).length; i++){
					p_Vector.add(tag_function3.get(key)[i]);
				}
				break;
			}
		}
						
		if (p_Vector.size() == observer) {
			for (int i = 0; i < tag_function3.get("default").length; i++){
				p_Vector.add(tag_function3.get("default")[i]);
			}
			observer = p_Vector.size();
		}else{
			observer = p_Vector.size();
		}
		
		//遍历function4表
		for (String key : tag_function4.keySet()){
			if (p_Name.contains(key)) {
				for (int i = 0; i < tag_function4.get(key).length; i++){
					p_Vector.add(tag_function4.get(key)[i]);
				}
				break;
			}
		}
		
		
		if (p_Vector.size() == observer) {
			for (int i = 0; i < tag_function4.get("default").length; i++){
				p_Vector.add(tag_function4.get("default")[i]);
			}
			observer = p_Vector.size();
		}else{
			observer = p_Vector.size();
		}
		
		//遍历type1表
		for (String key : tag_type1.keySet()){
			if (p_Name.contains(key)) {
				for (int i = 0; i < tag_type1.get(key).length; i++){
					p_Vector.add(tag_type1.get(key)[i]);
				}
				break;
			}
		}
						
		if (p_Vector.size() == observer) {
			for (int i = 0; i < tag_type1.get("default").length; i++){
				p_Vector.add(tag_type1.get("default")[i]);
			}
			observer = p_Vector.size();
		}else{
			observer = p_Vector.size();
		}
		
		//遍历type2表			
		for (String key : tag_type2.keySet()){
			if (p_Name.contains(key)) {
				for (int i = 0; i < tag_type2.get(key).length; i++){
					p_Vector.add(tag_type2.get(key)[i]);
				}
				break;
			}
		}
								
		if (p_Vector.size() == observer) {
			for (int i = 0; i < tag_type2.get("default").length; i++){
				p_Vector.add(tag_type2.get("default")[i]);
			}
			observer = p_Vector.size();
		}else{
			observer = p_Vector.size();
		}
		
		//遍历ingredients表
		for (String key : tag_ingredients.keySet()){
			if (p_Name.contains(key)) {
				for (int i = 0; i < tag_ingredients.get(key).length; i++){
					p_Vector.add(tag_ingredients.get(key)[i]);
				}
				break;
			}
		}
								
		if (p_Vector.size() == observer) {
			for (int i = 0; i < tag_ingredients.get("default").length; i++){
				p_Vector.add(tag_ingredients.get("default")[i]);
			}
			observer = p_Vector.size();
		}else{
			observer = p_Vector.size();
		}
		
//		System.out.println(p_Vector);
		return p_Vector;
	
	}
	
	//生成一个default user（用于测试）
	private Vector<Double> creat_random_user() {
		// TODO Auto-generated method stub
		Vector<Double> user_Vector = new Vector<>();
		for(int i = 0; i<21; i++){
			user_Vector.add(Math.random());
		}
		System.out.println(user_Vector.size());
		return user_Vector;
	}

	//测试主程序
	public static void main(String aString[]) throws Exception{
		
		VectorBuilder vBuilder = new VectorBuilder();
		
		vBuilder.JsonReader(null);
		
	}

	//getP
	public Map<String, Vector_module> getP(){
		return products;
	}

	//计算器
	private static double COSIN(Vector<Double> user,Vector<Double> product) throws ArithmeticException{
		double result = 0;
		if (user.size() != product.size()) {
			ArithmeticException arithmeticException = new ArithmeticException(
					"用户向量长度为：" + user.size() + 
					"商品向量长度为：" + product.size());
			System.err.println(arithmeticException);
			return 0;
		}else{
			double dot_product = 0;
			double distance_user = 0;
			double distance_product = 0;
			int i = 0;
			while(i<user.size()){
				dot_product = dot_product + user.get(i)*product.get(i);
				distance_product = distance_product + Math.pow(product.get(i), 2);
				distance_user = distance_user + Math.pow(user.get(i), 2);
				i++;
			}
			
			result = dot_product / (Math.sqrt(distance_user)*Math.sqrt(distance_product));
			
//			System.out.println("______\n");
//			System.out.println(result);
			
			return result;
		}
		
		
	}
}
