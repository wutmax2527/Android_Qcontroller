package th.co.infinitecorp.www.qcontroller.Screen.QTouch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.MainActivity;
import th.co.infinitecorp.www.qcontroller.R;
import th.co.infinitecorp.www.qcontroller.Service.PrinterService;
import th.co.infinitecorp.www.qcontroller.Service.QTouchService;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class TouchScreenStyle1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_screen_style1);
        Button btn_id1=(Button)findViewById(R.id.btn_id1);
        btn_id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QTouchService qTouchService=new QTouchService(TouchScreenStyle1.this);
                qTouchService.Call_REQUEST_QUEUE((byte) 1);
            }
        });
        Button btn_id2=(Button)findViewById(R.id.btn_id2);
        btn_id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QTouchService qTouchService=new QTouchService(TouchScreenStyle1.this);
                qTouchService.Call_REQUEST_QUEUE((byte) 2);
            }
        });
        Button btn_id3=(Button)findViewById(R.id.btn_id3);
        btn_id3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QTouchService qTouchService=new QTouchService(TouchScreenStyle1.this);
                qTouchService.Call_REQUEST_QUEUE((byte) 3);
            }
        });
        Button btn_id4=(Button)findViewById(R.id.btn_id4);
        btn_id4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QTouchService qTouchService=new QTouchService(TouchScreenStyle1.this);
                qTouchService.Call_REQUEST_QUEUE((byte) 4);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
