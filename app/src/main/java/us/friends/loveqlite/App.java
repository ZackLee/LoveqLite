package us.friends.loveqlite;

import android.app.Application;

import us.friends.loveqlite.util.FileUtils;

/**
 * Created by lino on 16/5/7.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileUtils.makeDirectory();
    }
}
