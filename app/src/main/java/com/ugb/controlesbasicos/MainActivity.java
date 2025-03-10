package com.ugb.controlesbasicos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btn;
    FloatingActionButton fab;
    TextView tempVal;
    String accion = "nuevo";
    String id="";
    String urlCompletaFoto;
    Intent tomarFotoIntent;
    ImageView img;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fabListarVehiculos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad();
            }
        });
        btn = findViewById(R.id.btnGuardarAgendaVehiculos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tempVal = findViewById(R.id.txtmarca);
                    String nombre = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtmodelo);
                    String direccion = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtaño);
                    String tel = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtNdeMotor);
                    String email = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtNdeChasis);
                    String dui = tempVal.getText().toString();

                    BasedeDatos db = new BasedeDatos(getApplicationContext(), "",null, 1);
                    String[] datos = new String[]{id,nombre,direccion,tel,email,dui, urlCompletaFoto};
                    mostrarMsg(accion);
                    String respuesta = db.administrar_vehiculos(accion, datos);
                    if(respuesta.equals("ok")){
                        Toast.makeText(getApplicationContext(), "Vehiculo guardado con exito", Toast.LENGTH_LONG).show();
                        abrirActividad();
                    }else{
                        Toast.makeText(getApplicationContext(), "Error al intentar guardar el vehiculo: "+ respuesta, Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        img = findViewById(R.id.btnImgVehiculo);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFotoVehiculo();
            }
        });
        mostrarDatosVehiculos();
    }
    private void tomarFotoVehiculo(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fotoVehiculo = null;
        try{
            fotoVehiculo = crearImagenVehiculo();
            if( fotoVehiculo!=null ){
                Uri uriFotovehiculo = FileProvider.getUriForFile(MainActivity.this,
                        "com.ugb.controlesbasicos.fileprovider", fotoVehiculo);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotovehiculo);
                startActivityForResult(tomarFotoIntent, 1);
            }else{
                mostrarMsg("No se pudo crear la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al abrir la camara: "+ e.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode==1 && resultCode==RESULT_OK){
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imageBitmap);
            }else{
                mostrarMsg("El usuario cancelo la toma de la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener la foto de la camara");
        }
    }
    private File crearImagenVehiculo() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
            fileName = "imagen_"+ fechaHoraMs +"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdirs();
        }
        File imagen = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = imagen.getAbsolutePath();
        return imagen;
    }
    private void mostrarDatosVehiculos(){
        try{
            Bundle parametros = getIntent().getExtras();//Recibir los parametros...
            accion = parametros.getString("accion");

            if(accion.equals("modificar")){
                String[] vehiculos = parametros.getStringArray("vehiculos");
                id = vehiculos[0];

                tempVal = findViewById(R.id.txtmarca);
                tempVal.setText(vehiculos[1]);

                tempVal = findViewById(R.id.txtmodelo);
                tempVal.setText(vehiculos[2]);

                tempVal = findViewById(R.id.txtaño);
                tempVal.setText(vehiculos[3]);

                tempVal = findViewById(R.id.txtNdeMotor);
                tempVal.setText(vehiculos[4]);

                tempVal = findViewById(R.id.txtNdeChasis);
                tempVal.setText(vehiculos[5]);

                urlCompletaFoto = vehiculos[6];
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imageBitmap);
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void abrirActividad(){
        Intent abrirActividad = new Intent(getApplicationContext(), lista_vehiculos.class);
        startActivity(abrirActividad);
    }
}