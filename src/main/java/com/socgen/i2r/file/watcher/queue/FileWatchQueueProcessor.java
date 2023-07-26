package com.socgen.i2r.file.watcher.queue;

import java.util.function.Consumer;

public class FileWatchQueueProcessor {
    private FileWatchQueueConsumer littleConsumer;
    private final Consumer<String> consumer;
    private final int threads;
    public FileWatchQueueProcessor(Consumer<String> consumer, int threads){
        this.consumer = consumer;
        this.threads = threads;
    }

    public void start(){
        littleConsumer = new FileWatchQueueConsumer(consumer);
        littleConsumer.start();
    }

    public boolean isNewVersion(String path){
        return littleConsumer.isNewVersion(path);
    }

    public void offer(String path){
        littleConsumer.offer(path);
    }
}
