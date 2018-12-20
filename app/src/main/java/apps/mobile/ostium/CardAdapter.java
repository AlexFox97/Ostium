package apps.mobile.ostium;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import apps.mobile.ostium.Module.CardObject;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardObject> events;

    public CardAdapter(List<CardObject> events) {
        this.events = events;
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        CardObject ci = events.get(i);
        cardViewHolder.vTitle.setText(ci.title);
        cardViewHolder.vDetails.setText(ci.details);
        cardViewHolder.vDate.setText(ci.date);
        //contactViewHolder.vTitle.setText(ci.name + " " + ci.surname);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout_ostium, viewGroup, false);

        return new CardViewHolder(itemView);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;
        protected TextView vDetails;
        protected TextView vDate;

        public CardViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.title);
            vDetails = (TextView) v.findViewById(R.id.details);
            vDate = (TextView) v.findViewById(R.id.date);
            //vTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}