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
        db.execSQL("CREATE TABLE CAFETERIA_LIST( cl_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type INTEGER, code INTEGER, date TEXT, quarter INTEGER, service_time TEXT);");
        db.execSQL("CREATE TABLE MENU_LIST( menu_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cl_id INTEGER, style TEXT, price TEXT, FOREIGN KEY (cl_id) REFERENCES CAFETERIA_LIST (cl_id));");
        db.execSQL("CREATE TABLE DISH_LIST( dish_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "menu_id INTEGER, dish TEXT, FOREIGN KEY (menu_id) REFERENCES MENU_LIST (menu_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insertCafeteria(int type, int cafeteriaCode, String date, int quarter, String serviceTime){
        // 중복된 cafeteria 정보가 없는 경우 insert
        db.execSQL("INSERT INTO CAFETERIA_LIST(type, code, date, quarter, service_time) " +
                "SELECT " + Integer.toString(type) + ", " + Integer.toString(cafeteriaCode) + ", '" + date + "', " + quarter +", '" + serviceTime + "' " +
                "WHERE NOT EXISTS (SELECT * FROM CAFETERIA_LIST WHERE (type=" + Integer.toString(type) +
                " AND code=" + Integer.toString(cafeteriaCode) + " AND date='" + date + "' AND quarter=" + quarter + "));");
        // 삽입한 Cafeteria의 id 값 리턴
        Cursor cursor = db.rawQuery("SELECT cl_id FROM CAFETERIA_LIST WHERE type=" + Integer.toString(type) +
                " AND code=" + Integer.toString(cafeteriaCode) + " AND quarter=" + quarter + ";", null);
        cursor.moveToNext();
        int cafeteriaId = cursor.getInt(0);
        cursor.close();
        return cafeteriaId;
    }

    public ArrayList<CafeteriaInfo> getMealsFromDB(int campusType, String date, int quarter) {
        // TODO : 날짜, 식사 시간에서 값을 찾아서 list로 리턴
        Cursor ciCursor = db.rawQuery("SELECT cl_id, code, service_time FROM CAFETERIA_LIST WHERE type=" + campusType + " AND date='" + date + "' AND quarter=" + quarter + ";", null);
        ArrayList<CafeteriaInfo> cafeteriaInfos = new ArrayList<>();
        ArrayList<MenuInfo> menuInfos = new ArrayList<>();
        int ciCode = -1;
        while (ciCursor.moveToNext()) {
            Cursor menuCursor = db.rawQuery("SELECT menu_id, style, price FROM MENU_LIST WHERE cl_id=" + ciCursor.getInt(0) + ";",null);
            while (menuCursor.moveToNext()) {
                Cursor dishCursor = db.rawQuery("SELECT dish FROM DISH_LIST WHERE menu_id=" + menuCursor.getInt(0) + ";", null);
                ArrayList<String> dishList = new ArrayList<>();
                while (dishCursor.moveToNext())
                    dishList.add(dishCursor.getString(0));
                dishCursor.close();
                menuInfos.add(new MenuInfo(menuCursor.getString(1), menuCursor.getString(2), dishList));
            }
            menuCursor.close();
            if (ciCode != ciCursor.getInt(1)) {
                cafeteriaInfos.add(new CafeteriaInfo(campusType, ciCursor.getInt(1), quarter, ciCursor.getString(2), menuInfos));
                menuInfos = new ArrayList<>();
            }
        }
        ciCursor.close();
        return cafeteriaInfos;
    }

    public int insertMenu(int cafeteriaId, String style, String price){
        // Cafeteria에 속하는 메뉴 (한식, 중식, 양식 등)
        db.execSQL("INSERT INTO MENU_LIST(cl_id, style, price) " +
                "SELECT " + Integer.toString(cafeteriaId) + ", '" + style + "', '" + price + "' " +
                "WHERE NOT EXISTS (SELECT * FROM MENU_LIST WHERE (cl_id=" + Integer.toString(cafeteriaId) + " AND style='" + style + "' AND price='" + price + "'));");
        // 삽입한 Menu의 id 값 찾아서 리턴
        Cursor cursor = db.rawQuery("SELECT menu_id FROM MENU_LIST WHERE cl_id=" + Integer.toString(cafeteriaId) + " AND style='" + style + "' AND price='" + price + "';", null);
        cursor.moveToNext();
        int menuId = cursor.getInt(0);
        cursor.close();
        return menuId;
    }

    public void insertDish(int menuId, String dish){
        // Menu에 속하는 식단 (밥, 김치 등)
        db.execSQL("INSERT INTO DISH_LIST(menu_id, dish) " +
                "SELECT " + Integer.toString(menuId) + ", '" + dish + "' " +
                "WHERE NOT EXISTS (SELECT * FROM DISH_LIST WHERE (menu_id=" + Integer.toString(menuId) + " AND dish='" + dish + "'));");
    }
}
