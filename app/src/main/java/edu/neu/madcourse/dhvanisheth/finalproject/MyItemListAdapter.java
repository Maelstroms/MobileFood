package edu.neu.madcourse.dhvanisheth.finalproject;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import edu.neu.madcourse.dhvanisheth.R;

/**
 * Created by Dhvani on 4/9/2016.
 */

public class MyItemListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private int layoutresourceId;
    private ArrayList<String> values;

    public MyItemListAdapter(Context context, int layotresourceId, ArrayList<String> values) {
        super(context, R.layout.finalproject_listview_item, values);
        this.context = context;
        this.layoutresourceId = layotresourceId;
        this.values = values;
    }

    private static class ViewHolder {
        EditText item;
        ImageButton delete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder =  null;
        if (holder == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutresourceId, parent, false);
            holder = new ViewHolder();
            holder.item = (EditText)row.findViewById(R.id.finalProjItemList);
            holder.delete = (ImageButton)row.findViewById(R.id.finalProjRemoveItem);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        String data = values.get(position);
        holder.item.setText(data);
        holder.delete.setImageResource(R.drawable.ic_delete_black_18dp);

        holder.delete.setTag(position);

        holder.delete.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View position) {

                                                 int pos = (int) position.getTag();
                                                 values.remove(pos);
                                                 MyItemListAdapter.this.notifyDataSetChanged();


                                             }
                                         }
        );

        holder.item.setId(position);

        holder.item.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = arg0.toString();
                    values.set(position, text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
            }
        });

        return row;
    }


}

