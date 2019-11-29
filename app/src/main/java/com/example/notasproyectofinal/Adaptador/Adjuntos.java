package com.example.notasproyectofinal.Adaptador;

import android.net.Uri;

public class Adjuntos {

    public static final int IMAGE_TYPE = 0;
    public static final int AUDIO_TYPE = 1;
    public static final int VIDEO_TYPE = 2;

    public int type; //Almacena la constante de tipo
    public Uri data; //Almacena la info del recurso
    public String text; //Da la descripcion al TextView

    public Adjuntos(int type,String text, Uri data) {
        this.type = type;
        this.data = data;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
