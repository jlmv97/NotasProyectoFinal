package com.example.notasproyectofinal.DAOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.notasproyectofinal.Archivos;
import com.example.notasproyectofinal.BaseDeDatos;
import com.example.notasproyectofinal.Tarea;
import com.example.notasproyectofinal.Tareas;

import java.util.ArrayList;

public class DAOTareas {
    SQLiteDatabase _sqLiteDatabase;
    Context ctx;

    public DAOTareas(Context ctx) {
        this.ctx = ctx;
        _sqLiteDatabase =
                new BaseDeDatos(ctx).getWritableDatabase();
    }

    public long insert(Tarea tarea){
        ContentValues contentValues
                = new ContentValues();

        contentValues.put(BaseDeDatos.COLUMNS_NAME_TAREA[1],
                tarea.getTitulo());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_TAREA[2],
                tarea.getDescripcion());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_TAREA[3],
                tarea.getFecha());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_TAREA[4],
                tarea.getHora());

        return  _sqLiteDatabase.insert(BaseDeDatos.TABLE_NAME_TAREA,
                null, contentValues);
    }

    public int update (Tarea tarea){

        ContentValues valoresParaActualizar =  new ContentValues();
        valoresParaActualizar.put(BaseDeDatos.COLUMNS_NAME_TAREA[1], tarea.getTitulo());
        valoresParaActualizar.put(BaseDeDatos.COLUMNS_NAME_TAREA[2], tarea.getDescripcion());
        valoresParaActualizar.put(BaseDeDatos.COLUMNS_NAME_TAREA[3], tarea.getFecha());
        valoresParaActualizar.put(BaseDeDatos.COLUMNS_NAME_TAREA[4], tarea.getHora());
        String campoParaActualizar = "_id = ?";
        String[] argumentosParaActualizar = {String.valueOf(tarea.getId())};
        return _sqLiteDatabase.update(BaseDeDatos.TABLE_NAME_TAREA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);

    }

    public int eliminar (int id){

        String[] argumentos = {String.valueOf(id)};
        return _sqLiteDatabase.delete(BaseDeDatos.TABLE_NAME_TAREA, "_id = ?", argumentos);

    }

    public ArrayList<Tarea> buscarporTitulo(String[] titulo){
        ArrayList<Tarea> tareas = new ArrayList<>();

        String[] columnasAConsultar = {BaseDeDatos.COLUMNS_NAME_TAREA[0], BaseDeDatos.COLUMNS_NAME_TAREA[1], BaseDeDatos.COLUMNS_NAME_TAREA[2], BaseDeDatos.COLUMNS_NAME_TAREA[3], BaseDeDatos.COLUMNS_NAME_TAREA[4]};
        Cursor cursor = _sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, columnasAConsultar, "titulo = ? OR descripcion = ?", titulo, null, null, null);

        if(titulo[0].equals("")){

            cursor = _sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, columnasAConsultar, null, null, null, null, null);
        }

        if (cursor == null){
            return tareas;
        }

        if (!cursor.moveToFirst()) return tareas;

        do {

            int idObtenidoDeBD = cursor.getInt(0);
            String tituloObtenidoDeBD = cursor.getString(1);
            String descripcionObtenidoDeBD = cursor.getString(2);
            String fechaObtenidoDeBD = cursor.getString(3);
            String horaObtenidoDeBD = cursor.getString(4);

            Tarea tareaObtenidoDeBD = new Tarea(idObtenidoDeBD, tituloObtenidoDeBD, descripcionObtenidoDeBD, fechaObtenidoDeBD, horaObtenidoDeBD);
            tareas.add(tareaObtenidoDeBD);

        } while (cursor.moveToNext());

        cursor.close();
        return tareas;
    }

    public ArrayList<Integer> buscarUltimoId(String[] id){
        ArrayList<Integer> tareas = new ArrayList<>();

        String[] columnasAConsultar = {BaseDeDatos.COLUMNS_NAME_TAREA[0]};
        Cursor cursor = _sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, columnasAConsultar, "_id = ? ", id, null, null, null);

        if(id[0].equals("")){

            cursor = _sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, columnasAConsultar, null, null, null, null, null);
        }

        if (cursor == null){
            return tareas;
        }

        if (!cursor.moveToFirst()) return tareas;

        do {

            int idObtenidoDeBD = cursor.getInt(0);

            tareas.add(idObtenidoDeBD);

        } while (cursor.moveToNext());

        cursor.close();
        return tareas;
    }
}
