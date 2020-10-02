package th.co.infinitecorp.www.qcontroller.ADAPTER;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.DivInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.EmployeeInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.GrpInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.PF_DIVMAP;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.StaInfo;
import th.co.infinitecorp.www.qcontroller.Management.LogMgr;
import th.co.infinitecorp.www.qcontroller.R;
import th.co.infinitecorp.www.qcontroller.SettingQActivity;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ItemHolder> {
    Context context;
    LayoutInflater layoutInflater;
    String TAG;
    List<DivInfo> divInfoList = new ArrayList<>();
    List<EmployeeInfo> employeeInfos = new ArrayList<>();
    List<StaInfo> staInfoList = new ArrayList<>();
    List<GrpInfo> grpInfoList = new ArrayList<>();
    List<PF_DIVMAP> pfDivmapList = new ArrayList<>();

    private AdapterRecycler.onDivMapInfoListener onDivMapInfoListener;
    private AdapterRecycler.onEmpInfoListener onEmpInfoListener;
    private AdapterRecycler.onDivInfoListener onDivInfoListener;
    private AdapterRecycler.onGrpInfoListener onGrpInfoListener;
    private AdapterRecycler.onStaInfoListener onStaInfoListener;

    public interface onDivInfoListener {
        void onSelectedItem(DivInfo divInfo, int position);
    }
    public interface onEmpInfoListener {
        void onSelectedEmpItem(EmployeeInfo employeeInfo, int position);
    }
    public interface onStaInfoListener {
        public void onSelectedItem(StaInfo staInfo, int position);
    }
    public interface onGrpInfoListener {
        void onSelectedItem(GrpInfo grpInfo, int position);
    }
    public interface onDivMapInfoListener {
        void onSelectedPFDivMap(PF_DIVMAP pfDivmap , int positon);
    }

    //region Contructer
    public AdapterRecycler(Context context, SettingQActivity.TAG_RC TAG_RC,int GroupID, AdapterRecycler.onDivMapInfoListener onDivMapInfoListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.onDivMapInfoListener = onDivMapInfoListener;
        this.TAG = TAG_RC.toString();
        List<PF_DIVMAP> info = LogMgr.Load_PF_DIVMAPInfo(context);
        for(PF_DIVMAP s : info)
        {
            if(GroupID == s.getGROUP_ID())
            {
                this.pfDivmapList.add(s);
            }
        }
    }

    public AdapterRecycler(Context context, SettingQActivity.TAG_RC TAG_RC, AdapterRecycler.onEmpInfoListener onEmpInfoListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.TAG = TAG_RC.toString();
        this.onEmpInfoListener = onEmpInfoListener;
        this.employeeInfos = LogMgr.Load_EmployeeInfo(context);
    }

    public AdapterRecycler(Context context, SettingQActivity.TAG_RC TAG_RC, AdapterRecycler.onDivInfoListener onDivInfoListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.TAG = TAG_RC.toString();
        this.onDivInfoListener = onDivInfoListener;
        this.divInfoList = LogMgr.Load_DivInfo(context);
    }

    public AdapterRecycler(Context context,SettingQActivity.TAG_RC TAG_RC, AdapterRecycler.onGrpInfoListener onGrpInfoListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.TAG = TAG_RC.toString();
        this.onGrpInfoListener = onGrpInfoListener;
        this.grpInfoList = LogMgr.Load_GrpInfo(context);
    }

    public AdapterRecycler(Context context, SettingQActivity.TAG_RC TAG_RC, AdapterRecycler.onStaInfoListener onStaInfoListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.TAG = TAG_RC.toString();
        this.onStaInfoListener = onStaInfoListener;
        this.staInfoList = LogMgr.Load_StaInfo(context);
    }
    //endregion

    @Override
    public AdapterRecycler.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (TAG)
        {
            case "DivisionInfo" :
                {
                    v = layoutInflater.inflate(R.layout.layout_cell_recycler,parent,false);
                    break;
                }
            case "EmpInfo":
                {
                    v = layoutInflater.inflate(R.layout.layout_cell_recycler, parent, false);
                    break;
                }
            case "StationInfo":
                {
                    v = layoutInflater.inflate(R.layout.layout_cell_sta,parent,false);
                    break;
                }
            case "GroupInfo":
                {
                    v = layoutInflater.inflate(R.layout.layout_cell_recycler, parent, false);
                    break;
                }
            case "PF_DIVMAP":
                {
                    v = layoutInflater.inflate(R.layout.layout_cell_sta,parent,false);
                    break;
                }
        }
        return new AdapterRecycler.ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterRecycler.ItemHolder holder, int position) {
        switch (TAG)
        {
            case "DivisionInfo" :
            {
                holder.bindDiv(divInfoList.get(position), onDivInfoListener , position);
                DivInfo info = divInfoList.get(position);
                holder.lb_cell_div_id.setText(info.getID().toString());
                holder.lb_cell_div_name.setText(info.getName());
                break;
            }
            case "EmpInfo":
            {
                holder.bindEmp(employeeInfos.get(position), onEmpInfoListener,position);
                EmployeeInfo info = employeeInfos.get(position);
                holder.lb_emp_code.setText(info.getCode());
                holder.lb_emp_name.setText(info.getName());
                break;
            }
            case "StationInfo":
            {
                holder.bindSta(staInfoList.get(position), onStaInfoListener , position);
                StaInfo info = staInfoList.get(position);
                holder.lb_cell_sta_id.setText(info.getID().toString());
                holder.lb_cell_sta_name.setText(info.getName());
                if(!info.getGroup_ID().equals(-1))
                {
                    holder.lb_cell_sta_group_id.setText(info.getGroup_ID().toString());
                }
                break;
            }
            case "GroupInfo":
            {
                holder.bindGrp(grpInfoList.get(position), onGrpInfoListener , position);
                GrpInfo info = grpInfoList.get(position);
                holder.lb_grp_id.setText(info.getID().toString());
                holder.lb_grp_name.setText(info.getName());
                break;
            }
            case "PF_DIVMAP":
            {
                holder.bindDivMap(pfDivmapList.get(position), onDivMapInfoListener , position);
                PF_DIVMAP info = pfDivmapList.get(position);
                holder.lb_dm_div_id.setText(info.getDIVISION_ID().toString());
                for (DivInfo data :LogMgr.Load_DivInfo(context)) {
                    if(data.getID().equals(info.getDIVISION_ID()))
                    {
                        holder.lb_dm_div_name.setText(data.getName());
                        break;
                    }
                }
                if(!info.getGROUP_ID().equals(-1))
                {
                    holder.lb_dm_grp_id.setText(info.getGROUP_ID().toString());
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        switch (TAG)
        {
            case "DivisionInfo" :
            {
                return divInfoList.size();
            }
            case "EmpInfo":
            {
                return employeeInfos.size();
            }
            case "StationInfo":
            {
                return staInfoList.size();
            }
            case "GroupInfo":
            {
                return grpInfoList.size();
            }
            case "PF_DIVMAP":
            {
                return pfDivmapList.size();
            }
        }
        return 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView lb_cell_div_id, lb_cell_div_name;
        private TextView lb_emp_code, lb_emp_name;
        private TextView lb_cell_sta_id, lb_cell_sta_name,lb_cell_sta_group_id;
        private TextView lb_grp_id, lb_grp_name;
        private TextView lb_dm_div_id,lb_dm_grp_id,lb_dm_div_name;
        public ItemHolder(View itemView) {
            super(itemView);
            switch (TAG)
            {
                case "DivisionInfo" :
                {
                    lb_cell_div_id = (TextView) itemView.findViewById(R.id.lb_cell_code);
                    lb_cell_div_name = (TextView) itemView.findViewById(R.id.lb_cell_name);
                    break;
                }
                case "EmpInfo":
                {
                    lb_emp_code = (TextView) itemView.findViewById(R.id.lb_cell_code);
                    lb_emp_name = (TextView) itemView.findViewById(R.id.lb_cell_name);
                    break;
                }
                case "StationInfo":
                {
                    lb_cell_sta_id = (TextView) itemView.findViewById(R.id.lb_cell_id);
                    lb_cell_sta_name = (TextView) itemView.findViewById(R.id.lb_cell_name);
                    lb_cell_sta_group_id = (TextView) itemView.findViewById(R.id.lb_cell_group_id);
                    break;
                }
                case "GroupInfo":
                {
                    lb_grp_id = (TextView) itemView.findViewById(R.id.lb_cell_code);
                    lb_grp_name = (TextView) itemView.findViewById(R.id.lb_cell_name);
                    break;
                }
                case "PF_DIVMAP":
                {
                    lb_dm_div_id = (TextView) itemView.findViewById(R.id.lb_cell_id);
                    lb_dm_grp_id = (TextView) itemView.findViewById(R.id.lb_cell_group_id);
                    lb_dm_div_name= (TextView) itemView.findViewById(R.id.lb_cell_name);
                    break;
                }
            }
        }

        public void bindDiv(final DivInfo item, final AdapterRecycler.onDivInfoListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedItem(item,position);
                }
            });
        }
        public void bindEmp(final EmployeeInfo item, final AdapterRecycler.onEmpInfoListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedEmpItem(item,position);
                }
            });
        }
        public void bindSta(final StaInfo item, final AdapterRecycler.onStaInfoListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedItem(item,position);
                }
            });
        }
        public void bindGrp(final GrpInfo item, final AdapterRecycler.onGrpInfoListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedItem(item,position);
                }
            });
        }
        public void bindDivMap(final PF_DIVMAP item, final AdapterRecycler.onDivMapInfoListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedPFDivMap(item,position);
                }
            });
        }
    }
}
