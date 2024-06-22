package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class lista_vehiculos extends AppCompatActivity {
    Bundle paramatros = new Bundle();
    BasedeDatos db;
    ListView lts;
    Cursor cVehiculos;
    final ArrayList<VEHICULOS> alVehiculos = new ArrayList<VEHICULOS>();
    final ArrayList<VEHICULOS> alVehiculosCopy = new ArrayList<VEHICULOS>();
    VEHICULOS datosVehiculos;
    FloatingActionButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_vehiculos);
        btn = findViewById(R.id.fabAgregarVehiculos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paramatros.putString("accion","nuevo");
                abrirActividad(paramatros);
            }
        });
        obtenerVehiculos();
        buscarVehiculos();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        cVehiculos.moveToPosition(info.position);
        menu.setHeaderTitle("Que deseas hacer con "+ cVehiculos.getString(1));//1 es el campo nombre
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            switch (item.getItemId()){
                case R.id.mnxAgregar:
                    paramatros.putString("accion", "nuevo");
                    abrirActividad(paramatros);
                    break;
                case R.id.mnxModificar:
                    String[] vehiculos = {
                            cVehiculos.getString(0), //idVehiculo
                            cVehiculos.getString(1), //marca
                            cVehiculos.getString(2), //modelo
                            cVehiculos.getString(3), //año
                            cVehiculos.getString(4), //numero de motor
                            cVehiculos.getString(5), //numero de chasis
                            cVehiculos.getString(6), //foto
                    };
                    paramatros.putString("accion", "modificar");
                    paramatros.putStringArray("vehiculos", vehiculos);
                    abrirActividad(paramatros);
                    break;
                case R.id.mnxEliminar:
                    eliminarVehiculo();
                    break;
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar el item: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarVehiculo(){
        try {
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_vehiculos.this);
            confirmar.setTitle("Esta seguro de eliminar a: ");
            confirmar.setMessage(cVehiculos.getString(1));
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String respuesta = db.administrar_vehiculos("eliminar", new String[]{cVehiculos.getString(0)});
                    if( respuesta.equals("ok") ){
                        mostrarMsg("Vehiculo eliminado con exito");
                        obtenerVehiculos();
                    }else{
                        mostrarMsg("Error al eliminar el vehiculo: "+ respuesta);
                    }
                }
            });
            confirmar.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirmar.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar: "+ e.getMessage());
        }
    }
    private void buscarVehiculos(){
        TextView tempVal = findViewById(R.id.txtBuscarVehiculos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    alVehiculos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alVehiculos.addAll(alVehiculosCopy);
                    }else{
                        for (VEHICULOS vehiculo : alVehiculosCopy){
                            String nombre = vehiculo.getNombre();
                            String direccion = vehiculo.getDireccion();
                            String tel = vehiculo.getTelefono();
                            String email = vehiculo.getEmail();
                            if( nombre.trim().toLowerCase().contains(valor) ||
                                direccion.trim().toLowerCase().contains(valor) ||
                                tel.trim().contains(valor) ||
                                email.trim().toLowerCase().contains(valor)){
                                alVehiculos.add(vehiculo);
                            }
                        }
                        IIMAGEN_ADAPTADOR adImagenes=new IIMAGEN_ADAPTADOR(getApplicationContext(), alVehiculos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar: "+ e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerVehiculos(){
        try{
            alVehiculos.clear();
            alVehiculosCopy.clear();

            db = new BasedeDatos(getApplicationContext(),"", null, 1);
            cVehiculos = db.obtener_vehiculos();
            if( cVehiculos.moveToFirst() ){
                lts = findViewById(R.id.ltsVehiculos);
                do{
                    datosVehiculos = new VEHICULOS(
                            cVehiculos.getString(0),//idVehiculo
                            cVehiculos.getString(1),//marca
                            cVehiculos.getString(2),//modelo
                            cVehiculos.getString(3),//año
                            cVehiculos.getString(4),//numero de motor
                            cVehiculos.getString(5),//numero de chasis
                            cVehiculos.getString(6) //foto
                    );
                    alVehiculos.add(datosVehiculos);
                }while (cVehiculos.moveToNext());
                alVehiculosCopy.addAll(alVehiculos);

                IIMAGEN_ADAPTADOR adImagenes = new IIMAGEN_ADAPTADOR(getApplicationContext(), alVehiculos);
                lts.setAdapter(adImagenes);

                registerForContextMenu(lts);
            }else {
                paramatros.putString("accion", "nuevo");
                abrirActividad(paramatros);
                mostrarMsg("No hay Datos de vehiculos.");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener los vehiculos: "+ e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}