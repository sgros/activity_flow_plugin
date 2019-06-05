// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.FilenameFilter;
import java.util.Comparator;
import java.io.File;

public class FileUtils
{
    public static void assertDirectory(final File file) {
        if (!file.exists() && !file.mkdirs()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Directory doesn't exist and can't be created: ");
            sb.append(file.getAbsolutePath());
            throw new IllegalStateException(sb.toString());
        }
        if (file.isDirectory() && file.canWrite()) {
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Directory is not writable directory: ");
        sb2.append(file.getAbsolutePath());
        throw new IllegalStateException(sb2.toString());
    }
    
    public static class FileLastModifiedComparator implements Comparator<File>
    {
        @Override
        public int compare(final File file, final File file2) {
            final long n = lcmp(file.lastModified(), file2.lastModified());
            if (n < 0) {
                return -1;
            }
            if (n == 0) {
                return 0;
            }
            return 1;
        }
    }
    
    public static class FilenameRegexFilter implements FilenameFilter
    {
        private Matcher mCachedMatcher;
        private final Pattern mPattern;
        
        public FilenameRegexFilter(final Pattern mPattern) {
            this.mPattern = mPattern;
        }
        
        @Override
        public boolean accept(final File file, final String s) {
            if (this.mCachedMatcher == null) {
                this.mCachedMatcher = this.mPattern.matcher(s);
            }
            else {
                this.mCachedMatcher.reset(s);
            }
            return this.mCachedMatcher.matches();
        }
    }
}
