package com.gilbertopapa.chatstreamcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gilbertopapa.chatstreamcode.R;
import com.gilbertopapa.chatstreamcode.helper.Preferences;
import com.gilbertopapa.chatstreamcode.model.Message;

import java.util.ArrayList;

/**
 * Created by GilbertoPapa on 02/08/2018.
 */

public class MassageAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> messages;
    private Context context;

    public MassageAdapter(Context c, ArrayList<Message> objects) {
        super(c, 0, objects);
        this.messages = objects;
        this.context = c;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;


        if( messages != null ){

            Preferences preferences = new Preferences(context);
            String idSender = preferences.getIdentify();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            Message message = messages.get( position );
            if (idSender.equals(message.getIdUserMsg())){
                view = inflater.inflate(R.layout.item_message_right, parent, false);
            }else{
                view = inflater.inflate(R.layout.item_message_left, parent, false);
            }
            TextView textMessageRight = (TextView) view.findViewById(R.id.tv_messageRight);
            textMessageRight.setText( message.getMsg());

            TextView textMessageLeft = (TextView) view.findViewById(R.id.tv_messageLeft);
            textMessageLeft.setText( message.getMsg());

        }

        return view;

    }
}
