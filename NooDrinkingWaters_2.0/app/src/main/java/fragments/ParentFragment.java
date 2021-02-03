package fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import app.AppController;
import app.DataSaving;
import db.SQLiteDatabaseHelper;

public class ParentFragment extends Fragment {

    Context context;
    Bundle extras;
    View returnView;
    Toast toast;
    public boolean IS_REQUEST_IN_PROCESS = false;
    public LinearLayoutManager linearLayoutManager;
    LinearLayout.LayoutParams llParams;
    FragmentManager fragmentManager;
    RelativeLayout.LayoutParams rlParams;
    DataSaving   dataSaving = AppController.getDataSaving();
    SQLiteDatabaseHelper sqLiteDatabaseHelper;

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showToast(String message, int length, int gravity) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, length);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    public void handleServerResponse(int REQUEST_TYPE, JSONArray jsonArray) {
        //this method will be called in child fragments
    }

    public void handleServerResponse(int REQUEST_TYPE, JSONObject string) {
        //this method will be called in child fragments
    }

    public void handleServerResponse(int REQUEST_TYPE, String string) {
        //this method will be called in child fragments
    }

}
