package vero.com.c4_hm_14_mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import vero.com.c4_hm_14_mvp.presenter.SplashPresenter;
import vero.com.c4_hm_14_mvp.view.ISplashView;

/**
 * 用户进入splash界面
 * 判断网络是否存在--现实加载框
 * 如果存在---》进入下一个页面
 * 如果不存在---》提示网络错误
 *
 * 这个情况下，Activity只显示UI，充当角色View，而控制逻辑都交给SplashPresenter
 */
public class SplashActivity extends AppCompatActivity implements ISplashView{
    private SplashPresenter mPresenter;//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter=new SplashPresenter(this);
    }

    @Override
    protected void onResume() {
        mPresenter.doUiLogic(SplashActivity.this);
        super.onResume();
    }

    /**UI具体展示*/
    @Override
    public void showLoadingDialog() {
        Log.i("showLoadingDialog","showLoadingDialog");
    }

    @Override
    public void startNextActivity() {
        Log.i("startNextActivity","startNextActivity");
    }

    @Override
    public void showNetWorkError() {
        Log.i("showNetWorkError","showNetWorkError");
    }

    @Override
    public void hideLoadingDialog() {
        Log.i("hideLoadingDialog","hideLoadingDialog");
    }
}
