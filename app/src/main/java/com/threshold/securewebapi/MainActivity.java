package com.threshold.securewebapi;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.threshold.hmacauth.SecureRequestInterpolator;
import com.threshold.hmacauth.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etServerIp;
    private TextView tvContent;
    private Button button1,button2,button3,button4;


    //  SecureRequestInterpolator1 interpolator;
    private SecureRequestInterpolator interpolator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
        initFields();
    }

    private void initFields() {
        //interpolator = new SecureRequestInterpolator1("username", "password");
        interpolator = new SecureRequestInterpolator("username", "password");//this is the key and password shared between server and client
    }

    private void initEvents() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    private void initView() {
        tvContent = (TextView) findViewById(R.id.tvContent);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        etServerIp = (EditText) findViewById(R.id.etServerIp);
    }

    private void testGet() {

        Request request = new Request.Builder()
                .url("http://"+getServerIp()+"/api/products/1")
                .build();
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interpolator);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, final IOException e) {
                    LogUtils.e(e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvContent.setText(e.toString());
                        }
                    });
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvContent.setText(result);
                        }
                    });
                }
            });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                testGet();
                break;
            case R.id.button2:
                testPost();
                break;
            case R.id.button3:
                testUploadFile();
                break;
            case R.id.button4:
                testDownloadFile();
                break;
        }
    }

    private String getServerIp()
    {
        return etServerIp.getText().toString();
    }

    private void testDownloadFile() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interpolator);
        Request request = new Request.Builder()
                .url("http://"+getServerIp()+"/api/fileupdown?filename=486a0837-2a85-4280-a09d-36717c730837.png") //make sure your server has the file.
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                LogUtils.d(e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final File file=new File(Environment.getExternalStorageDirectory()+File.separator+"xx.png");
                if (!file.exists()) {
                    file.createNewFile();
                }
                InputStream is = response.body().byteStream();
                BufferedInputStream input = new BufferedInputStream(is);
                OutputStream output = new FileOutputStream(file);

                byte[] data = new byte[1024];
                int readLength;
                while ((readLength= input.read(data)) >0) {
                    output.write(data,0,readLength);
                }
                output.flush();
                output.close();
                input.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(String.format("下载完成,文件位于：%s", file.getPath()));
                    }
                });
            }
        });
    }


    private void testUploadFile() {
        File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "testUpload.txt");
        if (!file1.exists()) {
            try {
               if (file1.createNewFile()){
                FileOutputStream outputStream=new FileOutputStream(file1);
                outputStream.write("File1:This is A File Created By WebApi Client!".getBytes("UTF-8"));
                   outputStream.flush();
                outputStream.close();
               }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "testUpload2.txt");
        if (!file2.exists()) {
            try {
                if (file2.createNewFile()){
                    FileOutputStream outputStream=new FileOutputStream(file2);
                    outputStream.write("File2:This is A File Created By WebApi Client!".getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                .addFormDataPart("hello", "android")
                .addFormDataPart("photo", file1.getName(), RequestBody.create(MediaType.parse("text/plain"), file1))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file2))
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url("http://"+getServerIp()+"/api/fileupdown")
                .build();

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interpolator);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                LogUtils.e(e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String resp = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(resp);
                    }
                });
            }
        });

    }

    private void testPost() {
        String body = "{\"Id\":3,\"Name\":\"电脑\",\"Category\":\"电子产品\"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        Request postRequest = new Request.Builder()
                .post(requestBody)
                .url("http://"+getServerIp()+"/api/products/")
                .build();

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interpolator);
        client.newCall(postRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                LogUtils.e(e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(result);
                    }
                });
            }
        });
    }
}
