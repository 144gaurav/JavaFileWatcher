package com.socgen.i2r.file.watcher.watch;

import com.socgen.i2r.file.watcher.config.WatcherConfig;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WatchService {
    private int watchThreads;
    private boolean isRunning = false;
    private ExecutorService executorService;
    protected final ConcurrentMap<WatchKey, WatcherConfig> configPerKey = new ConcurrentHashMap<>();

    public WatchService(int noOfThreads){
        this.watchThreads = noOfThreads;
    }

    public void start() {
        isRunning = true;
        executorService = Executors.newFixedThreadPool(watchThreads);
    }

    public void register(WatcherConfig config){
        registerWatchService(config);
    }

    private void run(java.nio.file.WatchService watchService){
        while(isRunning){
            try {
                handleEvent(watchService.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void registerWatchService(WatcherConfig config) {
        Path path = Paths.get(config.path);
        FileSystem fileSystem = path.toAbsolutePath().getRoot().getFileSystem();
        try {
            java.nio.file.WatchService watchService = fileSystem.newWatchService();
           WatchKey key =  path.register(watchService,StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            configPerKey.put(key,config);
            executorService.execute(() -> run(watchService));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

            }

    private void handleEvent(WatchKey key){
        List<WatchEvent> eventList = new ArrayList<>();
        WatcherConfig config = configPerKey.get(key);
        eventList.addAll(key.pollEvents());
        key.reset();
        eventList.forEach(event -> {
                        takeNewFileEvent(key,event,config);
        });
    }
    private void takeNewFileEvent(WatchKey key,WatchEvent event, WatcherConfig config){
        Path dir = (Path) key.watchable();
        Path context = (Path) event.context();
        Path fullPath = dir.resolve(context);
        config.eventConsumer.accept(fullPath.toString());
    }

}
