# SecureWebApi
The infrastructure of Secure WebApi.Both server and client can verify the signature which client or server signed.
This is look like amazon s3,ensure server is your server,client is your client.And others can't invoke your Api without your permission.
If you don't understand what I said,I suggest you read the document of amazon s3. 
This project contains sample and library.You can modify it to suit your own business.


> Attention Please: 
This is only the client of Android based on OKHttp.</br>
You need a server to host secure WebApi service,do the same thing which client signed and compare them.</br>
For Example Asp.Net MVC WebApi. 

> Update : Add RXJava and Retrofit Support Sample.
You will enjoy this,how simple it is:you can get/post 、upload or download file from server.
Talk is too much ,show me the code.Ok,You can see the library on webapiauthlib and sample is webapiauth.
app and hmacauth is old version of secure api which based on OkHttp 2.x,I suggest you see the new demo which I just said on top.

## ScreenShot

* Get Request

Clicked TestGet Button

![GetReq](https://github.com/wind0ws/SecureWebApi/blob/master/Screenshot/TestGet.png)
***

* Post Request

Clicked TestPost Button

![GetReq](https://github.com/wind0ws/SecureWebApi/blob/master/Screenshot/TestPost.png)
***

* UploadFile

Clicked UploadFile Button

![GetReq](https://github.com/wind0ws/SecureWebApi/blob/master/Screenshot/UploadFile.png)
***

* TestDownloadFile

Clicked DownloadFile Button

![GetReq](https://github.com/wind0ws/SecureWebApi/blob/master/Screenshot/TestDownloadFile.png)

