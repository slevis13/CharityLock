package comslevis13.github.charitylock;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by slevi on 11/30/2017.
 */

public class PaymentHandler extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);

//        String htmlString =
//                "\n" +
//                        "\n" +
//                        "\n" +
//                        "<html>\n" +
//                        "<head>\n" +
//                        "    <script src=\"file:///android_asset/pandajs.js/\" " +
//                        "type=\"text/javascript\"></script>\n" +
//                        "</head>\n" +
//                        "\n" +
//                        "<body>\n" +
//                        "<form id=\"panda_cc_form\" onsubmit=\"tokenize(panda_cc_form)\">\n" +
//                        "    <div>\n" +
//                        "        <label>First Name</label>\n" +
//                        "        <input type=\"text\" data-panda=\"first_name\">\n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    <div>\n" +
//                        "        <label>Last Name</label>\n" +
//                        "        <input type=\"text\" data-panda=\"last_name\">\n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    <div>\n" +
//                        "        <label>Credit Card Number</label>\n" +
//                        "        <input type=\"text\" data-panda=\"credit_card\">\n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    <div>\n" +
//                        "        <label>Expiration</label>\n" +
//                        "        <input type=\"text\" data-panda=\"expiration\">\n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    <div>\n" +
//                        "        <label>CVV</label>\n" +
//                        "        <input type=\"text\" data-panda=\"cvv\">\n" +
//                        "    </div>\n" +
//                        "\n" +
//                        "    <div id=\"tokenize\">\n" +
//                        "        <button type=\"submit\">Tokenize!</button>\n" +
//                        "    </div>\n" +
//                        "</form>\n" +
//                        "<script>\n" +
//                        "function tokenize(form_id) {\n" +
//                        "        try {\n" +
//                        "            Panda.init('pk_test_apOvOwYjV-c-cvupKOcjoQ', form_id);\n" +
//                        "            console.log(\"Init working\");\n" +
//                        "        }\n" +
//                        "        catch(err) {\n" +
//                        "            console.log(\"Error in init method -- ya boy\");\n" +
//                        "        }\n" +
//                        "        Panda.on('success', function(cardToken) {\n" +
//                        "\n" +
//                        "        });\n" +
//                        "        Panda.on('error', function(errors) {\n" +
//                        "\n" +
//                        "        });\n" +
//                        "    }</script>\n" +
//                        "</body>\n" +
//                        "</html>\n";
//        webview.loadDataWithBaseURL("file:///android_asset/",
//                htmlString,
//                "text/html", "UTF-8", null);

        //webview.loadUrl("file:///android_asset/pandaWebView.html");



        setContentView(webview);


    }




}
