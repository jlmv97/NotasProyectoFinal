package com.example.notasproyectofinal.DAOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.notasproyectofinal.Adaptador.Adjuntos;
import com.example.notasproyectofinal.Archivos;
import com.example.notasproyectofinal.BaseDeDatos;

import java.util.ArrayList;

public class DAORecursos {
    SQLiteDatabase sqLiteDatabase;
    Context context;

    public DAORecursos(Context context) {
        this.context = context;
        sqLiteDatabase = new BaseDeDatos(context).getWritableDatabase();
    }

    public long insert(Archivos archivos){
        ContentValues contentValues = new ContentValues();
        contentValues.put(BaseDeDatos.COLUMNS_NAME_ARCHIVOS[1],archivos.getTipo());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_ARCHIVOS[2],archivos.getDescripcion());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_ARCHIVOS[3],archivos.getRuta().toString());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_ARCHIVOS[4],archivos.getIdNotas());

        return sqLiteDatabase.insert(BaseDeDatos.TABLE_NAME_ARCHIVOS,null,contentValues);
    }

    public boolean delete (int id){
        return sqLiteDatabase.delete(BaseDeDatos.TABLE_NAME_ARCHIVOS,"_idNota=?",new String[]{Integer.toString(id)})>0;
    }

    public boolean update(int id, ContentValues contentValues){
        return sqLiteDatabase.update(BaseDeDatos.TABLE_NAME_ARCHIVOS, contentValues,
                "_idArchivo="+id,null) > 0;
    }

    public ArrayList<Archivos> buscarObjeto(String[] id){
        ArrayList<Archivos> rutas = new ArrayList<>();

        ////////////////
        String[] columnasAConsultar = {BaseDeDatos.COLUMNS_NAME_ARCHIVOS[0],BaseDeDatos.COLUMNS_NAME_ARCHIVOS[1],BaseDeDatos.COLUMNS_NAME_ARCHIVOS[2],BaseDeDatos.COLUMNS_NAME_ARCHIVOS[3],BaseDeDatos.COLUMNS_NAME_ARCHIVOS[4]};
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_ARCHIVOS, columnasAConsultar, "_idNota = ?", id, null, null, null);

        if(id[0].equals("")){

            cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_ARCHIVOS, columnasAConsultar, null, null, null, null, null);
        }

        if (cursor == null){
            return rutas;
        }

        if (!cursor.moveToFirst()) return rutas;

        do {

            int idObtenidoDeBD = cursor.getInt(0);
            Uri pathObtenidoDeBD = Uri.parse(cursor.getString(1));
            int tipoObtenidoDeBD = cursor.getInt(2);
            String descripcionObtenidoDeBD = cursor.getString(3);
            int idTareaObtenidoDeBD = cursor.getInt(4);

            Archivos rutaObtenidoDeBD = new Archivos(idObtenidoDeBD, pathObtenidoDeBD, tipoObtenidoDeBD, descripcionObtenidoDeBD, idTareaObtenidoDeBD);
            rutas.add(rutaObtenidoDeBD);

        } while (cursor.moveToNext());

        cursor.close();
        return rutas;
    }


}
