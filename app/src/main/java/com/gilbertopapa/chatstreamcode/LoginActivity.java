package com.gilbertopapa.chatstreamcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gilbertopapa.chatstreamcode.config.ConfigurationFireBase;
import com.gilbertopapa.chatstreamcode.helper.Base64Custom;
import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.gilbertopapa.chatstreamcode.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtEmail, edtKey;
    private User user;
    private FirebaseAuth firebaseAuth;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLoginId);
        edtEmail = (EditText) findViewById(R.id.edtEmailLoginId);
        edtKey = (EditText) findViewById(R.id.edtKeyLoginId);
        tvRegister = (TextView) findViewById(R.id.tvRegisterUser);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new User();
                user.setEmail(edtEmail.getText().toString());
                user.setKey(edtKey.getText().toString());
                authLogin();

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoginRegis = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intentLoginRegis);
                finish();
            }
        });

    }


    private void authLogin() {

        firebaseAuth = ConfigurationFireBase.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getKey()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String identifyUserLogin = Base64Custom.codeBase64(user.getEmail());
                    Preferences preferences = new Preferences(LoginActivity.this);
                    preferences.saveUserPreferencesIdentify(identifyUserLogin);

                    goMainActivity();
                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso", Toast.LENGTH_LONG).show();

                } else {
                    /*
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
                    }*/

                    Toast.makeText(LoginActivity.this, "Erro ao efetuar login", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void  checkUserLogin(){
        firebaseAuth = ConfigurationFireBase.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser()!=null){
            goMainActivity();
        }
    }

    private void goMainActivity() {
        Intent intentLoginMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intentLoginMain);
        finish();
    }

}
