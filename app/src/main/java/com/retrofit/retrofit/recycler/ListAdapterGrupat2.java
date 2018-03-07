package com.retrofit.retrofit.recycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.florida.R;

import com.retrofit.retrofit.R;

import java.util.ArrayList;

public class ListAdapterGrupat2 extends RecyclerView.Adapter<ListAdapterGrupat2.ViewHolder> {

    private ArrayList<CustomObject> arrayList;
    private Context context;
    private Class<?> cls;

    private int colorGreen, colorRed;
    private Drawable bgRed, bgGreen;

    public ListAdapterGrupat2(Context context, ArrayList<CustomObject> arrayList, Class<?> cls) {
        this.arrayList = arrayList;
        this.context = context;
        this.cls = cls;

        colorGreen = context.getResources().getColor(R.color.green);
        colorRed = context.getResources().getColor(R.color.red);
        bgRed = context.getResources().getDrawable(R.drawable.label_new);
        bgGreen = context.getResources().getDrawable(R.drawable.label_old);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_programarigrupat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomObject obj = arrayList.get(position);
     //   campul grup

        if (obj.getNetGrup().equals("0")) {
            holder.tvGrup.setVisibility(View.GONE);
            holder.tvGrup.setText(obj.getItem2());
        } else {
            holder.tvGrup.setVisibility(View.VISIBLE);
            holder.tvGrup.setText(obj.getItem2());

        }
     //   campul subgrup
        if (obj.getSubGrup().equals("0")) {
            holder.tvSubGrup.setVisibility(View.GONE);
            holder.tvSubGrup.setText(obj.getItem4());
        } else {
            holder.tvSubGrup.setVisibility(View.VISIBLE);
            holder.tvSubGrup.setText(obj.getItem4());
            holder.tvSubGrup.setTextColor(colorRed);
        }
     //   campul dreapta sus
        holder.item2.setText(obj.getItem2());
        //   campul stanga jos
        holder.item3.setText(obj.getItem3());
     //   campul stanga sus
        holder.item1.setText(obj.getItem1());
           //campul dreapta jos
        holder.item4.setText(obj.getNote());
       // holder.tvSubGrup.setText(obj.getItem4());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvGrup,tvSubGrup, item2, item3, item1, item4;

        ViewHolder(View v) {
            super(v);
            tvGrup = (TextView) v.findViewById(R.id.tvGrup);
            tvSubGrup = (TextView) v.findViewById(R.id.tvSubGrup);
            item2 = (TextView) v.findViewById(R.id.tvNumber);
            item3 = (TextView) v.findViewById(R.id.tvDate);
            item1 = (TextView) v.findViewById(R.id.tvName);
            item4 = (TextView) v.findViewById(R.id.tvState);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, cls);
                    intent.putExtra("IdM", arrayList.get(getAdapterPosition()).getNetAutonumber());
                    context.startActivity(intent);
                }
            });
        }
    }
}
