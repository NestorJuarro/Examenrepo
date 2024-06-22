package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BasedeDatos extends SQLiteOpenHelper {
    private static final String dbname = "Nestor_Quintanilla";
    private static final int v = 1;
    private static final String SQLdb = "CREATE TABLE vehiculos(idVehiculo integer primary key autoincrement, " +
            "marca text, modelo text, año text, NdeMotor text, NdeChasis text, foto text)";

    public BasedeDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, v);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLdb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // cambiar estructura de la BD
    }

    public String administrar_vehiculos(String accion, String[] datos) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "";
            if (accion.equals("nuevo")) {
                sql = "INSERT INTO vehiculos(marca,modelo,año,NdeMotor,NdeChasis,foto) VALUES('" + datos[1] + "', '" + datos[2] + "', '" + datos[3] + "', " +
                        "'" + datos[4] + "','" + datos[5] + "', '" + datos[6] + "' )";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE vehiculos SET marca='" + datos[1] + "', modelo='" + datos[2] + "', año='" + datos[3] + "', NdeMotor=" +
                        "'" + datos[4] + "', NdeChasis='" + datos[5] + "', foto='" + datos[6] + "' WHERE idVehiculo='" + datos[0] + "'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM vehiculos WHERE idVehiculo='" + datos[0] + "'";
            }
            db.execSQL(sql);
            return "ok";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Cursor obtener_vehiculos() {
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM vehiculos ORDER BY marca", null);
        return cursor;
    }
}
