package th.co.infinitecorp.www.qcontroller.ADAPTER;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.EmployeeInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.GrpInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.StaInfo;
import th.co.infinitecorp.www.qcontroller.R;

public class StationInfoAdapter extends RecyclerView.Adapter<StationInfoAdapter.StaItemHolder> {

    Context context;
    LayoutInflater layoutInflater;
    List<StaInfo> staInfoList;
    private StationInfoAdapter.onStaInfoListener onStaInfoListener;
    public interface onStaInfoListener {
        public void onSelectedItem(StaInfo staInfo, int position);
    }

    public StationInfoAdapter(Context context, List<StaInfo> staInfoList, StationInfoAdapter.onStaInfoListener onStaInfoListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.staInfoList = staInfoList;
        this.onStaInfoListener = onStaInfoListener;
    }

    @Override
    public StationInfoAdapter.StaItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.layout_cell_sta,parent,false);
        return new StationInfoAdapter.StaItemHolder(v);
    }

    @Override
    public void onBindViewHolder(StationInfoAdapter.StaItemHolder holder, int position) {
        holder.bind(staInfoList.get(position), onStaInfoListener , position);
        StaInfo info = staInfoList.get(position);
        holder.lb_cell_sta_id.setText(info.getID().toString());
        holder.lb_cell_sta_name.setText(info.getName());
        if(!info.getGroup_ID().equals(-1))
        {
            holder.lb_cell_sta_group_id.setText(info.getGroup_ID().toString());
        }

    }

    @Override
    public int getItemCount() {
        return staInfoList.size();
    }

    public class StaItemHolder extends RecyclerView.ViewHolder {
        private TextView lb_cell_sta_id, lb_cell_sta_name,lb_cell_sta_group_id;
        private LinearLayout container;

        public StaItemHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            lb_cell_sta_id = (TextView) itemView.findViewById(R.id.lb_cell_sta_id);
            lb_cell_sta_name = (TextView) itemView.findViewById(R.id.lb_cell_sta_name);
            lb_cell_sta_group_id = (TextView) itemView.findViewById(R.id.lb_cell_sta_group_id);
        }
        public void bind(final StaInfo item, final StationInfoAdapter.onStaInfoListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedItem(item,position);
                }
            });
        }
    }
}
