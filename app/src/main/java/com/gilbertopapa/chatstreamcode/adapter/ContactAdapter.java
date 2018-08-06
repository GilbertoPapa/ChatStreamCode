package com.gilbertopapa.chatstreamcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gilbertopapa.chatstreamcode.R;
import com.gilbertopapa.chatstreamcode.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts;
    private Context context;

    public ContactAdapter(Context c, ArrayList<Contact> objects) {
        super(c, 0, objects);
        this.contacts = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;


        if( contacts != null ){


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


            view = inflater.inflate(R.layout.list_contact, parent, false);


            TextView nameContact = (TextView) view.findViewById(R.id.tv_nameContact);
            TextView emailContact = (TextView) view.findViewById(R.id.tv_emailContact);

            Contact contact = contacts.get( position );
            nameContact.setText( contact.getName());
            emailContact.setText( contact.getEmail() );

        }

        return view;

    }
}
