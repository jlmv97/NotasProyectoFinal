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
            "_fecha TEXT NOT NULL" +
            ");";
    private String SCRIPT_ARCHIVOS_NOTAS = "CREATE TABLE IF NOT EXISTS ArchivosN ("+
            "_idArchivo INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "_tipo INTEGER NOT NULL,"+
            "_descripcion TEXT NOT NULL,"+
            "_ruta TEXT NOT NULL,"+
            "_idNota INTEGER NOT NULL,"+
            "FOREIGN KEY (_idNota) REFERENCES Notas(_id)"+
            ");";

    private String SCRIPT_TAREAS = "CREATE TABLE Tareas ("+
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "_titulo TEXT NOT NULL," +
            "_descripcion TEXT NOT NULL," +
            "_fecha TEXT ,"+
            "_hora TEXT);";

    private String SCRIPT_ARCHIVOS_TAREAS = "CREATE TABLE IF NOT EXISTS ArchivosT ("+
            "_idArchivo INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "_tipo INTEGER NOT NULL,"+
            "_descripcion TEXT NOT NULL,"+
            "_ruta TEXT NOT NULL,"+
            "_idTarea INTEGER NOT NULL,"+
            "FOREIGN KEY (_idTarea) REFERENCES Tareas(_id)"+
            ");";

    private String SCRIPT_RECORDATORIOS = "CREATE TABLE IF NOT EXISTS Recordatorios("+
            "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "_fecha TEXT NOT NULL,"+
            "_hora TEXT NOT NULL,"+
            "_idTarea INTEGER NOT NULL,"+
            "FOREIGN KEY (_idTarea) REFERENCES Tareas(_id));";

    public static final String[] COLUMNS_NAME_NOTA =
            {"_id", "_titulo", "_descripcion", "_fecha","_hora"};

    public static final String TABLE_NAME_NOTAS = "Notas";

    public static final String[] COLUMNS_NAME_TAREA =
            {"_id", "_titulo", "_texto", "_fecha"};

    public static final String TABLE_NAME_TAREA = "Tareas";

    public static final String TABLE_NAME_ARCHIVOS = "ArchivosN";

    public static final String[] COLUMNS_NAME_ARCHIVOS = {
        "_idArchivo","_descripcion","_tipo","_ruta","_idNota"};

    public static final String TABLE_NAME_ARCHIVOST = "ArchivosT";

    public static final String[] COLUMNS_NAME_ARCHIVOST = {
            "_idArchivo","_descripcion","_tipo","_ruta","_idTarea"};

    public static final String[] COLUMNS_NAME_RECORDATORIO =
            {"_id", "_fecha", "_hora", "_idTarea"};

    public static final String TABLE_NAME_RECORDATORIO = "Recordatorios";

    public BaseDeDatos(@Nullable Context context) {
        super(context, "MyDB", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SCRIPT_DB);
        sqLiteDatabase.execSQL(SCRIPT_TAREAS);
        sqLiteDatabase.execSQL(SCRIPT_ARCHIVOS_NOTAS);
        sqLiteDatabase.execSQL(SCRIPT_ARCHIVOS_TAREAS);
        sqLiteDatabase.execSQL(SCRIPT_RECORDATORIOS);

        /*sqLiteDatabase.insert(TABLE_NAME_NOTAS, null,
                valores(new Nota(0,"Prueba","Si sirvio, espero",
                        "123","2019/11/02")));*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTAS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TAREA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ARCHIVOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ARCHIVOST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RECORDATORIO);
        onCreate(sqLiteDatabase);
    }
}
