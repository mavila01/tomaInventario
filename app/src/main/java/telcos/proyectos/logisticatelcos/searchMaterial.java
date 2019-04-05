package telcos.proyectos.logisticatelcos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static telcos.proyectos.logisticatelcos.MainActivity.codigoinv;
import static telcos.proyectos.logisticatelcos.config.INSERT_INVENT;
import static telcos.proyectos.logisticatelcos.utilidades.ClienteWeb;

public class searchMaterial extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private ListView mListView;
    private EditText eBodega;
    ProgressDialog progressDialog;

    FloatingActionButton fab;

    String descEstado;
    String descBodega;

    ObtenerWebService hiloconexion;

    private static HashMap<String, Estados> estados = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_material);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        eBodega = (EditText) findViewById(R.id.editTextBodega);

        fab = (FloatingActionButton) findViewById(R.id.my_fab);

        progressDialog = new ProgressDialog(searchMaterial.this);

        MaterialAdapter mListAdapter = new MaterialAdapter(searchMaterial.this,
                codigosRepository.getInstance().getCodigos());

        mListView.setAdapter(mListAdapter);

        mListView.setTextFilterEnabled(true);
        setupSearchView();
        setListViewHeightBasedOnChildren(mListView);

        descEstado = getIntent().getExtras().getString("DescEstado");
        descBodega = getIntent().getExtras().getString("DescBodega");

        eBodega.setText((CharSequence) descBodega);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(searchMaterial.this)
                        .setTitle("Confirmación")
                        .setMessage("¿Seguro desea guardar el inventario? ")
                        .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                progressDialog.setTitle("Guardando inventario");
                                progressDialog.setMessage("Cargando... Espere un momento");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                int count = mListView.getAdapter().getCount();
                                for (int i = 0; i < count; i++) {
                                    Codigos a = (Codigos) mListView.getItemAtPosition(i);

                                    if (!a.getmCant().equals("")) {

                                        hiloconexion = new ObtenerWebService();
                                        hiloconexion.execute(INSERT_INVENT,"1",
                                                codigoinv.getText().toString(),
                                                descBodega,
                                                descBodega, //descripcion bodega
                                                a.getmCod(),
                                                a.getmDesc(),
                                                a.getmSerial(),
                                                String.valueOf(a.getmCant()),
                                                descEstado,
                                                descEstado,//Id estado
                                                "Usuario de prueba"
                                        );
                                    }
                                }
                            }

                        }).setNegativeButton("CANCELAR",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).show();

            }
        });
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i,view,listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth,View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //Busqueda
    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Buscar");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }

    public class ObtenerWebService extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {

            Toast to = Toast.makeText(searchMaterial.this,s,Toast.LENGTH_LONG);
            to.show();
            progressDialog.dismiss();
            //super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            String devuelve = "";
            if (params[1] == "1") {  //Insert

                try {

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("CODIGO_INVENTARIO",params[2]);
                    jsonParam.put("NAME_BODEGA",params[3]);
                    jsonParam.put("DESCRIPCION_BODEGA",params[4]);
                    jsonParam.put("CODIGO_MATERIAL",params[5]);
                    jsonParam.put("DESCRIPCION_MATERIAL",params[6]);
                    jsonParam.put("SERIAL",params[7]);
                    jsonParam.put("CANTIDAD",params[8]);
                    jsonParam.put("ESTADO_MATERIAL",params[9]);
                    jsonParam.put("ID_ESTADO_MATERIAL",params[10]);
                    jsonParam.put("USUARIO",params[11]);

                    JSONObject respuestaJSON = ClienteWeb(cadena,jsonParam);

                    int resultJSON = respuestaJSON.getInt("estado");


                    if (resultJSON == 1) {
                        devuelve = "Inventario registrado correctamente"; //Registrado Correctamente
                    } else {
                        devuelve = "Conexion fallida"; //Conexion Fallida
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return devuelve;

            }

            return null;
        }
    }
}
