package th.co.infinitecorp.www.qcontroller.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import th.co.infinitecorp.www.qcontroller.Database.Model.QMSDB;

public class DBManager {
    private static DBHelper dbHelper;

    private Context context;

    private static SQLiteDatabase database;

    public DBManager.Insert Insert=new  DBManager.Insert();
    public DBManager.Update Update=new DBManager.Update();
    public DBManager.Delete Delete=new DBManager.Delete();
    public DBManager.GetData GetData=new DBManager.GetData();

    public DBManager(Context c) {
        context = c;

    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
    public static class Insert {
        public void TB_Test(QMSDB.TB_Test tb) {
            try {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.SUBJECT, tb.getSUBJECT());
                cv.put(DBHelper.DESC, tb.DESC);
                database.insert(DBHelper.TABLE_NAME, null, cv);
            }catch (Exception ex){}
        }
    }
    public static class Update {
        public int TB_Test(QMSDB.TB_Test field) {
            try {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.SUBJECT, field.getSUBJECT());
                cv.put(DBHelper.DESC, field.getDESC());
                int i = database.update(DBHelper.TABLE_NAME, cv, DBHelper.ID + " = " + field.getID(), null);
                return i;
            }catch (Exception ex){}
            return -1;
        }
    }
    public static class Delete {
        public void TB_Test(QMSDB.TB_Test tb) {
            try {
                if(tb!=null)
                   database.delete(DBHelper.TABLE_NAME, DBHelper.ID + "=" + tb.getID(), null);
                else
                    database.delete(DBHelper.TABLE_NAME, DBHelper.ID+">0", null);
            }catch (Exception ex){}
        }
    }
    public static class GetData {
        public List<QMSDB.TB_Test> get_TB_Test_List() {
            List<QMSDB.TB_Test> list = new ArrayList<QMSDB.TB_Test>();
            database=dbHelper.getWritableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }

            while(!cursor.isAfterLast()) {
                QMSDB.TB_Test m=new QMSDB.TB_Test();
                m.setID(cursor.getLong(0));
                m.setSUBJECT(cursor.getString(1));
                m.setDESC(cursor.getString(2));
                list.add(m);
                cursor.moveToNext();
            }

            return list;
        }
    }

    public void insert_(String name, String desc) {
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(DBHelper.SUBJECT, name);
            contentValue.put(DBHelper.DESC, desc);
            database.insert(DBHelper.TABLE_NAME, null, contentValue);
        }catch (Exception ex){}
    }

    public Cursor fetch() {
        Cursor cursor=null;
        try {
            String[] columns = new String[]{DBHelper.ID, DBHelper.SUBJECT, DBHelper.DESC};
            cursor = database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
        }catch (Exception ex){}
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.SUBJECT, name);
            contentValues.put(DBHelper.DESC, desc);
            int i = database.update(DBHelper.TABLE_NAME, contentValues, DBHelper.ID + " = " + _id, null);
            return i;
        }catch (Exception ex){}
        return -1;
    }

    public void delete(long _id) {
        try {
             database.delete(DBHelper.TABLE_NAME, DBHelper.ID + "=" + _id, null);
        }catch (Exception ex){}
    }

    public List<QMSDB.TB_Test> get_TB_Test_List() {
        List<QMSDB.TB_Test> list = new ArrayList<QMSDB.TB_Test>();
        database=dbHelper.getWritableDatabase();
        Cursor cursor = database.query
                (DBHelper.TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {
            QMSDB.TB_Test m=new QMSDB.TB_Test();
            m.setID(cursor.getLong(0));
            m.setSUBJECT(cursor.getString(1));
            m.setDESC(cursor.getString(2));
            list.add(m);
            cursor.moveToNext();
        }

        return list;
    }

}
