package com.example.e_commerce.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerce.R;
import com.example.e_commerce.activity.ViewItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    public static ArrayList<DataModel> itemList;
    private int flag;
    public static int SHOPPING_CART_ACT=100;
    public static ArrayList<DataModel> shoppingList=new ArrayList<>();

    public MyAdapter(Context context, ArrayList<DataModel> itemList){
        this.context=context;
        this.itemList=itemList;
        this.flag=0;

    }
    public MyAdapter(Context context, ArrayList<DataModel> itemList,int flag){
        this.context=context;
        this.itemList=itemList;
        this.flag=flag;
    }

    int count = 1;
    //here count variable is not creating for each item

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        count = itemList.get(position).getNoOfItem();
        if(itemList.get(position).getImage2()!=null){
            Glide.with(context).load(itemList.get(position).getImage2()).into(holder.sellingItemImageOfListItem);
        }else{
            holder.sellingItemImageOfListItem.setImageResource(itemList.get(position).getImage1());
        }
        holder.sellingItemNameOfListItem.setText(itemList.get(position).getItemName());

        holder.noOfItemTV.setText(String.valueOf(itemList.get(holder.getAdapterPosition()).getNoOfItem()));
        //changes
        holder.itemDescriptionTV.setText(itemList.get(position).getItemDescription());
        String price="Rs: "+String.valueOf(itemList.get(position).getSingleItemPrice());
        holder.priceTV.setText(price);
        holder.decrementItemQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count >1){
                    count--;
                    holder.noOfItemTV.setText(String.valueOf(count));
                    itemList.get(holder.getAdapterPosition()).setNoOfItem(count);
                    String price="Rs: "+String.valueOf(itemList.get(holder.getAdapterPosition()).getSingleItemPrice()*count);
                    holder.priceTV.setText(price);
                }
                else Toast.makeText(context, "Sorry item can not be smaller than 1", Toast.LENGTH_SHORT).show();
            }
        });
        holder.incrementItemQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                holder.noOfItemTV.setText(String.valueOf(count));
                itemList.get(holder.getAdapterPosition()).setNoOfItem(count);
                String price="Rs: "+String.valueOf(itemList.get(holder.getAdapterPosition()).getSingleItemPrice()*count);
                holder.priceTV.setText(price);

            }
        });
        // Changes is done from here (below)
        if(flag==0){
            holder.listItemQtyLL.setVisibility(View.INVISIBLE);
        }
        if(flag==SHOPPING_CART_ACT){
            holder.addToCartTV.setVisibility(View.INVISIBLE);
            holder.imageAndDetailLL.setOrientation(LinearLayout.HORIZONTAL);
            holder.sellingItemImageOfListItem.getLayoutParams().width=140;
            holder.sellingItemImageOfListItem.getLayoutParams().height=140;

            holder.detailLL.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            holder.detailLL.setMinimumHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

            holder.sellingItemNameOfListItem.setGravity(Gravity.CENTER);
            holder.itemDescriptionTV.setGravity(Gravity.CENTER);
            holder.listItemQtyLL.setGravity(Gravity.CENTER);
            holder.priceAndCartLL.setGravity(Gravity.CENTER);
            holder.detailLL.setPadding(34,0,0,0);

        }

        holder.addToCartTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0){
                    itemList.get(holder.getAdapterPosition()).setNoOfItem(count);
                    shoppingList.add(itemList.get(holder.getAdapterPosition()));
                    Toast.makeText(context, "Item added to the cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemCardViewList_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewItem.class);
                if(itemList.get(holder.getAdapterPosition()).getImage2()!=null){
                    intent.putExtra("IMAGE2",itemList.get(holder.getAdapterPosition()).getImage2().toString());
                }else{
                    intent.putExtra("IMAGE2","");
                }

                intent.putExtra("IMAGE1",itemList.get(holder.getAdapterPosition()).getImage1());
                intent.putExtra("ITEM_NAME",itemList.get(holder.getAdapterPosition()).getItemName());
                intent.putExtra("ITEM_DESCRIPTION",itemList.get(holder.getAdapterPosition()).getItemDescription());

                intent.putExtra("ITEM_PRICE",itemList.get(holder.getAdapterPosition()).getSingleItemPrice());

                intent.putExtra("ITEM_QTY",itemList.get(holder.getAdapterPosition()).getNoOfItem());





                context.startActivity(intent);
                Log.d("NOTHING","ABCDEFGIJKLMNOPQRSTUVWXYZ");
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView sellingItemImageOfListItem;
        TextView sellingItemNameOfListItem;
        TextView decrementItemQty;
        TextView incrementItemQty;
        CircleImageView addToCartTV;
        TextView noOfItemTV;

        //Changes are done from below
        LinearLayout listItemQtyLL;
        LinearLayout imageAndDetailLL;
        LinearLayout detailLL;
        TextView itemDescriptionTV;
        LinearLayout priceAndCartLL;
        MaterialCardView itemCardViewList_Item;
        TextView priceTV;
        public MyViewHolder(View itemView){
            super(itemView);
            sellingItemImageOfListItem=itemView.findViewById(R.id.sellingItemImageOfListItem);
            sellingItemNameOfListItem=itemView.findViewById(R.id.sellingItemNameOfListItem);
            decrementItemQty=itemView.findViewById(R.id.decrementItemQty);
            incrementItemQty=itemView.findViewById(R.id.incrementItemQty);
            addToCartTV=itemView.findViewById(R.id.addToCartTV);
            noOfItemTV=itemView.findViewById(R.id.noOfItemTV);

            //changes are done from below
            listItemQtyLL=itemView.findViewById(R.id.listItemQtyLL);
            imageAndDetailLL=itemView.findViewById(R.id.imageAndDetailLL);
            detailLL=itemView.findViewById(R.id.detailLL);
            itemDescriptionTV=itemView.findViewById(R.id.itemDescriptionTV);
            priceAndCartLL=itemView.findViewById(R.id.priceAndCartLL);
            itemCardViewList_Item=itemView.findViewById(R.id.itemCardViewList_Item);
            priceTV=itemView.findViewById(R.id.priceTV);

        }

    }
}
