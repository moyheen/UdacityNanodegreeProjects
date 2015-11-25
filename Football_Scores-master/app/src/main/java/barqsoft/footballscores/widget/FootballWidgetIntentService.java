package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by moyinoluwa on 11/16/15.
 */
public class FootballWidgetIntentService extends IntentService {
    public static String home = "A";
    public static String away = "B";
    public static String homeGoals = "0";
    public static String awayGoals = "1";

    public FootballWidgetIntentService() {
        super("FootballWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        if (allWidgetIds != null) {
            for (int widgetId : allWidgetIds) {
                int layoutId = R.layout.widget_today_small;
                RemoteViews views = new RemoteViews(getPackageName(), layoutId);
                setRemoteContentDescription(views, home);

                views.setImageViewResource(R.id.widget_icon, R.drawable.ic_launcher);
                views.setTextViewText(R.id.widget_home, home);
                views.setTextViewText(R.id.widget_home_score, homeGoals);
                views.setTextViewText(R.id.widget_away, away);
                views.setTextViewText(R.id.widget_away_score, awayGoals);

                Intent serviceIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, serviceIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, views);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget_icon, description);
    }
}
