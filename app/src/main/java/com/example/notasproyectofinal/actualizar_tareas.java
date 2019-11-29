package com.example.notasproyectofinal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notasproyectofinal.Adaptador.Adjuntos;
import com.example.notasproyectofinal.Adaptador.MultiAdapter;
import com.example.notasproyectofinal.DAOS.DAORecursosT;
import com.example.notasproyectofinal.DAOS.DAOTareas;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class actualizar_tareas extends AppCompatActivity {

    EditText titulo;
    EditText mensaje;
    TextView  fechas;
    RecyclerView recycler;
    PopupMenu opciones;
    ImageButton menu;
    ImageButton grabar;
    public MultiAdapter adaptador;
    public ArrayList<Adjuntos> pls = new ArrayList<>();
    public DAOTareas tarea;
    private Calendar c = Calendar.getInstance();
    String currentPhotoPath;
    Tarea tareas;
    private int day, month, year, hour, min;
    String m,d,fecha, hora, minutos, hr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_tareas);
        adaptador = new MultiAdapter(pls,this);
        titulo = findViewById(R.id.txt_titulo_tarea);
        mensaje = findViewById(R.id.txt_cuerpo_tarea);
        menu = findViewById(R.id.btn_adjuntar);
        grabar = findViewById(R.id.btn_grabar);
        fechas = findViewById(R.id.txt_fecha);
        tarea = new DAOTareas(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
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

    //VALIDACION DE PERMISOS/////////////////////////////////////////////////////////
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

    //POP UP MENU///////////////////////////////////////////////////////

    public void Adjuntar(View view) {///Manda llamar los dintos metodos para adjuntar archivos
        opciones = new PopupMenu(this,view);
        opciones.getMenuInflater().inflate(R.menu.menu_adjuntar,opciones.getMenu());
        opciones.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.popCamara:
                        dispatchTakePictureIntent();
                        break;
                    case R.id.popVideo:
                        dispatchTakeVideoIntent();
                        break;
                    case R.id.popAjuntarV:
                        buscarVid();
                        break;
                    case R.id.popAjuntarF:
                        buscarImg();
                        break;
                }
                return false;
            }
        });
        opciones.show();
    }

    //METODOS PARA ADJUNTAR ARCHIVOS MULTIMEDIA

    public void buscarImg (){//AGREGAR IMAGEN DE LA GALLERIA
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent,3);
    }
    public void buscarVid() {//AGREGA VIDEO DE LA GALERIA
        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intentGaleria.setType("video/");
        startActivityForResult(intentGaleria.createChooser(intentGaleria,"Seleccione una app"),2);
    }
    @RequiresApi(api = Build.VERSION_CODES.N) //CREA EL ARCHIVO PARA COLOCAR LA FOTO
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new android.icu.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)//ABRE LA CAMARA
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.notasproyectofinal.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 0);
            }
        }
    }
    private void dispatchTakeVideoIntent() {//ABRE LA CAMARA PARA GRABAR
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, 1);
        }
    }

    //METODO PARA GRABAR AUDIO

    private MediaRecorder grabacion=null;
    private String archivoSalida = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void grabarAudio(View view){
        if(grabacion==null){
            String timeStamp = new android.icu.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Grabacion_"+timeStamp+".mp3";
            grabacion = new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            grabacion.setOutputFile(archivoSalida);

            try{
                grabacion.prepare();
                grabacion.start(); //comenzar a grabar
            }catch (IOException e){

            }
            grabar.setColorFilter(Color.argb(255, 255, 0, 0)); // Cuando este grabando lo pongo color rojo
            Toast.makeText(getApplicationContext(),"Grabando",Toast.LENGTH_SHORT).show();

        }else if(grabacion!=null){
            grabacion.stop();
            grabacion.release();
            grabacion = null; //para que pueda vo++lver a grabar si se presiona el boton nuevamente
            grabar.setColorFilter(Color.argb(255, 0, 0, 0)); // ya no grabando, regresa a color negro

            Adjuntos model = new Adjuntos(1, "", Uri.parse(archivoSalida));
            pls.add(model);
            recycler.setAdapter(adaptador);

            Toast.makeText(getApplicationContext(),"finalizada",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0://Tomar Foto
                Adjuntos adjuntos1 = new Adjuntos(0,"",Uri.parse(currentPhotoPath));
                pls.add(adjuntos1);
                recycler.setAdapter(adaptador);
                break;
            case 1://Tomar video
                Uri videoUri2 = data.getData();//videoView.setVideoURI(videoUri);
                Adjuntos adjuntos2 = new Adjuntos(2,"Adios", videoUri2);
                pls.add(adjuntos2);
                recycler.setAdapter(adaptador);
                break;
            case 2:
                Uri videoUri3 = data.getData();//videoView.setVideoURI(videoUri);
                Adjuntos adjuntos3 = new Adjuntos(2,"hola", videoUri3);
                pls.add(adjuntos3);
                recycler.setAdapter(adaptador);
                break;
            case 3:
                Uri ima = data.getData();
                Adjuntos adjuntos4 = new Adjuntos(Adjuntos.IMAGE_TYPE,"HH",ima);
                pls.add(adjuntos4);
                recycler.setAdapter(adaptador);
                break;
        }

    }
    //METODOS PARA INSERTAR LAS NOTAS Y LAS RUTAS DE LOS ARCHIVOS A LA BASE DE DATOS ///////////////////////////////////////////////
    public void AgregarTarea(View view) {///Agrega la nota a la base de datos
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String hour = new SimpleDateFormat( "HH:mm:ss").format(new Date());
        tarea.insert(new Tarea(0,titulo.getText().toString(),mensaje.getText().toString(),date,hour));
        if(pls.isEmpty()){

        }else{
            insertaruri();
        }
        finish();
        if(pls.isEmpty()){

        }else{
            insertaruri();
        }
    }

    public void insertaruri (){
        String[] Tarea1 = {""};
        DAOTareas daoTarea = new DAOTareas(this);
        ArrayList<Integer> arrayIds = new ArrayList<>();
        arrayIds = daoTarea.buscarUltimoId(Tarea1);

        for (int i = 0; i < pls.size(); i++) {


            ArchivosT ruta = new ArchivosT(0, pls.get(i).data, pls.get(i).type,pls.get(i).text ,arrayIds.get(arrayIds.size()-1));
            DAORecursosT daoRecursos = new DAORecursosT(this);

            daoRecursos.insert(ruta);
            Log.i("RUTAS", ""+ruta.getId() +" path= "+ruta.getRuta()+"idTarea= "+ruta.getIdTareas());


        }
    }

    private void abrirCalenadario(View view) {
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if((month+1)<10){
                    m = "0"+""+(month+1);
                }else{
                    m = ""+(month+1);
                }
                if(dayOfMonth<10){
                    d = "0"+""+dayOfMonth;
                }else{
                    d= ""+dayOfMonth;
                }
                //efecha.setText(dayOfMonth+"/"+(month + 1)+"/"+year);
                //btnFecha.setText(year+"/"+m+"/"+d);
                fecha= ""+year+"/"+m+"/"+d;

                abrirReloj();
            }
        } ,year,month,day);
        datePickerDialog.show();

    }

    private void abrirReloj() {
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if((hourOfDay+1)<10){
                    hora = "0"+""+(hourOfDay);
                }else{
                    hora = ""+(hourOfDay);
                }
                if(minute<10){
                    minutos = "0"+""+minute;
                }else{
                    minutos= ""+minute;
                }
                hr = hora+":"+minutos;
                min = minute;
                hour= hourOfDay;
                fechas.setText(fecha+"  "+hr);


            }
        },hour,min,false);
        timePickerDialog.show();
    }

    public void ActualizarTarea(View view) {
        tareas.setTitulo(titulo.getText().toString());
        tareas.setDescripcion(mensaje.getText().toString());
        tareas.setFecha(fecha);
        tareas.setHora(hr);

        DAOTareas dao = new DAOTareas(this);

        switch (view.getId()) {
            case R.id.btn_actualizarT:
                dao.update(tareas);
                finish();
        }

    }


    /*
    public void AgregarRecordatorio(View view) {
        fecha = findViewById(R.id.lbl_recordatorio);

        String fechaR;
        String fechaA;


        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Muestro la hora con el formato deseado
                fecha.setText(fecha.getText().toString()+" "+horaFormateada + ":" + minutoFormateado);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, true);
        recogerHora.show();
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                fecha.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual

             //También puede cargar los valores que usted desee

        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }*/
}
