package fcm;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.frandydevs.apps.noordrinkingwater.MainActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.frandydevs.apps.noordrinkingwater.RateUsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Map;

import app.DataSaving;

import static app.AppConfig.FIRE_BASE_TOKEN;
import static app.AppConfig.NEXT_NOTIFICATION_ID;
import static app.AppConfig.PREF_IS_LOGIN;
import static app.AppConfig.PREF_USER_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Context context;
    DataSaving dataSaving = new DataSaving();
    NotificationManager notificationManager;
    private static String DEFAULT_CHANNEL_ID = "ndw_default_channel";
    private static String DEFAULT_CHANNEL_NAME = "NDW Default";
    private String actionType = "", actionData = "";

    Map<String, String> data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = this;
        data = remoteMessage.getData();
        String userID = data.get("user_id");
        String isLogin = dataSaving.getPrefValue(context, PREF_IS_LOGIN);
        String loginUserID = dataSaving.getPrefValue(context, PREF_USER_ID);

        if (isLogin.equalsIgnoreCase("true") && userID != null && userID.equalsIgnoreCase(loginUserID)) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            createNotificationChannel(notificationManager);
            int nextNotificationID = Integer.parseInt(dataSaving.getPrefValue(context, NEXT_NOTIFICATION_ID));
            notificationManager.notify(nextNotificationID, getNotification(nextNotificationID, data).build());
            dataSaving.setPrefValue(context, NEXT_NOTIFICATION_ID, String.valueOf(nextNotificationID + 1));
        }
    }

    /*
     * Create NotificationChannel as required from Android 8.0 (Oreo)
     * */
    public static void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create channel only if it is not already created
            if (notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(
                        DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                ));
            }
        }
    }

    public NotificationCompat.Builder getNotification(int notificationID, Map<String, String> data) {
        Intent notificationIntent = new Intent(this, RateUsActivity.class);
        notificationIntent.putExtra("orderID", data.get("user_id"));
        notificationIntent.putExtra("salesTeam", data.get("sales_team"));
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                notificationID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Order Delivered")
                .setContentText("Order delivered successfully. Click here to rate our service.")
                .setGroup("group_" + notificationID)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Order delivered successfully. Click here to rate our service."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }
        return builder;
    }

    @Override
    public void onNewToken(String s) {
        context = this;
        super.onNewToken(s);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        dataSaving.setPrefValue(context, FIRE_BASE_TOKEN, token);
                    }
                });
    }
}
