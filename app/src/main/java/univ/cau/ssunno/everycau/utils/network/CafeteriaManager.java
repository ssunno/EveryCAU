package univ.cau.ssunno.everycau.utils.network;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CafeteriaManager {

    // 해당 날짜의 모든 식당 정보를 반환
    public ArrayList<CafeteriaInfo> getMeals(String date){ // date format : YYYYmmdd, 20160701
        return null;
    }

    // 해당 날짜, 해당 타임의 식단 정보
    public ArrayList<CafeteriaInfo> getMeals(String date, int time){  // TODO :  파라미터 값에 따른 요청 읽을 수 있도록 수정
        ArrayList<CafeteriaInfo> cafeteriaInfos = new ArrayList<>();
        // TODO : 학교 서버로부터 정보 받아서 리스트 구성
        // TODO : 클래스 -> 메서드로 이동
        RequestMealsByDate rmbd = new RequestMealsByDate();
        rmbd.start();
        try {rmbd.join();} catch (Exception e) { e. printStackTrace();}
        for (CafeteriaInfo cis : rmbd.getCafeteriaInfos())
            cafeteriaInfos.add(cis);
        return cafeteriaInfos;
    }

    // TODO : 클래스를 Manager 내부 메서드로 이동 및 수정 필요
    class RequestMealsByDate extends Thread {
        private ArrayList<CafeteriaInfo> cafeteriaInfos;

        public RequestMealsByDate() {
            this.cafeteriaInfos = new ArrayList<>();
        }

        @Override
        public void run() {
            try {
                cafeteriaInfos.add(requestMealsByCafeteria("20160708", 2));
                cafeteriaInfos.add(requestMealsByCafeteria("20160708", 3));
                cafeteriaInfos.add(requestMealsByCafeteria("20160708", 6));
                cafeteriaInfos.add(requestMealsByCafeteria("20160708", 7));
                cafeteriaInfos.add(requestMealsByCafeteria("20160708", 8));
                cafeteriaInfos.add(requestMealsByCafeteria("20160708", 11));
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }

        public ArrayList<CafeteriaInfo> getCafeteriaInfos() {
            return cafeteriaInfos;
        }

        private CafeteriaInfo requestMealsByCafeteria(String date, int cafeteriaValue) throws Exception{
            URL url = new URL("http://cautis.cau.ac.kr/SMT/tis/sMwsFoo010/selectList.do");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");

            // 데이터
            String param = URLEncoder.encode("today", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
            param += "&" + URLEncoder.encode("calvalue", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
            param += "&" + URLEncoder.encode("store", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(cafeteriaValue), "UTF-8");

            // 전송
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(param);
            osw.flush();
            // XML 파서 생성
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(conn.getInputStream(), "UTF-8");

            int eventType = xmlPullParser.getEventType();
            String tagName = null, serviceTime = "", type = null, price = null;
            ArrayList<MenuInfo> menuInfos = new ArrayList<>();
            ArrayList<String> dishList = new ArrayList<>();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        tagName = xmlPullParser.getName();
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagName){
                            case "raw":
                                if( type != null && price != null ) {
                                    menuInfos.add(new MenuInfo(type, price, dishList));
                                    while (!dishList.isEmpty()) dishList.remove(0);
                                    type = null;
                                    price = null;
                                }
                                // new menu
                                break;
                            case "menunm":
                                String[] menunm = xmlPullParser.getText().split("\\(");
                                serviceTime += menunm[0] + " ";
                                type = menunm[1];
                                break;
                            case "tm":
                                serviceTime += xmlPullParser.getText();
                                break;
                            case "amt": // 가격
                                price = xmlPullParser.getText();
                                break;
                            case "menunm1": // 메뉴 리스트
                                for (String dish : xmlPullParser.getText().split("<br>"))
                                    dishList.add(dish);
                                break;
                        }
                        // if tagName == tag than get Data
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return new CafeteriaInfo(0, cafeteriaValue, serviceTime, menuInfos);
        }
    }

}

