package com.smonline.virtual.client.stub;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import com.smonline.virtual.R;

/**
 * @author Lody
 *
 */
public class DaemonService extends Service {

    private static final int NOTIFY_ID = 1001;

	public static void startup(Context context) {
		context.startService(new Intent(context, DaemonService.class));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		startup(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		startService(new Intent(this, InnerService.class));
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.mipmap.notification_icon);
		builder.setContentText(this.getString(R.string.foreground_notification_tip));
		startForeground(NOTIFY_ID, builder.build());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public static final class InnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.mipmap.notification_icon);
		builder.setContentText(this.getString(R.string.foreground_notification_tip));
		startForeground(NOTIFY_ID, builder.build());
		stopForeground(true);
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
        }

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
	}
}
