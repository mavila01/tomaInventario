package telcos.proyectos.logisticatelcos.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import telcos.proyectos.logisticatelcos.R;

import static telcos.proyectos.logisticatelcos.connection.config.GET_USER;
import static telcos.proyectos.logisticatelcos.connection.utilidades.ClienteWeb;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private EditText editTextUser;
    private EditText editTextPass;
    private Button btnIngresar;

    private View mLoginFormView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("Preferences",Context.MODE_PRIVATE);
        bindUI();
        setCredentialsIfExist();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editTextUser.getText().toString();
                String pass = editTextPass.getText().toString();

                login(user,pass);
            }
        });
    }

    private void bindUI() {
        editTextUser = (EditText) findViewById(R.id.editTextUser);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        btnIngresar = (Button) findViewById(R.id.buttonIngresar);
        mLoginFormView = findViewById(R.id.etUser);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void setCredentialsIfExist() {
        String user = getUserPrefs();

        if (!TextUtils.isEmpty(user)) {
            editTextUser.setText(user);
        }
    }

    private void login(String user,String pass) {
        editTextUser.setError(null);
        editTextPass.setError(null);


        boolean cancel = false;
        View focusView = null;

        if (!isValidUser(user)) {
            editTextUser.setError("Este campo es requerido");
            focusView = editTextUser;
            cancel = true;
        } else if (!isValidPass(pass)) {
            editTextPass.setError("Este campo es requerido");
            focusView = editTextPass;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            LoginTask mAuthTask = new LoginTask();
            mAuthTask.execute(GET_USER,"1",
                    user,
                    pass);

        }
    }


    public class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            if (s == "Usuario Existe") {
                goToMain();
                showProgress(false);
                saveOnPreferences(editTextUser.getText().toString());
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else if (s == "Usuario No existe") {
                Toast to = Toast.makeText(getApplicationContext(),"Usuario o clave incorrecto",Toast.LENGTH_LONG);
                to.show();
                showProgress(false);
            } else {
                Toast to = Toast.makeText(getApplicationContext(),"Verificar conexion",Toast.LENGTH_LONG);
                to.show();
                showProgress(false);
            }


        }

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;
            String devuelve = "";
            if (params[1] == "1") {  //Ingresar Login
                try {

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("usuario",params[2]);
                    jsonParam.put("pass",params[3]);

                    JSONObject respuestaJSON = ClienteWeb(cadena,jsonParam);

                    int resultJSON = respuestaJSON.getInt("estado");

                    if (resultJSON == 1) {
                        devuelve = "Usuario Existe";
                    } else if (resultJSON == 2) {
                        devuelve = "Usuario No existe";
                    } else {
                        devuelve = "Error inesperado";
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return devuelve;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void saveOnPreferences(String user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        editor.putString("user",user);
        editor.apply();
    }

    private boolean isValidUser(String user) {
        return !TextUtils.isEmpty(user);
    }

    private boolean isValidPass(String pass) {
        return !TextUtils.isEmpty(pass);
    }

    private void goToMain() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String getUserPrefs() {
        return prefs.getString("user","");
    }

}
