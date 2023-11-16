package sdf;

import java.util.HashMap;
import java.util.Map;

public class Item {
    
    private final String productID;
    private String productTitle;  
    private double productRating;
    private double productPrice;
    private Map<String, Item> items = new HashMap<>();

    public Map<String, Item> getItems() {
        return items;
    }

    public Item(String id){
        this.productID = id;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Double getProductRating() {
        return productRating;
    }

    public void setProductRating(Double productRating) {
        this.productRating = productRating;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
}
