package th.co.infinitecorp.www.qcontroller.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.BranchInfo;

import static android.content.Context.MODE_PRIVATE;

public class Cookie {
    //Branch info
    public  static void SaveBranchDataInfo(Context context, List<BranchInfo> infos) {
        SharedPreferences.Editor editor=context.getSharedPreferences("BranchInfo",Context.MODE_PRIVATE).edit();
        Type type=new TypeToken<List<BranchInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        editor.putString("branchData",json);
        editor.apply();
    }
    public static List<BranchInfo> RetrievedBranchDataInfo(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("BranchInfo",Context.MODE_PRIVATE);
        String strData=sharedPreferences.getString("branchData",null);
        Type type=new TypeToken<List<BranchInfo>>(){}.getType();
        if(strData!=null)
        {
            Gson gson=new Gson();
            List<BranchInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<BranchInfo> list=new ArrayList<>();
        return list;
    }
    public static void ClearBranch(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("BranchInfo", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

}
