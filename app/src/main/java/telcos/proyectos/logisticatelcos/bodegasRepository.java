package telcos.proyectos.logisticatelcos;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static telcos.proyectos.logisticatelcos.utilidades.ClienteWeb;

public class bodegasRepository {

    private static bodegasRepository repository = new bodegasRepository();
    private static HashMap<String, Bodegas> bodegas = new HashMap<>();

    public static bodegasRepository getInstance() {
        return repository;
    }

    public bodegasRepository() {
    }

    public static void saveBodegas(Bodegas codes) {
        bodegas.put(codes.getIdbodega(),codes);
    }

    public ArrayList<String> getRespuestas() {

        ArrayList<String> respuestaFiltro = new ArrayList<>();

        for (Bodegas respuesta : bodegas.values()) {
            respuestaFiltro.add(respuesta.getDescbodega());
        }

        return respuestaFiltro;
    }

    public static class ObtenerBodegas extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String idbodega;
            String descripcionbodega;

            JSONObject respuestaJSON = ClienteWeb(cadena,null);
            try {
                int resultJSON = respuestaJSON.getInt("estado");
                if (resultJSON == 1) {
                    JSONArray nombreJSON = respuestaJSON.getJSONArray("descripcion");
                    for (int i = 0; i < nombreJSON.length(); i++) {
                        idbodega = nombreJSON.getJSONObject(i).getString("Name");
                        descripcionbodega = nombreJSON.getJSONObject(i).getString("Caption");
                        saveBodegas(new Bodegas(idbodega,descripcionbodega));
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
