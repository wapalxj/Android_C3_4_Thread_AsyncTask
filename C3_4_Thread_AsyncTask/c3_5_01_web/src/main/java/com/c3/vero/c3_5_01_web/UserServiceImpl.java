package com.c3.vero.c3_5_01_web;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vero on 2015/12/4.
 */
public class UserServiceImpl implements UserService{

    //连接Server
    @Override
    public void UserLogin(String loginName, String loginPassword) throws Exception {
        Log.i("uuuuuuuuuuuuuuuuuuuu",loginName);
        Log.i("pppppppppppppppppppp", loginPassword);

//        Thread.sleep(3000);

//        验证代码出错
//        userName=null;
//        if (userName.equals("aaa")){
//
//        }

/*
        //验证用户名密码:此步已经转移到服务器中验证
        if (userName.equals("vero")&&password.equals("123456")){

        }else {
            throw  new ServiceRuleException(Login_Activity.MSG_Register_FAILED);
        }
*/
//0.HttpURLconnection方式
        String uri="http://192.168.56.1:8080/vero/Servlet_login?loginName="+loginName
                +"&loginPassword="+loginPassword;
        URL url=new URL(uri);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Accept-Encoding", "identity");
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);
        InputStream is=connection.getInputStream();
        if (connection.getResponseCode()!=200){
            throw new ServiceRuleException(Login_Activity.SERVER_REQUEST_ERROR);
        }
            if (is!=null){
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                char[] data=new char[1024];
                int len=0;
                StringBuilder result=new StringBuilder();
                while (-1!=(len=br.read(data))){
                    result.append(new StringBuilder(new String(data,0,len)));
                }
                Log.i("iiiiiiiiiiiiiiiiiiii",""+result);
                if (result.toString().equals("vero_failed")){
                    throw new ServiceRuleException(Login_Activity.MSG_LOGIN_FAILED);
                }
                br.close();
            }

        is.close();

//1.GET方式
//        HttpClient client =new DefaultHttpClient();
//        String uri="http://172.17.16.3:8080/vero/Servlet?loginName="+loginName
//                    +"&loginPassword="+loginPassword;
////        String uri="http://192.168.56.1:8080/vero/Servlet?loginName="+loginName
////                +"&loginPassword="+loginPassword;
//        HttpGet get=new HttpGet(uri);

