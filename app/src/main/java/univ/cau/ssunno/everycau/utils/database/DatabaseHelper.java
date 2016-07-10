package univ.cau.ssunno.everycau.utils.database;

import android.content.Context;
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

    public void insertCafeteria(int type, int cafeteriaCode, String serviceTime){
        db.execSQL("INSERT INTO CAFETERIA_LIST " +
                "VALUES (" + Integer.toString(type) + ", " + Integer.toString(cafeteriaCode) + ", '" +
                serviceTime+"');");
    }

    public void insertMenu(int cafeteriaId, String style, String price){

    }

    public void insertDish(int menuId, String dish){

    }
}
