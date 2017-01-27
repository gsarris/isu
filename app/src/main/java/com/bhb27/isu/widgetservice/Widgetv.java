/*
 * Copyright (C) Felipe de Leon <fglfgl27@gmail.com>
 *
 * This file is part of iSu.
 *
 * iSu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iSu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with iSu.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.bhb27.isu.widgetservice;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bhb27.isu.Main;
import com.bhb27.isu.PerAppActivity;
import com.bhb27.isu.R;
import com.bhb27.isu.tools.Tools;
import com.bhb27.isu.tools.Constants;

import android.app.Activity;

public class Widgetv extends AppWidgetProvider {

    private static final String ACTION_SU = "SU";
    private static final String ACTION_SELINUX = "SELinux";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId: appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layoutv);
            remoteViews.setTextViewText(R.id.iSuMain, "SU" + "\n" + (Tools.SuBinary(Constants.xbin_su) ?
                context.getString(R.string.activated) : context.getString(R.string.deactivated)));
            remoteViews.setTextViewText(R.id.iSuMonitor, "SELinux" + "\n" + Tools.getSELinuxStatus());
            remoteViews.setOnClickPendingIntent(R.id.iSuMain, getPendingSelfIntent(context, ACTION_SU));
            remoteViews.setOnClickPendingIntent(R.id.iSuMonitor, getPendingSelfIntent(context, ACTION_SELINUX));

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_SU.equals(intent.getAction())) {
            Tools.SwitchSu(!Tools.SuBinary(Constants.xbin_su), context);
            Tools.DoAToast("SU", context);
        }
        if (ACTION_SELINUX.equals(intent.getAction())) {
            Tools.SwitchSelinux(!Tools.isSELinuxActive(), context);
            Tools.DoAToast("Selinux", context);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
