package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by moyinoluwa on 11/16/15.
 */
public class FootballWidgetIntentService extends IntentService {

    public static final String LEAGUE_COL = "league";
    public static final String DATE_COL = "date";
    public static final String TIME_COL = "time";
    public static final String HOME_COL = "home";
    public static final String AWAY_COL = "away";
    public static final String HOME_GOALS_COL = "home_goals";
    public static final String AWAY_GOALS_COL = "away_goals";
    public static final String MATCH_ID = "match_id";
    public static final String MATCH_DAY = "match_day";

    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL
    };

    private static final int INDEX_LEAGUE = 0;
    private static final int INDEX_HOME_GOALS = 1;

    public FootballWidgetIntentService() {
        super("FootballWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int footballResourceId = R.drawable.ic_launcher;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FootballWidgetProvider.class));

        Uri footballDateUri = DatabaseContract.scores_table.buildScoreWithDate();
        Cursor cursor = getContentResolver().query(footballDateUri, SCORES_COLUMNS, null, null,
                DatabaseContract.scores_table.DATE_COL + " ASC");

        if (cursor == null) {
            return;
        }

        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }

        int league = cursor.getInt(INDEX_LEAGUE);
        String home_goals = cursor.getString(INDEX_HOME_GOALS);
        cursor.close();

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget_today_small;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            views.setImageViewResource(R.id.widget_icon, footballResourceId);

            setRemoteContentDescription(views, String.valueOf(league));

            views.setTextViewText(R.id.widget_high_score, home_goals);

            Intent serviceIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, serviceIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget_icon, description);
    }
}
