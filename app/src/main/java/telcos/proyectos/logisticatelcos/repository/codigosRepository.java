package telcos.proyectos.logisticatelcos.repository;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import telcos.proyectos.logisticatelcos.models.Codigos;

import static telcos.proyectos.logisticatelcos.connection.utilidades.ClienteWeb;

public class codigosRepository {

    private static codigosRepository repository = new codigosRepository();
    private static HashMap<String, Codigos> codis = new HashMap<>();

    public static codigosRepository getInstance() {
        return repository;
    }

    public codigosRepository() {
    }

    private static void saveCodigos(Codigos codes) {
        codis.put(codes.getmCod(),codes);
    }

    public List<Codigos> getCodigos() {
        return new ArrayList<>(codis.values());
    }

    public static class ObtenerMateriales extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String codigo;
            String descripcion;
            String serial;

            JSONObject respuestaJSON = ClienteWeb(cadena,null);
            try {

                int resultJSON = respuestaJSON.getInt("estado");
                if (resultJSON == 1) {
                    JSONArray codigoJSON = respuestaJSON.getJSONArray("materiales");
                    for (int i = 0; i < codigoJSON.length(); i++) {
                        codigo = codigoJSON.getJSONObject(i).getString("CODIGO");
                        descripcion = codigoJSON.getJSONObject(i).getString("DESCRIPCION");
                        serial = codigoJSON.getJSONObject(i).getString("REQSERIAL");

                        if(serial.equals("1")) {
                            saveCodigos(new Codigos(codigo,descripcion,"",null,1));
                        }else {
                            saveCodigos(new Codigos(codigo,descripcion,"",null,0));
                        }
                    }
                }

            } catch (
                    JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
