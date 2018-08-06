package com.gilbertopapa.chatstreamcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gilbertopapa.chatstreamcode.R;
import com.gilbertopapa.chatstreamcode.model.Talk;

import java.util.ArrayList;

/**
 * Created by GilbertoPapa on 06/08/2018.
 */

public class TalkAdapter extends ArrayAdapter<Talk> {

    private ArrayList<Talk> talks;
    private Context context;

    public TalkAdapter( Context c, ArrayList<Talk> objects) {
        super(c, 0, objects);
        this.context = c;
        this.talks = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = null;


        if( talks != null ){


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


            view = inflater.inflate(R.layout.list_talks, parent, false);


            TextView nameTalk = (TextView) view.findViewById(R.id.tv_nameTalk);
            TextView lastTalk = (TextView) view.findViewById(R.id.tv_lastTalk);

            Talk talk = talks.get( position );
            nameTalk.setText( talk.getName());
            lastTalk.setText( talk.getMsg() );

        }

        return view;
    }
}
