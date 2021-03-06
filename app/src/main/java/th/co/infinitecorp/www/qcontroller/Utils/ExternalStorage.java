package th.co.infinitecorp.www.qcontroller.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.os.Environment.DIRECTORY_MUSIC;

public class ExternalStorage {
    private static final String tag = ExternalStorage.class.getSimpleName();
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    //Read&Write Method
    public static boolean writeFile(File myExternalFile,String text) {
        try {
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(text.getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String readFile(File myExternalFile) {
        String myData="";
        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  myData;
    }
    public static boolean deleteFile(File myExternalFile) {
        File file = myExternalFile;
        boolean deleted = file.delete();
        return  deleted;
    }

    public boolean checkFileExist(Context context,String fileName)
    {
        //String filePath= Environment.getExternalStorageDirectory().getPath()+fileName;
        //String filePath= Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)+fileName;
        String filePath=context.getExternalFilesDir("")+fileName;
       File file=new File(filePath);
       if(file.exists()&&file.canRead()) {
           return true;
       }else {
           return false;
       }
    }
    public static String getFilePath(Context context,String fileName) {
        //String filePath= Environment.getExternalStorageDirectory().getPath()+fileName;
        //String filePath= Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)+fileName;
        String filePath=context.getExternalFilesDir("")+fileName;
        return filePath;
    }
    public static String getExistFilePath(Context context,String fileName) {
        //String filePath= Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)+fileName;
        String filePath=context.getExternalFilesDir("")+fileName;
        File file=new File(filePath);
        if(file.exists()&&file.canRead()) {
            return filePath;
        }else {
            return "";
        }
    }

}