        //响应
//        HttpResponse response=client.execute(get);
//
//        if (response.getStatusLine().getStatusCode()!=200){
//                throw new ServiceRuleException(Login_Activity.SERVER_REQUEST_ERROR);
//        }
//
//        String result= EntityUtils.toString(response.getEntity(), "utf-8");
//
//        if (result.equals("vero_failed")){
//                throw new ServiceRuleException(Login_Activity.MSG_Register_FAILED);
//        }

//2.Post方式
//        NameValuePair-->List<NameValuePair>--->setEntity--->client.execute()

//        HttpClient client=new DefaultHttpClient();
//        String uri="http://172.17.16.3:8080/vero/Servlet";
//        HttpPost post=new HttpPost(uri);
//
//        NameValuePair paramLoginName=new BasicNameValuePair("loginName",loginName);
//        NameValuePair paramLoginPassword=new BasicNameValuePair("loginPassword",loginPassword);
//
//        List<NameValuePair> list= new ArrayList<>();
//        list.add(paramLoginName);
//        list.add(paramLoginPassword);
//        post.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
//
//        //响应
//        HttpResponse response=client.execute(post);
//        if (response.getStatusLine().getStatusCode()!=200){
//                throw new ServiceRuleException(Login_Activity.SERVER_REQUEST_ERROR);
//        }
//
//        String result= EntityUtils.toString(response.getEntity(), "utf-8");
//
//        if (result.equals("vero_failed")){
//                throw new ServiceRuleException(Login_Activity.MSG_Register_FAILED);
//        }

/*
//3.Post方式+参数设置
        //可以在post方式中设置参数

        //请求参数
        HttpParams params=new BasicHttpParams();
        //设置params编码集
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        //设置客户端请求服务器连接的超时时间,超时会抛出异常
        HttpConnectionParams.setConnectionTimeout(params, 3000);
        //设置服务器响应超时时间,超时会抛出异常
        HttpConnectionParams.setSoTimeout(params, 3000);

        SchemeRegistry schreg=new SchemeRegistry();
        //设置处http外的其他协议
        schreg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
        schreg.register(new Scheme("https", PlainSocketFactory.getSocketFactory(),433));

        ClientConnectionManager conman=new ThreadSafeClientConnManager(params,schreg);

        HttpClient client=new DefaultHttpClient(conman,params);
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        String uri="http://192.168.56.1:8080/vero/Servlet";
        HttpPost post=new HttpPost(uri);


        NameValuePair paramLoginName=new BasicNameValuePair("loginName",loginName);
        NameValuePair paramLoginPassword = new BasicNameValuePair("loginPassword",loginPassword);

        List<NameValuePair> list= new ArrayList<>();
        list.add(paramLoginName);
        list.add(paramLoginPassword);
        post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
        //响应
        HttpResponse response=client.execute(post);
        if (response.getStatusLine().getStatusCode()!=200){
            throw new ServiceRuleException(Login_Activity.SERVER_REQUEST_ERROR);
        }

        String result= EntityUtils.toString(response.getEntity(), "utf-8");

        if (result.equals("vero_failed")){
            throw new ServiceRuleException(Login_Activity.MSG_Register_FAILED);
        }

*/
    }

    @Override
    public void UserRegister(String registerName, String password,List<String> hobbies) throws Exception{
//        Thread.sleep(3000);
//        String uri="http://192.168.56.1:8080/vero/Servlet_register?registerName="+registerName
//                +"&password="+password;
        //get:
//        String uri="http://192.168.56.1:8080/vero/Servlet_register?data={\"name\":\"vero\",\"hobbies\":[\"ball\",\"LOL\",\"coding\"]}";
        //POST:

        String uri="http://192.168.56.1:8080/vero/Servlet_register";
        URL url=new URL(uri);
        HttpURLConnection connection= null;

        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Encoding", "identity");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(0);
        connection.setUseCaches(false);
        connection.connect();




//错误！！！！----->connection.getResponseCode()要放在输出流之后，不然数据传不过去
//        if (connection.getResponseCode()!=200){
//            throw new ServiceRuleException(Register_Activity.SERVER_REQUEST_ERROR);
//        }

//        发送JSON
        OutputStream os= connection.getOutputStream();
        if (os!=null){
//            1.--字符流OK
//            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
//            Map<String,String> para=new HashMap<>();
//            para.put("data", "qwwewqe");
//            para.put("name", "vvvvverooo");
//            bw.write(change(para).toString());
//            bw.flush();
//            bw.close();
//            os.close();

//                            2.--字符流OK
            JSONObject json=new JSONObject("{name:"+registerName+",age:25}");
            JSONArray jsonArray=new JSONArray();
            json.put("addr","ynu");
            json.put("level","student");
            if (!hobbies.isEmpty()){
                for (String hobby:hobbies){
                    jsonArray.put(hobby);
                }

            }
            json.put("hobbies",jsonArray);
            PrintWriter pw=new PrintWriter(os);
            pw.print("data="+json.toString());
            pw.flush();
            pw.close();
            os.close();
//                            3.字节流--ok
//                            BufferedOutputStream bs=new BufferedOutputStream(os);
//                            Map<String,String> para=new HashMap<>();
//                            para.put("data", "veroerero");
//                            byte [] s;
//                            s=change(para).toString().getBytes();
//                            bs.write(s);
//                            bs.flush();
//                            bs.close();
//                            os.close();

        }
        //connection.getResponseCode()要放在输出流之后，不然数据传不过去
//        Log.i("CCCCCCCCCCCCCCCCCC", "" + connection.getResponseCode());




//        接收-->解析JSON
        InputStream is=connection.getInputStream();
        if (is!=null){

            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            char[] data=new char[1024];
            int len=0;
            StringBuilder result=new StringBuilder();
            while (-1!=(len=br.read(data))){
                result.append(new StringBuilder(new String(data,0,len)));
            }
            Log.i("iiiiiiiiiiiiiiiiiiii", "" + result);

            JSONObject jsonObject=new JSONObject(result.toString());
            String state=jsonObject.getString("result");
            if (state.equals("success")){
                Log.i("result",state);
            }else {
                String errorMsg=jsonObject.getString("errorMsg");
                Log.i("result:",state+":"+errorMsg);
            }

            br.close();
            is.close();
        }



        connection.disconnect();


    }

    //封装map
    public StringBuffer change(Map<String,String> parameterMap){
        StringBuffer parameterBuffer = new StringBuffer();
        for (Map.Entry<String,String>entry:parameterMap.entrySet()){
            parameterBuffer.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        parameterBuffer.deleteCharAt(parameterBuffer.length()-1);
        return parameterBuffer;
    }

}
