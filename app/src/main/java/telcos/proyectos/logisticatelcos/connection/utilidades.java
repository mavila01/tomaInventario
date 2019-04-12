package telcos.proyectos.logisticatelcos.connection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class utilidades {

    public static JSONObject ClienteWeb(String direccionURL,JSONObject jsonParam) {

        URL url = null;
        String devuelve = "";

        try {
            HttpURLConnection urlConn;

            DataOutputStream printout;
            DataInputStream input;
            url = new URL(direccionURL);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(5*1000);
            if (jsonParam != null) {
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type","application/json");
                urlConn.setRequestProperty("Accept","application/json");
                urlConn.connect();
                //Envio de parametros
                OutputStream os = urlConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os,"UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();
            }else {
                urlConn.setRequestProperty("User-Agent","Mozilla/5.0" +
                        "(Linux; Android 1.5; es-ES) Ejemplo HTTP");
            }

            int respuesta = urlConn.getResponseCode();

            StringBuilder result = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }


                JSONObject respuestaJSON = new JSONObject(result.toString());

                return respuestaJSON;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
