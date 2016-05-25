package domain;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vero on 2016/5/23.
 */
public class NewsService {

    public static List<NewsItem> getAllNewsItems(final String path){
        final List<NewsItem> items=new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(path);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code=conn.getResponseCode();
                    if (code==200){
                        InputStream is=conn.getInputStream();
//                        ByteArrayOutputStream bos=new ByteArrayOutputStream();
//                        int len=0;
//                        byte [] buff=new byte[1024];
//                        while ((len=is.read(buff))>0){
//                            bos.write(buff,0,len);
//                        }
//                        //XML---->String
//                        String data=bos.toString();
//                        Log.i("data111111111",data);
                        //解析

                        XmlPullParser parser= Xml.newPullParser();
                        parser.setInput(is,"utf-8");
                        //
                        int type=parser.getEventType();
                        NewsItem item=null;
                        while (type!=XmlPullParser.END_DOCUMENT){
                            if (type==XmlPullParser.START_TAG){
                                if ("item".equals(parser.getName())){
                                    item=new NewsItem();
                                }else if("title".equals(parser.getName())){
                                    item.setTitle(parser.nextText());
                                }else if("description".equals(parser.getName())){
                                    item.setDescription(parser.nextText());
                                }else if("image".equals(parser.getName())){
                                    item.setImage(parser.nextText());
                                }else if("type".equals(parser.getName())){
                                    item.setType(parser.nextText());
                                }else if("comment".equals(parser.getName())){
                                    item.setComment(parser.nextText());
                                }
                            }else if(type==XmlPullParser.END_TAG){
                                if (item!=null){
                                    items.add(item);
                                }
                            }
                            type=parser.next();
                        }
                    }else {

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return items;
    }
}
