package com.c3.vero.c3_5_04_web_upload;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by vero on 2015/12/10.
 */
public interface UserService {
    String upLoad(InputStream in,Map<String,String> data)throws Exception;
}
