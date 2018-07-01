package com.programadoresperuanos.www.ejemploandroid_may2018_sqlite_basico;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AsistenteSQLite extends SQLiteOpenHelper {

    String tabla1 = "trabajadores";
    String cadenaSQL1 = "CREATE TABLE "+tabla1+"(codigo INTEGER PRIMARY KEY AUTOINCREMENT, nombres VARCHAR(255), dni VARCHAR(255), direccion VARCHAR(255))";
    String cadenaSQL2 = "DROP TABLE IF EXISTS "+tabla1;

    public AsistenteSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(cadenaSQL1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(cadenaSQL2);
        sqLiteDatabase.execSQL(cadenaSQL1);
    }
}
