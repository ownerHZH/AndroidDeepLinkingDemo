package cc.lkme.deeplinking1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //Button open_html;

    EditText dditText;
    Button wai,nei,button3;
    Button webview2Btn,browser2Btn,intent2Btn;
    TextView schemeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        open_html = (Button) findViewById(R.id.open_html);
        open_html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BrowserActivity.class));
            }
        });
        if (getIntent().getData()!= null){
            //获取query中的数据
            String click_id = getIntent().getData().getQueryParameter("click_id");
        }*/

        dditText=(EditText)findViewById(R.id.editText);
        wai=(Button)findViewById(R.id.buttonWai);
        wai.setOnClickListener(clickListener);
        nei=(Button)findViewById(R.id.buttonNei);
        nei.setOnClickListener(clickListener);
        button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(clickListener);

        webview2Btn=(Button)findViewById(R.id.webview2);
        webview2Btn.setOnClickListener(clickListener);
        browser2Btn=(Button)findViewById(R.id.browser2);
        browser2Btn.setOnClickListener(clickListener);
        intent2Btn=(Button)findViewById(R.id.intent2);
        intent2Btn.setOnClickListener(clickListener);
        schemeTextView=(TextView) findViewById(R.id.textView);

        //http://zt.jd.com/ad/appjump.shtml?turl=http%3A%2F%2Fsale.jd.com%2Fm%2Fact%2FOFtlMS3Ksg.html&platform=1
        //ctrip://wireless/InlandHotel?checkInDate=20170504&checkOutDate=20170505&hotelId=687592&allianceid=288562&sid=960124&sourceid=2504&ouid=Android_Singapore_687592
        //weixin://dl/moments
        dditText.setText("http://zt.jd.com/ad/appjump.shtml?turl=http%3A%2F%2Fsale.jd.com%2Fm%2Fact%2FOFtlMS3Ksg.html&platform=1");

    }

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttonNei://webview
                    openByWebView(dditText.getText().toString().trim());
                    break;
                case R.id.buttonWai://browser
                    openByBrowser(dditText.getText().toString().trim());
                    break;
                case R.id.button3://intent
                    openByIntent(dditText.getText().toString().trim());
                    break;
                case R.id.browser2://browser2
                    openByBrowser(schemeTextView.getText().toString().trim());
                    break;
                case R.id.webview2://webview2
                    openByWebView(schemeTextView.getText().toString().trim());
                    break;
                case R.id.intent2://intent2
                    openByIntent(schemeTextView.getText().toString().trim());
                    break;
            }
        }
    };

    private void openByIntent(String str) {


            Intent intent = new Intent();
            Uri content_url = Uri.parse(str);
            intent.setData(content_url);
            intent.setAction("android.intent.action.VIEW");
       //     intent.addCategory(Intent.CATEGORY_BROWSABLE);
            //intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(268435456);
            if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                MainActivity.this.startActivity(intent);
                return;
            }
           return;
    }

    private void openByBrowser(String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(str);
        intent.setData(content_url);
        intent.addFlags(268435456);
//        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);
    }

    private void openByWebView(String str) {

        Intent intent = new Intent();
        Context context = getApplicationContext();
        intent.setClassName(context, "cc.lkme.deeplinking1.AdActivity");
        intent.putExtra("url", str);
        intent.addFlags(268435456);
        context.startActivity(intent);
        return;
    }



}


/**
 * if(adIntentUtils.isAcceptedScheme(dditText.getText().toString().trim())){
 // The link is ILP
 webview.setWebViewClient(new WebViewClient(){
@Override
public boolean shouldOverrideUrlLoading(WebView view, String url) {
Log.e("IntentUtils","shouldOverrideUrlLoading");
if(adIntentUtils.shouldOverrideUrlLoadingByApp(view,url))
{
return true;
}
Log.e("IntentUtils","false");
return super.shouldOverrideUrlLoading(view, url);
}
});
 webview.loadUrl(dditText.getText().toString().trim());

 }else {
 //The link is Scheme
 try {
 Intent intent = Intent.parseUri(dditText.getText().toString().trim(), Intent.URI_INTENT_SCHEME);
 MainActivity.this.startActivity(intent);
 } catch (URISyntaxException e) {
 e.printStackTrace();
 }
 }
 */
