package telcos.proyectos.logisticatelcos.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import telcos.proyectos.logisticatelcos.R;
import telcos.proyectos.logisticatelcos.adapters.BodegasAdapter;
import telcos.proyectos.logisticatelcos.adapters.EstadoAdapter;
import telcos.proyectos.logisticatelcos.repository.bodegasRepository;
import telcos.proyectos.logisticatelcos.repository.codigosRepository;
import telcos.proyectos.logisticatelcos.repository.estadoRepository;

import static telcos.proyectos.logisticatelcos.connection.config.GET_ESTADO;
import static telcos.proyectos.logisticatelcos.connection.config.GET_MATERIALES;
import static telcos.proyectos.logisticatelcos.connection.config.GET_NODO;
import static telcos.proyectos.logisticatelcos.connection.config.GET_PREINVENTARIO;
import static telcos.proyectos.logisticatelcos.connection.utilidades.ClienteWeb;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    public static EditText codigoinv;
    public Button consulta;
    public Button digitar;
    public ImageButton limpiarBod;
    public static Spinner spEstado;
    public static AutoCompleteTextView acBodega;

    public estadoRepository.ObtenerEstado hiloconexion;
    public bodegasRepository.ObtenerBodegas hiloconexion2;
    public ObtenerInventario hiloconexion3;

    BodegasAdapter listAdapter;
    EstadoAdapter listAdapter2;

    public static Object nameEstado;
    public static Object nameBodega;
    String estadoItem = "";
    String bodegaItem = "";
    String idEstado = "";
    public int validacion = 0;
    int p;

    public static final int OKRESULT = 1;
    public static final int ERRORRESULT = 2;
    public static final int NOIDRESULT = 3;

    ProgressDialog progressDialog;
    AlertDialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setIcon(R.mipmap.ic_telcos);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefs = getSharedPreferences("Preferences",Context.MODE_PRIVATE);
        bindUI();

        acBodega.setHint("Buscar bodega");
        acBodega.setThreshold(1);

        acBodega.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean b) {
                if (b) {
                    acBodega.showDropDown();
                }
            }
        });

        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id) {


                //String m = a.getIdEstado();
                //Log.v("Estado ",m);
                p = position;

                nameEstado = parent.getItemAtPosition(position);
                estadoItem = Objects.toString(id + 1,null);

                //Log.v("Estado ",nameEstado.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        acBodega.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,View view,int position,long rowId) {
                nameBodega = (String) parent.getItemAtPosition(position);
            }
        });

    }

    private void bindUI() {
        codigoinv = (EditText) findViewById(R.id.editTextCodigoInv);
        consulta = (Button) findViewById(R.id.buttonConsultInve);
        digitar = (Button) findViewById(R.id.buttonInventario);
        limpiarBod = (ImageButton) findViewById(R.id.btLimpiarBod);
        spEstado = (Spinner) findViewById(R.id.spinnerEstadoMat);
        acBodega = (AutoCompleteTextView) findViewById(R.id.spinnerBodega);
        progressDialog = new ProgressDialog(MainActivity.this);
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        consulta.setOnClickListener(this);
        digitar.setOnClickListener(this);
        limpiarBod.setOnClickListener(this);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cerrar:
                logOut();
                return true;
            case R.id.menu_olvidar:
                removeSharedPreferences();
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void removeSharedPreferences() {
        prefs.edit().clear().apply();
    }

    public void enviarValidadorCod() {
        boolean cancel = false;
        View focusView = null;

        String scodigoinv = codigoinv.getText().toString();
        if (TextUtils.isEmpty(scodigoinv)) {
            codigoinv.setError(getString(R.string.error_field_required));
            focusView = codigoinv;
            cancel = true;
        }
        if (cancel) {
            progressDialog.dismiss();
            focusView.requestFocus();
        } else {
            hiloconexion3 = new ObtenerInventario();
            String cadenallamada4 = GET_PREINVENTARIO + "?codigo=" + scodigoinv;
            hiloconexion3.execute(cadenallamada4,"1");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonConsultInve:
                progressDialog.setTitle("Consultando");
                progressDialog.setMessage("Cargando... Espere un momento");
                progressDialog.setCancelable(false);
                progressDialog.show();

                enviarValidadorCod();
                break;
            case R.id.buttonInventario:
                if (validacion == 1) {

                    Intent i = new Intent(MainActivity.this,SearchActivity.class);
                    i.putExtra("DescEstado",nameEstado.toString());
                    i.putExtra("DescBodega",nameBodega.toString());
                    startActivity(i);
                } else {
                    alertDialog.setTitle("Advertencia!");
                    alertDialog.setMessage("No existe información");
                    alertDialog.show();
                }
                break;
            case R.id.btLimpiarBod:
                acBodega.setText("");
                break;

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ObtenerInventario extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if (!(s.equals("null"))) {
                switch (s) {
                    case "No se obtuvo registro":
                        progressDialog.dismiss();
                        alertas("Alerta!",s);
                        break;
                    case "1":
                        consultas();
                        validacion = 1;
                        break;
                    case "0":
                        s = "El inventario no se encuentra activo";
                        progressDialog.dismiss();
                        alertas("Alerta!",s);
                        break;
                    case "Se necesita un identificador":
                        progressDialog.dismiss();
                        alertas("Advertencia!",s);
                        break;
                    case "Conexion fallida":
                        progressDialog.dismiss();
                        alertas("Error!",s);
                        break;
                }
            } else {
                progressDialog.dismiss();
                alertas("Alerta!","No se obtuvo registro");
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String muestra = null;

            if (params[1].equals("1")) {
                try {
                    JSONObject respuestaJSON = ClienteWeb(cadena,null);
                    if (respuestaJSON != null) {

                        int resultJSON = respuestaJSON.getInt("estado");
                        switch (resultJSON) {
                            case OKRESULT:
                                JSONArray estadoJSON = respuestaJSON.getJSONArray("descripcion");

                                for (int i = 0; i < estadoJSON.length(); i++) {
                                    muestra = estadoJSON.getJSONObject(i).getString("PREPINVENTESTADO");
                                }
                                if (muestra == null) {
                                    muestra = "No se obtuvo registro";
                                }
                                break;
                            case ERRORRESULT:
                                muestra = "No se obtuvo registro";
                                break;
                            case NOIDRESULT:
                                muestra = "Se necesita un identificador";
                                break;

                            default:
                                break;
                        }
                    }
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
            return muestra;
        }
    }

    public void alertas(String msg,String s) {
        alertDialog.setTitle(msg);
        alertDialog.setMessage(s);
        alertDialog.show();
    }

    public void consultas() {

        hiloconexion = new estadoRepository.ObtenerEstado();
        String cadenallamada = GET_ESTADO + "?codigo=" + codigoinv.getText().toString();
        hiloconexion.execute(cadenallamada,"1");

        hiloconexion2 = new bodegasRepository.ObtenerBodegas();
        String cadenallamada2 = GET_NODO + "?codigo=" + codigoinv.getText().toString();
        hiloconexion2.execute(cadenallamada2,"2");

        codigosRepository.ObtenerMateriales hiloconexion3 = new codigosRepository.ObtenerMateriales();
        String cadenallamada3 = GET_MATERIALES + "?codigo=" + codigoinv.getText().toString();
        hiloconexion3.execute(cadenallamada3);

        listAdapter = new BodegasAdapter(MainActivity.this,
                bodegasRepository.getInstance().getRespuestas());
        listAdapter.setDropDownViewResource(R.layout.myspinner);
        populateSpinner(acBodega,listAdapter);

        listAdapter2 = new EstadoAdapter(MainActivity.this,
                estadoRepository.getInstance().getEstados());
        listAdapter2.setDropDownViewResource(R.layout.myspinner);
        populateSpinner2(spEstado,listAdapter2);

        progressDialog.dismiss();

        int a = listAdapter.original.size();
        if (!(a == 0)) {
            acBodega.showDropDown();
        }


    }

    private void populateSpinner(AutoCompleteTextView spinner,BodegasAdapter arrayList) {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < arrayList.getCount(); i++) {
            lables.add((String) arrayList.getItem(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,lables);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(spinnerAdapter);

    }

    private void populateSpinner2(Spinner spinner,EstadoAdapter arrayList) {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < arrayList.getCount(); i++) {
            lables.add((String) arrayList.getItem(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,lables);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(spinnerAdapter);

    }

}
