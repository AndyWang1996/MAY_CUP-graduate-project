package Constructer;

public class raw_item {

    private String name;
    private String url;
    private String link;
    private String price;

    public raw_item(String n, String u, String l, String p){
        this.name = n;
        this.url = u;
        this.link = l;
        this.price = p;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }

    public String getLink(){
        return link;
    }

    public String getPrice(){
        return price;
    }

}
