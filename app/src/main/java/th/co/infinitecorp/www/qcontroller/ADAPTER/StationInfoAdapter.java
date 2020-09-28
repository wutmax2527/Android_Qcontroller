package th.co.infinitecorp.www.qcontroller.ADAPTER;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class StationInfoAdapter extends RecyclerView.Adapter<StationInfoAdapter.StaItemHolder> {

    @Override
    public StationInfoAdapter.StaItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(StationInfoAdapter.StaItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StaItemHolder extends RecyclerView.ViewHolder {

        public StaItemHolder(View itemView) {
            super(itemView);
        }
    }
}
