package univ.cau.ssunno.everycau.utils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import univ.cau.ssunno.everycau.utils.network.CafeteriaInfo;
import univ.cau.ssunno.everycau.utils.network.MenuInfo;

public class DatabaseHelper extends SQLiteOpenHelper {
    // TODO : 서울, 안성 등의 코드를 따로 관리해야함
    // TODO : DB에 메뉴, 식사가 중복으로 insert 되지 않도록 관리해야함
    // TODO : 같은 service_time( 조식/중식 등 ) 이지만 시간만 다른 경우가 있다.
    public static final int SEOUL = 0;
    public static final int ANSUNG = 1;
    private SQLiteDatabase db = null;
    private static volatile DatabaseHelper dbHelperInstance = null;

    public DatabaseHelper(Context context) {
        super(context, "everyCAU.db", null, 1);
        this.db = this.getWritableDatabase();
    }

    // DB is singleton
    public static DatabaseHelper getInstance(Context context){
        if(dbHelperInstance == null)
            synchronized (DatabaseHelper.class) {
                if (dbHelperInstance == null)
                    dbHelperInstance = new DatabaseHelper(context);
            }
        return dbHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // quarter : 분기 표시, 0: 종일, 1:조식, 2:중식, 3:석식
        db.execSQL("CREATE TABLE CAFETERIA( c_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "campus INTEGER, code INTEGER, date TEXT, quarter INTEGER);");
        db.execSQL("CREATE TABLE MENUS( m_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "c_id INTEGER, style TEXT, price TEXT, calorie INTEGER, FOREIGN KEY (c_id) REFERENCES CAFETERIA (c_id));");
        db.execSQL("CREATE TABLE DISH( d_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "m_id INTEGER, dish TEXT, FOREIGN KEY (m_id) REFERENCES MENUS (m_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insertCafeteria(int campus, int code, String date, int quarter){
        // 중복된 cafeteria 정보가 없는 경우 insert
        db.execSQL("INSERT INTO CAFETERIA(campus, code, date, quarter) " +
                "SELECT " + Integer.toString(campus) + ", " + Integer.toString(code) + ", '" + date + "', " + quarter +
                " WHERE NOT EXISTS (SELECT * FROM CAFETERIA WHERE (campus=" + Integer.toString(campus) +
                " AND code=" + Integer.toString(code) + " AND date='" + date + "' AND quarter=" + quarter + "));");
        // 삽입한 Cafeteria의 id 값 리턴
        Cursor cursor = db.rawQuery("SELECT c_id FROM CAFETERIA WHERE campus=" + Integer.toString(campus) +
                " AND code=" + Integer.toString(code) + " AND date='" + date + "' AND quarter=" + quarter + ";", null);
        cursor.moveToNext();
        int cafeteriaId = cursor.getInt(0);
        cursor.close();
        return cafeteriaId;
    }
    public int insertMenu(int cafeteriaId, String style, String price, String calorie){
        // Cafeteria에 속하는 메뉴 (한식, 중식, 양식 등)
        db.execSQL("INSERT INTO MENUS(c_id, style, price, calorie) " +
                "SELECT " + Integer.toString(cafeteriaId) + ", '" + style + "', '" + price + "', '" + calorie + "' " +
                "WHERE NOT EXISTS (SELECT * FROM MENUS WHERE (c_id=" + Integer.toString(cafeteriaId) +
                " AND style='" + style + "' AND price='" + price + "' AND calorie='" + calorie + "'));");
        // 삽입한 Menu의 id 값 찾아서 리턴
        Cursor cursor = db.rawQuery("SELECT m_id FROM MENUS WHERE c_id=" + Integer.toString(cafeteriaId) +
                " AND style='" + style + "' AND price='" + price + "' AND calorie='" + calorie + "';", null);
        cursor.moveToNext();
        int menuId = cursor.getInt(0);
        cursor.close();
        return menuId;
    }

    public void insertDish(int menuId, String dish){
        // Menu에 속하는 식단 (밥, 김치 등)
        db.execSQL("INSERT INTO DISH(m_id, dish) " +
                "SELECT " + Integer.toString(menuId) + ", '" + dish + "' " +
                "WHERE NOT EXISTS (SELECT * FROM DISH WHERE (m_id=" + Integer.toString(menuId) + " AND dish='" + dish + "'));");
    }

    public ArrayList<CafeteriaInfo> getMenusFromDB(int campus, int quarter, String date) {
        Cursor cafeteriaCursor = db.rawQuery("SELECT c_id, code FROM CAFETERIA WHERE campus=" + campus + " AND date='" + date + "' AND quarter=" + quarter + ";", null);
        ArrayList<CafeteriaInfo> cafeteriaInfos = new ArrayList<>();

        while (cafeteriaCursor.moveToNext()) {
            Cursor menuCursor = db.rawQuery("SELECT m_id, style, calorie, price FROM MENUS WHERE c_id=" + Integer.toString(cafeteriaCursor.getInt(0)) + ";", null);
            while (menuCursor.moveToNext()) {
                Cursor dishCursor = db.rawQuery("SELECT dish FROM DISH WHERE m_id=" + Integer.toString(menuCursor.getInt(0)) + ";", null);
                ArrayList<String> dishList = new ArrayList<>();
                while (dishCursor.moveToNext()) dishList.add(dishCursor.getString(0));
                dishCursor.close();
                cafeteriaInfos.add(new CafeteriaInfo(cafeteriaCursor.getInt(1), menuCursor.getString(1), menuCursor.getString(2), menuCursor.getString(3), dishList));
            }
            menuCursor.close();
        }
        cafeteriaCursor.close();
        return cafeteriaInfos;
    }
}
