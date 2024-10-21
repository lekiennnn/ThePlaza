package com.example.doan2.ShoppingCart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan2.Interface.FilterableAdapter;
import com.example.doan2.Product.Item;
import com.example.doan2.R;
import com.example.doan2.Interface.RecyclerViewInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> implements FilterableAdapter {

    private List<Item> shoppingCart;
    private final RecyclerViewInterface recyclerViewInterface;
    private final ShoppingCartActivity activity;

    public ShoppingCartAdapter(List<Item> shoppingCart, RecyclerViewInterface recyclerViewInterface, ShoppingCartActivity activity) {
        this.shoppingCart = shoppingCart;
        this.recyclerViewInterface = recyclerViewInterface;
        this.activity = activity;
    }

    @Override
    public void setFilteredList(List<Item> filteredList) {
        this.shoppingCart = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.shopping_cart_items, parent, false);
        return new ShoppingCartViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        Item currentItem = shoppingCart.get(position);
        String capitalizedItemName = currentItem.getName().substring(0, 1).toUpperCase() + currentItem.getName().substring(1);

        Picasso.get().load(currentItem.getImage()).into(holder.imageImg);

        holder.nameTxt.setText(capitalizedItemName);
        holder.priceTxt.setText(currentItem.getPrice() + "$");
        holder.quantityTxt.setText(String.valueOf(currentItem.getQuantity()));

        holder.selectSingleCb.setOnCheckedChangeListener(null);
        holder.selectSingleCb.setChecked(currentItem.isSelected());

        holder.selectSingleCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentItem.setSelected(isChecked);
                activity.updateTotalCost();
                updateSelectAllCheckbox();
            }
        });

        holder.increaseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setQuantity(currentItem.getQuantity() + 1);
                holder.quantityTxt.setText(String.valueOf(currentItem.getQuantity()));
                activity.updateTotalCost();
            }
        });

        holder.decreaseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.getQuantity() > 1) {
                    currentItem.setQuantity(currentItem.getQuantity() - 1);
                    holder.quantityTxt.setText(String.valueOf(currentItem.getQuantity()));
                    activity.updateTotalCost();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCart.size();
    }

    // Check if all items are selected
    private boolean areAllItemsSelected() {
        for (Item item : shoppingCart) {
            if (!item.isSelected()) {
                return false;
            }
        }
        return true;
    }

    // Update Select All checkbox
    private void updateSelectAllCheckbox() {
        boolean allSelected = areAllItemsSelected();
        activity.setSelectAllCheckbox(allSelected);
    }

    public static class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageImg, decreaseImg, increaseImg;
        TextView nameTxt, priceTxt, quantityTxt;
        CheckBox selectSingleCb;

        public ShoppingCartViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            imageImg = itemView.findViewById(R.id.image_img);
            decreaseImg = itemView.findViewById(R.id.decrease_img);
            increaseImg = itemView.findViewById(R.id.increase_img);
            quantityTxt = itemView.findViewById(R.id.quantity_txt);
            nameTxt = itemView.findViewById(R.id.name_txt);
            priceTxt = itemView.findViewById(R.id.price_txt);
            selectSingleCb = itemView.findViewById(R.id.selectSingle_cb);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
