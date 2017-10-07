package com.giulia.floatingactionbutton;

/**
 * Created by giulia on 07/10/17.
 */


import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter {
    Context context;
    private SparseBooleanArray selectedListItemsIds;
    List multipleSelectionList;

    public CustomListViewAdapter(Context context, int resourceId, List items) {
        super(context, resourceId, items);
        this.context = context;
        selectedListItemsIds = new SparseBooleanArray();
        this.multipleSelectionList = items;
    }
    /*private view holder class*/
    private class ViewHolder {
        ImageView imgCountryFlag;
        TextView txtCountryName;
    }




    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        SelectedItem rowItem = (SelectedItem) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.items_list, null);
            holder = new ViewHolder();
            holder.txtCountryName = (TextView) convertView.findViewById(R.id.textView);
            holder.imgCountryFlag = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.txtCountryName.setText(rowItem.getItemName());
        holder.imgCountryFlag.setImageResource(rowItem.getImageId());

        return convertView;
    }


    public void remove(SelectedItem object) {
        multipleSelectionList.remove(object);
        notifyDataSetChanged();
    }
    public void rename(int position,SelectedItem object){
        multipleSelectionList.set(position,object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedListItemsIds.get(position));
    }

    public void removeSelection() {
        selectedListItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedListItemsIds.put(position, value);
        else
            selectedListItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedListItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedListItemsIds;
    }
    public void setData(List list){

        this.multipleSelectionList = list;
        notifyDataSetChanged();


    }
}