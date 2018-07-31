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

    private ListView listViewContacts;
    private ArrayAdapter adapter;
    private ArrayList<Contact> contacts;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContact;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerContact);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerContact);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        contacts = new ArrayList<>();


        View view = inflater.inflate(R.layout.fragment_contact, container, false);


        listViewContacts = (ListView) view.findViewById(R.id.lv_contact);
        /*adapter = new ArrayAdapter(
                getActivity(),
                R.layout.list_contact,
                contacts
        );*/
        adapter = new ContactAdapter(getActivity(), contacts);
        listViewContacts.setAdapter( adapter );


        Preferences preferences = new Preferences(getActivity());
        String identifyUserLogin = preferences.getIdentify();

        firebase = ConfigurationFireBase.getFirebase()
                    .child("contacts")
                    .child( identifyUserLogin );


        valueEventListenerContact = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                contacts.clear();


                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Contact contact = dados.getValue( Contact.class );
                    contacts.add( contact );


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentTalk = new Intent(getActivity(), TalkActivity.class);

                Contact contact = contacts.get(position);

                intentTalk.putExtra("name", contact.getName() );
                intentTalk.putExtra("email", contact.getEmail() );

                startActivity(intentTalk);

            }
        });

        return view;

    }

}
