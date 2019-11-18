package com.example.notasproyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DAONota {
    SQLiteDatabase sqLiteDatabase;
    Context contexto;


    public DAONota(Context contexto) {
        this.contexto = contexto;
        sqLiteDatabase = new BaseDeDatos(contexto).getWritableDatabase();
    }

    public long insert(Nota nota) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BaseDeDatos.COLUMNS_NAME_NOTA[1], nota.getTitulo());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_NOTA[2], nota.getTexto());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_NOTA[3], nota.getRecordatorio());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_NOTA[4], nota.getFecha().toString());

        return sqLiteDatabase.insert(BaseDeDatos.TABLE_NAME_NOTAS, null, contentValues);
    }

    public boolean delete (int id){
        return sqLiteDatabase.delete(BaseDeDatos.TABLE_NAME_NOTAS,
                "_id=?",new String[]{Integer.toString(id)}) > 0;
    }

    public boolean update(int id, ContentValues contentValues){
        return sqLiteDatabase.update(BaseDeDatos.TABLE_NAME_NOTAS, contentValues,
                "_id="+id,null) > 0;
    }

    public Nota notaPorID(int id){
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,
                null,"_id="+id,null,null,null, null);

        Nota nota = null;

        if (cursor.moveToFirst()) {
            do {
                try {
                    nota =
                            new Nota(cursor.getInt(0), cursor.getString(1),
                                    cursor.getString(2), cursor.getString(3),new SimpleDateFormat("yyyy/MM/dd").parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return nota;
    }

    public Cursor getAllCursor() {
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,
                BaseDeDatos.COLUMNS_NAME_NOTA,
                null,
                null,
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Nota notaPorTitulo(String titulo){
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,
                null,"_titulo=?",new String[]{titulo},null,null, null);
        Nota nota = null;

        if (cursor.moveToFirst()) {
            do {
                try {
                    nota =
                            new Nota(cursor.getInt(0), cursor.getString(1),
                                    cursor.getString(2), cursor.getString(3), new SimpleDateFormat("yyyy/MM/dd").parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return nota;
    }


    public Nota llenar (){
        Cursor query =sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,BaseDeDatos.COLUMNS_NAME_NOTA, null,null, null, null,null);
        Nota nice=null;
        while (query.moveToNext()){
            nice = new Nota(query.getInt(0),query.getString(1),query.getString(2),query.getString(3),
                    null);
        }
        return nice;
    }

    public String buscar(String criterio){
        String valores="";
        Cursor query =sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,BaseDeDatos.COLUMNS_NAME_NOTA, "_id=?",new String[]{criterio+""}, null, null,null);
        while(query.moveToNext()){
            valores = "ID: "+query.getInt(0)+"\n Titulo: "+query.getString(1)+
                    "\n Texto: "+query.getString(2)+"\n Recordatorio: "+query.getString(3)+
                    "\n Fecha: "+query.getString(4);
        }
        return valores;
    }
}
