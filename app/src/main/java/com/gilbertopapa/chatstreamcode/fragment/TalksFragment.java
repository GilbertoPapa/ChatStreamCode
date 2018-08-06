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
import com.gilbertopapa.chatstreamcode.adapter.TalkAdapter;
import com.gilbertopapa.chatstreamcode.config.ConfigurationFireBase;
import com.gilbertopapa.chatstreamcode.helper.Base64Custom;
import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.gilbertopapa.chatstreamcode.model.Talk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalksFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Talk> adapter;
    private ArrayList<Talk> talks;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    public TalksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_talk, container, false);


        talks = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_talks);
        adapter = new TalkAdapter(getActivity(), talks);
        listView.setAdapter( adapter );


        Preferences preferences = new Preferences(getActivity());
        String identifyUserLogin = preferences.getIdentify();


        databaseReference = ConfigurationFireBase.getFirebase()
                    .child("talks")
                    .child( identifyUserLogin );

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                talks.clear();
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Talk talk = dados.getValue( Talk.class );
                    talks.add(talk);
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

                Talk talk = talks.get(position);
                Intent intentTalk = new Intent(getActivity(), TalkActivity.class );

                intentTalk.putExtra("nome", talk.getName() );
                String email = Base64Custom.decodeBase64( talk.getIdUser() );
                intentTalk.putExtra("email", email );

                startActivity(intentTalk);

            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }
}
