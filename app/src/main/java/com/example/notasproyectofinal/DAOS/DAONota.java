package com.example.notasproyectofinal.DAOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notasproyectofinal.Nota;

import java.util.ArrayList;

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
        contentValues.put(BaseDeDatos.COLUMNS_NAME_NOTA[3], nota.getFecha());

        return sqLiteDatabase.insert(BaseDeDatos.TABLE_NAME_NOTAS, null, contentValues);
    }

    public boolean delete (int id){
        return sqLiteDatabase.delete(BaseDeDatos.TABLE_NAME_NOTAS,
                "_id=?",new String[]{Integer.toString(id)}) > 0;
    }

    public int update (Nota nota){

        ContentValues valoresParaActualizar =  new ContentValues();
        valoresParaActualizar.put(BaseDeDatos.COLUMNS_NAME_NOTA[1], nota.getTitulo());
        valoresParaActualizar.put(BaseDeDatos.COLUMNS_NAME_NOTA[2], nota.getTexto());
        String campoParaActualizar = "_id = ?";
        String[] argumentosParaActualizar = {String.valueOf(nota.getId())};
        return sqLiteDatabase.update(BaseDeDatos.TABLE_NAME_NOTAS, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);

    }
/*
    public boolean update(int id, ContentValues contentValues){
        return sqLiteDatabase.update(BaseDeDatos.TABLE_NAME_NOTAS, contentValues,
                "_id="+id,null) > 0;
    }*/

    public Nota notaPorID(int id){
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,
                null,"_id="+id,null,null,null, null);

        Nota nota = null;

        if (cursor.moveToFirst()) {
            do {

                    nota =
                            new Nota(cursor.getInt(0), cursor.getString(1),
                                    cursor.getString(2),cursor.getString(3));
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

                    nota =
                            new Nota(cursor.getInt(0), cursor.getString(1),
                                    cursor.getString(2),cursor.getString(3));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return nota;
    }

    public ArrayList<Nota> agregar(String[] titulo){
        ArrayList<Nota> notas = new ArrayList<>();

        String[] columnasAConsultar = {BaseDeDatos.COLUMNS_NAME_NOTA[0], BaseDeDatos.COLUMNS_NAME_NOTA[1], BaseDeDatos.COLUMNS_NAME_NOTA[2],BaseDeDatos.COLUMNS_NAME_NOTA[3]};
        Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS, BaseDeDatos.COLUMNS_NAME_NOTA, null,null, null, null, null);

        if(titulo[0].equals("")){

            cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS, columnasAConsultar, null, null, null, null, null);
        }

        if (cursor == null){
            return notas;
        }

        if (!cursor.moveToFirst()) return notas;

        do {

            int idObtenidoDeBD = cursor.getInt(0);
            String tituloObtenidoDeBD = cursor.getString(1);
            String descripcionObtenidoDeBD = cursor.getString(2);
            String fechaObtenidoDBD = cursor.getString(3);

            Nota notaObtenidoDeBD = new Nota(idObtenidoDeBD, tituloObtenidoDeBD, descripcionObtenidoDeBD,fechaObtenidoDBD);
            notas.add(notaObtenidoDeBD);

        } while (cursor.moveToNext());

        cursor.close();
        return notas;
    }


    public Nota llenar (){
        Cursor query =sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,BaseDeDatos.COLUMNS_NAME_NOTA, null,null, null, null,null);
        Nota nice=null;
        while (query.moveToNext()){
            nice = new Nota(query.getInt(0),query.getString(1),query.getString(2),query.getString(3));
        }
        return nice;
    }

    public String buscar(String criterio){
        String valores="";
        Cursor query =sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS,BaseDeDatos.COLUMNS_NAME_NOTA, "_id=?",new String[]{criterio+""}, null, null,null);
        while(query.moveToNext()){
            valores = "ID: "+query.getInt(0)+"\n Titulo: "+query.getString(1)+
                    "\n Texto: "+query.getString(2)+
                    "\n Fecha: "+query.getString(3);
        }
        return valores;
    }

        public ArrayList<Integer> buscarUltimoId( String[] id){
            ArrayList<Integer> notas = new ArrayList<>();

            String[] columnasAConsultar = {BaseDeDatos.COLUMNS_NAME_NOTA[0]};
            Cursor cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS, columnasAConsultar, "_id = ? ", id, null, null, null);

            if(id[0].equals("")){

                cursor = sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_NOTAS, columnasAConsultar, null, null, null, null, null);
            }

            if (cursor == null){
                return notas;
            }

            if (!cursor.moveToFirst()) return notas;

            do {

                int idObtenidoDeBD = cursor.getInt(0);

                notas.add(idObtenidoDeBD);

            } while (cursor.moveToNext());

            cursor.close();
            return notas;
        }
    }


