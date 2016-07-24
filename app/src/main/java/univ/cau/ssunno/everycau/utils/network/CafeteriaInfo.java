package univ.cau.ssunno.everycau.utils.network;

import java.util.ArrayList;

public class CafeteriaInfo {
    private int cafeteriaCode;
    private String dishStyle, calorie, price;
    private ArrayList<String> dishList;

    public CafeteriaInfo(int cafeteriaCode, String dishStyle, String calorie, String price, ArrayList<String> dishList) {
        this.cafeteriaCode = cafeteriaCode;
        this.dishStyle = dishStyle;
        this.calorie = calorie;
        this.price = price;
        this.dishList = dishList;
    }

    public String getCafeteriaName() {
        switch (cafeteriaCode){
            case 2:
                return "슬기마루 (205관 1층)";
            case 3:
                return "참마루 (205관 B1층)";
            case 6:
                return "학생식당 (303관 B1층)";
            case 7:
                return "교직원식당 (303관 B1층)";
            case 8:
                return "기숙사식당 (308관)";
            case 11:
                return "University Club (102관 11층)";
            case 12:
                return "기숙사식당 (309관)";
            default:
                return "굶기";
        }
    }

    public String getPrice() {
        return price;
    }

    public int getCafeteriaCode() {
        return cafeteriaCode;
    }

    public String getDishStyle() {
        return dishStyle;
    }

    public String getCalorie() {
        return calorie;
    }

    public ArrayList<String> getDishList() {
        return dishList;
    }
}
