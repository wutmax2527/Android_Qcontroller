package th.co.infinitecorp.www.qcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

import th.co.infinitecorp.www.qcontroller.ADAPTER.EmpInfoAdapter;
import th.co.infinitecorp.www.qcontroller.API.QcontrollerApi;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.BranchInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.DivInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.EmployeeInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Management.LogMgr;
import th.co.infinitecorp.www.qcontroller.Management.Setting_System;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class SettingQActivity extends AppCompatActivity {

    private LinearLayout lt_Main, lt_Setting, lt_QDisplay;
    private LinearLayout lt_Setting_System, lt_Setting_User, lt_Setting_Profile, lt_Setting_Branch, lt_Setting_Resource;
    private LinearLayout lt_Setting_System_Server, lt_Setting_System_Branch;
    private Switch switch_mode;
    private TextView txt_system_server, txt_system_branchid,txt_branch_id,txt_branch_code,txt_branch_name,txt_branch_IP,txt_branch_open,txt_branch_close;

    private Button btn_setting, btn_QDisplay, btn_Setting_System, btn_Setting_User, btn_Setting_Profile, btn_Setting_Branch, btn_Setting_Resource, btn_save_system,btn_save_branch,btn_branch_open,btn_branch_close;
    private Button btn_bk_setting, btn_bk_setting_system, btn_home_setting_system,btn_bk_user,btn_user_addEmp,btn_home_user,btn_bk_setting_branch,btn_home_branch;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView RC_EmpInfo;


    private static enum MyPage {
        Main,
        Setting,
        QDisplay,
        System,
        User,
        Profile,
        Branch,
        Resource
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_q);
        ConnectView();
        if(GData.ONLINE_MODE)
        {
            CallAPI();
            btn_user_addEmp.setVisibility(View.GONE);
            lt_Setting_System_Server.setVisibility(View.VISIBLE);
            lt_Setting_System_Branch.setVisibility(View.VISIBLE);
        }else
        {
            btn_user_addEmp.setVisibility(View.VISIBLE);
            lt_Setting_System_Server.setVisibility(View.GONE);
            lt_Setting_System_Branch.setVisibility(View.GONE);
        }
        RefreshData();
        ChangePage(MyPage.Main);
    }

    private void ConnectView() {
        lt_Main = (LinearLayout) findViewById(R.id.lt_Main);
        lt_Setting = (LinearLayout) findViewById(R.id.lt_Setting);
        lt_QDisplay = (LinearLayout) findViewById(R.id.lt_QDisplay);

        lt_Setting_System = (LinearLayout) findViewById(R.id.lt_Setting_System);
        lt_Setting_User = (LinearLayout) findViewById(R.id.lt_Setting_User);
        lt_Setting_Profile = (LinearLayout) findViewById(R.id.lt_Setting_Profile);
        lt_Setting_Branch = (LinearLayout) findViewById(R.id.lt_Setting_Branch);
        lt_Setting_Resource = (LinearLayout) findViewById(R.id.lt_Setting_Resource);
        lt_Setting_System_Server = (LinearLayout) findViewById(R.id.lt_Setting_System_Server);
        lt_Setting_System_Branch = (LinearLayout) findViewById(R.id.lt_Setting_System_Branch);

        txt_system_server = (TextView) findViewById(R.id.txt_system_server);
        txt_system_server.setText(GData.TARGET_SERVER);
        txt_system_branchid = (TextView) findViewById(R.id.txt_system_branchid);
        txt_system_branchid.setText(GData.Branch_ID.toString());

        RC_EmpInfo = (RecyclerView)findViewById(R.id.RC_EmpInfo);

        switch_mode = (Switch) findViewById(R.id.switch_mode);
        switch_mode.setChecked(GData.ONLINE_MODE);
        switch_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    lt_Setting_System_Server.setVisibility(View.GONE);
                    lt_Setting_System_Branch.setVisibility(View.GONE);
                } else {
                    lt_Setting_System_Server.setVisibility(View.VISIBLE);
                    lt_Setting_System_Branch.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_setting = (Button) findViewById(R.id.btn_Setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Setting);
                //Log.d("Test enum", "" + MyPage.Setting);
            }
        });

        btn_QDisplay = (Button) findViewById(R.id.btn_QDisplay);
        btn_QDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.QDisplay);
            }
        });
        btn_Setting_System = (Button) findViewById(R.id.btn_Setting_System);
        btn_Setting_System.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.System);
            }
        });
        btn_Setting_User = (Button) findViewById(R.id.btn_Setting_User);
        btn_Setting_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.User);
            }
        });
        btn_Setting_Profile = (Button) findViewById(R.id.btn_Setting_Profile);
        btn_Setting_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Profile);
            }
        });
        btn_Setting_Branch = (Button) findViewById(R.id.btn_Setting_Branch);
        btn_Setting_Branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Branch);
            }
        });
        btn_Setting_Resource = (Button) findViewById(R.id.btn_Setting_Resource);
        btn_Setting_Resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Resource);
            }
        });
        btn_bk_setting = (Button)findViewById(R.id.btn_bk_setting) ;
        btn_bk_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
            }
        });
        btn_bk_setting_system = (Button) findViewById(R.id.btn_bk_setting_system);
        btn_bk_setting_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Setting);
            }
        });
        btn_home_setting_system = (Button) findViewById(R.id.btn_home_setting_system);
        btn_home_setting_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
                String aa = "";
            }
        });
        btn_bk_user = (Button)findViewById(R.id.btn_bk_user);
        btn_bk_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Setting);
            }
        });
        btn_save_system = (Button) findViewById(R.id.btn_save_system);
        btn_save_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_server = "";
                int branchID = 0;
                if (txt_system_server != null) {
                    txt_server = txt_system_server.getText().toString();
                }

                if (txt_system_branchid != null) {
                    try {
                        branchID = Integer.parseInt(txt_system_branchid.getText().toString());
                    } catch (Exception ex) {
                        branchID = -1;
                    }
                }
                Setting_System system = new Setting_System(switch_mode.isChecked(), txt_server, branchID);

                if(LogMgr.SaveSettingSystem(SettingQActivity.this,system))
                {
                    Toast.makeText(SettingQActivity.this, "บึนทึกข้อมูล STSTEM สำเร็จ", Toast.LENGTH_LONG).show();
                    GData.Branch_ID = branchID;
                    GData.TARGET_SERVER = txt_system_server.getText().toString();
                    GData.ONLINE_MODE = switch_mode.isChecked();

                }else
                {
                    Toast.makeText(SettingQActivity.this, "บึนทึกข้อมูล STSTEM สำเร็จ", Toast.LENGTH_LONG).show();
                }

                hideKeyboard(SettingQActivity.this);
                if(switch_mode.isChecked())
                {
                    CallAPI();
                    btn_user_addEmp.setVisibility(View.GONE);
                }else
                {
                    btn_user_addEmp.setVisibility(View.VISIBLE);
                    LogMgr.Delete_EmployeeInfo(SettingQActivity.this);
                }
                RefreshData();
            }
        });

        btn_home_user = (Button)findViewById(R.id.btn_home_user);
        btn_home_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
            }
        });
        btn_user_addEmp = (Button)findViewById(R.id.btn_user_addEmp);
        btn_user_addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(SettingQActivity.this);
                final View addEmpView = inflater.inflate(R.layout.layout_add_emp,null);
                final AlertDialog dialog = new AlertDialog.Builder(SettingQActivity.this).create();
                dialog.setView(addEmpView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                final TextView txt_add_emp_id = addEmpView.findViewById(R.id.txt_add_emp_id);
                final TextView txt_add_emp_code = addEmpView.findViewById(R.id.txt_add_emp_code);
                final TextView txt_add_emp_name = addEmpView.findViewById(R.id.txt_add_emp_name);
                final TextView txt_add_emp_user = addEmpView.findViewById(R.id.txt_add_emp_user);
                final TextView txt_add_emp_pass = addEmpView.findViewById(R.id.txt_add_emp_pass);

                final ImageView btn_add_emp_close = addEmpView.findViewById(R.id.btn_add_emp_close);
                btn_add_emp_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                final Button btn_user_addEmp_save = addEmpView.findViewById(R.id.btn_user_addEmp_save);
                btn_user_addEmp_save.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        String id = txt_add_emp_id.getText().toString();
                        String code = txt_add_emp_code.getText().toString();
                        String name = txt_add_emp_name.getText().toString();
                        String user = txt_add_emp_user.getText().toString();
                        String pass = txt_add_emp_pass.getText().toString();

                        if(id.equals(""))
                        {
                            txt_add_emp_id.setHintTextColor(Color.RED);
                            txt_add_emp_id.setHint("กรุณากรอก ID");
                        }
                        else if(code.equals(""))
                        {
                            txt_add_emp_code.setHintTextColor(Color.RED);
                            txt_add_emp_code.setHint("กรุณากรอก Code");
                        }
                        else if(name.equals(""))
                        {
                            txt_add_emp_name.setHintTextColor(Color.RED);
                            txt_add_emp_name.setHint("กรุณากรอก Name");
                        }
                        else if(user.equals(""))
                        {
                            txt_add_emp_user.setHintTextColor(Color.RED);
                            txt_add_emp_user.setHint("กรุณากรอก Username");
                        }
                        else if(pass.equals(""))
                        {
                            txt_add_emp_pass.setHintTextColor(Color.RED);
                            txt_add_emp_pass.setHint("กรุณากรอก Password");
                        }
                        else
                        {
                            txt_add_emp_id.setHintTextColor(Color.GRAY);
                            txt_add_emp_code.setHintTextColor(Color.GRAY);
                            txt_add_emp_name.setHintTextColor(Color.GRAY);
                            txt_add_emp_user.setHintTextColor(Color.GRAY);
                            txt_add_emp_pass.setHintTextColor(Color.GRAY);

                            //Setting_System system = LogMgr.GetSetting_System(getApplication());

                            List<EmployeeInfo> employeeInfoList = LogMgr.Load_EmployeeInfo(SettingQActivity.this);
                            if(employeeInfoList != null && employeeInfoList.size() > 0)
                            {
                                for (EmployeeInfo data : employeeInfoList)
                                {
                                    if(data.getId() != null && data.getId().equals(id))
                                    {
                                        Toast.makeText(SettingQActivity.this,"ID นี้มีอยู่ในระบบแล้ว" ,Toast.LENGTH_LONG).show();
                                        return;
                                     }else if(data.getCode() != null && data.getCode().equals(code))
                                    {
                                        Toast.makeText(SettingQActivity.this,"Code นี้มีอยู่ในระบบแล้ว" ,Toast.LENGTH_LONG).show();
                                        return;
                                    }else if(data.getUsername() != null && data.getUsername().equals(user))
                                    {
                                        Toast.makeText(SettingQActivity.this,"Username นี้มีอยู่ในระบบแล้ว" ,Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }
                            List<EmployeeInfo> empInfoList = LogMgr.Load_EmployeeInfo(SettingQActivity.this);
                            EmployeeInfo empNewData = new EmployeeInfo(GData.Branch_ID,id,code,name,user,pass);
                            empInfoList.add(empNewData);
                            if(LogMgr.Save_EmployeeInfo(SettingQActivity.this,empInfoList))
                            {
                                Toast.makeText(SettingQActivity.this,"บันทึกข้อมูลพนักงานสำเร็จ",Toast.LENGTH_LONG).show();
                                RefreshData();
                                dialog.dismiss();
                            }else
                                {
                                    Toast.makeText(SettingQActivity.this,"บันทึกข้อมูลพนักงานไม่สำเร็จ",Toast.LENGTH_LONG).show();
                                }

                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        btn_bk_setting_branch = (Button)findViewById(R.id.btn_bk_setting_branch);
        btn_bk_setting_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Setting);
            }
        });
        btn_home_branch = (Button)findViewById(R.id.btn_home_branch);
        btn_home_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
            }
        });
        txt_branch_id = (TextView)findViewById(R.id.txt_branch_id);
        txt_branch_code = (TextView)findViewById(R.id.txt_branch_code);
        txt_branch_name = (TextView)findViewById(R.id.txt_branch_name);
        txt_branch_IP = (TextView)findViewById(R.id.txt_branch_IP);
        txt_branch_open = (TextView)findViewById(R.id.txt_branch_open);
        txt_branch_close = (TextView)findViewById(R.id.txt_branch_close);
        btn_branch_open = (Button)findViewById(R.id.btn_branch_open);
        btn_branch_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog picker = new TimePickerDialog(SettingQActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                txt_branch_open.setText(String.format("%02d", sHour) + ":" + String.format("%02d", sMinute));
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
        btn_branch_close = (Button)findViewById(R.id.btn_branch_close);
        btn_branch_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog picker = new TimePickerDialog(SettingQActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                txt_branch_close.setText(String.format("%02d", sHour) + ":" + String.format("%02d", sMinute));
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
        btn_save_branch = (Button)findViewById(R.id.btn_save_branch);
        btn_save_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = txt_branch_IP.getText().toString();
                if(ip.equals(""))
                {
                    Toast.makeText(SettingQActivity.this,"กรุณากรอก IP",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

    }

    private void RefreshData()
    {
        //EmployeeInfo
        List<EmployeeInfo> employeeInfos = LogMgr.Load_EmployeeInfo(SettingQActivity.this);
        EmpInfoAdapter adapter = new EmpInfoAdapter(SettingQActivity.this, employeeInfos, new EmpInfoAdapter.onEmpInfoListener() {
            @Override
            public void onSelectedItem(EmployeeInfo employeeInfo, int position) {
                LayoutInflater inflater = LayoutInflater.from(SettingQActivity.this);
                final View DtEmpView = inflater.inflate(R.layout.layout_detail_employee,null);
                final AlertDialog dialog = new AlertDialog.Builder(SettingQActivity.this).create();
                dialog.setView(DtEmpView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                final ImageView btn_dt_emp_close = DtEmpView.findViewById(R.id.btn_dt_emp_close);
                btn_dt_emp_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final TextView txt_dt_id = (TextView)DtEmpView.findViewById(R.id.txt_dt_id);
                txt_dt_id.setText(employeeInfo.getId());
                final TextView txt_dt_code = (TextView)DtEmpView.findViewById(R.id.txt_dt_code);
                txt_dt_code.setText(employeeInfo.getCode());
                final TextView txt_dt_name = (TextView)DtEmpView.findViewById(R.id.txt_dt_name);
                txt_dt_name.setText(employeeInfo.getName());
                final TextView txt_dt_initials = (TextView)DtEmpView.findViewById(R.id.txt_dt_initials);
                txt_dt_initials.setText(employeeInfo.getInitials());
                final TextView txt_dt_company = (TextView)DtEmpView.findViewById(R.id.txt_dt_company);
                txt_dt_company.setText(employeeInfo.getCompany());
                final TextView txt_dt_department = (TextView)DtEmpView.findViewById(R.id.txt_dt_department);
                txt_dt_department.setText(employeeInfo.getDepartment());
                final TextView txt_dt_division = (TextView)DtEmpView.findViewById(R.id.txt_dt_division);
                txt_dt_division.setText(employeeInfo.getDivision());
                final TextView txt_dt_phone = (TextView)DtEmpView.findViewById(R.id.txt_dt_phone);
                txt_dt_phone.setText(employeeInfo.getTelephone());
                final TextView txt_dt_title = (TextView)DtEmpView.findViewById(R.id.txt_dt_title);
                txt_dt_title.setText(employeeInfo.getTitle());
                final TextView txt_dt_address = (TextView)DtEmpView.findViewById(R.id.txt_dt_address);
                txt_dt_address.setText(employeeInfo.getAddress1());

                dialog.show();
            }
        });
        layoutManager = new LinearLayoutManager(this); //new GridLayoutManager(this,2);
        RC_EmpInfo.setLayoutManager(layoutManager);
        RC_EmpInfo.setAdapter(adapter);

        //Branch Info
        txt_branch_id.setText("Branch ID : " + GData.Branch_ID.toString());
        List<BranchInfo> branchInfoList = LogMgr.Load_BranchInfo(SettingQActivity.this);
        if(branchInfoList != null && branchInfoList.size() > 0)
        {
            //txt_branch_code.setText();
        }
    }
    private void CallAPI()
    {
        //EmployeeInfo
        String searchEmp = "{'branch_id' : '" + GData.Branch_ID + "'}";
        new QcontrollerApi().RequestEmployeeInfoDetail(searchEmp, new QcontrollerApi.EmployeeInfoDetailListener() {
            @Override
            public void onEmployeeInfoResult(List<EmployeeInfo> info, Integer http_code) {
                if (http_code == 200) {
                    LogMgr.Save_EmployeeInfo(SettingQActivity.this,info);
                }
            }
        });

        //DivInfo
        String searchDiv = "{BRANCH_ID:" + GData.Branch_ID.toString() + "}";
        new QcontrollerApi().RequestBranchInfoDetail(searchDiv, new QcontrollerApi.BranchInfoDetailListener() {
            @Override
            public void onBranchInfoResult(List<BranchInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("BranchInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_BranchInfo(SettingQActivity.this,info);
                }
            }
        });

        RefreshData();
    }

    private void ChangePage(MyPage page) {
        hideKeyboard(SettingQActivity.this);
        SetHidePage();
        switch (page) {
            case Main: {
                lt_Main.setVisibility(View.VISIBLE);
                break;
            }
            case Setting: {
                lt_Setting.setVisibility(View.VISIBLE);
                break;
            }
            case QDisplay: {
                lt_QDisplay.setVisibility(View.VISIBLE);
                break;
            }
            case System: {
                lt_Setting_System.setVisibility(View.VISIBLE);
                break;
            }
            case User: {
                lt_Setting_User.setVisibility(View.VISIBLE);
                //EmployeeInfo();
                break;
            }
            case Profile: {
                lt_Setting_Profile.setVisibility(View.VISIBLE);
                break;
            }
            case Resource: {
                lt_Setting_Resource.setVisibility(View.VISIBLE);
                break;
            }
            case Branch: {
                lt_Setting_Branch.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void SetHidePage() {
        for (MyPage p : MyPage.values()) {
            switch (p) {
                case Main: {
                    lt_Main.setVisibility(View.GONE);
                    break;
                }
                case Setting: {
                    lt_Setting.setVisibility(View.GONE);
                    break;
                }
                case QDisplay: {
                    lt_QDisplay.setVisibility(View.GONE);
                    break;
                }
                case System: {
                    lt_Setting_System.setVisibility(View.GONE);
                    break;
                }
                case User: {
                    lt_Setting_User.setVisibility(View.GONE);
                    break;
                }
                case Profile: {
                    lt_Setting_Profile.setVisibility(View.GONE);
                    break;
                }
                case Resource: {
                    lt_Setting_Resource.setVisibility(View.GONE);
                    break;
                }
                case Branch: {
                    lt_Setting_Branch.setVisibility(View.GONE);
                    break;
                }
            }
        }
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}