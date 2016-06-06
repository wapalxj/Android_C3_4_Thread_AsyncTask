package vero.com.c4_hm_14_mvp.view;

/**
 * Created by vero on 2016/6/5.
 * 定义可能有的UI操作
 */
public interface ISplashView {
    /**现实加载框*/
    void showLoadingDialog();
    /**进入下一个界面*/
    void startNextActivity();
    /**显示加载框*/
    void showNetWorkError();
    /**隐藏加载框*/
    void hideLoadingDialog();
}
