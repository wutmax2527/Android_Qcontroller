package th.co.infinitecorp.www.qcontroller.ADAPTER;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.EmployeeInfo;
import th.co.infinitecorp.www.qcontroller.R;

public class EmpInfoAdapter extends RecyclerView.Adapter<EmpInfoAdapter.EmpItemHolder> {

    Context context;
    LayoutInflater layoutInflater;
    List<EmployeeInfo> employeeInfos;
    private final onEmpInfoListener empInfoListener;

    public EmpInfoAdapter(Context context, List<EmployeeInfo> employeeInfos, onEmpInfoListener empInfoListener) {
        this.context = context;
        this.employeeInfos = employeeInfos;
        this.empInfoListener = empInfoListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public interface onEmpInfoListener {
        public void onSelectedItem(EmployeeInfo employeeInfo, int position);
    }

    @NonNull
    @Override
    public EmpItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cell_employee, parent, false);
        View v = layoutInflater.inflate(R.layout.layout_cell_employee, parent, false);
        EmpItemHolder viewHolder = new EmpItemHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmpItemHolder holder, int position) {
        holder.bind(employeeInfos.get(position), empInfoListener,position);
        EmployeeInfo info = employeeInfos.get(position);
        holder.lb_emp_code.setText(info.getCode());
        holder.lb_emp_name.setText(info.getName());

    }

    @Override
    public int getItemCount() {
        return employeeInfos.size();
    }

    public class EmpItemHolder extends RecyclerView.ViewHolder {
        private TextView lb_emp_code, lb_emp_name;
        private LinearLayout container;

        public EmpItemHolder(@NonNull View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            lb_emp_code = (TextView) itemView.findViewById(R.id.lb_emp_code);
            lb_emp_name = (TextView) itemView.findViewById(R.id.lb_emp_name);
        }

        public void bind(final EmployeeInfo item, final onEmpInfoListener listener,final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedItem(item,position);
                }
            });
        }
    }
}
