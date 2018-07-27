package com.gilbertopapa.chatstreamcode.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gilbertopapa.chatstreamcode.config.ConfigurationFireBase;
import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.gilbertopapa.chatstreamcode.model.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.gilbertopapa.chatstreamcode.R;
import java.util.ArrayList;


import whatsapp.cursoandroid.com.whatsapp.activity.ConversaActivity;
import whatsapp.cursoandroid.com.whatsapp.adapter.ConversaAdapter;
import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalksFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Contact> adapter;
    private ArrayList<Contact> conversas;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversas;

    public TalksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_talk, container, false);

        // Monta listview e adapter
        conversas = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_conversas);
        adapter = new ConversaAdapter(getActivity(), conversas );
        listView.setAdapter( adapter );

        // recuperar dados do usuário
        Preferences preferencias = new Preferences(getActivity());
        String idUsuarioLogado = preferencias.getIdentificador();

        // Recuperar conversas do Firebase
        firebase = ConfigurationFireBase.getFirebase()
                    .child("conversas")
                    .child( idUsuarioLogado );

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                conversas.clear();
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Conversa conversa = dados.getValue( Conversa.class );
                    conversas.add(conversa);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //Adicionar evento de clique na lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Conversa conversa = conversas.get(position);
                Intent intent = new Intent(getActivity(), ConversaActivity.class );

                intent.putExtra("nome", conversa.getNome() );
                String email = Base64Custom.decodificarBase64( conversa.getIdUsuario() );
                intent.putExtra("email", email );

                startActivity(intent);

            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerConversas);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    }
}
