package com.example.notasproyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.notasproyectofinal.MultimediaFragment.Multimedia;
import com.example.notasproyectofinal.NotasFragment.Notas;

public class Seleccion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);
    }

    public void SeleccionarMultimedia(View view) {
        Multimedia mul = new Multimedia();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor,mul)
                .addToBackStack(null)
                .commit();

    }

    public void EnviarNotas(View view) {
        Notas nota = new Notas();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor,nota)
                .addToBackStack(null)
                .commit();
    }
}
