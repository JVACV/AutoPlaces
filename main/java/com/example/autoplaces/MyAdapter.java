package com.example.autoplaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.autoplaces.pojos.Placess;

public class MyAdapter extends BaseAdapter {

    MainActivity mActivity;
    Placess places_;

    public MyAdapter(MainActivity mainActivity, Placess places_) {

        this.mActivity=mainActivity;
        this.places_=places_;
    }

    @Override
    public int getCount() {
        return places_.getList().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater=LayoutInflater.from(mActivity);
        View v = inflater.inflate(R.layout.indi_view, null);

        TextView tv1 = v.findViewById(R.id.tv1);
        TextView tv2 = v.findViewById(R.id.tv2);

        tv1.setText(places_.getList().get(i).getName());
        tv2.setText(places_.getList().get(i).getVicinity());
        return v;
    }

}
