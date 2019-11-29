package com.example.notasproyectofinal;

import java.io.Serializable;
import java.util.Date;

public class Nota implements Serializable {
    int id;
    String titulo;
    String texto;
    String fecha;

    public Nota(){
    }

    public Nota(int id, String titulo, String texto, String fecha){
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.fecha = fecha;
    }

    public int getId(){ return id; }

    public void setId(int id) { this.id = id; }

    public String getTitulo(){ return titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTexto(){ return texto; }

    public void setTexto(String texto) { this.texto = texto; }

    public String getFecha(){ return fecha; }

    public void setFecha(String fecha) { this.fecha = fecha; }

    public String toString(){
        return "Nota " + titulo + "\n" + fecha;
    }
}
