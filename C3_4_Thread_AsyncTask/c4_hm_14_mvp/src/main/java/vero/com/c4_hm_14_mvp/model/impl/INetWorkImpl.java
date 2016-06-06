package vero.com.c4_hm_14_mvp.model.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import vero.com.c4_hm_14_mvp.model.INetWork;

/**
 * Created by vero on 2016/6/5.
 */
public class INetWorkImpl implements INetWork{
    @Override
    public boolean isNetWorkOk(Context context) {
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager==null){
            return false;
        }else {
            NetworkInfo info=manager.getActiveNetworkInfo();
            if (info==null){
                return false;
            }else {
                if (info.isAvailable()){
                    return true;
                }
            }
        }
        return false;
    }
}
