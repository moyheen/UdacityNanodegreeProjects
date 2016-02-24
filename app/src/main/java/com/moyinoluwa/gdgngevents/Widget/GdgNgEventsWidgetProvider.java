package com.moyinoluwa.gdgngevents.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.moyinoluwa.gdgngevents.UpcomingEventsFragment;

/**
 * Implementation of App Widget functionality.
 */
public class GdgNgEventsWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, GdgNgEventsIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, GdgNgEventsIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (UpcomingEventsFragment.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, GdgNgEventsIntentService.class));
        }
    }
}

