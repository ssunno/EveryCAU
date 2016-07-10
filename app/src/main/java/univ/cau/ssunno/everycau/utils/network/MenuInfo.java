package univ.cau.ssunno.everycau.utils.network;

import java.util.ArrayList;

public class MenuInfo {
    private String type;
    private String price;
    private ArrayList<String> dishs;
    public MenuInfo(String type, String price, ArrayList<String> dishs) {
        this.type = type;
        this.price = price;
        this.dishs = dishs;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public ArrayList<String> getDishs() {
        return dishs;
    }
}
