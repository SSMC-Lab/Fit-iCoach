package fruitbasket.com.bodyfit.Login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lkeye on 2018/10/14
 */
public class UserDataManager {  //用户数据管理类

    private static final String TAG="UserDataManager";
    private static final String DB_NAME="user_data";
    private static final String TABLE_NAME="users";
    public static final String ID="_id";
    public static final String USER_NAME="user_name";
    public static final String USER_PWD="user_pwd";
    private static final int DB_VERSION=3;
    private Context mContext=null;

    //创建用户book表
    private static final String DB_CREATE="CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key," + USER_NAME + " varchar,"
            + USER_PWD + " varchar" + ");";

    private SQLiteDatabase mSQLiteDatabase=null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    private static class DataBaseManagementHelper extends SQLiteOpenHelper{

        DataBaseManagementHelper (Context context){
            super(context,DB_NAME,null,DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            sqLiteDatabase.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            onCreate(sqLiteDatabase);
        }
    }

    public UserDataManager (Context context){
        mContext=context;
    }

    //打开数据库
    public void openDataBase() throws SQLException{
        mDatabaseHelper =new DataBaseManagementHelper(mContext);
        mSQLiteDatabase=mDatabaseHelper.getWritableDatabase();
    }

    //关闭数据库
    public void closeDataBase () throws SQLException {
        mDatabaseHelper.close();
    }

    public long insertUserData(UserData userData){
        String userName=userData.getUserName();
        String userPwd=userData.getUserPwd();
        ContentValues values=new ContentValues();
        values.put(USER_NAME,userName);
        values.put(USER_PWD,userPwd);
        return mSQLiteDatabase.insert(TABLE_NAME,ID,values);
    }

    //更新用户信息，如修改密码
    public boolean updateUserData(UserData userData){
        String userName=userData.getUserName();
        String userPwd=userData.getUserPwd();
        ContentValues values=new ContentValues();
        values.put(USER_NAME,userName);
        values.put(USER_PWD,userPwd);
        return mSQLiteDatabase.update(TABLE_NAME,values,null,null)>0;
    }

    //根据id删除用户
    public boolean deleteUserData(int id) {
        return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
    }

    //根据用户名查找用户，用于判断注册时用户名是否存在
    public int findUserByName(String userName){
        int result=0;

        Cursor cursor=mSQLiteDatabase.query(TABLE_NAME, new String[]{USER_NAME}, USER_NAME+"=?",new String[]{userName},  null, null, null);
        if(cursor!=null){
            result =cursor.getCount();
            cursor.close();
        }
        return result;
    }

    public int findUserByNameAndPwd(String userName,String pwd){
        int result=0;
        Cursor cursor=mSQLiteDatabase.query(TABLE_NAME,new String[]{USER_NAME,USER_PWD},USER_NAME+"=? and "+USER_PWD+"=?",new String[]{userName,pwd},null,null,null);
        if(cursor!=null){
            result=cursor.getCount();
            cursor.close();
        }
        return result;
    }

    public boolean deleteAllUserDatas(){
        return mSQLiteDatabase.delete(TABLE_NAME,null,null)>0;
    }


}
