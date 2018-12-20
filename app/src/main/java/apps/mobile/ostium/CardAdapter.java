package apps.mobile.ostium;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import apps.mobile.ostium.Module.CardObject;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ContactViewHolder> {

    private List<CardObject> contactList;

    public CardAdapter(List<CardObject> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        CardObject ci = contactList.get(i);
        contactViewHolder.vTitle.setText(ci.title);
        contactViewHolder.vDetails.setText(ci.details);
        contactViewHolder.vDate.setText(ci.date);
        //contactViewHolder.vTitle.setText(ci.name + " " + ci.surname);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout_ostium, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;
        protected TextView vDetails;
        protected TextView vDate;

        public ContactViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.title);
            vDetails = (TextView) v.findViewById(R.id.details);
            vDate = (TextView) v.findViewById(R.id.date);
            //vTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}