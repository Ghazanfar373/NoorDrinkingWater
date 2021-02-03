package Web;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import app.AppController;
import app.DataSaving;

public class WebRequestHandlerInstance {

    private static final String BASE_URL = "http://new.noorwaters.com/";
    public static final String PROCESS_URL = "process";

    private static int DEFAULT_TIMEOUT = 30 * 1000;
    private static AsyncHttpClient client = new AsyncHttpClient();

    private static void setTimeOut() {
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        clearHeaders();
        addHeaders(false);
        setTimeOut();
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        post(url, params, responseHandler, true);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, boolean shouldIncludeHeader) {
        clearHeaders();
        addHeaders(shouldIncludeHeader);
        setTimeOut();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static void clearHeaders() {
        client.removeAllHeaders();
    }

    public static void addHeaders(boolean shouldIncludeHeader) {
        if (shouldIncludeHeader) {
            client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        }
    }
}