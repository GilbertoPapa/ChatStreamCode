package com.gilbertopapa.chatstreamcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

public class AuthenticatorActivity extends AppCompatActivity {

    private EditText codeAutentic;
    private Button btnAutentic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        codeAutentic = (EditText)findViewById(R.id.edtCodeAthId);
        btnAutentic = (Button)findViewById(R.id.btnAthId);

        SimpleMaskFormatter simpleMaskFormatterPhone = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskCode = new MaskTextWatcher(codeAutentic, simpleMaskFormatterPhone);
        codeAutentic.addTextChangedListener(maskCode);

        btnAutentic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences preferences = new Preferences(AuthenticatorActivity.this);
                HashMap<String,String> user = preferences.getDataUser();

                String tokenServer = user.get("token");
                String tokenInsert = codeAutentic.getText().toString();

                if (tokenInsert.equals(tokenServer)){

                    Toast.makeText(AuthenticatorActivity.this,"Código validado",Toast.LENGTH_LONG).show();
                    Intent intentAuthMain = new Intent(AuthenticatorActivity.this,MainActivity.class);
                    startActivity(intentAuthMain);
                }else{
                    Toast.makeText(AuthenticatorActivity.this,"Código não validado ",Toast.LENGTH_LONG).show();
                }


            }
        });

    }
}
