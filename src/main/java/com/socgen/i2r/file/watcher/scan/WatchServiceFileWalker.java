package com.socgen.i2r.file.watcher.scan;

import com.socgen.i2r.file.watcher.ServiceSupplier;
import com.socgen.i2r.file.watcher.config.WatcherConfig;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;

public class WatchServiceFileWalker implements FileVisitor<Path> {

    private final Consumer<String> fileConsumer;
    private final Filter<String> folderFilter;
    private final Filter<String> fileFilter;

    public WatchServiceFileWalker(Consumer<String> fileConsumer, Filter<String> folderFilter, Filter<String> fileFilter) {
        this.fileConsumer = fileConsumer;
        this.folderFilter = folderFilter;
        this.fileFilter = fileFilter;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if(folderFilter.accept(dir.toString())){
            WatcherConfig config = new WatcherConfig(fileConsumer,dir.toString(),folderFilter,fileFilter);
            ServiceSupplier.getWatchService().register(config);
            return FileVisitResult.CONTINUE;
        }else {
           return FileVisitResult.SKIP_SUBTREE;
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(fileFilter.accept(file.toString())){
            fileConsumer.accept(file.toString());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
