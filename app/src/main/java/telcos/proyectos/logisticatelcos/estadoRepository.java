package telcos.proyectos.logisticatelcos;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static telcos.proyectos.logisticatelcos.utilidades.ClienteWeb;

public class estadoRepository {

    private static estadoRepository repository = new estadoRepository();
    private static HashMap<String, Estados> estados = new HashMap<>();

    public static estadoRepository getInstance() {
        return repository;
    }

    public estadoRepository() {
    }

    public static void saveBodegas(Estados codes) {
        estados.put(codes.getIdEstado(),codes);
    }

    public ArrayList<String> getEstados() {
        ArrayList<String> respuestaFiltro = new ArrayList<>();

        for (Estados respuesta : estados.values()) {
            respuestaFiltro.add(respuesta.getDescEstado());
        }

        return respuestaFiltro;
    }

    public ArrayList<String> getRespuestas(String descEstado) {

        ArrayList<String> respuestaFiltro= new ArrayList<>();

        for (Estados respuesta: estados.values()){
            if (respuesta.getDescEstado()==descEstado){
                respuestaFiltro.add(respuesta.getIdEstado());
            }
        }

        return respuestaFiltro;
    }

    public static class ObtenerEstado extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String idestado;
            String descripcionestado;

            JSONObject respuestaJSON = ClienteWeb(cadena,null);
            try {
                int resultJSON = respuestaJSON.getInt("estado");
                if (resultJSON == 1) {
                    JSONArray estadoJSON = respuestaJSON.getJSONArray("descripcion");
                    for (int i = 0; i < estadoJSON.length(); i++) {
                        idestado = estadoJSON.getJSONObject(i).getString("ESTADOMATERIALCOD");
                        descripcionestado = estadoJSON.getJSONObject(i).getString("ESTADOMATERIALDSC");
                        saveBodegas(new Estados(idestado,descripcionestado));
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
