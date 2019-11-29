package com.example.notasproyectofinal.DAOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notasproyectofinal.BaseDeDatos;
import com.example.notasproyectofinal.Recordatorio;

import java.util.ArrayList;

public class DAORecordatorio {
    SQLiteDatabase _sqLiteDatabase;
    Context ctx;

    public DAORecordatorio(Context ctx) {
        this.ctx = ctx;
        _sqLiteDatabase =
                new BaseDeDatos(ctx).getWritableDatabase();
    }

    public long insert(Recordatorio recordatorio){
        ContentValues contentValues
                = new ContentValues();

        contentValues.put(BaseDeDatos.COLUMNS_NAME_RECORDATORIO[1],
                recordatorio.getFecha());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_RECORDATORIO[2],
                recordatorio.getHora());
        contentValues.put(BaseDeDatos.COLUMNS_NAME_RECORDATORIO[3],
                recordatorio.getIdTarea());

        return  _sqLiteDatabase.insert(BaseDeDatos.TABLE_NAME_RECORDATORIO,
                null, contentValues);
    }

    public ArrayList<Recordatorio> buscar(String[] t){
        ArrayList<Recordatorio> recordatorios = new ArrayList<>();

        String[] columnasAConsultar = {BaseDeDatos.COLUMNS_NAME_RECORDATORIO[0], BaseDeDatos.COLUMNS_NAME_RECORDATORIO[1], BaseDeDatos.COLUMNS_NAME_RECORDATORIO[2], BaseDeDatos.COLUMNS_NAME_RECORDATORIO[3]};
        Cursor cursor = _sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_RECORDATORIO, columnasAConsultar, "t = ?", t, null, null, null);

        if(t[0].equals("")){

            cursor = _sqLiteDatabase.query(BaseDeDatos.TABLE_NAME_RECORDATORIO, columnasAConsultar, null, null, null, null, null);
        }

        if (cursor == null){
            return recordatorios;
        }

        if (!cursor.moveToFirst()) return recordatorios;

        do {

            int idObtenidoDeBD = cursor.getInt(0);
            String fechaObtenidoDeBD = cursor.getString(1);
            String horaObtenidoDeBD = cursor.getString(2);
            int idTareaObtenidoDeBD = cursor.getInt(3);

            Recordatorio recordatorioObtenidoDeBD = new Recordatorio(idObtenidoDeBD, fechaObtenidoDeBD, horaObtenidoDeBD, idTareaObtenidoDeBD);
            recordatorios.add(recordatorioObtenidoDeBD);

        } while (cursor.moveToNext());

        cursor.close();
        return recordatorios;
    }

    public int eliminar (int id){

        String[] argumentos = {String.valueOf(id)};
        return _sqLiteDatabase.delete(BaseDeDatos.TABLE_NAME_RECORDATORIO, "_id = ?", argumentos);

    }
}
