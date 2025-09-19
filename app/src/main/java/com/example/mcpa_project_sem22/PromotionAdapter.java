package com.example.mcpa_project_sem22;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {

    private List<Promotion> promotionList;

    public PromotionAdapter(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion, parent, false);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        Promotion promotion = promotionList.get(position);
        holder.titleTextView.setText(promotion.getTitle());
        holder.descriptionTextView.setText(promotion.getDescription());
        holder.discountTextView.setText(promotion.getStartDate());
        holder.expiryTextView.setText(promotion.getEndDate());
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    public static class PromotionViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView discountTextView;
        TextView expiryTextView;

        public PromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.promotion_title);
            descriptionTextView = itemView.findViewById(R.id.promotion_description);
            discountTextView = itemView.findViewById(R.id.promotion_discount);
            expiryTextView = itemView.findViewById(R.id.promotion_expiry);
        }
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
        notifyDataSetChanged();
    }
}