package univ.cau.ssunno.everycau.utils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public DatabaseHelper(Context context) {
        super(context, "everyCAU.db", null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CAFETERIA_LIST( cl_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type INTEGER, code INTEGER, service_time TEXT);");
        db.execSQL("CREATE TABLE MENU_LIST( menu_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cl_id INTEGER, style TEXT, price TEXT, FOREIGN KEY (cl_id) REFERENCES CAFETERIA_LIST (cl_id));");
        db.execSQL("CREATE TABLE DISH_LIST( dish_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "menu_id INTEGER, dish TEXT, FOREIGN KEY (menu_id) REFERENCES MENU_LIST (menu_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insertCafeteria(int type, int cafeteriaCode, String serviceTime){
        // 중복된 cafeteria 정보가 없는 경우 insert
        db.execSQL("INSERT INTO CAFETERIA_LIST(type, code, service_time) " +
                "VALUES (" + Integer.toString(type) + ", " + Integer.toString(cafeteriaCode) + ", '" + serviceTime+"') " +
                "WHERE NOT EXISTS (SELECT * FROM CAFETERIA_LIST WHERE type=" + Integer.toString(type) +
                ", code=" + Integer.toString(cafeteriaCode) + ", service_time='" + serviceTime + "');");
        Cursor cursor = db.rawQuery("SELECT cl_id FROM CAFETERIA_LIST WHERE type=" + Integer.toString(type) +
                ", code=" + Integer.toString(cafeteriaCode) + ", service_time='" + serviceTime + "');", null);
        // 삽입한 Cafeteria의 id 값 리턴
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    public void getMealsList(String date, String serviceTime) {
        // TODO : 날짜, 식사 시간에서 값을 찾아서 list로 리턴
    }

    public int insertMenu(int cafeteriaId, String style, String price){
        // Cafeteria에 속하는 메뉴 (한식, 중식, 양식 등)
        db.execSQL("INSERT INTO MENU_LIST(cl_id, style, price) " +
                "VALUES (" + Integer.toString(cafeteriaId) + ", '" + style + "', '" + price + "');");
        Cursor cursor = db.rawQuery("SELECT menu_id FROM MENU_LIST WHERE cl_id=" + Integer.toString(cafeteriaId) + ", style='" + style + "', price='" + price + "';", null);
        cursor.moveToNext();
        // 삽입한 Menu의 id 값 찾아서 리턴
        return cursor.getInt(0);

    }

    public void insertDish(int menuId, String dish){
        // Menu에 속하는 식단 (밥, 김치 등)
        db.execSQL("INSERT INTO DISH_LIST(menu_id, dish) " +
                "VALUES (" + Integer.toString(menuId) + ", '" + dish + "');");
    }
}
