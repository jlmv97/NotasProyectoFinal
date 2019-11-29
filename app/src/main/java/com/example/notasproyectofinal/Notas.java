package com.example.notasproyectofinal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Notas extends AppCompatActivity {
    //Variables
    EditText titulo;
    EditText mensaje;
    RecyclerView recycler;
    PopupMenu opciones;
    ImageButton menu;
    public MultiAdapter adaptador;
    public ArrayList<Adjuntos> pls = new ArrayList<>();
    public DAONota nota;
    private Calendar c = Calendar.getInstance();
    private Uri selectedImage;
    private Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adaptador = new MultiAdapter(pls,this);
        titulo = findViewById(R.id.txt_titulo_nota);
        mensaje = findViewById(R.id.txt_cuerpo_nota);
        menu = findViewById(R.id.btn_adjuntar);
        nota = new DAONota(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler = findViewById(R.id.recy_multi);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adaptador);

        if (validarPermisos()) {
            menu.setEnabled(true);

        } else {
            menu.setEnabled(false);
        }

    }

    private boolean validarPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if( (checkSelfPermission(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if( (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if( (shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ){
            PermisosRecomendados();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,RECORD_AUDIO}, 100);
        }

        return false;
    }

    private void PermisosRecomendados() {
        AlertDialog.Builder dialogo =  new AlertDialog.Builder(this);
        dialogo.setTitle("Permisos no otorgados");
        dialogo.setMessage("Ahora no joven! debe dar permiso para que la app pueda funcionar");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M) //esto se agrego porque marcaba error el requestPermissions
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA}, 100);
            }
        });
        dialogo.show();
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
