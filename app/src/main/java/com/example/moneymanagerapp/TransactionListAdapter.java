package com.example.moneymanagerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransViewHolder> {
    public List<Transaction> transList;
    private LayoutInflater inflater;

    public TransactionListAdapter(Context context, List<Transaction> transList){
        // Odpowiada za tworzenie instancji zawartości plików XML układu w odpowiadających im obiektach view
        inflater = LayoutInflater.from(context);
        this.transList = transList;
    }

    class TransViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView transTitleText;
        public final TextView transTypeText;
        public final TextView transAmountText;
        public final ImageView transTypeImage;
        final  TransactionListAdapter adapter;

        public TransViewHolder(@NonNull View itemView, TransactionListAdapter adapter) {
            super(itemView);
            transTypeImage = itemView.findViewById(R.id.trans_image);
            transTitleText = itemView.findViewById(R.id.trans_title);
            transTypeText = itemView.findViewById(R.id.trans_type_text);
            transAmountText = itemView.findViewById(R.id.trans_amount_text);
            this.adapter = adapter;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final Intent intent;

            int position = getLayoutPosition();
            Transaction element = transList.get(position);
            transList.set(position, element);
            adapter.notifyItemChanged(position);

            intent = new Intent(view.getContext(), TransActivity.class);
            intent.putExtra("crimeUUID",element.getId().toString());
            view.getContext().startActivity(intent);

        }
    }

    @NonNull
    @Override
    public TransViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.single_list_item, parent, false);
        return new TransViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TransViewHolder holder, int position) {
        Transaction current = transList.get(position);
        if(current.getTransType().equals("income")){
            holder.transTypeImage.setColorFilter(Color.argb(255, 0, 255, 0));
        }
        else if(current.getTransType().equals("expense")){
            holder.transTypeImage.setColorFilter(Color.argb(255, 255, 0, 0));
        }
        holder.transTitleText.setText(current.getTitle());
        holder.transTypeText.setText(current.getTransType());
        holder.transAmountText.setText(current.getAmount().toString());
    }

    @Override
    public int getItemCount() {
        return transList.size();
    }

    public void  setCrimes(List<Transaction> transactions){
        this.transList = transactions;
    }
}




