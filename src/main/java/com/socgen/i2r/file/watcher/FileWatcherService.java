package com.socgen.i2r.file.watcher;

import com.socgen.i2r.file.watcher.scan.ScanService;
import com.socgen.i2r.file.watcher.watch.WatchService;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileWatcherService {
    public static void main(String[] args) {
        String path = "E://watcher//test";
        WatcherExtraction watcherExtraction = new WatcherExtraction(path);
        WatchService watchService = ServiceSupplier.getWatchService();
        watchService.start();
        watcherExtraction.watcherServiceStart();
        ScanService scanService = ServiceSupplier.getScanService();
        scanService.start();
        ScheduledExecutorService taskScheduler = initScheduler();
        taskScheduler.scheduleAtFixedRate(() -> watcherExtraction.wakeUp(),1,500,TimeUnit.SECONDS);
        }

        private static ScheduledExecutorService initScheduler(){
        BasicThreadFactory factory = new BasicThreadFactory.Builder().build();
        return Executors.newSingleThreadScheduledExecutor(factory);
        }
    }
