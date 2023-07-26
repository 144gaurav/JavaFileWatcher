package com.socgen.i2r.file.watcher.scan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanService {

    private final int numThreads;
    private ExecutorService executorService;

    public ScanService(int numThreads) {
        this.numThreads = numThreads;
    }

    public void start(){
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    public synchronized void startFolderScanThread(String path, WatchServiceFileWalker walker){
        executorService.execute(() -> scanWalkFolder(path,walker));
    }

    public static void scanWalkFolder(String path, WatchServiceFileWalker walker){
        try {
            Files.walkFileTree(Paths.get(path),walker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
