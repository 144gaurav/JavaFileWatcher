package com.socgen.i2r.file.watcher;

import com.socgen.i2r.file.watcher.queue.FileWatchQueueProcessor;
import com.socgen.i2r.file.watcher.watch.WatchService;
import com.socgen.i2r.file.watcher.config.WatcherConfig;
import com.socgen.i2r.file.watcher.scan.ScanService;

public class WatcherExtraction {
    private String path;
    private WatcherConfig watcherConfig;
    private WatchService watchService = ServiceSupplier.getWatchService();
    private ScanService scanService = ServiceSupplier.getScanService();
    protected final FileWatchQueueProcessor fileWatchQueueProcessor;

    public WatcherConfig getWatcherConfig() {
        return watcherConfig;
    }

    public String getPath() {
        return path;
    }

    public WatcherExtraction(String path){
        this.path = path;
        this.watcherConfig = new WatcherConfig(this::consumeFile, path,this::filterFolder,this::fileFilter);
        this.fileWatchQueueProcessor = new FileWatchQueueProcessor(PrintFileContent::display,1);
    }

    public void watcherServiceStart(){
        fileWatchQueueProcessor.start();
        registerAllWatchedFolders();
    }

    protected void registerAllWatchedFolders(){
        watchService.register(watcherConfig);
    }

    public void wakeUp(){
        scanService.startFolderScanThread(path,watcherConfig.watchServiceFileWalker);
    }

    protected boolean filterFolder(String path){
        return true;
    }

    protected boolean fileFilter(String path){
        return true;
    }

    private synchronized void consumeFile(String path){
        if(!fileWatchQueueProcessor.isNewVersion(path)){
            fileWatchQueueProcessor.offer(path);
        }
    }
}
