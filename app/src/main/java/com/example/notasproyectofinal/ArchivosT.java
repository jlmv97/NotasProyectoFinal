package com.example.notasproyectofinal;

import android.net.Uri;

public class ArchivosT {
    int id;
    Uri ruta;
    int tipo;
    String descripcion;
    int idTareas;

    public ArchivosT(int id, Uri ruta, int tipo, String descripcion, int idTareas) {
        this.id = id;
        this.ruta = ruta;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.idTareas = idTareas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getRuta() {
        return ruta;
    }

    public void setRuta(Uri ruta) {
        this.ruta = ruta;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdTareas() {
        return idTareas;
    }

    public void setIdTareas(int idNotas) {
        this.idTareas = idNotas;
    }
}
