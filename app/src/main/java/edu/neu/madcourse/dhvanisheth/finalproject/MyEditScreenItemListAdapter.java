package edu.neu.madcourse.dhvanisheth.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import edu.neu.madcourse.dhvanisheth.R;

/**
 * Created by Dhvani on 4/9/2016.
 */

public class MyEditScreenItemListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private int layoutresourceId;
    private ArrayList<String> values;
    private ArrayList<String> numberOfDays;


    public MyEditScreenItemListAdapter(Context context, int layotresourceId,
                                       ArrayList<String> values,ArrayList<String> numberOfDays) {
        super(context, R.layout.finalproject_edit_screen_item, values);
        this.context = context;
        this.layoutresourceId = layotresourceId;
        this.values = values;
        this.numberOfDays = numberOfDays;
    }

    private static class ViewHolder {
        TextView item;
        ImageButton delete;
        EditText numberOfDays;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder =  null;
        if (holder == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutresourceId, parent, false);
            holder = new ViewHolder();
            holder.item = (TextView)row.findViewById(R.id.finalProjEditItemList);
            holder.delete = (ImageButton)row.findViewById(R.id.finalProjEditRemoveItem);
            holder.numberOfDays = (EditText)row.findViewById(R.id.finalProjEditNumberOfDays);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }


        String dataDays = numberOfDays.get(position);
        holder.numberOfDays.setText(dataDays);
        holder.numberOfDays.setTag(position);
        holder.numberOfDays.setId(position);
        holder.numberOfDays.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = arg0.toString();

                numberOfDays.set(position, text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });


        holder.delete.setImageResource(R.drawable.ic_delete_black_18dp);
        holder.delete.setTag(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View position) {
                                                 int pos = (int) position.getTag();
                                                 values.remove(pos);
                                                 MyEditScreenItemListAdapter.this.notifyDataSetChanged();
                                             }
                                         }
        );

        String data = values.get(position);
        holder.item.setText(data);
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

