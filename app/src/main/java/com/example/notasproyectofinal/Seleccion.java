package com.example.notasproyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.example.notasproyectofinal.MultimediaFragment.Multimedia;
import com.example.notasproyectofinal.NotasFragment.Notas;

public class Seleccion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void SeleccionarMultimedia(View view) {
        Multimedia mul = new Multimedia();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor,mul)
                .commit();

    }

    public void EnviarNotas(View view) {
        Notas nota = new Notas();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor,nota)
                .commit();
    }

}
