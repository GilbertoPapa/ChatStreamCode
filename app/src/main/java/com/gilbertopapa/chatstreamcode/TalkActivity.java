package com.gilbertopapa.chatstreamcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.gilbertopapa.chatstreamcode.adapter.MassageAdapter;
import com.gilbertopapa.chatstreamcode.config.ConfigurationFireBase;
import com.gilbertopapa.chatstreamcode.helper.Base64Custom;
import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.gilbertopapa.chatstreamcode.model.Message;
import com.gilbertopapa.chatstreamcode.model.Talk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TalkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String nameReceiver, emailReceiver, idReceiver;
    private EditText edtMsg;
    private ImageButton btnMsg;
    private String nameSender,idSender;
    private DatabaseReference databaseReference;
    private ListView listView;
    private ArrayList<Message> messageArray;
    private ArrayAdapter<Message> arrayAdapter;
    private ValueEventListener valueEventListenerMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        toolbar = (Toolbar) findViewById(R.id.toolbar_talk);
        edtMsg = (EditText) findViewById(R.id.edt_msg);
        btnMsg = (ImageButton) findViewById(R.id.btn_sendMsg);
        listView = (ListView) findViewById(R.id.lv_talks);

        Preferences preferences = new Preferences(TalkActivity.this);
        idSender = preferences.getIdentify();
        nameSender = preferences.getUserName();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nameReceiver = bundle.getString("name");
            emailReceiver = bundle.getString("email");
            idReceiver = Base64Custom.codeBase64(emailReceiver);

        }

        toolbar.setTitle(nameReceiver);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        messageArray = new ArrayList<>();

        arrayAdapter = new MassageAdapter(TalkActivity.this, messageArray);
        listView.setAdapter(arrayAdapter);

        databaseReference = ConfigurationFireBase.getFirebase()
                .child("msg")
                .child(idSender)
                .child(idReceiver);

        valueEventListenerMsg = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                messageArray.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    messageArray.add(message);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListenerMsg);

        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textMsg = edtMsg.getText().toString();

                if (textMsg.isEmpty()) {
                    Toast.makeText(TalkActivity.this, "Digite uma mensagem !", Toast.LENGTH_LONG).show();
                } else {

                    Message message = new Message();
                    message.setIdUserMsg(idSender);
                    message.setMsg(textMsg);

                    //remetente
                    Boolean aBooleanMessageSender = saveMsg(idSender, idReceiver, message);
                    if (!aBooleanMessageSender) {
                        Toast.makeText(TalkActivity.this, "Problema ao salvar sua mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                    } else {
                        //destinatário
                        Boolean aBooleanMessageReceiver = saveMsg(idReceiver, idSender, message);
                        if (!aBooleanMessageReceiver) {
                            Toast.makeText(TalkActivity.this, "Problema ao enviar sua mensagem !", Toast.LENGTH_LONG).show();
                        }
                    }

                    //remetente
                    Talk talk = new Talk();
                    talk.setIdUser(idReceiver);
                    talk.setName(nameReceiver);
                    talk.setMsg(textMsg);
                    Boolean aBooleanTalkSender = saveTalk(idSender, idReceiver, talk);
                    if (!aBooleanTalkSender) {
                        Toast.makeText(TalkActivity.this, "Problema ao salvar sua mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                    } else {
                        //destinatário
                        talk = new Talk();
                        talk.setIdUser(idSender);
                        talk.setName(nameSender);
                        talk.setMsg(textMsg);
                        Boolean aBooleanTalkReceiver = saveTalk(idReceiver, idSender, talk);
                        if (!aBooleanTalkReceiver) {
                            Toast.makeText(TalkActivity.this, "Problema ao enviar sua mensagem !", Toast.LENGTH_LONG).show();
                        }


                        edtMsg.setText("");
                    }
                }
            }
        });

    }


    private boolean saveTalk(String idSender, String idReceiver, Talk talk){

        try {

            databaseReference = ConfigurationFireBase.getFirebase().child("talks");
            databaseReference
                    .child(idSender)
                    .child(idReceiver)
                    .setValue(talk);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveMsg(String idSender, String idReceiver, Message message) {
        try {

            databaseReference = ConfigurationFireBase.getFirebase().child("msg");
            databaseReference
                    .child(idSender)
                    .child(idReceiver)
                    .push()
                    .setValue(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerMsg);
    }
}
