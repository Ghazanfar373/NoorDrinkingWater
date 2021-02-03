package Web;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.frandydevs.apps.noordrinkingwater.ParentActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import fragments.ParentFragment;

public class LoopJRequestHandler extends JsonHttpResponseHandler {
    //important notes
    public int REQUEST_TYPE = -1;//17 last generated
    ProgressDialog dialog;
    Toast toast;
    private Context context;
    private ParentFragment parentFragment;
    private ParentActivity parentActivity;
    String dialogMessage = "";
    boolean shouldHideDialog = true;

    public LoopJRequestHandler(Context context, int REQUEST_TYPE, ParentFragment parentFragment, ParentActivity parentActivity,
                               String dialogMessage, boolean shouldHideDialog, ProgressDialog progressDialog) {
        this.context = context;
        this.parentFragment = parentFragment;
        this.parentActivity = parentActivity;
        this.REQUEST_TYPE = REQUEST_TYPE;
        this.dialogMessage = dialogMessage;
        this.shouldHideDialog = shouldHideDialog;
        dialog = progressDialog;//if there is are multiple requests after one another then calling class will send same create dialog object and send same reference again and again, so
        // that we can get a single instance of dialog.
    }

    public LoopJRequestHandler(Context context, int REQUEST_TYPE, ParentFragment parentFragment,
                               ParentActivity parentActivity, String dialogMessage) {
        this.context = context;
        this.parentFragment = parentFragment;
        this.parentActivity = parentActivity;
        this.REQUEST_TYPE = REQUEST_TYPE;
        this.dialogMessage = dialogMessage;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        showDialog(dialogMessage);
        if (parentFragment != null) {
            parentFragment.IS_REQUEST_IN_PROCESS = true;
        } else {
            parentActivity.IS_REQUEST_IN_PROCESS = true;
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (shouldHideDialog) {//otherwise dialog will be handled in response function of calling class
            hideDialog();
        }
        if (parentFragment != null) {
            parentFragment.IS_REQUEST_IN_PROCESS = false;
        } else {
            parentActivity.IS_REQUEST_IN_PROCESS = false;
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        if (parentFragment == null) {
            parentActivity.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            parentFragment.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
        }
        hideDialog();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        if (parentFragment == null) {
            parentActivity.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            parentFragment.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
        }
        hideDialog();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        if (parentFragment == null) {
            parentActivity.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
        } else {
            parentFragment.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
        }
        hideDialog();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        if (response != null) {
            if (parentFragment == null) {
                parentActivity.handleServerResponse(REQUEST_TYPE, response);
            } else {
                parentFragment.handleServerResponse(REQUEST_TYPE, response);
            }
        } else {
            if (parentFragment == null) {
                parentActivity.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
            } else {
                parentFragment.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
            }
            hideDialog();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        if (responseString != null) {
            if (parentFragment == null) {
                parentActivity.handleServerResponse(REQUEST_TYPE, responseString);
            } else {
                parentFragment.handleServerResponse(REQUEST_TYPE, responseString);
            }
        } else {
            if (parentFragment == null) {
                parentActivity.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
            } else {
                parentFragment.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
            }
            hideDialog();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);
        if (response != null) {
            if (parentFragment == null) {
                parentActivity.handleServerResponse(REQUEST_TYPE, response);
            } else {
                parentFragment.handleServerResponse(REQUEST_TYPE, response);
            }
        } else {
            if (parentFragment == null) {
                parentActivity.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
            } else {
                parentFragment.showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
            }
            hideDialog();
        }
    }

    private void showDialog(String message) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.setCancelable(false);
            dialog.setMessage(message);
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

