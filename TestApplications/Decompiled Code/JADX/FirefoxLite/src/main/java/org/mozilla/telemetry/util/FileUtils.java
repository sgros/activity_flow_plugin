package org.mozilla.telemetry.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

    public static class FileLastModifiedComparator implements Comparator<File> {
        public int compare(File file, File file2) {
            int i = (file.lastModified() > file2.lastModified() ? 1 : (file.lastModified() == file2.lastModified() ? 0 : -1));
            if (i < 0) {
                return -1;
            }
            return i == 0 ? 0 : 1;
        }
    }

    public static class FilenameRegexFilter implements FilenameFilter {
        private Matcher mCachedMatcher;
        private final Pattern mPattern;

        public FilenameRegexFilter(Pattern pattern) {
            this.mPattern = pattern;
        }

        public boolean accept(File file, String str) {
            if (this.mCachedMatcher == null) {
                this.mCachedMatcher = this.mPattern.matcher(str);
            } else {
                this.mCachedMatcher.reset(str);
            }
            return this.mCachedMatcher.matches();
        }
    }

    public static void assertDirectory(File file) {
        StringBuilder stringBuilder;
        if (!file.exists() && !file.mkdirs()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Directory doesn't exist and can't be created: ");
            stringBuilder.append(file.getAbsolutePath());
            throw new IllegalStateException(stringBuilder.toString());
        } else if (!file.isDirectory() || !file.canWrite()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Directory is not writable directory: ");
            stringBuilder.append(file.getAbsolutePath());
            throw new IllegalStateException(stringBuilder.toString());
        }
    }
}
