package fruitbasket.com.bodyfit.helper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import fruitbasket.com.bodyfit.utils.PermissionUtil;
import fruitbasket.com.bodyfit.utils.ToolUtil;


/**
 * 权限帮助类
 */

public class PermissionHelper {

    private Activity mActivity;
    private PermissionInterface mPermissionInterface;
    private static final String TAG = "PermissionHelper";




    public PermissionHelper(Activity activity,PermissionInterface permissionInterface){
        mActivity=activity;
        mPermissionInterface=permissionInterface;
    }

    /**
     * 开始请求权限
     * 方法内部已经对Android M或以上版本进行了判断，外部使用不再需要进行重复判断
     */
    public void requestPermissions(){
        String [] deniedPermissions = PermissionUtil.getDeniedPermissions(mActivity,mPermissionInterface.getPermissions());
        if(deniedPermissions!=null && deniedPermissions.length>0 ){
            PermissionUtil.requestPermissions(mActivity,deniedPermissions,mPermissionInterface.getPermissionsRequestCode());
        } else{
            mPermissionInterface.requestPermissionsSuccess();
        }
    }

    /**
     * 在Activity中的onRequestPermissionsResult中调用
     */

    public boolean requestPermissionsResult(int requestCode,String []permissions,int [] grantResults){
        if(requestCode==mPermissionInterface.getPermissionsRequestCode()){
            boolean isAllGranted=true;
            for(int result:grantResults){
                if(result==PackageManager.PERMISSION_DENIED){
                    isAllGranted=false;
                    break;
                }
            }
            if(isAllGranted)
                mPermissionInterface.requestPermissionsSuccess();
            else
                mPermissionInterface.requestPermissionsFail();
            return true;
        }
        return false;
    }



}
