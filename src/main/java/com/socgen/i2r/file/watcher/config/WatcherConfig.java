package com.socgen.i2r.file.watcher.config;

import com.socgen.i2r.file.watcher.scan.WatchServiceFileWalker;
import lombok.Builder;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.function.Consumer;

public class WatcherConfig {
    public final Consumer<String> eventConsumer;
    public final WatchServiceFileWalker watchServiceFileWalker;
    public final String path;
    public final DirectoryStream.Filter<String> folderFilter;
    public final DirectoryStream.Filter<String> fileFilter;

    @Builder
    public WatcherConfig(Consumer<String> eventConsumer, String path, DirectoryStream.Filter<String> folderFilter, DirectoryStream.Filter<String> fileFilter) {
        this.eventConsumer = eventConsumer;
        this.watchServiceFileWalker = new WatchServiceFileWalker(eventConsumer,folderFilter,fileFilter);
        this.path = path;
        this.folderFilter = folderFilter;
        this.fileFilter = fileFilter;
    }
}
