package com.threshold.webapiauth;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.threshold.webapiauth.api.ApiClient;
import com.threshold.webapiauth.api.IFileUpDownApi;
import com.threshold.webapiauth.api.IProductsApi;
import com.threshold.webapiauth.model.FileModel;
import com.threshold.webapiauth.model.Product;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Bind(R.id.etServerIp)
    EditText etServerIp;
    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.button3)
    Button button3;
    @Bind(R.id.button4)
    Button button4;
    @Bind(R.id.tvContent)
    TextView tvContent;


    private SecureRequestInterpolator interpolator;

    private ApiClient apiClient;
    // private FileUpDownClient fileUpDownClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        initView();
//        initEvents();
        initFields();
    }

    private void initFields() {
        //interpolator = new SecureRequestInterpolator1("username", "password");
        interpolator = new SecureRequestInterpolator(Constant.APP_KEY, Constant.APP_SECRET);//this is the key and password shared between server and client
        apiClient = new ApiClient(getServerUrl());
        // fileUpDownClient = new FileUpDownClient(getServerUrl());
    }

  /*  private void initEvents() {
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
    }*/

    private void testRetrofitGet() {
        IProductsApi productsApi = apiClient.getProductsApi();
        productsApi.getProducts()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<Product>, Observable<Product>>() {
                    @Override
                    public Observable<Product> call(List<Product> products) {
                        return Observable.from(products);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        tvContent.setText("");
                    }
                })
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        Toast.makeText(MainActivity.this, "finallyDo请求完成", Toast.LENGTH_SHORT).show();
                    }
                }).subscribe(new Subscriber<Product>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                Toast.makeText(MainActivity.this, "onError请求失败了....", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Product product) {
                // Toast.makeText(MainActivity.this, " onNext获得结果 ;）", Toast.LENGTH_SHORT).show();
                tvContent.setText(String.format("%s\n%s", tvContent.getText(), product));
            }
        });
    }


    private void testRetrofitPost() {
        IProductsApi productsApi = apiClient.getProductsApi();
        productsApi.addProduct(new Product(100, "AppleJuice", "Fruit", 12.13d))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Product>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                        Toast.makeText(MainActivity.this, "onError请求失败了....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Product product) {
                        Toast.makeText(MainActivity.this, " onNext获得结果 ;）", Toast.LENGTH_SHORT).show();
                        tvContent.setText(product.toString());
                    }
                });
    }

    private void testRetrofitDownload() {
        IFileUpDownApi api = apiClient.getFileUpDownApi();
        api.downloadFile("486a0837-2a85-4280-a09d-36717c730837.png")
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, File>() {
                    @Override
                    public File call(ResponseBody responseBody) {
                        File file = null;
                        if (responseBody != null) {
                            file = new File(Environment.getExternalStorageDirectory() + File.separator + "xx.png");
                            if (!file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            InputStream inputStream = responseBody.byteStream();
                            //BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            byte[] buffer = new byte[2048];
                            try {
                                OutputStream outputStream = new FileOutputStream(file);
                                int readLength;
                                while ((readLength = inputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, readLength);
                                }
                                outputStream.flush();
                                outputStream.close();
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return file;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        tvContent.setText("");
                    }
                })
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                        Toast.makeText(MainActivity.this, "onError请求失败了....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(File file) {
                        String str;
                        if (file != null) {
                            str = "下载成功:" + file.getPath();
                        } else {
                            str = "下载失败";
                        }
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                        tvContent.setText(str);
                    }
                });
    }


    /**
     * Upload File To Server
     * See https://github.com/square/retrofit/issues/1063
     */
    private void testRetrofitUpload() {
        File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "testUpload.txt");
        if (!file1.exists()) {
            try {
                if (file1.createNewFile()) {
                    FileOutputStream outputStream = new FileOutputStream(file1);
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
                if (file2.createNewFile()) {
                    FileOutputStream outputStream = new FileOutputStream(file2);
                    outputStream.write("File2:This is A File Created By WebApi Client!".getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RequestBody requestBody1 = RequestBody.create(MultipartBody.FORM, file1);
        RequestBody requestBody2 = RequestBody.create(MultipartBody.FORM, file2);
        IFileUpDownApi fileUpDownApi = apiClient.getFileUpDownApi();
        Map<String, RequestBody> map = new HashMap<>();
        map.put("file\"; filename=\" 1.txt", requestBody1);
        map.put("file\"; filename=\" 2.txt", requestBody2);
        fileUpDownApi.uploadFile(map)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<FileModel>, Observable<FileModel>>() {
                    @Override
                    public Observable<FileModel> call(List<FileModel> fileModels) {
                        return Observable.from(fileModels);
                    }
                })
                .map(new Func1<FileModel, String>() {
                    @Override
                    public String call(FileModel fileModel) {
                        return fileModel.toString();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        tvContent.setText("");
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        Toast.makeText(MainActivity.this, "onError请求失败了....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(String s) {
                        tvContent.setText(String.format("%s\n%s", tvContent.getText(), s));
                    }
                });
    }


    private void testGet() {
        Request request = new Request.Builder()
                .url(getServerUrl() + "api/products")
                .build();
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interpolator);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                //LogUtils.e(e.toString());
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


    @OnClick({R.id.button1,R.id.button2,R.id.button3,R.id.button4})
    @Override
    public void onClick(View v) {
        apiClient.setBaseUrl(getServerUrl());
        //  fileUpDownClient.setBaseUrl(getServerUrl());
        switch (v.getId()) {
            case R.id.button1:
//                testGet();
                testRetrofitGet();
                break;
            case R.id.button2:
                testRetrofitPost();
//                testPost();
                break;
            case R.id.button3:
                testRetrofitUpload();
//                testUploadFile();
                break;
            case R.id.button4:
                testRetrofitDownload();
//                testDownloadFile();
                break;
        }
    }

    private String getServerUrl() {
        return String.format("http://%s/", etServerIp.getText().toString());
    }

    private void testDownloadFile() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interpolator);
        Request request = new Request.Builder()
                .url(getServerUrl() + "api/fileupdown?filename=486a0837-2a85-4280-a09d-36717c730837.png") //make sure your  the file exists on your server.
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                //  LogUtils.d(e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final File file = new File(Environment.getExternalStorageDirectory() + File.separator + "xx.png");
                if (!file.exists()) {
                    file.createNewFile();
                }
                InputStream is = response.body().byteStream();
                BufferedInputStream input = new BufferedInputStream(is);
                OutputStream output = new FileOutputStream(file);

                byte[] data = new byte[1024];
                int readLength;
                while ((readLength = input.read(data)) > 0) {
                    output.write(data, 0, readLength);
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
                if (file1.createNewFile()) {
                    FileOutputStream outputStream = new FileOutputStream(file1);
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
                if (file2.createNewFile()) {
                    FileOutputStream outputStream = new FileOutputStream(file2);
                    outputStream.write("File2:This is A File Created By WebApi Client!".getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file1", file1.getName(), RequestBody.create(MediaType.parse("text/plain"), file1))
                .addFormDataPart("file2", file2.getName(), RequestBody.create(MediaType.parse("text/plain"), file2))
                .build();
       /* RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("hello", "android")
                .addFormDataPart("photo", file1.getName(), RequestBody.create(MediaType.parse("text/plain"), file1))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file2))
                .build();*/
//        RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
//                .addFormDataPart("hello", "android")
//                .addFormDataPart("photo", file1.getName(), RequestBody.create(MediaType.parse("text/plain"), file1))
//                .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""),
//                        RequestBody.create(MediaType.parse("application/octet-stream"),
//                                file2))
//                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(getServerUrl() + "api/fileupdown")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interpolator).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                // LogUtils.e(e.toString());
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
                .url(getServerUrl() + "api/products/")
                .build();

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interpolator);
        client.newCall(postRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                // LogUtils.e(e.toString());
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
