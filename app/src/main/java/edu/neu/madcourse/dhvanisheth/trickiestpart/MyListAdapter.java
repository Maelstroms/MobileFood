package edu.neu.madcourse.dhvanisheth.trickiestpart;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.neu.madcourse.dhvanisheth.R;

/**
 * Created by Dhvani on 4/9/2016.
 */
////public class MyListAdapter extends BaseAdapter {
////
////    @Override
////    public int getCount() {
////        // TODO Auto-generated method stub
//////            if(arrText != null && arrText.length != 0){
//////                return arrText.length;
//////            }
////        return 0;
////    }
////
////    @Override
////    public Object getItem(int position) {
////        // TODO Auto-generated method stub
//////            return arrText[position];
////        return splitResultMain[position];
////    }
////
////    @Override
////    public long getItemId(int position) {
////        // TODO Auto-generated method stub
////        return position;
////    }
////
////    @Override
////    public View getView(int position, View convertView, ViewGroup parent) {
////
////        //ViewHolder holder = null;
////        final ViewHolder holder;
////        if (convertView == null) {
////
////            holder = new ViewHolder();
////            LayoutInflater inflater = TrickiestPartMainActivity.this.getLayoutInflater();
////            convertView = inflater.inflate(R.layout.trickiestpart_listview_item, null);
////            holder.editText1 = (EditText) convertView.findViewById(R.id.editText1);
////
////            convertView.setTag(holder);
////
////        } else {
////
////            holder = (ViewHolder) convertView.getTag();
////        }
////
////        holder.ref = position;
////
////        holder.editText1.setText(splitResultMain[position]);
////        holder.editText1.addTextChangedListener(new TextWatcher() {
////
////            @Override
////            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
////                // TODO Auto-generated method stub
////
////            }
////
////            @Override
////            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
////                                          int arg3) {
////                // TODO Auto-generated method stub
////
////            }
////
////            @Override
////            public void afterTextChanged(Editable arg0) {
////                // TODO Auto-generated method stub
////                splitResultMain[holder.ref] = arg0.toString();
////            }
////        });
////
////        return convertView;
////    }
////
////    private class ViewHolder {
////        TextView textView1;
////        EditText editText1;
////        int ref;
////    }
////
////
////}
//
//public class MyListAdapter extends BaseAdapter {
//    private static LayoutInflater mInflater = null;
////    public ArrayList myItems = new ArrayList();
//
//    String [] result;
//    ArrayList<String> filteredList;
//    Context context;
//    public ArrayList myFilteredItems = new ArrayList();
//
//
//    public MyListAdapter(TrickiestPartMainActivity mainActivity, ArrayList<String> prgmNameList) {
//
////        for (int i = 0; i < 20; i++) {
////            ListItem listItem = new ListItem();
////            listItem.caption = "Caption" + i;
////            myItems.add(listItem);
////        }
//        context = mainActivity;
//        filteredList = prgmNameList;
//        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        notifyDataSetChanged();
//    }
//
//    public int getCount() {
////        return myItems.size();
//        return filteredList.size();
//    }
//
//    public Object getItem(int position) {
////        return position;
//        return null;
//    }
//
//    public long getItemId(int position) {
////        return position;
//        return 0;
//    }
//
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.trickiestpart_listview_item, null);
//            holder.item = (EditText) convertView
//                    .findViewById(R.id.editText1);
//            holder.delete = (ImageView) convertView.findViewById(R.id.removeItem);
//            convertView.setTag(holder);
//            System.out.println();
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        //Fill EditText with the value you have in data source
////        for (int i = 0; i < filteredList.size(); i++) {
////            holder.item.setText(filteredList.get(i));
//
//
////        holder.caption.setId(position);
//
//            //we need to update adapter once we finish with editing
////        holder.item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
////            public void onFocusChange(View v, boolean hasFocus) {
////                if (!hasFocus) {
////                    final int position = v.getId();
////                    final EditText Caption = (EditText) v;
////                    filteredList.set(position, Caption.getText().toString());
////                }
////            }
////        });
//
//        holder.item.setText(filteredList.get(position));
////        holder.item.setId(position);
//            holder.item.addTextChangedListener(new TextWatcher() {
//
//                @Override
//                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//                                              int arg3) {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable arg0) {
//                    // TODO Auto-generated method stub
//                    filteredList.set(position, arg0.toString());
//                }
//            });
//
////        }
//
//        return convertView;
//    }
//}
//
//
//
//
//
//
//class ViewHolder {
//    EditText item;
//    ImageView delete;
//}
//
//class ListItem {
//    String caption;
//}

public class MyListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private int layoutresourceId;
    private ArrayList<String> values;

//    private String [] values;
    public MyListAdapter(Context context, int layotresourceId, ArrayList<String> values) {
        super(context, R.layout.trickiestpart_listview_item, values);
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
            holder.item = (EditText)row.findViewById(R.id.editText1);
            holder.delete = (ImageButton)row.findViewById(R.id.removeItem);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

//        String data = values[position];
        String data = values.get(position);
        holder.item.setText(data);
        holder.delete.setImageResource(R.drawable.ic_delete_black_18dp);
        /*LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.trickiestpart_listview_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.editText1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.removeItem);
        textView.setText(values.get(position));*/
        // change the icon for Windows and iPhone
//        String s = values[position];
//        if (s.startsWith("iPhone")) {
//            imageView.setImageResource(R.drawable.no);
//        } else {
//            imageView.setImageResource(R.drawable.ok);
//        }

        //imageView.setImageResource(R.drawable.ic_delete_black_36dp);
        holder.delete.setTag(position);

        holder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View position) {

                int pos = (int) position.getTag();
                values.remove(pos);
                MyListAdapter.this.notifyDataSetChanged();


            }
                                         }
        );

        return row;
    }
}

