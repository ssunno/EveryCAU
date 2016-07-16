package univ.cau.ssunno.everycau.utils.network;

import java.util.ArrayList;

public class MenuInfo {
    private String style;
    private String price;
    private ArrayList<String> dishs;
    public MenuInfo(String style, String price, ArrayList<String> dishs) {
        this.style = style;
        this.price = price;
        this.dishs = dishs;
    }

    public String getStyle() {
        return style;
    }

    public String getPrice() {
        return price;
    }

    public ArrayList<String> getDishs() {
        return dishs;
    }
}
