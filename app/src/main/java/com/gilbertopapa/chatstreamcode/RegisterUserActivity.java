package com.gilbertopapa.chatstreamcode;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gilbertopapa.chatstreamcode.config.ConfigurationFireBase;
import com.gilbertopapa.chatstreamcode.helper.Base64Custom;
import com.gilbertopapa.chatstreamcode.helper.Permission;
import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.gilbertopapa.chatstreamcode.model.User;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Random;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText name, phone, country, locale, email;
    private Button btnRegister;
    private String[] permissions = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    private User user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Permission.checkPermission(1, this, permissions);

        name = (EditText) findViewById(R.id.edtNameId);

        phone = (EditText) findViewById(R.id.edtPhoneId);
        SimpleMaskFormatter simpleMaskFormatterPhone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskPhone = new MaskTextWatcher(phone, simpleMaskFormatterPhone);
        phone.addTextChangedListener(maskPhone);

        locale = (EditText) findViewById(R.id.edtLocaleId);
        SimpleMaskFormatter simpleMaskFormatterLocale = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskLocale = new MaskTextWatcher(locale, simpleMaskFormatterLocale);
        locale.addTextChangedListener(maskLocale);

        country = (EditText) findViewById(R.id.edtCountryId);
        SimpleMaskFormatter simpleMaskFormatterCountry = new SimpleMaskFormatter("+NN");
        final MaskTextWatcher maskCountry = new MaskTextWatcher(country, simpleMaskFormatterCountry);
        country.addTextChangedListener(maskCountry);

        email = (EditText) findViewById(R.id.edtEmailLoginId);

        btnRegister = (Button) findViewById(R.id.btnRegisterId);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameUser, phoneUser, phoneMask, tokenUser, msgSendSMS;


                nameUser = name.getText().toString();
                phoneUser =
                        country.getText().toString()
                                + locale.getText().toString()
                                + phone.getText().toString();

                phoneMask = phoneUser = phoneUser.replace("+", "");
                phoneMask = phoneMask.replace("-", "");

                Random random = new Random();
                int numberRandom = random.nextInt(9999 - 1000) + 1000;
                tokenUser = String.valueOf(numberRandom);

                msgSendSMS = "Código de confirmação: " + tokenUser + ",este código também é sua senha.";

                Preferences preferences = new Preferences(RegisterUserActivity.this);
                preferences.saveUserPreferences(nameUser, phoneUser, tokenUser);

                //teste phone
                phoneMask = "5515988307378";
                boolean CheckSMS = sendSMS("+" + phoneMask, msgSendSMS);


                if (CheckSMS) {
                    Intent intentRegisAuth = new Intent(RegisterUserActivity.this, AuthenticatorActivity.class);
                    startActivity(intentRegisAuth);
                    finish();

                } else {

                    Toast.makeText(RegisterUserActivity.this, "Problemas ao enviar o SMS ", Toast.LENGTH_LONG).show();
                }
                //HashMap<String,String> user = preferences.getDataUser();
                // Log.i("TOKEN", "T:" +user.get("token"));

            }
        });

    }

    private void registerUserFinal() {

        firebaseAuth = ConfigurationFireBase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getKey()).addOnCompleteListener(RegisterUserActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterUserActivity.this, "Usuario cadastrado com sucesso !", Toast.LENGTH_LONG).show();

                    String identifyUser = Base64Custom.codeBase64(user.getEmail());
                    user.setId(identifyUser);
                    user.save();


                    Preferences preferences = new Preferences(RegisterUserActivity.this);
                    preferences.saveUserPreferencesIdentify(identifyUser);

                    userLogin();

                } else {
                    String erroException = "";
                    try {

                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        e.printStackTrace();
                        erroException = "Digite uma senha mais forte, que contenha letras e numeros";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroException = "O email digitado é invalido !";
                        e.printStackTrace();

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroException = "O email já pertence a um usuario";
                        e.printStackTrace();

                    } catch (Exception e) {
                        erroException = " Problemas ao cadastrar usuario ";
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterUserActivity.this, "Erro" + erroException, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean sendSMS(String phone, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, msg, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                alertPermission();
            }
        }
    }

    private void alertPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilização do app, aceitar as permissões");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void userLogin(){
        Intent intentUserLogin = new Intent(RegisterUserActivity.this,LoginActivity.class);
        startActivity(intentUserLogin);
        finish();
    }

}
