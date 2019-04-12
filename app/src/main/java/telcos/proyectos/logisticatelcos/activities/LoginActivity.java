package telcos.proyectos.logisticatelcos.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import telcos.proyectos.logisticatelcos.R;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private EditText editTextUser;
    private EditText editTextPass;
    private Switch switchRecordar;
    private Button btnIngresar;

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
                if (login(user,pass)) {
                    goToMain();
                    saveOnPreferences(user,pass);
                }
            }
        });
    }

    private void bindUI() {
        editTextUser = (EditText) findViewById(R.id.editTextUser);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        switchRecordar = (Switch) findViewById(R.id.switchRecordar);
        btnIngresar = (Button) findViewById(R.id.buttonIngresar);
    }

    private void setCredentialsIfExist() {
        String user = getUserPrefs();

        if (!TextUtils.isEmpty(user)){
            editTextUser.setText(user);
        }
    }

    private boolean login(String user,String pass) {
        if (!isValidUser(user)) {
            Toast.makeText(this,"Usuario incorrecto, por favor intente de nuevo",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidPass(pass)) {
            Toast.makeText(this,"Contraseña incorrecta, por favor intente de nuevo",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void saveOnPreferences(String user,String pass) {
        if (switchRecordar.isChecked()) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user",user);
            editor.apply();
        }
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
