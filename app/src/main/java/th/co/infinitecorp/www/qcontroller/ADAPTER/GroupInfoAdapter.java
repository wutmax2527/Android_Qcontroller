package th.co.infinitecorp.www.qcontroller.ADAPTER;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.GrpInfo;
import th.co.infinitecorp.www.qcontroller.R;

public class GroupInfoAdapter extends RecyclerView.Adapter<GroupInfoAdapter.GrpItemHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<GrpInfo> grpInfoList;
    private final onGrpInfoListener onGrpInfoListener;

    public interface onGrpInfoListener {
        public void onSelectedItem(GrpInfo grpInfo, int position);
    }

    public GroupInfoAdapter(Context context, List<GrpInfo> grpInfoList, GroupInfoAdapter.onGrpInfoListener onGrpInfoListener) {
        this.context = context;
        this.grpInfoList = grpInfoList;
        this.onGrpInfoListener = onGrpInfoListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public GrpItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.layout_cell_recycler, parent, false);
        GrpItemHolder holder = new GrpItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(GrpItemHolder holder, int position) {
        holder.bind(grpInfoList.get(position), onGrpInfoListener , position);
        GrpInfo info = grpInfoList.get(position);
        holder.lb_grp_id.setText(info.getID().toString());
        holder.lb_grp_name.setText(info.getName());
    }

    @Override
    public int getItemCount() { return grpInfoList.size(); }


    public class GrpItemHolder extends RecyclerView.ViewHolder {
        private TextView lb_grp_id, lb_grp_name;
        private LinearLayout container;

        public GrpItemHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            lb_grp_id = (TextView) itemView.findViewById(R.id.lb_cell_code);
            lb_grp_name = (TextView) itemView.findViewById(R.id.lb_cell_name);
        }

        public void bind(final GrpInfo item, final GroupInfoAdapter.onGrpInfoListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedItem(item,position);
                }
            });
        }
    }
}
