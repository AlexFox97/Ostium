package apps.mobile.ostium.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import apps.mobile.ostium.R;
import apps.mobile.ostium.TagInfo;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private List<TagInfo> tagList;

    public TagAdapter(List<TagInfo> tagList) {
        this.tagList = tagList;
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    @Override
    public void onBindViewHolder(TagViewHolder tagViewHolder, int i) {
        TagInfo ti = tagList.get(i);
        tagViewHolder.vLocationName.setText(ti.locationName);
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.location_tag_layout, viewGroup, false);

        return new TagViewHolder(itemView);
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        protected TextView vLocationName;

        public TagViewHolder(View v) {
            super(v);
            vLocationName = (TextView) v.findViewById(R.id.tagTitle);
        }
    }
}