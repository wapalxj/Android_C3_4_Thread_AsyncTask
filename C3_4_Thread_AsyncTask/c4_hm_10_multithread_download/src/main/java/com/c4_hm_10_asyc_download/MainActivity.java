package com.c4_hm_10_asyc_download;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static int threadCount=3;
    private static int currRunningThreads=3;//正在下载的线程数目
    //	private static String path="http://127.0.0.1:8080/file.txt";
    //注意Android不能使用127.0.0.1
    private static String path="http://113.55.50.139:8080/ff.exe";
    private EditText et_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        et_path= (EditText) findViewById(R.id.et_path);
    }
    public void download(View v){
        et_path.getText().toString().trim();

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(path);
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code=conn.getResponseCode();
                    if (code==200) {
                        int length=conn.getContentLength();
                        //getFileName(path)--->ff.exe
                        //  /mnt/sdcard/ff/exe
                        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getFileName(path));

                        //创建和下载文件大小相同的空文件
                        RandomAccessFile raf=new RandomAccessFile(file, "rw");
                        raf.setLength(length);
                        raf.close();

                        //每块线程的大小
                        int blockSize=length/threadCount;
                        //开3条线程
                        for(int threadId=0;threadId<threadCount;threadId++){
                            int startIndex=threadId*blockSize;
                            int endIndex=(threadId+1)*blockSize-1;
                            if (threadId==(threadCount-1)) {//最后1条线程
                                endIndex=length-1;
                            }
                            new DownloadFilePartThread(threadId, startIndex, endIndex).start();
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private  class DownloadFilePartThread extends Thread{
        private int threadId;//线程ID
        private int startIndex;//线程的下载开始位置
        private int endIndex;//线程的下载结束的位置
        private int currPosition;//当前线程下载到的位置

        public DownloadFilePartThread(int threadId,int startIndex,int endIndex) {
            this.threadId=threadId;
            this.startIndex=startIndex;
            this.endIndex=endIndex;
        }
        @Override
        public void run() {
            System.out.println("第"+threadId+"线程开始下载了：从"+startIndex+"--"+endIndex);
            try {
                URL url=new URL(path);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                //在多线程下载的时候，每条线程需要目标文件的一部分数据
                //通过设置请求头去实现。告诉服务器，只需要部分数据
                //下载范围startIndex------->endIndex
                //conn.setRequestProperty("range", "bytes="+startIndex+"-"+endIndex);
                //获得服务器返回的目标段的数据
                //是否断点下载

                File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getFileName(path));
                RandomAccessFile raf=new RandomAccessFile(file, "rw");

                File ilf=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+threadId+".position");
                if (ilf.exists()&&ilf.length()>0) {
                    //如果ilf存在，则说明之前下载过
                    System.out.println("之前下载过");
                    BufferedReader br=new BufferedReader(new FileReader(ilf));
                    String vl=br.readLine();
                    int alreadyWritePosition=Integer.parseInt(vl);
                    //告诉服务器，从这个位置开始下载
                    conn.setRequestProperty("range", "bytes="+alreadyWritePosition+"-"+endIndex);
                    //从哪个位置开始写
                    raf.seek(alreadyWritePosition);
                }else {
                    System.out.println("之前没有下载过");
                    conn.setRequestProperty("range", "bytes="+startIndex+"-"+endIndex);
                    //从哪个位置开始写
                    raf.seek(startIndex);
                }
                //状态码206
                int code=conn.getResponseCode();
                if (code==206) {
                    //拿到数据
                    //数据下载完成后存放在当前工程目录下
                    InputStream is=conn.getInputStream();

                    int length=0;
                    byte[] buff=new byte[1024*1024];
//					byte[] buff=new byte[5];//改小一点测试断点下载
                    while ((length=is.read(buff))>0) {
                        raf.write(buff,0,length);
                        //位置记录在info文件中
                        currPosition=currPosition+length;//记录当前下载的位置
                        File info=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+threadId+".position");//记录下载位置

                        //硬盘设备：机械硬盘（小马达），固态硬盘（SSD，没有小马达）
                        //只使用File类+OutputStream不会实时的写入硬盘设备，导致测试断点下载时info文件的错误
//						OutputStream os=new FileOutputStream(info);
//						os.write((currPosition+"").getBytes());
//						os.close();
                        //解决：使用RandomAccessFile，但是RWD模式会损坏硬盘(可以将buff数组变大来解决)
                        RandomAccessFile rf=new RandomAccessFile(info, "rwd");
                        rf.write((currPosition+"").getBytes());
                        rf.close();
                    }
                    is.close();
                    raf.close();
                    System.out.println("第"+threadId+"线程下载结束了");

                    //删除文件
                    //等所有的线程都下载在完成后删除文件
                    //计数器---->记录每正在下载的线程数目，
                    //当计数器小于=0的时候。删除记录文件
                    synchronized (MainActivity.class) {
                        currRunningThreads--;
                        if (currRunningThreads<=0) {
                            //将记录下载位置的文件删掉
                            for (int threadId = 0; threadId < threadCount; threadId++) {
                                File fff=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+threadId+".position");
                                fff.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+threadId+".position.finished"));
                                File fll=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+threadId+".position.finished");
								fll.delete();
                                System.out.println(fll.getName().toString()+"记录文件被删除了");
                            }
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public  String getFileName(String path) {
        int index=path.lastIndexOf("/");
//		System.out.println(path.substring(index+1));
        return path.substring(index+1);

    }
}
