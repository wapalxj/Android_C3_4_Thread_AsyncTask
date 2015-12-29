package com.c3.vero.c3_5_04_web_upload;


import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by vero on 2015/12/10.
 */
public class UserServiceImp implements UserService {
    String BOUNDARY = java.util.UUID.randomUUID ( ).toString ( ) ;
    String PREFIX = "--" , LINEND = "\r\n" ;
    @Override
    public String upLoad(InputStream in, Map<String, String> data) throws Exception {
        String urlStr="http://192.168.56.1:8080/vero/Upload";
        URL url=new URL(urlStr);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Encoding", "identity");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
        connection.setDoOutput(true);
        connection.setReadTimeout(8000);
        connection.setConnectTimeout(8000);
        connection.setUseCaches(false);
        connection.connect();

        //httpmine
        //普通数据封装
        MultipartEntity entity=new MultipartEntity();
        for (Map.Entry<String,String>entry:data.entrySet()){
            String key =entry.getKey();
            String value=entry.getValue();
            entity.addPart(key,new StringBody(value, Charset.forName("UTF-8")));
        }
        //二进制文件封装
        entity.addPart("file",new InputStreamBody(in,"multipart/form-data","test.jpg"));


        OutputStream os=connection.getOutputStream();
        byte [] d =entity.toString().getBytes();
        os.write(d);
        //end
        byte [ ] end_data = ( PREFIX + BOUNDARY + PREFIX + LINEND )
                .getBytes ( ) ;
        os.write ( end_data ) ;

        os.flush();
        os.close();


        InputStream is=connection.getInputStream();
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        String re=reader.readLine();
        reader.close();
        is.close();
        connection.disconnect();
        return re;
    }
}
