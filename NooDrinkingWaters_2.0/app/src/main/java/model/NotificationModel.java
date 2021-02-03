package model;

/**
 * Created by Oracle on 4/9/2018.
 */

public class NotificationModel {
    private String notification;
    private String notificationDate;
    private String dataId;
    private String status;

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "NotificationModel{" +
                        "notification = '" + notification + '\'' +
                        ",notification_date = '" + notificationDate + '\'' +
                        ",data_id = '" + dataId + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
