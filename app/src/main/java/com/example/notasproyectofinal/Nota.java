package com.example.notasproyectofinal;

import java.io.Serializable;

public class Nota implements Serializable {
    int id;
    String titulo;
    String texto;
    String recordatorio;
    String fecha;

    public Nota(){
    }

    public Nota(int id, String titulo, String texto, String recordatorio, String fecha){
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.recordatorio = recordatorio;
        this.fecha = fecha;
    }

    public int getId(){ return id; }

    public void setId(int id) { this.id = id; }

    public String getTitulo(){ return titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTexto(){ return texto; }

    public void setTexto(String texto) { this.texto = texto; }

    public String getRecordatorio(){ return recordatorio; }

    public void setRecordatorio(String recordatorio) { this.recordatorio = recordatorio; }

    public String getFecha(){ return fecha; }

    public void setFecha(String fecha) { this.fecha = fecha; }
}
