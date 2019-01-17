package myPlug;

import myPlug.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

import java.util.concurrent.*;

public class BackgroundService {

    private static ScheduledExecutorService executor = null;

    public static void start () {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        int interval = prop.getInt(Settings.INTERVAL, 0);
        if (interval == 0) {
            return;
        }
        if (executor != null) {
            stop();
        }
        RandomBackgroundTask task = new RandomBackgroundTask();
        executor = Executors.newSingleThreadScheduledExecutor();
        try {
            int delay = prop.isValueSet(IdeBackgroundUtil.EDITOR_PROP)
                    ? interval
                    : 0;
            executor.scheduleAtFixedRate(task, delay, interval, TimeUnit.SECONDS);
        } catch (RejectedExecutionException e) {
            stop();
        }
    }

    public static void stop () {
        if (executor != null && !executor.isTerminated()) {
            executor.shutdownNow();
        }
        executor = null;
    }

    public static void restart () {
        stop();
        start();
    }
}
