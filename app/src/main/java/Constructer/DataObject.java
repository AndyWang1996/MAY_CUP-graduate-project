package Constructer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 这个类的作用是把不能放进Bundle里的List<Product_Constructer>转换成一个map
 * 用Bundle的setSerializable 方法把数据通过handler传递给主线程
 */

public class DataObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String,List<raw_item>> map;

    public void setMap(Map<String, List<raw_item>> map) {
        this.map = map;
    }

    public Map<String, List<raw_item>> getMap() {
        return map;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}
