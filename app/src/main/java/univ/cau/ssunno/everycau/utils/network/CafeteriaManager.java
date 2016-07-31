package univ.cau.ssunno.everycau.utils.network;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import univ.cau.ssunno.everycau.utils.database.Constant;
import univ.cau.ssunno.everycau.utils.database.DatabaseHelper;

public class CafeteriaManager {

    public CafeteriaManager() {
    }

    // 해당 날짜, 해당 타임의 식단 정보
    public ArrayList<CafeteriaInfo> getMeals(String date, int quarter){
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(null);
        ArrayList<CafeteriaInfo> meals;
        meals = dbHelper.getMenusFromDB(Constant.CAMPUS_SEOUL, getQuarterByCurrentTime(quarter), date);
        for (CafeteriaInfo ci : dbHelper.getMenusFromDB(Constant.CAMPUS_SEOUL, Constant.QUARTER_ALLDAY, date)) meals.add(ci);
        return meals;
    }

    public void updateCafeteria() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String currentTime = Constant.getCurrentDate();
                    syncCafeteria(currentTime, Constant.CAFE_DORMITORY);
                    syncCafeteria(currentTime, Constant.CAFE_SEULKI);
                    syncCafeteria(currentTime, Constant.CAFE_CHARM);
                    syncCafeteria(currentTime, Constant.CAFE_STUDENT);
                    syncCafeteria(currentTime, Constant.CAFE_EMPOLYEE);
                    syncCafeteria(currentTime, Constant.CAFE_UNIVERSITY);
                    syncCafeteria(currentTime, Constant.CAFE_NEW_DORMITORY);
                } catch ( Exception e) {e.printStackTrace();}
            }
        }).start();
    }

    private void syncCafeteria(String date, int code) throws Exception{
        URL url = new URL("http://cautis.cau.ac.kr/SMT/tis/sMwsFoo010/selectList.do");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");

        // 데이터
        String param = URLEncoder.encode("today", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
        param += "&" + URLEncoder.encode("calvalue", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
        param += "&" + URLEncoder.encode("store", "UTF-8") + "=" + URLEncoder.encode(String.format("%02d", code), "UTF-8");

        // 전송
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
        osw.write(param);
        osw.flush();
        // XML 파서 생성
        XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
        xmlPullParser.setInput(conn.getInputStream(), "UTF-8");

        int eventType = xmlPullParser.getEventType();
        String tagName = null, style = null, price = null, calorie;
        ArrayList<String> dishList = new ArrayList<>();
        int quarter = 0;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    break;
                case XmlPullParser.END_TAG:
                    tagName = "";
                    break;
                case XmlPullParser.TEXT:
                    switch (tagName){
                        case "raw":
                            if( style != null && price != null) {
                                calorie = dishList.get(0);
                                dishList.remove(0);

                                DatabaseHelper dbHelper = DatabaseHelper.getInstance(null);
                                if ( !price.equals("0 원") ){
                                    int c_id = dbHelper.insertCafeteria(getCampusByCafeteria(code), code, date, quarter);
                                    int m_id = dbHelper.insertMenu(c_id, style, price, calorie);
                                    for (String dish : dishList) dbHelper.insertDish(m_id, dish);
                                }

                                style = null;
                                price = null;
                                dishList = new ArrayList<>();
                            }
                            // new menu
                            break;
                        case "menunm":
                            String[] menunm = xmlPullParser.getText().split("\\(");
                            try{
                            switch(menunm[0]) {
                                // 앞쪽이 쿼터인 경우 -> 뒤쪽은 스타일
                                case "조식": quarter = Constant.QUARTER_BREAKFAST; style = cleanStr(menunm[1]); break;
                                case "중식": quarter = Constant.QUARTER_LUNCH; style = cleanStr(menunm[1]); break;
                                case "석식":case "학생석식": quarter = Constant.QUARTER_DINNER; style = cleanStr(menunm[1]); break;
                                // 뒤쪽이 쿼터인 경우 -> 앞족은 스타일
                                default:
                                    switch(cleanStr(menunm[1])) {
                                        case "조식": quarter = Constant.QUARTER_BREAKFAST; style = menunm[0]; break;
                                        case "중식": quarter = Constant.QUARTER_LUNCH; style = menunm[0]; break;
                                        case "석식":case "학생석식": quarter = Constant.QUARTER_DINNER; style = menunm[0]; break;
                                        default: quarter = Constant.QUARTER_ALLDAY; style = cleanStr(menunm[1]); break;
                                    }
                            }} catch (ArrayIndexOutOfBoundsException e) { // 하나만 기재되는 경우
                                switch(cleanStr(menunm[0])) {
                                    case "조식": quarter = Constant.QUARTER_BREAKFAST; style = menunm[0]; break;
                                    case "중식": quarter = Constant.QUARTER_LUNCH; style = menunm[0]; break;
                                    case "석식":case "학생석식": quarter = Constant.QUARTER_DINNER; style = menunm[0]; break;
                                    default: quarter = Constant.QUARTER_ALLDAY; style = menunm[0]; break;
                                }
                            }
                            break;
                        case "tm": // 시간
                            // serviceTime += "(" + xmlPullParser.getText() + ")";
                            break;
                        case "amt": // 가격
                            price = xmlPullParser.getText();
                            break;
                        case "menunm1": // 메뉴 리스트
                            for (String dish : xmlPullParser.getText().split("<br>"))
                                dishList.add(dish);
                            break;
                    }
                    break;
                default:
                    break;
            }
            eventType = xmlPullParser.next();
        }
    }

    private String cleanStr(String str) {
        return str.split("\\)")[0];
    }

    private int getQuarterByCurrentTime(int currentTime) {
        if ( currentTime < 600 ) // AM 10:00
            return Constant.QUARTER_BREAKFAST;
        else if ( currentTime < 900) // 15:00
            return Constant.QUARTER_LUNCH;
        else // 20:00
            return Constant.QUARTER_DINNER;
    }
    private int getCampusByCafeteria(int cafeteria) {
        int campus;
        switch(cafeteria) {
            case Constant.CAFE_SEULKI:
            case Constant.CAFE_CHARM:
            case Constant.CAFE_STUDENT:
            case Constant.CAFE_EMPOLYEE:
            case Constant.CAFE_DORMITORY:
            case Constant.CAFE_UNIVERSITY:
            case Constant.CAFE_NEW_DORMITORY:
                campus = Constant.CAMPUS_SEOUL;
                break;
            default:
                campus = Constant.CAMPUS_ANSUNG;
                break;
        }
        return campus;
    }
}

