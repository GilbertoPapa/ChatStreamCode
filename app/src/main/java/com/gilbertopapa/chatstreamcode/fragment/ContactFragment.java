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

import com.gilbertopapa.chatstreamcode.R;
import com.gilbertopapa.chatstreamcode.TalkActivity;
import com.gilbertopapa.chatstreamcode.adapter.ContactAdapter;
import com.gilbertopapa.chatstreamcode.config.ConfigurationFireBase;
import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.gilbertopapa.chatstreamcode.model.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contact> contacts;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener( valueEventListenerContatos );
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener( valueEventListenerContatos );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //Instânciar objetos
        contacts = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        //Monta listview e adapter
        listView = (ListView) view.findViewById(R.id.lv_contatos);
        /*adapter = new ArrayAdapter(
                getActivity(),
                R.layout.list_contact,
                contacts
        );*/
        adapter = new ContactAdapter(getActivity(), contacts);
        listView.setAdapter( adapter );

        //Recuperar contacts do firebase
        Preferences preferencias = new Preferences(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfigurationFireBase.getFirebase()
                    .child("contacts")
                    .child( identificadorUsuarioLogado );

        //Listener para recuperar contacts
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                contacts.clear();

                //Listar contacts
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Contact contato = dados.getValue( Contact.class );
                    contacts.add( contato );


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), TalkActivity.class);

                // recupera dados a serem passados
                Contact contato = contacts.get(position);

                // enviando dados para conversa activity
                intent.putExtra("nome", contato.getName() );
                intent.putExtra("email", contato.getEmail() );

                startActivity(intent);

            }
        });

        return view;

    }

}
