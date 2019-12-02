package com.example.notasproyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import com.example.notasproyectofinal.DAOS.DAORecordatorio;
import com.example.notasproyectofinal.DAOS.DAORecursosT;
import com.example.notasproyectofinal.DAOS.DAOTareas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Tareas extends AppCompatActivity implements Dialogo.ExampleDilaogListener {
    //Variables
    EditText titulo;
    EditText mensaje;
    TextView fechas;
    RecyclerView recycler;
    PopupMenu opciones;
    ImageButton menu;
    ImageButton grabar;
    public MultiAdapter adaptador;
    public ArrayList<Adjuntos> pls = new ArrayList<>();
    public DAOTareas tarea;
    private int day, month, year, hour, min;
    String m,d,fecha, hora, minutos, hr;
    String currentPhotoPath;
    final Calendar c = Calendar.getInstance();
    final Calendar calendarRecordatorios = Calendar.getInstance();
    ArrayList <Recordatorio> listaRecordatorios = new ArrayList<>();
    ArrayList <provicional> noSave = new ArrayList<>();
    private int dayRecordaotio, monthRecordatorio, yearRecordatorio, hourRecordatorio, minRecordatorio;
    String mRecordatorio,dRecordatorios,fechaRecordatorio, horaRecordatorio, minutosRecordatorio, hrRecordatorios;
    Tarea tr;
    private String descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adaptador = new MultiAdapter(pls,this);
        titulo = findViewById(R.id.txt_titulo);
        mensaje = findViewById(R.id.txt_cuerpo);
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
        dialogo.setTitle(R.string.sure);
        dialogo.setMessage(R.string.sorry);

        dialogo.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
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
        openDialog();
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
        openDialog();
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
            Toast.makeText(getApplicationContext(),R.string.grabando,Toast.LENGTH_SHORT).show();

        }else if(grabacion!=null){
            grabacion.stop();
            grabacion.release();
            grabacion = null; //para que pueda vo++lver a grabar si se presiona el boton nuevamente
            grabar.setColorFilter(Color.argb(255, 0, 0, 0)); // ya no grabando, regresa a color negro

            Adjuntos model = new Adjuntos(1, "", Uri.parse(archivoSalida));
            pls.add(model);
            recycler.setAdapter(adaptador);

            Toast.makeText(getApplicationContext(),R.string.finalizado,Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0://Tomar Foto
                Adjuntos adjuntos1 = new Adjuntos(0,descripcion,Uri.parse(currentPhotoPath));
                pls.add(adjuntos1);
                recycler.setAdapter(adaptador);
                break;
            case 1://Tomar video
                Uri videoUri2 = data.getData();//videoView.setVideoURI(videoUri);
                Adjuntos adjuntos2 = new Adjuntos(2,descripcion, videoUri2);
                pls.add(adjuntos2);
                recycler.setAdapter(adaptador);
                break;
            case 2:
                Uri videoUri3 = data.getData();//videoView.setVideoURI(videoUri);
                Adjuntos adjuntos3 = new Adjuntos(2,descripcion, videoUri3);
                pls.add(adjuntos3);
                recycler.setAdapter(adaptador);
                break;
            case 3:
                Uri ima = data.getData();
                Adjuntos adjuntos4 = new Adjuntos(Adjuntos.IMAGE_TYPE,descripcion,ima);
                pls.add(adjuntos4);
                recycler.setAdapter(adaptador);
                break;
        }

    }
    //METODOS PARA INSERTAR LAS TAREAS Y LAS RUTAS DE LOS ARCHIVOS A LA BASE DE DATOS ///////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void AgregarTarea(View view) {///Agrega la nota a la base de datos
        if (titulo.getText().toString().isEmpty()||mensaje.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.llenar,Toast.LENGTH_SHORT);
        }else {
            tr = new Tarea(0, titulo.getText().toString(), mensaje.getText().toString(), fecha, hr);
            tarea.insert(tr);
            if (pls.isEmpty()) {

            } else {
                insertaruri();
                insertRecordatorios();
            }
            crearNotificacion(year, month, day, hour, min);
            finish();
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
//SE AGREGA LA FECHA
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


    public void abrirCalendario(View view) {
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
                fecha= ""+year+"/"+m+"/"+d;

                abrirReloj();
            }
        } ,year,month,day);
        datePickerDialog.show();
    }

    //AGREGA LOS RECORDATORIOS
    public void insertRecordatorios(){
        String[] Tarea1 = {""};
        DAOTareas daoTareas = new DAOTareas(this);
        ArrayList<Integer>arrayIds = new ArrayList<>();
        arrayIds = daoTareas.buscarUltimoId(Tarea1);
        for(int i = 0;i < noSave.size(); i++){
            crearNRecordatorio(noSave.get(i).getYear(),
                    noSave.get(i).month,noSave.get(i).getDay(),
                    noSave.get(i).hour,noSave.get(i).getMin());
            Recordatorio recordatorio = new Recordatorio(0,noSave.get(i).fecha,
                    noSave.get(i).getHora(),arrayIds.get(arrayIds.size()-1));
            DAORecordatorio daoRecordatorio = new DAORecordatorio(this);
            daoRecordatorio.insert(recordatorio);
        }
    }
    //CREA LOS RECORDATORIOS
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)//Esta avisa de cuando se llega ek limite de la tarea
    public void crearNotificacion(int year, int month, int day, int hour, int min){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        intent.putExtra("tarea","Realizar la tarea "+tr.getTitulo());
        PendingIntent broadcast = PendingIntent.getBroadcast(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        c.set(year,month,day,hour,min,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),broadcast);
    }

    public void crearNRecordatorio(int year, int month, int day, int hour, int min){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent notiRintent = new Intent(this, AlarmReceiver.class);
        notiRintent.putExtra("tarea","Recordatorio de la tarea "+tr.getTitulo());
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 200,notiRintent,PendingIntent.FLAG_UPDATE_CURRENT);
        calendarRecordatorios.set(year,month,day,hour,min,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendarRecordatorios.getTimeInMillis(),broadcast);
    }

    public void abrirCaleRecordatorio(){
        yearRecordatorio = calendarRecordatorios.get(Calendar.YEAR);
        monthRecordatorio = calendarRecordatorios.get(Calendar.MONTH);
        dayRecordaotio = calendarRecordatorios.get(Calendar.DAY_OF_MONTH);
        hourRecordatorio = calendarRecordatorios.get(Calendar.HOUR_OF_DAY);
        minRecordatorio = calendarRecordatorios.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if ((month+1)<10){
                    mRecordatorio = "0"+""+(month+1);
                }else{
                    mRecordatorio = ""+(month+1);
                }
                if(dayOfMonth<10){
                    dRecordatorios = "0"+""+dayOfMonth;
                }else{
                    dRecordatorios = ""+dayOfMonth;
                }
                fechaRecordatorio = ""+year+"/"+mRecordatorio+"/"+dRecordatorios;
                abrirRelojR();
            }
        },yearRecordatorio,monthRecordatorio,dayRecordaotio);
        datePickerDialog.show();
    }

    public void abrirRelojR(){
        hourRecordatorio = calendarRecordatorios.get(Calendar.HOUR_OF_DAY);
        minRecordatorio = calendarRecordatorios.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if((hourOfDay+1)<10){
                    horaRecordatorio = "0"+""+(hourOfDay);
                }else{
                    horaRecordatorio = ""+(hourOfDay);
                }
                if (minute<10){
                    minutosRecordatorio = "0"+""+minute;
                }else{
                    minutosRecordatorio = ""+minute;
                }
                hrRecordatorios = horaRecordatorio+";"+minutosRecordatorio;
                minRecordatorio = minute;
                hourRecordatorio = hourOfDay;
                guardarRecordatorio(yearRecordatorio,monthRecordatorio,dayRecordaotio,hourRecordatorio,minRecordatorio,fechaRecordatorio,hrRecordatorios);
            }
        },hourRecordatorio,minRecordatorio,false);
        timePickerDialog.show();
    }

    public void guardarRecordatorio(int year, int month, int day, int hour, int min, String fecha, String hora){
        provicional provicional = new provicional(year,month,day,hour,min,fecha,hora);
        noSave.add(provicional);

    }

    public void agregar_recorda(View view) {
        abrirCaleRecordatorio();
    }



    //DIALOGO PARA AGREGAR DESCRIPCION
    public void openDialog(){
        Dialogo dialog = new Dialogo();
        dialog.show(getSupportFragmentManager(), "Dialogo");
    }

    @Override
    public void applyTexts(String descripcion) {
        //txtDescripcion.setText(descripcion); //solo para probar si obtiene
        this.descripcion = descripcion; // la variable global descripcion obtiene el valor de lo que hay en el input del Dialog
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adaptador);
    }
}
