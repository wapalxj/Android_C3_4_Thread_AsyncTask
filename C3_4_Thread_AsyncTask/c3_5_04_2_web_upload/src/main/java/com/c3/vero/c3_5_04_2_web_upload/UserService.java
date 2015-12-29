package com.c3.vero.c3_5_04_2_web_upload;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by vero on 2015/12/10.
 */
public interface UserService {
    String upLoad(File file, Map<String, String> data)throws Exception;
}
