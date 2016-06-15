package us.friends.loveqlite;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import us.friends.loveqlite.bean.Datum;

/**
 * Created by lino on 16/5/12.
 */
public class NotificationUtil {
    public static NotificationManager notificationManager;

    public static Notification.Builder publicNotification(Context context, final Datum datum) {
        final NotificationManager notificationManager = getNotificationManager(context);

        final Notification.Builder notification = new Notification.Builder(context);
        notification.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.downloading))
                .setContentText(datum.getFile_name())
                .setAutoCancel(true);
        notificationManager.notify(Integer.valueOf(datum.getId()), notification.build());
        return notification;
    }

    public static void updateNotification(Context context, Notification.Builder notification, Datum datum, int progress) {
        final NotificationManager notificationManager = getNotificationManager(context);
        notification.setProgress(100, progress, false);
        notificationManager.notify(Integer.valueOf(datum.getId()), notification.build());
    }

    public static void completeNotification(Context context, Notification.Builder notification, Datum datum) {
        File file = new File(Environment.getExternalStorageDirectory() + "/Music/" + datum.getFile_name());
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setDataAndType(Uri.fromFile(file), "audio/*");
        PendingIntent playIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        final NotificationManager notificationManager = getNotificationManager(context);
        notification.setContentTitle(context.getString(R.string.download_finish))
                .setContentText(datum.getFile_name())
                .setProgress(0, 0, false)
                .setVibrate(new long[]{0, 300, 500, 700})
                .setContentIntent(playIntent);
        notificationManager.notify(Integer.valueOf(datum.getId()), notification.build());
    }

    public static NotificationManager getNotificationManager(Context context) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

}
