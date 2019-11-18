package com.example.notasproyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {
    private String SCRIPT_DB = "CREATE TABLE IF NOT EXISTS Notas (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "_titulo TEXT NOT NULL," +
            "_texto TEXT NOT NULL," +
            "_recordatorio TEXT," +
            "_fecha DATE NOT NULL" +
            ");";

    public static final String[] COLUMNS_NAME_NOTA =
            {"_id", "_titulo", "_texto", "_recordatorio", "_fecha"};

    public static final String TABLE_NAME_NOTAS = "Notas";

    public BaseDeDatos(@Nullable Context context) {
        super(context, "MyDB", null, 1);
    }

    public ContentValues valores(Nota nota){
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMNS_NAME_NOTA[1], nota.getTitulo());
        contentValues.put(COLUMNS_NAME_NOTA[2], nota.getTexto());
        contentValues.put(COLUMNS_NAME_NOTA[3], nota.getRecordatorio());
        contentValues.put(COLUMNS_NAME_NOTA[4], nota.getFecha().toString());

        return contentValues;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SCRIPT_DB);
        /*sqLiteDatabase.insert(TABLE_NAME_NOTAS, null,
                valores(new Nota(0,"Prueba","Si sirvio, espero",
                        "123","2019/11/02")));*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTAS);
        onCreate(sqLiteDatabase);
    }
}
