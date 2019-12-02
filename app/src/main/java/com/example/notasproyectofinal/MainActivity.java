package com.example.notasproyectofinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.notasproyectofinal.DAOS.DAONota;
import com.example.notasproyectofinal.DAOS.DAORecursos;
import com.example.notasproyectofinal.DAOS.DAOTareas;
import com.example.notasproyectofinal.MultimediaFragment.MostrarTFragment;
import com.example.notasproyectofinal.NotasFragment.MostrarNFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    public int contenedor=0;
    EditText criterio1;
    EditText criterio2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.menu_tab);
        criterio1 = findViewById(R.id.txt_buscadoN);
        criterio2 = findViewById(R.id.txt_busquedaT);

        MostrarNFragment mn = new MostrarNFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor,mn)
                .commit();

        FloatingActionButton fab = findViewById(R.id.fab);//Coloca el fragmetno de mostar nota
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrAgregarNotas();
            }
        });

        //Tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        MostrarNFragment mn = new MostrarNFragment();
                        FragmentTransaction transactionN = getSupportFragmentManager().beginTransaction();
                        transactionN.replace(R.id.contenedor,mn)
                                .commit();
                        contenedor=0;
                        break;
                    case 1:
                        MostrarTFragment mt = new MostrarTFragment();
                        FragmentTransaction transactionT = getSupportFragmentManager().beginTransaction();
                        transactionT.replace(R.id.contenedor,mt)
                                .commit();
                        contenedor=1;
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void IrAgregarNotas (){
        if(contenedor == 0){
            Intent addNota = new Intent(this,Notas.class);
            startActivity(addNota);
        }else{
        Intent addTarea = new Intent(this,Tareas.class);
        startActivity(addTarea);
        }
    }

}
