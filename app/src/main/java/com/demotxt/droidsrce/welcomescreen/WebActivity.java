package com.demotxt.droidsrce.welcomescreen;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ProgressBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WebActivity extends AppCompatActivity {
    private WebView myWebView;
    //ImageView superImageView;
    ProgressBar superProgressBar;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(WebActivity.this, WebActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.Full_Screen:
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    return true;
                case R.id.Screenshot:
                    View v = getWindow().getDecorView().getRootView();
                    v.setDrawingCacheEnabled(true);
                    Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                    v.setDrawingCacheEnabled(false);
                    try {
                        FileOutputStream fos = new FileOutputStream(new File(Environment
                                .getExternalStorageDirectory().toString(), "SCREEN"
                                + System.currentTimeMillis() + ".png"));
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                        Toast.makeText(WebActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.about:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(WebActivity.this);
                    builder1.setMessage("Are you sure you want to navigate away from this page?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent1 = new Intent(WebActivity.this, About.class);
                            startActivity(intent1);
                        }
                    }).setNegativeButton("No",null);

                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                    return true;
                    case R.id.exit:
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                builder.setMessage("Are you sure you want to Logout?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }).setNegativeButton("No",null);

                AlertDialog alert = builder.create();
                alert.show();
                    //android.os.Process.killProcess(android.os.Process.myPid());
                    //System.exit(1);
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        myWebView = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://lms.kdu.ac.lk/lms/");
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSetting = myWebView.getSettings();

        superProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);
        superProgressBar.setMax(100);

        myWebView.setWebViewClient(new android.webkit.WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                superProgressBar.setProgress(newProgress);
            }





        });

        myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri myUri = Uri.parse(url);
                Intent superIntent = new Intent(Intent.ACTION_VIEW);
                superIntent.setData(myUri);
                startActivity(superIntent);
            }
        });




        //This function enable Zoom in Zoom out Feature on WebView
        webSetting.setBuiltInZoomControls(true);

        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int icon = R.drawable.largepreview;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, "Custom Notification", when);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_layout_notification);
        contentView.setImageViewResource(R.id.image, R.drawable.largepreview);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "Welcome to KDU LMS");
        notification.contentView = contentView;

        Intent notificationIntent = new Intent(this, WebActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;

        notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound

        mNotificationManager.notify(1, notification);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},00);



    }

    private class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }



    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Exit App");
            dialog.setMessage("Browser has nothing to go back, so what next?");
            dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
            dialog.setCancelable(false);
            dialog.setNegativeButton("Stay here", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
    }


}
