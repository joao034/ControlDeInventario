package com.example.controlinventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView titulo;
    private Spinner spnProcesos;
    private RequestQueue queue, queue1;
    private List<ProcesoValidacion> listaProcesos;
    private List<ProcesoValidacionDetalle> listaDetallesProceso;
    private ArrayAdapter <ProcesoValidacion> adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titulo = (TextView)findViewById(R.id.titulo);
        spnProcesos = (Spinner)findViewById(R.id.procesos);
        recyclerView = findViewById(R.id.res_detalle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cargarProcesos("http://192.168.100.123/servicios/cargarProcesos.php");

    }

    public void cargarProcesos(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Cargando", Toast.LENGTH_SHORT).show();
                ProcesoValidacionDAO dao = new ProcesoValidacionDAO();
                //lleno la listaProcesos
                listaProcesos = dao.listarProcesos(response);
                //Crea el adapter pasandole la lista de procesos
                adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, listaProcesos);
                spnProcesos.setAdapter(adapter);

                spnProcesos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cargarDetalleProceso("http://192.168.100.123/servicios/cargarDetalleProceso.php?titulo=" + parent.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void cargarDetalleProceso(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DetalleValidacionDAO daoDetalle = new DetalleValidacionDAO();
                //lleno la listaProcesos
                listaDetallesProceso = daoDetalle.listarProcesos(response);
                //Crea el adapter pasandole la lista de procesos
                AdapterDatos adapterDatos = new AdapterDatos(MainActivity.this, listaDetallesProceso);
                recyclerView.setAdapter(adapterDatos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue1 = Volley.newRequestQueue(this);
        queue1.add(stringRequest);
    }

}