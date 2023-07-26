package com.socgen.i2r.file.watcher.queue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class FileWatchQueueConsumer {
    private ExecutorService executorServicePool = Executors.newFixedThreadPool(1);
    private Consumer<String> fileConsumer;
    private PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<String>(1000);

    public FileWatchQueueConsumer(Consumer<String> fileConsumer) {
        this.fileConsumer = fileConsumer;
    }

    public void offer(String path) {
            queue.offer(path);
    }

    public void start(){
        executorServicePool.execute(() -> processQueue());
    }

    public void processQueue(){
        while(true){
            try {
                String path = queue.take();
                if(tryLockAndConsume(path)){
                    fileConsumer.accept(path);
                    //PrintFileContent.display(path);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isNewVersion(String path){
        boolean isQueued = queue.contains(path);
        if(isQueued){
            System.out.println("Already Queued : " + path.toString());
        }
        return isQueued;
    }

    private boolean tryLockAndConsume(String path){
        try(
                RandomAccessFile ras = new RandomAccessFile(new File(path),"rw");
                FileLock osLock = ras.getChannel().tryLock();
                ){
            System.out.println("locked file : " + path);
            return true;
        } catch (IOException e) {
            return false;
        }

    }
}
