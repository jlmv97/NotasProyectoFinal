package com.example.notasproyectofinal.DAOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notasproyectofinal.Tarea;

import java.util.ArrayList;

public class DAOTareas {
    SQLiteDatabase sqLiteDatabase;
    Context ctx;

    public DAOTareas(Context ctx) {
        this.ctx = ctx;
        sqLiteDatabase =
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

        return  sqLiteDatabase.insert(BaseDeDatos.TABLE_NAME_TAREA,
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
        return sqLiteDatabase.update(BaseDeDatos.TABLE_NAME_TAREA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);

    }

    public int eliminar (int id){

        String[] argumentos = {String.valueOf(id)};
        return sqLiteDatabase.delete(BaseDeDatos.TABLE_NAME_TAREA, "_id = ?", argumentos);

    }

    public ArrayList<Tarea> agregar(String[] titulo){
        ArrayList<Tarea> tareas = new ArrayList<>();

        String[] columnasAConsultar = {BaseDeDatos.COLUMNS_NAME_TAREA[0], BaseDeDatos.COLUMNS_NAME_TAREA[1], BaseDeDatos.COLUMNS_NAME_TAREA[2], BaseDeDatos.COLUMNS_NAME_TAREA[3], BaseDeDatos.COLUMNS_NAME_TAREA[4]};
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA,BaseDeDatos.COLUMNS_NAME_TAREA, null, null, null, null,null);

        if(titulo[0].equals("")){

            cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, BaseDeDatos.COLUMNS_NAME_TAREA, null, null, null, null, null);
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

    public ArrayList<Tarea> buscarporTitulo(String[] titulo){
        ArrayList<Tarea> tareas = new ArrayList<>();

        String[] columnasAConsultar = {BaseDeDatos.COLUMNS_NAME_TAREA[0], BaseDeDatos.COLUMNS_NAME_TAREA[1], BaseDeDatos.COLUMNS_NAME_TAREA[2],BaseDeDatos.COLUMNS_NAME_TAREA[3]};
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, columnasAConsultar, "_titulo = ? OR _descripcion = ?", titulo, null, null, "_titulo");

        if(titulo[0].equals("")){

            cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, columnasAConsultar, null, null, null, null, null);
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
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, columnasAConsultar, "_id = ? ", id, null, null, null);

        if(id[0].equals("")){

            cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA, columnasAConsultar, null, null, null, null, null);
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

    public Cursor getAllCursor() {
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA,
                BaseDeDatos.COLUMNS_NAME_TAREA,
                null,
                null,
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Tarea preparacion(String titulo){
        Cursor query =sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,BaseDeDatos.COLUMNS_NAME_NOTA, "_titulo=?",new String[]{titulo+""}, null, null,null);
        Tarea nice=null;
        while (query.moveToNext()){
            nice = new Tarea(query.getInt(0),query.getString(1),query.getString(2),query.getString(3),query.getString(4));
        }
        return nice;
    }

    public String buscar(String criterio){
        String valores="";
        Cursor query =sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_TAREA,BaseDeDatos.COLUMNS_NAME_TAREA, "_titulo=?",new String[]{criterio+""}, null, null,null);
        while(query.moveToNext()){
            valores = "ID: "+query.getInt(0)+"\n Titulo: "+query.getString(1)+
                    "\n Descripcion: "+query.getString(2)+
                    "\n Fecha: "+query.getString(3)+
                    "\n Hora: "+query.getString(4);
        }
        return valores;
    }
}
