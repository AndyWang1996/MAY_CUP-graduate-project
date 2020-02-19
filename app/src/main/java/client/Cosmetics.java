package client;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Cosmetics implements Comparable<Cosmetics>{
	public static final String ID = "id";
	public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String IMAGELINK = "imagelink";
    public static final String CATEGORY = "category";
    public static final String SHADES = "shades";
    
    public static final String MINPRICE = "minprice";
    public static final String MAXPRICE = "maxsprice";
    
    public static final String TABLE_NAME = "cosmetics_info";
    public static final String selectSql = "select * from " + TABLE_NAME + ";";
    public static final String createTableSql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
			+ ID + " bigint NOT NULL auto_increment, "
			+ NAME + " TEXT NOT NULL, "
			+ PRICE + " int NOT NULL, "
			+ IMAGELINK + " TEXT NOT NULL, "
			+ CATEGORY + " TEXT NOT NULL, "
			+ "PRIMARY KEY (`" + ID + "`))"
			+ "ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;";

    public long _ID = 0;
    public String _name = "";
    public int _price = 0;
    public String _imagelink = "";
    public String _category = "";
    public String _shades = "";
    public int _minPrice = 0;
    public int _maxPrice = 0;
    
    public void parseJson(JSONObject jsonObj) {
    	try {
    		if(jsonObj.has(ID)) {
    			_ID = jsonObj.getLong(ID);
    		}
    		if(jsonObj.has(NAME)) {
    			_name = jsonObj.getString(NAME);
    		}
    		if(jsonObj.has(PRICE)) {
    			String priceStr = jsonObj.getString(PRICE);
    			_price = Integer.valueOf(priceStr.substring(1));
    		}
    		if(jsonObj.has(IMAGELINK)) {
    			_imagelink = jsonObj.getString(IMAGELINK);
    		}
    		if(jsonObj.has(SHADES)) {
    			_shades = "";
    			boolean isFirst = true;
    			JSONArray tmpArray = jsonObj.getJSONArray(SHADES);
    			for(int i = 0; i < tmpArray.length(); ++i) {
    				String tmpShade = tmpArray.getString(i);
    				if(tmpShade != null) {
    					if(isFirst) {
    						_shades += tmpShade;
    						isFirst = false;
    					}else {
    						_shades += ",";
    						_shades += tmpShade;
    					}
    				}
    			}
    		}
    		if(jsonObj.has(CATEGORY)) {
    			_category = jsonObj.getString(CATEGORY);
    		}
    		if(jsonObj.has(MAXPRICE)) {
    			_maxPrice = jsonObj.getInt(MAXPRICE);
    		}
    		if(jsonObj.has(IMAGELINK)) {
    			_minPrice = jsonObj.getInt(MINPRICE);
    		}
    	}catch(JSONException e) {
    		e.printStackTrace();
    	}
    }

    public JSONObject getJsonObj(){
        try{
            JSONObject obj = new JSONObject();
            obj.put(ID, _ID);
            if(!_name.isEmpty()) {
            	obj.put(NAME, _name);
            }
            if(_price != 0) {
            	obj.put(PRICE, _price);
            }
            obj.put(IMAGELINK, _imagelink);
            obj.put(CATEGORY, _category);
            obj.put(MAXPRICE, _maxPrice);
            obj.put(MINPRICE, _minPrice);
            String [] shadeList = _shades.split(",");
            JSONArray shadeArray = new JSONArray();
            int len = shadeList.length;
            for(int i = 0; i < len; ++i) {
            	shadeArray.put(shadeList[i]);
            }
            obj.put(SHADES, shadeArray);
            return obj;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public void parseResultSet(ResultSet rltSet) {
    	try {
    		_ID = rltSet.getLong(ID);
    		_name = rltSet.getString(NAME);
    		_price = rltSet.getInt(PRICE);
    		_imagelink = rltSet.getString(IMAGELINK);
    		_category = rltSet.getString(CATEGORY);
    		_shades = rltSet.getString(SHADES);
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    public String getInsertSql() {
    	String paramMeters = "(" + NAME + ", " + PRICE + ", " + IMAGELINK + ", " + SHADES + ")";
    	String valuse = "('" + _name + "', '" + _price + "', '" + _imagelink + "', '" + _shades + "')";
    	String sql = "INSERT INTO " + TABLE_NAME + paramMeters + " values " + valuse;
    	return sql;
    }
    
    public String getUpdateSql() {
    	String sql = "";//"update " + TABLE_NAME + "set " + STATUS + "=" + _status + " where " + BILL_ID + "=" + _billID + ";";
    	return sql;
    }
    
    public String getDeleteSql() {
    	String sql = "";//"delete from " + TABLE_NAME + " where " + CARD_ID + "=" + _cardID + ";";
    	return sql;
    }
    
    public String getWhereSelectSql() {
    	String selectWhereSql = "select * from " + TABLE_NAME + "";
    	boolean hasWhere = false;
    	if(!_name.isEmpty()) {
	    	selectWhereSql += (" where " + NAME + " like %" + _name + "%");
	    	hasWhere = true;
    	}
    	if(!_shades.isEmpty()) {
    		if(!hasWhere) {
    			hasWhere = true;
    			selectWhereSql += " where ";
    		}else {
    			selectWhereSql += " and ";
    		}
    		selectWhereSql += (SHADES + " like %" + _shades + "%");
    	}
    	if(_maxPrice > _minPrice) {
    		if(!hasWhere) {
    			hasWhere = true;
    			selectWhereSql += " where ";
    		}else {
    			selectWhereSql += " and ";
    		}
    		selectWhereSql += (PRICE + ">=" + _minPrice + " and " + PRICE + "<=" + _maxPrice);
    	}
    	selectWhereSql += ";";
    	return selectWhereSql;
    }

	@Override
	public int compareTo(Cosmetics another) {
		if(this._price > another._price) {
			return 1;
		}
		return -1;
	}
}
