package com.socgen.i2r.file.watcher;

import com.socgen.i2r.file.watcher.scan.ScanService;
import com.socgen.i2r.file.watcher.watch.WatchService;

public class ServiceSupplier {
    private static ServiceSupplier serviceSupplier;
    private static final ScanService scanService = new ScanService(1);
    private static final WatchService watchService = new WatchService(1);

    public static ScanService getScanService(){
        return scanService;
    }

    public static WatchService getWatchService(){
        return watchService;
    }
}
