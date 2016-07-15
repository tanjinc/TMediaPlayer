package com.tanjinc.tmediaplayer.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.R;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/4/23.
 */
public class PlayerMenuAdapter extends RecyclerView.Adapter<PlayerMenuAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "PlayerMenuAdapter";

    private ArrayList<PlayerMenuWidget.PlayerMenuData> mMenuDataArrayList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private int mSelectedPosition = -1;

    public PlayerMenuAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null && (Integer)v.getTag() != -1) {
            mItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setData(ArrayList<PlayerMenuWidget.PlayerMenuData> menuData) {
        mMenuDataArrayList = menuData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.player_menu_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mItemLayout = view;
        viewHolder.mMenuTitle = (TextView) view.findViewById(R.id.menu_name);
        viewHolder.mSelectedImg = (ImageView) view.findViewById(R.id.menu_item_selected);
        viewHolder.mDivider =  view.findViewById(R.id.menu_divider);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mMenuDataArrayList.get(position).isTitle) {
            holder.mDivider.setVisibility(View.GONE);
            holder.mSelectedImg.setVisibility(View.GONE);
            holder.mMenuTitle.setVisibility(View.VISIBLE);
            holder.mMenuTitle.setSelected(false);
            holder.mMenuTitle.setText(mMenuDataArrayList.get(position).mMenuItem);
            holder.mItemLayout.setBackgroundColor(mContext.getResources().getColor(R.color.textcolorwhite_trans));
            holder.mItemLayout.setTag(-1);
        } else {
            if (position == getItemCount() -1||
                    (position+1 < getItemCount() && mMenuDataArrayList.get(position+1).isTitle)) {
                holder.mDivider.setVisibility(View.INVISIBLE) ;
            } else {
                holder.mDivider.setVisibility(View.VISIBLE);
            }
            holder.mSelectedImg.setVisibility(View.INVISIBLE);
            holder.mMenuTitle.setVisibility(View.VISIBLE);
            holder.mMenuTitle.setText(mMenuDataArrayList.get(position).mMenuItem);
            holder.mItemLayout.setOnClickListener(this);
            holder.mItemLayout.setTag(position);
            holder.mItemLayout.setBackgroundColor(mContext.getResources().getColor(R.color.player_menu_widget_bg));

            boolean isSelected = mMenuDataArrayList.get(position).isSelected;
            if (isSelected) {
                holder.mMenuTitle.setSelected(true);
                holder.mSelectedImg.setVisibility(View.VISIBLE);
            } else {
                holder.mMenuTitle.setSelected(false);
                holder.mSelectedImg.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mMenuDataArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mMenuTitle;
        ImageView mSelectedImg;
        View mDivider;
        RelativeLayout mItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
