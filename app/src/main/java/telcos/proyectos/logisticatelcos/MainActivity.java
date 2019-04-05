package telcos.proyectos.logisticatelcos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static telcos.proyectos.logisticatelcos.config.GET_ESTADO;
import static telcos.proyectos.logisticatelcos.config.GET_MATERIALES;
import static telcos.proyectos.logisticatelcos.config.GET_NODO;
import static telcos.proyectos.logisticatelcos.config.GET_PREINVENTARIO;
import static telcos.proyectos.logisticatelcos.utilidades.ClienteWeb;

public class MainActivity extends AppCompatActivity {

    public static EditText codigoinv;
    public Button consulta;
    public Button digitar;
    public static Spinner spEstado;
    public static AutoCompleteTextView spBodega;

    public estadoRepository.ObtenerEstado hiloconexion;
    public bodegasRepository.ObtenerBodegas hiloconexion2;
    public ObtenerInventario hiloconexion3;

    public static Object nameEstado;
    public static Object nameBodega;
    String estadoItem = "";
    String bodegaItem = "";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codigoinv = (EditText) findViewById(R.id.editTextCodigoInv);
        consulta = (Button) findViewById(R.id.buttonConsultInve);
        digitar = (Button) findViewById(R.id.buttonInventario);
        spEstado = (Spinner) findViewById(R.id.spinnerEstadoMat);
        spBodega = (AutoCompleteTextView) findViewById(R.id.spinnerBodega);

        progressDialog = new ProgressDialog(MainActivity.this);


        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id) {


                //String m = a.getIdEstado();
                //Log.v("Estado ",m);
                //int p = position;
                nameEstado = parent.getItemAtPosition(position);
                estadoItem = Objects.toString(id + 1,null);

                //Log.v("Estado ",nameEstado.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spBodega.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,View view,int position,long rowId) {
                nameBodega = (String) parent.getItemAtPosition(position);
            }
        });
        /*spBodega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id) {

                nameBodega = parent.getItemAtPosition(position);
                *//*Toast.makeText(getActivity(), nameBodega.toString(),
                        Toast.LENGTH_SHORT).show();*//*

                bodegaItem = Objects.toString(id + 1,null);
            }

                @Override
                public void onNothingSelected (AdapterView < ? > parent){
                }
            });*/

        consulta.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){
                hiloconexion3 = new ObtenerInventario();
                String cadenallamada4 = GET_PREINVENTARIO + "?codigo=" + codigoinv.getText().toString();
                hiloconexion3.execute(cadenallamada4,"1");
            }
            });

        digitar.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){

                Intent i = new Intent(MainActivity.this,searchMaterial.class);
                i.putExtra("DescEstado",nameEstado.toString());
                i.putExtra("DescBodega",nameBodega.toString());
                startActivity(i);
            }
            });


        }

        public class ObtenerInventario extends AsyncTask<String, Void, String> {
            @Override
            protected void onPostExecute(String s) {
                //super.onPostExecute(s);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                if (s.equals("No se obtuvo registro")) {
                    alertDialog.setTitle("Alerta!");
                    alertDialog.setMessage(s);
                    alertDialog.show();
                } else if (s.equals("1")) {

                    progressDialog.setTitle("Consultando");
                    progressDialog.setMessage("Cargando... Espere un momento");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    hiloconexion = new estadoRepository.ObtenerEstado();
                    String cadenallamada = GET_ESTADO + "?codigo=" + codigoinv.getText().toString();
                    hiloconexion.execute(cadenallamada,"1");

                    hiloconexion2 = new bodegasRepository.ObtenerBodegas();
                    String cadenallamada2 = GET_NODO + "?codigo=" + codigoinv.getText().toString();
                    hiloconexion2.execute(cadenallamada2,"2");

                    codigosRepository.ObtenerMateriales hiloconexion3 = new codigosRepository.ObtenerMateriales();
                    String cadenallamada3 = GET_MATERIALES + "?codigo=" + codigoinv.getText().toString();
                    hiloconexion3.execute(cadenallamada3);

                    bodegasAdapter listAdapter = new bodegasAdapter(MainActivity.this,
                            bodegasRepository.getInstance().getRespuestas());
                    listAdapter.setDropDownViewResource(R.layout.myspinner);
                    populateSpinner(spBodega,listAdapter);

                    estadoAdapter listAdapter2 = new estadoAdapter(MainActivity.this,
                            estadoRepository.getInstance().getEstados());
                    listAdapter2.setDropDownViewResource(R.layout.myspinner);
                    populateSpinner2(spEstado,listAdapter2);

                    progressDialog.dismiss();

                } else if (s.equals("0")) {
                    s = "El inventario no se encuentra activo";
                    alertDialog.setTitle("Alerta!");
                    alertDialog.setMessage(s);
                    alertDialog.show();
                } else if (s.equals("Se necesita un identificador")) {
                    alertDialog.setTitle("Alerta!");
                    alertDialog.setMessage(s);
                    alertDialog.show();
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

                if (params[1] == "1") {
                    try {
                        JSONObject respuestaJSON = ClienteWeb(cadena,null);


                        int resultJSON = respuestaJSON.getInt("estado");
                        if (resultJSON == 1) {
                            JSONArray estadoJSON = respuestaJSON.getJSONArray("descripcion");
                            for (int i = 0; i < estadoJSON.length(); i++) {
                                muestra = estadoJSON.getJSONObject(i).getString("PREPINVENTESTADO");
                            }
                        } else if (resultJSON == 2) {
                            muestra = "No se obtuvo registro";
                        } else if (resultJSON == 3) {
                            muestra = "Se necesita un identificador";
                        }

                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }
                }
                return muestra;
            }
        }

        private void populateSpinner (AutoCompleteTextView spinner,bodegasAdapter arrayList){
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

        private void populateSpinner2 (Spinner spinner,estadoAdapter arrayList){
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
