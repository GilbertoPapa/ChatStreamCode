package com.gilbertopapa.chatstreamcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.gilbertopapa.chatstreamcode.adapter.TabAdapter;
import com.gilbertopapa.chatstreamcode.config.ConfigurationFireBase;
import com.gilbertopapa.chatstreamcode.helper.Base64Custom;
import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.gilbertopapa.chatstreamcode.helper.SlidingTabLayout;
import com.gilbertopapa.chatstreamcode.model.Contact;
import com.gilbertopapa.chatstreamcode.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String idenfyContact;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = ConfigurationFireBase.getFirebaseAuth();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);


        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slt_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_page);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_exit:
                //logoutUser();
                return true;
            case R.id.item_config:
                return true;
            case R.id.item_add:
                openRegisterContact();
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void openRegisterContact() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailContact = editText.getText().toString();
                if (emailContact.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Preencha o e-mail", Toast.LENGTH_LONG).show();
                } else {
                    idenfyContact = Base64Custom.codeBase64(emailContact);
                    databaseReference = ConfigurationFireBase.getFirebase().child("Users").child(idenfyContact);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {

                                User userContact = dataSnapshot.getValue(User.class);

                                Preferences preferences = new Preferences(MainActivity.this);
                                String identifyUserLogin = preferences.getIdentify();

                                databaseReference = ConfigurationFireBase.getFirebase().child("Contacts")
                                        .child(identifyUserLogin)
                                        .child(idenfyContact);

                                Contact contact = new Contact();
                                contact.setIdentfyContact(idenfyContact);
                                contact.setEmail(userContact.getEmail());
                                contact.setName(userContact.getName());
                                databaseReference.setValue(contact);

                            } else {
                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intentLogout);
        finish();
    }


}
