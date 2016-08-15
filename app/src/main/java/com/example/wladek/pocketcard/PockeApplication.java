package com.example.wladek.pocketcard;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by wladek on 8/15/16.
 */
@ReportsCrashes(mailTo = "wladek.airo@gmail.com", mode = ReportingInteractionMode.SILENT)
public class PockeApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
        ACRA.init(this);

    }
}
