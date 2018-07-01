package com.programadoresperuanos.www.ejemploandroid_may2018_sqlite_basico;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    EditText et_codigo,et_nombres,et_dni,et_direccion;
    ImageButton imb_primero,imb_ultimo,imb_siguiente,imb_anterior;
    Button btn_nuevo,btn_modificar,btn_limpiar,btn_eliminar,btn_busqueda;

    SQLiteDatabase basedatos;
    Cursor RegistrosTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        et_codigo = findViewById(R.id.editText);
        et_nombres = findViewById(R.id.editText2);
        et_dni = findViewById(R.id.editText3);
        et_direccion = findViewById(R.id.editText4);
        btn_nuevo = findViewById(R.id.btn_nuevo);
        btn_modificar = findViewById(R.id.btn_modificar);
        btn_busqueda = findViewById(R.id.btn_busqueda);
        btn_eliminar = findViewById(R.id.btn_eliminar);
        btn_limpiar = findViewById(R.id.btn_limpiar);

        imb_primero = findViewById(R.id.imb_first);
        imb_ultimo = findViewById(R.id.imb_last);
        imb_siguiente = findViewById(R.id.imb_next);
        imb_anterior = findViewById(R.id.imb_prev);

        btn_nuevo.setOnClickListener(this);
        btn_modificar.setOnClickListener(this);
        btn_busqueda.setOnClickListener(this);
        btn_eliminar.setOnClickListener(this);
        btn_limpiar.setOnClickListener(this);
        imb_primero.setOnClickListener(this);
        imb_ultimo.setOnClickListener(this);
        imb_siguiente.setOnClickListener(this);
        imb_anterior.setOnClickListener(this);

        AsistenteSQLite administrador = new AsistenteSQLite(getApplicationContext(),"Personal",null,1);
        basedatos = administrador.getWritableDatabase();
        RegistrosTotal = basedatos.rawQuery("SELECT * FROM trabajadores",null);
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
            case R.id.opcion3:
                startActivity(new Intent(getApplicationContext(),Main3Activity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            // Nuevo Registro
            case R.id.btn_nuevo:
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
                    basedatos.insert("trabajadores",null,nuevoRegistro);
                    RegistrosTotal = basedatos.rawQuery("SELECT * FROM trabajadores",null);
                    Context c = getApplicationContext();
                    String m = "Los datos se ingresaron correctamente.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                    limpiarCampos();
                }
                break;
            // Busqueda de registros
            case R.id.btn_busqueda:
                String codigo_input = et_codigo.getText().toString();
                if(codigo_input.equals(""))
                {
                    Context c = getApplicationContext();
                    String m = "ERROR, El codigo esta vacio.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                else
                {
                    RegistrosTotal = basedatos.rawQuery("SELECT * FROM trabajadores",null);
                    boolean sw = true;
                    Cursor RegistrosVerificacion = basedatos.rawQuery("SELECT * FROM trabajadores WHERE codigo="+codigo_input,null);
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
            // Modificar registros
            case R.id.btn_modificar:
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
                    Cursor registroVerificacion = basedatos.rawQuery("SELECT * FROM trabajadores WHERE codigo="+codigo_input2,null);
                    if(registroVerificacion.moveToFirst())
                    {
                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("nombres",nombres_input2);
                        nuevoRegistro.put("dni",dni_input2);
                        nuevoRegistro.put("direccion",direccion_input2);
                        basedatos.update("trabajadores",nuevoRegistro,"codigo="+codigo_input2,null);
                        Context c = getApplicationContext();
                        String m = "Se actualizo el registro correctamente.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                        limpiarCampos();
                    }
                    else
                    {
                        Context c = getApplicationContext();
                        String m = "ERROR, El codigo no existe.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                    }
                }
                break;
            // Eliminar registros
            case R.id.btn_eliminar:
                String codigo_input3 =  et_codigo.getText().toString();
                if(codigo_input3.equals(""))
                {
                    Context c = getApplicationContext();
                    String m = "El codigo esta vacio.";
                    Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                }
                else
                {
                    Cursor registroVerificacion = basedatos.rawQuery("SELECT * FROM trabajadores WHERE codigo="+codigo_input3,null);
                    if(registroVerificacion.moveToFirst())
                    {
                        basedatos.delete("trabajadores","codigo="+codigo_input3,null);
                        RegistrosTotal = basedatos.rawQuery("SELECT * FROM trabajadores",null);
                        Context c = getApplicationContext();
                        String m = "El registro se elimino correctamente.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                        limpiarCampos();
                    }
                    else
                    {
                        Context c = getApplicationContext();
                        String m = "ERROR, El codigo no existe.";
                        Toast.makeText(c,m,Toast.LENGTH_LONG).show();
                    }
                }
                break;
            // Desplazar al primer registro
            case R.id.imb_first:

                RegistrosTotal = basedatos.rawQuery("SELECT * FROM trabajadores",null);
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
            // Desplazar al ultimo registro
            case R.id.imb_last:
                RegistrosTotal = basedatos.rawQuery("SELECT * FROM trabajadores",null);
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
            // Desplazar al registro anterior
            case R.id.imb_prev:
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
            // Desplazar al registro siguiente
            case R.id.imb_next:
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
            // Limpiar campos de texto
            case R.id.btn_limpiar:
                limpiarCampos();
                break;
            default:
                break;
        }
    }

    private void limpiarCampos() {
        et_codigo.setText("");
        et_nombres.setText("");
        et_dni.setText("");
        et_direccion.setText("");
    }
}
