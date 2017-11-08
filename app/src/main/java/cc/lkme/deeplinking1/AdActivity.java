package cc.lkme.deeplinking1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;


public class AdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String str=getIntent().getStringExtra("url");
        if(null==str){
            finish();
        }else
        {
            AdActivity.this.requestWindowFeature(1);
        }
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ad);
        //final ADIntentUtils adIntentUtils=new ADIntentUtils(AdActivity.this);
        WebView webview=new WebView(AdActivity.this);// findViewById(R.id.webviewmain);
        //webview.getSettings().setJavaScriptEnabled(true);
        webview.setInitialScale(100);
        webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    AdActivity.this.startActivity(intent);
                }
            }
        });
        rmDangerousJSInterface(webview);
        WebSettings settings = webview.getSettings();
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setSaveFormData(false);
        // The link is ILP
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("IntentUtils","shouldOverrideUrlLoading");
                /*
                if(adIntentUtils.shouldOverrideUrlLoadingByApp(view,url))
                {
                    return true;
                }*/
                if(isThirdPartyScheme(Uri.parse(url)))
                {
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        intent.addFlags(268435456);
                        AdActivity.this.startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        return true;
                    }
                }
                Log.e("IntentUtils","false");
                return false;
            }
        });
        enableHTML5Cache(webview);
        AdActivity.this.setContentView(webview, new ViewGroup.LayoutParams(-1, -1));
        webview.loadUrl(str);
    }

    private final String[] DANGEROUS_JS_INTERFACES = new String[]{"searchBoxJavaBridge_"};
    @SuppressLint({"NewApi"})
    private void rmDangerousJSInterface(WebView webview) {
        try {
            for (String str : this.DANGEROUS_JS_INTERFACES) {
                webview.removeJavascriptInterface(str);
            }
        } catch (Throwable th) {
        }
    }

    private void enableHTML5Cache(WebView webview) {
        WebSettings settings = webview.getSettings();
        File cacheDir = null;
        if (Build.VERSION.SDK_INT >= 8) {
            cacheDir = webview.getContext().getExternalCacheDir();
        }
        if (cacheDir == null) {
            cacheDir = webview.getContext().getCacheDir();
        }
        File gdtCache = new File(cacheDir, "gdtadmobwebcache");
        if (!gdtCache.exists()) {
            gdtCache.mkdirs();
        }
        String cachePath = gdtCache.getAbsolutePath();
        //GDTLogger.d("cache-PATH=" + cachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCachePath(cachePath);
        settings.setAppCacheMaxSize(8388608);
        settings.setAppCacheEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        File gdtDataBaseCache = new File(cacheDir, "gdtadmobwebdatabase");
        if (!gdtDataBaseCache.exists()) {
            gdtDataBaseCache.mkdirs();
        }
        String dataBasePath = gdtDataBaseCache.getAbsolutePath();
        //GDTLogger.d("webdatabase-PATH=" + cachePath);
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(dataBasePath);
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(dataBasePath);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });
    }

    private boolean isThirdPartyScheme(Uri uri) {
        String scheme = uri.getScheme();
        return (scheme.equals("http") || scheme.equals("https")) ? false : true;
    }
}
