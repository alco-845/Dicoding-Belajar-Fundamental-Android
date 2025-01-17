package com.alcorp.widgetapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class RandomNumbersWidget : AppWidgetProvider() {

    companion object {
        private const val WIDGET_CLICK = "widgets_click"
        private const val WIDGET_ID_EXTRA = "widget_id_extra"
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.random_numbers_widget)
        val lastUpdate = "Random: " + NumberGenerator.generate(100)
        views.setTextViewText(R.id.appwidget_text, lastUpdate)
        views.setOnClickPendingIntent(R.id.btn_click, getPendingSelfIntent(context, appWidgetId, WIDGET_CLICK))
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // lakukan looping untuk update semua widget
        for (appWidgetId in appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    // gunakan onEnabled untuk memasukan function yang relevan ketika widget pertama dibuat
//    override fun onEnabled(context: Context?) {
//        super.onEnabled(context)
//    }

    // gunakan onDisabled untuk memasukan function yang relevan ketika widget terakhir dibuat
//    override fun onDisabled(context: Context?) {
//        super.onDisabled(context)
//    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (WIDGET_CLICK == intent.action){
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val views = RemoteViews(context.packageName, R.layout.random_numbers_widget)
            val lastUpdate = "Random: " + NumberGenerator.generate(100)
            val appWidgetId = intent.getIntExtra(WIDGET_ID_EXTRA, 0)
            views.setTextViewText(R.id.appwidget_text, lastUpdate)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getPendingSelfIntent(context: Context, appWidgetId: Int, action: String): PendingIntent {
        val intent = Intent(context, javaClass)
        intent.action = action
        intent.putExtra(WIDGET_ID_EXTRA, appWidgetId)
        return PendingIntent.getBroadcast(context, appWidgetId, intent, 0)
    }
}