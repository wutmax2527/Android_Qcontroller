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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import th.co.infinitecorp.www.qcontroller.ADAPTER.DivinfoAdapter;
import th.co.infinitecorp.www.qcontroller.ADAPTER.EmpInfoAdapter;
import th.co.infinitecorp.www.qcontroller.ADAPTER.GroupInfoAdapter;
import th.co.infinitecorp.www.qcontroller.ADAPTER.StationInfoAdapter;
import th.co.infinitecorp.www.qcontroller.API.QcontrollerApi;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.BranchInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.DivInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.EmployeeInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.GrpInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.StaInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.Mapping.DivMapGroupInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Management.LogMgr;
import th.co.infinitecorp.www.qcontroller.Management.Setting_System;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class SettingQActivity extends AppCompatActivity {

    private LinearLayout lt_Main, lt_Setting, lt_QDisplay;
    private LinearLayout lt_Setting_System, lt_Setting_User, lt_Setting_Profile, lt_Setting_Branch, lt_Setting_Resource;
    private LinearLayout lt_Setting_System_Server, lt_Setting_System_Branch,lt_Setting_Group,lt_Setting_Div,lt_Setting_Station,lt_Setting_DivMap;
    private Switch switch_mode;
    private TextView txt_system_server, txt_system_branchid,txt_branch_id,txt_branch_code,txt_branch_name,txt_branch_IP,txt_branch_open,txt_branch_close;


    private Button btn_setting, btn_QDisplay, btn_Setting_System, btn_Setting_User, btn_Setting_Profile, btn_Setting_Branch, btn_Setting_Resource, btn_save_system,btn_save_branch,btn_branch_open,btn_branch_close;
    private Button btn_bk_setting, btn_bk_setting_system, btn_home_setting_system,btn_bk_user,btn_user_addEmp,btn_home_user,btn_bk_setting_branch,btn_home_branch,btn_bk_profile,btn_home_profile;
    private Button btn_bk_group,btn_home_group,btn_add_group,btn_bk_station,btn_home_station,btn_add_station,btn_add_div;
    private Button btn_group,btn_div,btn_station,btn_divmap;
    private Button btn_bk_div,btn_home_div,btn_bk_div_map,btn_home_div_map,btn_add_div_map;

    RecyclerView.LayoutManager layoutManagerEmp,layoutManagerGrp,layoutManagerSta,layoutManagerDiv,layoutManagerDivMap;
    RecyclerView RC_EmpInfo,RC_GroupInfo,RC_Stainfo,RC_Divinfo,RC_DivMap;

    private static enum MyPage {
        Main,
        Setting,
        QDisplay,
        System,
        User,
        Profile,
        Branch,
        Resource,
        Group,
        Division,
        Station,
        Division_Map
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
            btn_add_group.setVisibility(View.GONE);
            btn_add_station.setVisibility(View.GONE);
            btn_add_div.setVisibility(View.GONE);
            lt_Setting_System_Server.setVisibility(View.VISIBLE);
            lt_Setting_System_Branch.setVisibility(View.VISIBLE);
        }else
        {
            btn_user_addEmp.setVisibility(View.VISIBLE);
            btn_add_group.setVisibility(View.VISIBLE);
            btn_add_station.setVisibility(View.VISIBLE);
            btn_add_div.setVisibility(View.VISIBLE);
            lt_Setting_System_Server.setVisibility(View.GONE);
            lt_Setting_System_Branch.setVisibility(View.GONE);
            LogMgr.Delete_EmployeeInfo(SettingQActivity.this);
            LogMgr.Delete_GrpInfo(SettingQActivity.this);
            LogMgr.Delete_StaInfo(SettingQActivity.this);
            LogMgr.Delete_DivInfo(SettingQActivity.this);
            RefreshDataEmp();
            RefreshGroup();
            RefreshStation();
            RefreshDiv();
        }

        RefreshDataEmp();
        RefreshDataBranch();
        RefreshGroup();
        ChangePage(MyPage.Main);
    }

    private void ConnectView() {

        //region LinearLayout
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

        lt_Setting_Group = (LinearLayout) findViewById(R.id.lt_Setting_Group);
        lt_Setting_Div = (LinearLayout) findViewById(R.id.lt_Setting_Div);
        lt_Setting_Station = (LinearLayout) findViewById(R.id.lt_Setting_Station);
        lt_Setting_DivMap = (LinearLayout) findViewById(R.id.lt_Setting_DivMap);
        //endregion

        //region Main
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
        //endregion

        //region Setting
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
        //endregion

        //region System
        txt_system_server = (TextView) findViewById(R.id.txt_system_server);
        txt_system_server.setText(GData.TARGET_SERVER);
        txt_system_branchid = (TextView) findViewById(R.id.txt_system_branchid);
        txt_system_branchid.setText(GData.Branch_ID.toString());
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
                    btn_add_group.setVisibility(View.GONE);
                    btn_add_station.setVisibility(View.GONE);
                    btn_add_div.setVisibility(View.GONE);
                }else
                {
                    btn_user_addEmp.setVisibility(View.VISIBLE);
                    btn_add_group.setVisibility(View.VISIBLE);
                    btn_add_station.setVisibility(View.VISIBLE);
                    btn_add_div.setVisibility(View.VISIBLE);
                    LogMgr.Delete_EmployeeInfo(SettingQActivity.this);
                    LogMgr.Delete_GrpInfo(SettingQActivity.this);
                    LogMgr.Delete_StaInfo(SettingQActivity.this);
                    LogMgr.Delete_DivInfo(SettingQActivity.this);
                    RefreshDataEmp();
                    RefreshGroup();
                    RefreshStation();
                    RefreshDiv();
                }
            }
        });
        //endregion

        //region EmployeeInfo
        RC_EmpInfo = (RecyclerView)findViewById(R.id.RC_EmpInfo);
        btn_bk_user = (Button)findViewById(R.id.btn_bk_user);
        btn_bk_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Setting);
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
                                RefreshDataEmp();
                                RefreshDataBranch();
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
        //endregion

        //region Branch Info
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
                String code = txt_branch_code.getText().toString();
                String name = txt_branch_name.getText().toString();
                String openTime = txt_branch_open.getText().toString();
                String closeTime = txt_branch_close.getText().toString();

                if(ip.equals(""))
                {
                    Toast.makeText(SettingQActivity.this,"กรุณากรอก IP",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!validIP(ip))
                {
                    Toast.makeText(SettingQActivity.this,"รูปแบบ IP ไม่ถูกต้อง (xxx.xxx.xxx.xxx)",Toast.LENGTH_LONG).show();
                    return;
                }

                List<BranchInfo> branchInfoArrayList = new ArrayList<>();
                List<BranchInfo> branchInfoList = LogMgr.Load_BranchInfo(SettingQActivity.this);
                if(branchInfoList != null && branchInfoList.size() > 0)
                {
                    BranchInfo branchInfo = branchInfoList.get(0);
                    branchInfo.setCode(code);
                    branchInfo.setName(name);
                    branchInfo.setIPAddress(ip);
                    branchInfo.setOpen(openTime);
                    branchInfo.setClose(closeTime);
                    branchInfoArrayList.add(branchInfo);
                }
                LogMgr.Save_BranchInfo(SettingQActivity.this ,branchInfoArrayList );

            }
        });
        //endregion

        //region Profile
        btn_bk_profile = (Button)findViewById(R.id.btn_bk_profile);
        btn_bk_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Setting);
            }
        });
        btn_home_profile = (Button)findViewById(R.id.btn_home_profile);
        btn_home_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
            }
        });

        btn_group = (Button)findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Group);
            }
        });
        btn_div = (Button)findViewById(R.id.btn_div);
        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Division);
            }
        });
        btn_station = (Button)findViewById(R.id.btn_station);
        btn_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Station);
            }
        });
        btn_divmap = (Button)findViewById(R.id.btn_divmap);
        btn_divmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Division_Map);
            }
        });
        //endregion

        //region Setting Group
        RC_GroupInfo = (RecyclerView)findViewById(R.id.RC_GroupInfo);
        btn_bk_group = (Button)findViewById(R.id.btn_bk_group);
        btn_bk_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Profile);
            }
        });
        btn_home_group = (Button)findViewById(R.id.btn_home_group);
        btn_home_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
            }
        });
        btn_add_group =(Button) findViewById(R.id.btn_add_group);
        btn_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(SettingQActivity.this);
                final View addGroupView = inflater.inflate(R.layout.layout_add_group,null);
                final AlertDialog dialog = new AlertDialog.Builder(SettingQActivity.this).create();
                dialog.setView(addGroupView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                final ImageView btn_dt_group_close = addGroupView.findViewById(R.id.btn_dt_group_close);
                btn_dt_group_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                final TextView txt_group_id = addGroupView.findViewById(R.id.txt_group_id);
                final TextView  txt_group_name = addGroupView.findViewById(R.id.txt_group_name);
                final TextView  txt_group_detail = addGroupView.findViewById(R.id.txt_group_detail);
                final TextView  btn_save_group = addGroupView.findViewById(R.id.btn_save_group);
                btn_save_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = txt_group_id.getText().toString();
                        String name = txt_group_name.getText().toString();
                        String detail = txt_group_detail.getText().toString();
                        int ID = -1;
                        if(id.equals(""))
                        {
                            Toast.makeText(SettingQActivity.this,"กรุณากรอก Group ID",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else
                        {
                            List<GrpInfo> grpInfos = LogMgr.Load_GrpInfo(SettingQActivity.this);
                            if(grpInfos != null && grpInfos.size() > 0)
                            {
                                for(GrpInfo data : grpInfos)
                                {
                                    if(data.getID().equals(id))
                                    {
                                        Toast.makeText(SettingQActivity.this,"ID นี้มีการใช้งานอยู่ฝนระบบแล้ว",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }
                        }
                        try
                        {
                            ID = Integer.parseInt(id);
                        }catch (Exception ex)
                        {
                            Toast.makeText(SettingQActivity.this,"ID ต้องเป็นตัวเลขเท่านั้น",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(name.equals(""))
                        {
                            Toast.makeText(SettingQActivity.this,"กรุณากรอกชื่อ Group",Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<GrpInfo> grpInfos = LogMgr.Load_GrpInfo(SettingQActivity.this);
                        GrpInfo grpInfo = new GrpInfo(GData.Branch_ID,ID,name,detail);
                        grpInfos.add(grpInfo);
                        if(LogMgr.Save_GrpInfo(SettingQActivity.this,grpInfos))
                        {
                            Toast.makeText(SettingQActivity.this,"บันทึกข้อมูลสำเร็จ",Toast.LENGTH_LONG).show();
                            RefreshGroup();
                            dialog.dismiss();
                        }else
                        {
                            Toast.makeText(SettingQActivity.this,"บันทึกข้อมูลไม่สำเร็จ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        //endregion

        //region Setting Divinfo
        btn_bk_div = (Button)findViewById(R.id.btn_bk_div);
        btn_bk_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Profile);
            }
        });
        btn_home_div = (Button)findViewById(R.id.btn_home_div);
        btn_home_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
            }
        });
        RC_Divinfo = (RecyclerView)findViewById(R.id.RC_Divinfo);
        btn_add_div = (Button)findViewById(R.id.btn_add_div);
        btn_add_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(SettingQActivity.this);
                final View addDivView = inflater.inflate(R.layout.layout_add_div,null);
                final AlertDialog dialog = new AlertDialog.Builder(SettingQActivity.this).create();
                dialog.setView(addDivView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                final ImageView btn_dt_div_close = addDivView.findViewById(R.id.btn_dt_div_close);
                btn_dt_div_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { dialog.dismiss(); }});
                final TextView txt_div_id = addDivView.findViewById(R.id.txt_div_id);
                txt_div_id.setTransformationMethod(null);
                final TextView txt_div_name = addDivView.findViewById(R.id.txt_div_name);
                final TextView txt_div_desc = addDivView.findViewById(R.id.txt_div_desc);
                final TextView txt_div_qstart = addDivView.findViewById(R.id.txt_div_qstart);
                txt_div_qstart.setTransformationMethod(null);
                final TextView txt_div_qend = addDivView.findViewById(R.id.txt_div_qend);
                txt_div_qend.setTransformationMethod(null);
                final TextView txt_div_copy = addDivView.findViewById(R.id.txt_div_copy);
                txt_div_copy.setTransformationMethod(null);
                final TextView txt_div_alert = addDivView.findViewById(R.id.txt_div_alert);
                txt_div_alert.setTransformationMethod(null);
                final TextView txt_div_seq = addDivView.findViewById(R.id.txt_div_seq);
                txt_div_seq.setTransformationMethod(null);

                final Button btn_save_div = addDivView.findViewById(R.id.btn_save_div);
                btn_save_div.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txt_id = txt_div_id.getText().toString();
                        String name = txt_div_name.getText().toString();
                        String desc = txt_div_desc.getText().toString();
                        String txt_qstart = txt_div_qstart.getText().toString();
                        String txt_qend = txt_div_qend.getText().toString();
                        String txt_copy = txt_div_copy.getText().toString();
                        String alert = txt_div_alert.getText().toString();
                        String txt_seq = txt_div_seq.getText().toString();
                        Integer id = 0;
                        Integer qstart = 0;
                        Integer qend = 0;
                        Integer copy = 0;
                        Integer seq = 0;

                        int ID = -1;
                        if(txt_id.equals(""))
                        {
                            Toast.makeText(SettingQActivity.this,"กรุณากรอก Division ID",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else
                        {
                            List<DivInfo> divInfos = LogMgr.Load_DivInfo(SettingQActivity.this);
                            if(divInfos != null && divInfos.size() > 0)
                            {
                                for(DivInfo data : divInfos)
                                {
                                    if(data.getID().equals(txt_id))
                                    {
                                        Toast.makeText(SettingQActivity.this,"ID นี้มีการใช้งานอยู่ฝนระบบแล้ว",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }
                        }
                        if(name.equals(""))
                        {
                            Toast.makeText(SettingQActivity.this,"กรุณากรอกชื่อ Division",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(txt_qstart.equals(""))
                        {
                            txt_qstart = "0";
                        }
                        if(txt_qend.equals(""))
                        {
                            txt_qend = "0";
                        }
                        if(txt_copy.equals(""))
                        {
                            txt_copy = "0";
                        }
                        if(alert.equals(""))
                        {
                            alert = "0";
                        }
                        if(txt_seq.equals(""))
                        {
                            txt_seq = "0";
                        }

                        id = Integer.parseInt(txt_id);
                        qstart = Integer.parseInt(txt_qstart);
                        qend = Integer.parseInt(txt_qend);
                        copy = Integer.parseInt(txt_copy);
                        seq = Integer.parseInt(txt_seq);
                        DivInfo info = new DivInfo(GData.Branch_ID,id,name,desc,qstart,qend,copy,alert,seq,0,0);
                        List<DivInfo> divInfos = LogMgr.Load_DivInfo(SettingQActivity.this);
                        divInfos.add(info);
                        if(LogMgr.Save_DivInfo(SettingQActivity.this,divInfos))
                        {
                            Toast.makeText(SettingQActivity.this,"บันทึกข้อมูลสำเร็จ",Toast.LENGTH_LONG).show();
                            RefreshDiv();
                            dialog.dismiss();
                        }else
                        {
                            Toast.makeText(SettingQActivity.this,"บันทึกข้อมูลไม่สำเร็จ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        //endregion

        //region Setting Stainfo
        RC_Stainfo = (RecyclerView)findViewById(R.id.RC_Stainfo);
        btn_bk_station = (Button)findViewById(R.id.btn_bk_station);
        btn_bk_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Profile);
            }
        });
        btn_home_station = (Button)findViewById(R.id.btn_home_station);
        btn_home_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
            }
        });
        btn_add_station = (Button)findViewById(R.id.btn_add_station);
        btn_add_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(SettingQActivity.this);
                final View addStaView = inflater.inflate(R.layout.layout_add_sta,null);
                final AlertDialog dialog = new AlertDialog.Builder(SettingQActivity.this).create();
                dialog.setView(addStaView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                final ImageView btn_dt_sta_close = addStaView.findViewById(R.id.btn_dt_sta_close);
                btn_dt_sta_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                final TextView txt_sta_id = addStaView.findViewById(R.id.txt_sta_id);
                final TextView txt_sta_name = addStaView.findViewById(R.id.txt_sta_name);
                final  Spinner spinner = addStaView.findViewById(R.id.spin_station_group);

                List<GrpInfo> grpInfoList = LogMgr.Load_GrpInfo(SettingQActivity.this);
                List<String> dataListGrp = new ArrayList<>();
                dataListGrp.add("Please Select");
                for (GrpInfo data : grpInfoList ) {
                    dataListGrp.add(data.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingQActivity.this, R.layout.spinner_cell, dataListGrp);
                spinner.setAdapter(adapter);
                final Button btn_save_sta = addStaView.findViewById(R.id.btn_save_sta);
                btn_save_sta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = txt_sta_id.getText().toString();
                        String name = txt_sta_name.getText().toString();
                        String groupName = spinner.getSelectedItem().toString();
                        int ID = -1;
                        int groupID = -1;

                        if(id.equals(""))
                        {
                            Toast.makeText(SettingQActivity.this,"กรุณากรอก Station ID",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else
                        {
                            List<StaInfo> staInfos = LogMgr.Load_StaInfo(SettingQActivity.this);
                            if(staInfos != null && staInfos.size() > 0)
                            {
                                for(StaInfo data : staInfos)
                                {
                                    if(data.getID().equals(id))
                                    {
                                        Toast.makeText(SettingQActivity.this,"ID นี้มีการใช้งานอยู่ฝนระบบแล้ว",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }
                        }
                        try
                        {
                            ID = Integer.parseInt(id);
                        }catch (Exception ex)
                        {
                            Toast.makeText(SettingQActivity.this,"ID ต้องเป็นตัวเลขเท่านั้น",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(name.equals(""))
                        {
                            Toast.makeText(SettingQActivity.this,"กรุณากรอกชื่อ Station",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(!groupName.equals("Please Select"))
                        {
                            List<GrpInfo> grpInfos = LogMgr.Load_GrpInfo(SettingQActivity.this);
                            if(grpInfos != null && grpInfos.size() > 0)
                            {
                                for(GrpInfo data : grpInfos)
                                {
                                    if(data.getName().equals(groupName))
                                    {
                                        groupID = data.getID();
                                        break;
                                    }
                                }
                            }
                        }

                        List<StaInfo> staInfoList = LogMgr.Load_StaInfo(SettingQActivity.this);
                        StaInfo staInfo = new StaInfo(GData.Branch_ID,ID,name,groupID);
                        staInfoList.add(staInfo);
                        if(LogMgr.Save_StaInfo(SettingQActivity.this,staInfoList))
                        {
                            Toast.makeText(SettingQActivity.this,"บันทึกข้อมูลสำเร็จ",Toast.LENGTH_LONG).show();
                            RefreshStation();
                            dialog.dismiss();
                        }else
                        {
                            Toast.makeText(SettingQActivity.this,"บันทึกข้อมูลไม่สำเร็จ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        //endregion

        //region Setting Div Map Group
        btn_bk_div_map = (Button)findViewById(R.id.btn_bk_div_map);
        btn_bk_div_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { ChangePage(MyPage.Profile);
            }
        });
        btn_home_div_map = (Button)findViewById(R.id.btn_home_div_map);
        btn_home_div_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePage(MyPage.Main);
            }
        });
        //endregion
    }

    private void RefreshDataEmp()
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
        layoutManagerEmp = new LinearLayoutManager(this); //new GridLayoutManager(this,2);
        RC_EmpInfo.setLayoutManager(layoutManagerEmp);
        RC_EmpInfo.setAdapter(adapter);
    }
    private void RefreshDataBranch()
    {
        //Branch Info
        txt_branch_id.setText("Branch ID : " + GData.Branch_ID.toString());
        List<BranchInfo> branchInfoList = LogMgr.Load_BranchInfo(SettingQActivity.this);
        if(branchInfoList != null && branchInfoList.size() > 0)
        {
            BranchInfo branchInfo = branchInfoList.get(0);
            txt_branch_code.setText(branchInfo.getCode().trim());
            txt_branch_name.setText(branchInfo.getName());
            txt_branch_IP.setText(branchInfo.getIPAddress());
            txt_branch_open.setText(branchInfo.getOpen());
            txt_branch_close.setText(branchInfo.getClose());
        }
    }
    private void RefreshGroup()
    {
        List<GrpInfo> grpInfoList = LogMgr.Load_GrpInfo(SettingQActivity.this);
        GroupInfoAdapter adapter = new GroupInfoAdapter(SettingQActivity.this, grpInfoList, new GroupInfoAdapter.onGrpInfoListener() {
            @Override
            public void onSelectedItem(GrpInfo grpInfo, int position) {

            }
        });
        layoutManagerGrp = new LinearLayoutManager(this);
        RC_GroupInfo.setLayoutManager(layoutManagerGrp);
        RC_GroupInfo.setAdapter(adapter);
    }
    private void RefreshStation()
    {
        List<StaInfo> staInfos = LogMgr.Load_StaInfo(SettingQActivity.this);
        StationInfoAdapter adapter = new StationInfoAdapter(SettingQActivity.this, staInfos, new StationInfoAdapter.onStaInfoListener() {
            @Override
            public void onSelectedItem(StaInfo staInfo, int position) {

            }
        });
        layoutManagerSta = new LinearLayoutManager(this);
        RC_Stainfo.setLayoutManager(layoutManagerSta);
        RC_Stainfo.setAdapter(adapter);
    }
    private void RefreshDiv()
    {
        List<DivInfo> divInfos = LogMgr.Load_DivInfo(SettingQActivity.this);
        DivinfoAdapter adapter = new DivinfoAdapter(SettingQActivity.this, divInfos, new DivinfoAdapter.onDivInfoListener() {
            @Override
            public void onSelectedItem(DivInfo div, int position)
            {
                LayoutInflater inflater = LayoutInflater.from(SettingQActivity.this);
                final View dtDivView = inflater.inflate(R.layout.layout_detail_div,null);
                final AlertDialog dialog = new AlertDialog.Builder(SettingQActivity.this).create();
                dialog.setView(dtDivView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                final ImageView btn_dt_div_close = dtDivView.findViewById(R.id.btn_dt_div_close);
                btn_dt_div_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { dialog.dismiss(); }
                });
                final TextView txt_dt_id = dtDivView.findViewById(R.id.txt_dt_id);
                txt_dt_id.setText(div.getID().toString());
                final TextView txt_dt_name = dtDivView.findViewById(R.id.txt_dt_name);
                txt_dt_name.setText(div.getName());
                final TextView txt_dt_des = dtDivView.findViewById(R.id.txt_dt_des);
                txt_dt_des.setText(div.getDesc());
                final TextView txt_dt_qstart = dtDivView.findViewById(R.id.txt_dt_qstart);
                txt_dt_qstart.setText(div.getQStart().toString());
                final TextView txt_dt_qend = dtDivView.findViewById(R.id.txt_dt_qend);
                txt_dt_qend.setText(div.getQStop().toString());
                final TextView txt_dt_copy = dtDivView.findViewById(R.id.txt_dt_copy);
                txt_dt_copy.setText(div.getPrint_Coppies().toString() + " Copy");
                final TextView txt_dt_alert = dtDivView.findViewById(R.id.txt_dt_alert);
                txt_dt_alert.setText(div.getServtime_alert().toString());
                final TextView txt_dt_seq = dtDivView.findViewById(R.id.txt_dt_seq);
                txt_dt_seq.setText(div.getSeq().toString());
                dialog.show();
            }});
        layoutManagerDiv = new LinearLayoutManager(this);
        RC_Divinfo.setLayoutManager(layoutManagerDiv);
        RC_Divinfo.setAdapter(adapter);
    }
    private void RefreshDivMap()
    {
        List<DivMapGroupInfo> divMapGroupInfoList = LogMgr.Load_DivMapGroupInfo(SettingQActivity.this);

    }
    private void CallAPI()
    {
        //region ApiEmployeeInfo
        String searchEmp = "{'branch_id' : '" + GData.Branch_ID + "'}";
        new QcontrollerApi().RequestEmployeeInfoDetail(searchEmp, new QcontrollerApi.EmployeeInfoDetailListener() {
            @Override
            public void onEmployeeInfoResult(List<EmployeeInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("EmployeeInfo http_code="+http_code));
                if (http_code == 200) {
                    LogMgr.Save_EmployeeInfo(SettingQActivity.this,info);
                    RefreshDataEmp();

                }
                GData.callAPI_Index++;
            }
        });
        //endregion

        //region ApiDivInfo
        String searchDiv = "{'ID':" + GData.Branch_ID.toString() + "}";
        new QcontrollerApi().RequestBranchInfoDetail(searchDiv, new QcontrollerApi.BranchInfoDetailListener() {
            @Override
            public void onBranchInfoResult(List<BranchInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("BranchInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_BranchInfo(SettingQActivity.this,info);
                    RefreshDataBranch();
                }
                GData.callAPI_Index++;
            }
        });
        //endregion

        //region Group Info
        new  QcontrollerApi().RequestGrpInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.GrpInfoDetailListener() {
            @Override
            public void onGrpInfoResult(List<GrpInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("GrpInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_GrpInfo(SettingQActivity.this,info);
                    RefreshGroup();
                }
                GData.callAPI_Index++;
            }
        });
        //endregion

        //region Station Info
        new QcontrollerApi().RequestStaInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.StaInfoDetailListener() {
            @Override
            public void onStaInfoResult(List<StaInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("StaInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_StaInfo(SettingQActivity.this,info);
                    RefreshStation();
                }
                GData.callAPI_Index++;
            }
        });
        //endregion

        //region Div Info
        new  QcontrollerApi().RequestDivInfoDetail("{BRANCH_ID:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.DivInfoDetailListener() {
            @Override
            public void onDivInfoResult(List<DivInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("DivInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_DivInfo(SettingQActivity.this,info);
                    RefreshDiv();
                }
                GData.callAPI_Index++;
            }
        });
        //endregion

        //region Div Map

        //endregion
    }

    private boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
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
            case Group: {
                lt_Setting_Group.setVisibility(View.VISIBLE);
            }
            case Division: {
                lt_Setting_Div.setVisibility(View.VISIBLE);
            }
            case Division_Map: {
                lt_Setting_DivMap.setVisibility(View.VISIBLE);
            }
            case Station: {
                lt_Setting_Station.setVisibility(View.VISIBLE);
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
                case Group: {
                    lt_Setting_Group.setVisibility(View.GONE);
                }
                case Division: {
                    lt_Setting_Div.setVisibility(View.GONE);
                }
                case Division_Map: {
                    lt_Setting_DivMap.setVisibility(View.GONE);
                }
                case Station: {
                    lt_Setting_Station.setVisibility(View.GONE);
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