package com.programadoresperuanos.www.ejemploandroid_may2018_sqlite_basico;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_codigo,et_nombres,et_dni,et_direccion;
    ImageButton ib_nuevo,ib_ver,ib_editar,ib_eliminar,ib_limpiar,ib_primero,ib_ultimo,ib_siguiente,ib_anterior;
    SQLiteDatabase basedatos;
    Cursor RegistrosTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_codigo = findViewById(R.id.editText);
        et_nombres = findViewById(R.id.editText2);
        et_dni = findViewById(R.id.editText3);
        et_direccion = findViewById(R.id.editText4);
        ib_nuevo = findViewById(R.id.imageButton);
        ib_ver = findViewById(R.id.imageButton2);
        ib_editar = findViewById(R.id.imageButton3);
        ib_eliminar = findViewById(R.id.imageButton4);
        ib_limpiar = findViewById(R.id.imageButton5);
        ib_primero = findViewById(R.id.imageButton6);
        ib_ultimo = findViewById(R.id.imageButton7);
        ib_siguiente = findViewById(R.id.imageButton8);
        ib_anterior = findViewById(R.id.imageButton9);
        ib_nuevo.setOnClickListener(this);
        ib_ver.setOnClickListener(this);
        ib_editar.setOnClickListener(this);
        ib_eliminar.setOnClickListener(this);
        ib_limpiar.setOnClickListener(this);
        ib_primero.setOnClickListener(this);
        ib_ultimo.setOnClickListener(this);
        ib_siguiente.setOnClickListener(this);
        ib_anterior.setOnClickListener(this);
        AsistenteSQLite administrador = new AsistenteSQLite(getApplicationContext(),"Educacion",null,1);
        basedatos = administrador.getWritableDatabase();
        RegistrosTotal = basedatos.rawQuery("SELECT * FROM alumnos",null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mimenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.opcion1:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            case R.id.opcion2:
                startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageButton:
                // Nuevo
                String nombres_input = et_nombres.getText().toString();
                String dni_input = et_dni.getText().toString();
                String direccion_input = et_direccion.getText().toString();
                if(et_nombres.equals("") || dni_input.equals("") || et_direccion.equals(""))
                {
                    Context c = getApplicationContext();
                    String m = "ERROR, Los datos vacios.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                else
                {
                    ContentValues nuevoRegistro = new ContentValues();
                    nuevoRegistro.put("nombres",nombres_input);
                    nuevoRegistro.put("dni",dni_input);
                    nuevoRegistro.put("direccion",direccion_input);
                    basedatos.insert("alumnos",null,nuevoRegistro);
                    RegistrosTotal = basedatos.rawQuery("SELECT * FROM alumnos",null);
                    Context c = getApplicationContext();
                    String m = "Los datos se ingresaron correctamente.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                    limpiar();
                }
                break;
            case R.id.imageButton2:
                // Ver
                String codigo_input = et_codigo.getText().toString();
                if(codigo_input.equals(""))
                {
                    Context c = getApplicationContext();
                    String m = "ERROR, El codigo esta vacio.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                else
                {
                    RegistrosTotal = basedatos.rawQuery("SELECT * FROM alumnos",null);
                    boolean sw = true;
                    Cursor RegistrosVerificacion = basedatos.rawQuery("SELECT * FROM alumnos WHERE codigo="+codigo_input,null);
                    if(RegistrosVerificacion.moveToFirst())
                    {
                        while (sw)
                        {
                            if(RegistrosTotal.moveToNext())
                            {
                                if(RegistrosTotal.getString(0).equals(codigo_input))
                                {
                                    sw = false;
                                }
                            }
                        }
                        et_nombres.setText(RegistrosTotal.getString(1));
                        et_dni.setText(RegistrosTotal.getString(2));
                        et_direccion.setText(RegistrosTotal.getString(3));
                    }
                    else
                    {
                        Context c = getApplicationContext();
                        String m = "ERROR, El codigo no existe.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.imageButton3:
                // Editar
                String codigo_input2 = et_codigo.getText().toString();
                String nombres_input2 = et_nombres.getText().toString();
                String dni_input2 = et_dni.getText().toString();
                String direccion_input2 = et_direccion.getText().toString();

                if(codigo_input2.equals("") || nombres_input2.equals("")
                        || dni_input2.equals("") || direccion_input2.equals(""))
                {
                    Context c = getApplicationContext();
                    String m = "ERROR, Hay campos vacios.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                else
                {
                    Cursor registroVerificacion = basedatos.rawQuery("SELECT * FROM alumnos WHERE codigo="+codigo_input2,null);
                    if(registroVerificacion.moveToFirst())
                    {
                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("nombres",nombres_input2);
                        nuevoRegistro.put("dni",dni_input2);
                        nuevoRegistro.put("direccion",direccion_input2);
                        basedatos.update("alumnos",nuevoRegistro,"codigo="+codigo_input2,null);
                        Context c = getApplicationContext();
                        String m = "Se actualizo el registro correctamente.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                        limpiar();
                    }
                    else
                    {
                        Context c = getApplicationContext();
                        String m = "ERROR, El codigo no existe.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.imageButton4:
                // Eliminar
                String codigo_input3 =  et_codigo.getText().toString();
                if(codigo_input3.equals(""))
                {
                    Context c = getApplicationContext();
                    String m = "El codigo esta vacio.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                else
                {
                    Cursor registroVerificacion = basedatos.rawQuery("SELECT * FROM alumnos WHERE codigo="+codigo_input3,null);
                    if(registroVerificacion.moveToFirst())
                    {
                        basedatos.delete("alumnos","codigo="+codigo_input3,null);
                        RegistrosTotal = basedatos.rawQuery("SELECT * FROM alumnos",null);
                        Context c = getApplicationContext();
                        String m = "El registro se elimino correctamente.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                        limpiar();
                    }
                    else
                    {
                        Context c = getApplicationContext();
                        String m = "ERROR, El codigo no existe.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.imageButton5:
                // Primero
                RegistrosTotal = basedatos.rawQuery("SELECT * FROM alumnos",null);
                if(RegistrosTotal.moveToFirst())
                {
                    et_codigo.setText(RegistrosTotal.getString(0));
                    et_nombres.setText(RegistrosTotal.getString(1));
                    et_dni.setText(RegistrosTotal.getString(2));
                    et_direccion.setText(RegistrosTotal.getString(3));
                }
                else
                {
                    Context c = getApplicationContext();
                    String m = "No hay registros.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageButton6:
                // Ultimo
                RegistrosTotal = basedatos.rawQuery("SELECT * FROM alumnos",null);
                if(RegistrosTotal.moveToLast())
                {
                    et_codigo.setText(RegistrosTotal.getString(0));
                    et_nombres.setText(RegistrosTotal.getString(1));
                    et_dni.setText(RegistrosTotal.getString(2));
                    et_direccion.setText(RegistrosTotal.getString(3));
                }
                else
                {
                    Context c = getApplicationContext();
                    String m = "No hay registros.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageButton7:
                // Anterior
                if(RegistrosTotal.moveToPrevious())
                {
                    et_codigo.setText(RegistrosTotal.getString(0));
                    et_nombres.setText(RegistrosTotal.getString(1));
                    et_dni.setText(RegistrosTotal.getString(2));
                    et_direccion.setText(RegistrosTotal.getString(3));
                }
                else
                {
                    Context c = getApplicationContext();
                    String m = "No hay registros anteriores.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageButton8:
                // Limpiar
                limpiar();
                break;
            case R.id.imageButton9:
                // Siguiente
                if(RegistrosTotal.moveToNext())
                {
                    et_codigo.setText(RegistrosTotal.getString(0));
                    et_nombres.setText(RegistrosTotal.getString(1));
                    et_dni.setText(RegistrosTotal.getString(2));
                    et_direccion.setText(RegistrosTotal.getString(3));
                }
                else
                {
                    Context c = getApplicationContext();
                    String m = "No hay registros siguientes.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void limpiar() {
        et_codigo.setText("");
        et_nombres.setText("");
        et_dni.setText("");
        et_direccion.setText("");
    }
}
