package muguihai.com.c4_z_byd_network_state_listener;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by vero on 2016/11/2.
 */

public class NetUtil {
    //没有网络
    private static final  int NETWORK_NONE=-1;
    //移动网络
    private static final  int NETWORK_MOBILE=0;
    //WIFI
    private static final  int NETWORK_WIFI=1;

    public static int getNetworkState(Context context){
        //得到连接管理器
        ConnectivityManager cManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return 0;
    }
}
