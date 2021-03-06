package com.example.moneymanagerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransViewHolder> implements Filterable {
    public List<Transaction> transList;
    public List<Transaction> getTransactionListFilter = new ArrayList<>();
    private LayoutInflater inflater;

    public TransactionListAdapter(Context context, List<Transaction> transList){
        // Odpowiada za tworzenie instancji zawartości plików XML układu w odpowiadających im obiektach view
        inflater = LayoutInflater.from(context);
        this.transList = transList;
        this.getTransactionListFilter = transList;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0) {
                    filterResults.values = getTransactionListFilter;
                    filterResults.count = getTransactionListFilter.size();
                }else {
                    String searchStr = constraint.toString().toLowerCase();
                    List<Transaction> transList = new ArrayList<>();
                    for(Transaction transaction: getTransactionListFilter){
                        if(transaction.getTitle().toLowerCase().contains(searchStr)){
                            transList.add(transaction);
                        }
                    }
                    filterResults.values = transList;
                    filterResults.count = transList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                transList = (List<Transaction>)results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
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
            intent.putExtra("transactionUUID",element.getId().toString());
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
            holder.transTypeImage.setColorFilter(Color.argb(255, 0, 128, 0));
            holder.transAmountText.setText(current.getAmount().toString());
        }
        else if(current.getTransType().equals("expense")){
            holder.transTypeImage.setColorFilter(Color.argb(255, 220, 0, 0));
            holder.transAmountText.setText("-" + current.getAmount().toString());
        }
        holder.transTitleText.setText(current.getTitle());
        holder.transTypeText.setText(current.getTransType());
    }

    @Override
    public int getItemCount() {
        return transList.size();
    }

    public void  setCrimes(List<Transaction> transactions){
        this.transList = transactions;
    }
}




