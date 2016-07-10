package univ.cau.ssunno.everycau.utils.network;

import java.util.ArrayList;

public class CafeteriaInfo {
    public static final int SEOUL = 0;
    public static final int ANSUNG = 1;
    private int campusType; // 0 : 서울, 1 : 안성
    private int cafeteriaValue;
    private String serviceTime;
    private ArrayList<MenuInfo> menus;

    public CafeteriaInfo(int campusType, int cafeteriaValue, String serviceTime, ArrayList<MenuInfo> menus) {
        this.campusType = campusType;
        this.cafeteriaValue = cafeteriaValue;
        this.serviceTime = serviceTime;
        this.menus = menus;
    }

    public String getName() {
        switch (cafeteriaValue){
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

    public String getServiceTime() {
        return serviceTime;
    }

    public ArrayList<MenuInfo> getMenus() {
        return menus;
    }
}