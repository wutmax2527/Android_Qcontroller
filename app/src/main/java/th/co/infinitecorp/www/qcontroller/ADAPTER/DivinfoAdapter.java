package th.co.infinitecorp.www.qcontroller.ADAPTER;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.DivInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.StaInfo;
import th.co.infinitecorp.www.qcontroller.R;

public class DivinfoAdapter extends RecyclerView.Adapter<DivinfoAdapter.DivItemHolder> {

    Context context;
    LayoutInflater layoutInflater;
    List<DivInfo> divInfoList;
    private DivinfoAdapter.onDivInfoListener onDivInfoListener;
    public interface onDivInfoListener {
        public void onSelectedItem(DivInfo divInfo, int position);
    }

    public DivinfoAdapter(Context context, List<DivInfo> divInfoList, DivinfoAdapter.onDivInfoListener onDivInfoListener) {
        this.context = context;
        this.divInfoList = divInfoList;
        this.onDivInfoListener = onDivInfoListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public DivinfoAdapter.DivItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.layout_cell_recycler,parent,false);
        return new DivinfoAdapter.DivItemHolder(v);
    }

    @Override
    public void onBindViewHolder(DivinfoAdapter.DivItemHolder holder, int position) {
        holder.bind(divInfoList.get(position), onDivInfoListener , position);
        DivInfo info = divInfoList.get(position);
        holder.lb_cell_div_id.setText(info.getID().toString());
        holder.lb_cell_div_name.setText(info.getName());
    }

    @Override
    public int getItemCount() { return divInfoList.size(); }

    public class DivItemHolder extends RecyclerView.ViewHolder {
        private TextView lb_cell_div_id, lb_cell_div_name;
        private LinearLayout container;
        public DivItemHolder(View itemView) {
            super(itemView);
            lb_cell_div_id = (TextView) itemView.findViewById(R.id.lb_cell_code);
            lb_cell_div_name = (TextView) itemView.findViewById(R.id.lb_cell_name);
        }
        public void bind(final DivInfo item, final DivinfoAdapter.onDivInfoListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedItem(item,position);
                }
            });
        }
    }
}
