package org.esaip.tchinconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.esaip.tchinconnect.R;
import org.esaip.tchinconnect.models.Card;
import org.w3c.dom.Text;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Card> cardList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public CardAdapter(Context context, List<Card> data) {
        this.mInflater = LayoutInflater.from(context);
        this.cardList = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.profile_card_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.contactName.setText(card.getName() +" "+ card.getSurname());
        holder.contactJobName.setText(card.getJob());
        if(card.getJobDescription().length()>40){
            holder.contactJobDesc.setText(card.getJobDescription().substring(0, 37) +"...");
        }
        else {
            holder.contactJobDesc.setText(card.getJobDescription());
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return cardList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView contactName;
        TextView contactJobName;
        TextView contactJobDesc;

        ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactJobName = itemView.findViewById(R.id.contactJobName);
            contactJobDesc = itemView.findViewById(R.id.contactJobDesc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Card getItem(int id) {
        return cardList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}