package com.c3.vero.c3_5_04_2_web_upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by vero on 2015/12/10.
 */
public class UserServiceImp implements UserService {
    private static String BOUNDARY = java.util.UUID.randomUUID ( ).toString ( ) ;//正文分界符
    private static String PREFIX = "--" ;//正文分界头
    private static String LINE_END = "\r\n" ;//换行
    @Override
    public String upLoad(File file, Map<String, String> params) throws Exception {
        String urlStr="http://192.168.56.1:8080/vero/Upload";
        URL url=new URL(urlStr);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Encoding", "identity");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
        connection.setDoOutput(true);
        connection.setReadTimeout(8000);
        connection.setConnectTimeout(8000);
        connection.setUseCaches(false);
        connection.connect();

        if (file!=null){
            OutputStream os=connection.getOutputStream();
            BufferedOutputStream bos=new BufferedOutputStream(os);
            StringBuilder sb=new StringBuilder();
            sb.append(LINE_END);//正文换行
            /**
             * 无论什么数据，name为key，服务器接收
             */
            if (params!=null ){//字符串参数
                for (Map.Entry entry:params.entrySet()){
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);//正文第一行: 分界行
                    sb.append("Content-Disposition: form-data; name=\""
                            + entry.getKey() + "\"" + LINE_END);//正文第二行：参数格式和name属性
                    sb.append("Content-Type: text/plain; charset=UTF-8" + LINE_END);//正文第三行：正文类型
                    sb.append("Content-Transfer-Encoding: 8bit" + LINE_END );//正文第四行：数据编码
                    sb.append(LINE_END);//换行
                    sb.append(entry.getValue());//正文内容
                    sb.append(LINE_END);//换行
                }
            }
            //文件参数
            sb.append(PREFIX);//开始拼接文件参数
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=img; filename=" + file.getName() + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=UTF-8" + LINE_END);
            sb.append(LINE_END);
            bos.write(sb.toString().getBytes());

            //文件数据
            InputStream is=new FileInputStream(file);
            BufferedInputStream bis=new BufferedInputStream(is);
            byte[] data=new byte[10240];
            int len=0;
            while (-1!=(len=bis.read(data))){
                bos.write(data,0,len);
            }
            bos.write(LINE_END.getBytes());//换行
            byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();//分界行
            bos.write(end_data);//换行
            bos.flush();

            bis.close();
            is.close();
            bos.close();
            os.close();


        }

        String result;
        int code=connection.getResponseCode();
        if(code==200){
            InputStream is=connection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            result=reader.readLine();
            reader.close();
            is.close();
            connection.disconnect();
        }else {
            result="服务器连接失败！";
        }
        return result;

    }
}


