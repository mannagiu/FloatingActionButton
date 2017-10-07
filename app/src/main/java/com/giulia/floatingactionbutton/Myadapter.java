package com.giulia.floatingactionbutton;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by giulia on 26/06/17.
 */

public class Myadapter extends BaseAdapter {

    private Context context;
    private ArrayList<Integer> listid;
    private ArrayList<String> nameList;
    private SparseBooleanArray selectedListItemsIds;





    public Myadapter(Context context, ArrayList<Integer> listid, ArrayList<String> nameList) {
        super();
        this.context = context;
        this.listid = listid;
        this.nameList = nameList;
        selectedListItemsIds = new SparseBooleanArray();

    }
    /*private class ViewHolder {
        ImageView img;
        TextView apkName;
        CheckBox ck1;
    }*/


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate( context, R.layout.items_list, null );
        }

        ImageView images = (ImageView) convertView.findViewById( R.id.imageView );
        TextView text = (TextView) convertView.findViewById( R.id.textView );
        images.setImageResource( listid.get( position ) );
        text.setText( nameList.get( position ) );
        return convertView;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public String getItem(int position) {
        return nameList.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemPosition(String item){
        return nameList.indexOf(item);
    }

    public Integer getImageId(int position){
        return listid.get(position);
    }

    public void setData(ArrayList<Integer> list1, ArrayList<String> list2){

        this.listid = list1;
        this.nameList = list2;
        notifyDataSetChanged();

    }
    public void remove(String stringa) {
        listid.remove(getItemPosition(stringa));
        nameList.remove(stringa);
        notifyDataSetChanged();
    }

    public void rename (int pos, String r)
    {
        nameList.set(pos,r);
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
}

        /*holder = new ViewHolder();

            holder.img=(ImageView) convertView.findViewById(R.id.imageView);
            holder.apkName= (TextView) convertView.findViewById(R.id.textView);
            holder.ck1=(CheckBox) convertView.findViewById(R.id.cb);
            holder.img.setImageResource(listid.get(position));
            holder.apkName.setText(nameList.get(position));
            holder.ck1.setOnCheckedChangeListener(SecondPage,context);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        holder.ck1.setChecked(false);

        if (itemChecked[position])
            holder.ck1.setChecked(true);
        else
            holder.ck1.setChecked(false);

        holder.ck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (holder.ck1.isChecked())
                    itemChecked[position] = true;
                else
                    itemChecked[position] = false;
            }
        });
*/





