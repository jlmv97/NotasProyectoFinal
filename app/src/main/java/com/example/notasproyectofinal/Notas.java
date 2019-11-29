package com.example.notasproyectofinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.notasproyectofinal.Adaptador.Adjuntos;
import com.example.notasproyectofinal.Adaptador.MultiAdapter;
import com.example.notasproyectofinal.DAOS.DAONota;
import com.example.notasproyectofinal.DAOS.DAORecursos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Notas extends AppCompatActivity {
    //Variables
    EditText titulo;
    EditText mensaje;
    RecyclerView recycler;
    PopupMenu opciones;
    public MultiAdapter adaptador;
    public ArrayList<Adjuntos> pls = new ArrayList<>();
    public DAONota nota;
    private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);
        adaptador = new MultiAdapter(pls,this);
        titulo = findViewById(R.id.txt_titulo_nota);
        mensaje = findViewById(R.id.txt_cuerpo_nota);
        nota = new DAONota(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler = findViewById(R.id.recy_multi);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adaptador);
    }

    public void Adjuntar(View view) {///Manda llamar los dintos metodos para adjuntar archivos
        opciones = new PopupMenu(this,view);
        opciones.getMenuInflater().inflate(R.menu.menu_adjuntar,opciones.getMenu());
        opciones.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.popAudio:
                        Toast.makeText(getBaseContext(),"Grabar audio",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.popCamara:
                        Toast.makeText(getBaseContext(),"Camara",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.popAjuntar:
                        buscarImg();
                        break;
                }
                return false;
            }
        });
        opciones.show();
    }

    public void buscarImg (){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                Uri ima = data.getData();
                Adjuntos adjuntos = new Adjuntos(Adjuntos.IMAGE_TYPE,"",ima);
                pls.add(adjuntos);
                recycler.setAdapter(adaptador);

                break;
        }
    }

    public void AgregarNota(View view) {///Agrega la nota a la base de datos
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        nota.insert(new Nota(0,titulo.getText().toString(),mensaje.getText().toString(),date));
        if(pls.isEmpty()){

        }else{
            insertaruri();
        }
        Intent home = new Intent(this,MainActivity.class);
        startActivity(home);
        finish();
        if(pls.isEmpty()){

        }else{
            insertaruri();
        }
    }

    public void insertaruri (){
        String[] Notas1 = {""};
        DAONota daoNotas = new DAONota(this);
        ArrayList<Integer> arrayIds = new ArrayList<>();
        arrayIds = daoNotas.buscarUltimoId(Notas1); //El array que me gusrda todos los ids de las Notas

            for (int i = 0; i < pls.size(); i++) {


                Archivos ruta = new Archivos(0, pls.get(i).data, pls.get(i).type,pls.get(i).text ,arrayIds.get(arrayIds.size()-1));
                DAORecursos daoRecursos = new DAORecursos(this);

                daoRecursos.insert(ruta);
                Log.i("RUTAS", ""+ruta.getId() +" path= "+ruta.getRuta()+"idNota= "+ruta.getIdNotas());
                //finish();
                Log.i("RUTAS", ""+ruta.getId() +" path= "+ruta.getRuta()+ "idNota= "+ruta.getIdNotas());


        }
    }
}
