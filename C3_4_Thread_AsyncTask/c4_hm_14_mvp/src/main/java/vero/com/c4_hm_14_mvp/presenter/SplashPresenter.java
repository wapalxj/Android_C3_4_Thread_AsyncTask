package vero.com.c4_hm_14_mvp.presenter;

import android.content.Context;

import vero.com.c4_hm_14_mvp.model.INetWork;
import vero.com.c4_hm_14_mvp.model.impl.INetWorkImpl;
import vero.com.c4_hm_14_mvp.view.ISplashView;

/**
 * Created by vero on 2016/6/5.
 * /**
 * 用户进入splash界面
 * 判断网络是否存在--现实加载框
 * 如果存在---》进入下一个页面
 * 如果不存在---》提示网络错误
 *
 */
public class SplashPresenter{
    //面向接口编程
    INetWork mINetWork;
    ISplashView mISplashView;

    public SplashPresenter(ISplashView mISplashView) {
        this.mINetWork = new INetWorkImpl();
        this.mISplashView = mISplashView;
    }

    public void doUiLogic(Context context){
        mISplashView.showLoadingDialog();
        if (mINetWork.isNetWorkOk(context)){
            //网络可用
            mISplashView.startNextActivity();
        }else {
            //网络不可用
            mISplashView.showNetWorkError();
        }
        mISplashView.hideLoadingDialog();
    }
}
