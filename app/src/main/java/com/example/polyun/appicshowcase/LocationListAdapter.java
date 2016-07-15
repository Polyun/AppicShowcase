package com.example.polyun.appicshowcase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LocationListAdapter extends BaseAdapter {
    private ArrayList<IndoorsLocation> listData;
    private LayoutInflater layoutInflater;

    public LocationListAdapter(Context aContext, ArrayList<IndoorsLocation> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.location_list_row, null);
            holder = new ViewHolder();
            holder.Address = (TextView) convertView.findViewById(R.id.Address);
            holder.Building_ID = (TextView) convertView.findViewById(R.id.Building_ID);
            holder.API_Key = (TextView) convertView.findViewById(R.id.API_Key);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.Address.setText(listData.get(position).getAddress());
        holder.Building_ID.setText(String.valueOf(listData.get(position).getBuilding_ID()));
        holder.API_Key.setText(listData.get(position).getAPI_Key());
        return convertView;
    }

    static class ViewHolder {
        TextView Address;
        TextView Building_ID;
        TextView API_Key;
    }
}