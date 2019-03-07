package apps.mobile.ostium;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import apps.mobile.ostium.Module.CardObject;
import apps.mobile.ostium.Module.LocationObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static apps.mobile.ostium.MainActivity.savedLocations;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardObject> events;

    public CardAdapter(List<CardObject> events) {
        this.events = events;
    }

    public static void addLocationTags(View v) {
        final Context context = v.getContext();
        ArrayList<String> locationTitlesTemp = new ArrayList<>();
        final Boolean checkedLocations[] = new Boolean[savedLocations.size()];
        Arrays.fill(checkedLocations, false);
        for (LocationObject location : savedLocations) {
            locationTitlesTemp.add(location.getTitle());
        }

        final String[] locationTitles = GetStringArray(locationTitlesTemp);
        final List<String> locationList = Arrays.asList(locationTitles);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogInterface.OnMultiChoiceClickListener multiListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // Update the current focused item's checked status
                checkedLocations[which] = isChecked;

                // Get the current focused item
                String currentItem = locationList.get(which);

                // Notify the current action
                //Toast.makeText(context, currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();

            }
        };
        builder.setMultiChoiceItems(locationTitles, null, multiListener);
        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle("Tag some locations?");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Current card

            }
        });

        AlertDialog addEventAlert = builder.create();
        addEventAlert.show();
    }

    private static String[] GetStringArray(ArrayList<String> arr) {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int position) {
        CardObject ci = events.get(position);
        cardViewHolder.vTitle.setText(ci.title);
        cardViewHolder.vDetails.setText(ci.details);
        cardViewHolder.vDate.setText(ci.date);
        String s = ci.getLocationsToString();
        cardViewHolder.vTags.setText(s);
        //cardViewHolder.vTags.setAdapter();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout_ostium, viewGroup, false);

        return new CardViewHolder(itemView);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public View view;
        protected TextView vTitle;
        protected TextView vDetails;
        protected TextView vDate;
        protected TextView vTags;
        // protected RecyclerView vTags;

        public CardViewHolder(final View view) {
            super(view);
            vTitle = (TextView) view.findViewById(R.id.title);
            vDetails = (TextView) view.findViewById(R.id.details);
            vDate = (TextView) view.findViewById(R.id.date);
            vTags = view.findViewById(R.id.tagList);
            //vTags = (RecyclerView) view.findViewById(R.id.tagList);
            view.setOnClickListener(new View.OnClickListener() {  // <--- here
                @Override
                public void onClick(View v) {
                    addLocationTags(v);
                }
            });
        }
    }
}