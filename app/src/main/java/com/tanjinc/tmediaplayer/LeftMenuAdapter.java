package com.tanjinc.tmediaplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tanjincheng on 16/4/1.
 */
public class LeftMenuAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mInflater;


    private enum MenuItemType {
        TYPE_NORMAL, TYPE_SEPERATOR, TYPE_NO_ICON
    }

    LeftMenuAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    private ArrayList<MenuItem> mList = new ArrayList<>(Arrays.asList(
            new MenuItem("seting"),
            new MenuItem("hehe"),
            new MenuItem()

    ));

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuItem item = mList.get(position);
        switch (item.type) {
            case TYPE_NORMAL:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.drawer_item,parent,false);
                }
                TextView itemView = (TextView) convertView;
                itemView.setText(item.name);
                Drawable icon = mContext.getResources().getDrawable(item.icon);
                if (icon != null) {
                    icon.setBounds(0, 0, 24, 24);
                    TextViewCompat.setCompoundDrawablesRelative(itemView, icon, null, null, null);
                }
                break;
            case TYPE_NO_ICON:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.drawer_item_subheader, parent, false);
                }
                TextView subHeader = (TextView) convertView;
                subHeader.setText(item.name);
                break;
            case TYPE_SEPERATOR:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.drawer_item_separator, parent, false);
                }
                break;

        }
        return convertView;
    }

    public class MenuItem {
        private int icon;
        private MenuItemType type;
        private String name;

        public static final int NO_ICON = 0;

        MenuItem(int icon, String name) {
            this.icon = icon;
            this.name = name;
            if (icon == NO_ICON && TextUtils.isEmpty(name)) {
                type = MenuItemType.TYPE_SEPERATOR;
            } else if (icon == NO_ICON ){
                type = MenuItemType.TYPE_NO_ICON;
            } else {
                type = MenuItemType.TYPE_NORMAL;
            }
        }

        MenuItem(String name) {
            this(NO_ICON, name);
        }

        MenuItem(){
            this(null);
        }
    }
}
