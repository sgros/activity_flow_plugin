// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.time;

import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.text.Format;

abstract class FormatCache<F extends Format>
{
    static final int NONE = -1;
    private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache;
    private final ConcurrentMap<MultipartKey, F> cInstanceCache;
    
    static {
        cDateTimeInstanceCache = new ConcurrentHashMap<MultipartKey, String>(7);
    }
    
    FormatCache() {
        this.cInstanceCache = new ConcurrentHashMap<MultipartKey, F>(7);
    }
    
    private F getDateTimeInstance(final Integer n, final Integer n2, final TimeZone timeZone, final Locale locale) {
        Locale default1 = locale;
        if (locale == null) {
            default1 = Locale.getDefault();
        }
        return this.getInstance(getPatternForStyle(n, n2, default1), timeZone, default1);
    }
    
    static String getPatternForStyle(final Integer n, final Integer n2, final Locale locale) {
        final MultipartKey multipartKey = new MultipartKey(new Object[] { n, n2, locale });
        String pattern;
        if ((pattern = FormatCache.cDateTimeInstanceCache.get(multipartKey)) == null) {
            Label_0063: {
                if (n != null) {
                    break Label_0063;
                }
                try {
                    DateFormat dateFormat = DateFormat.getTimeInstance(n2, locale);
                    while (true) {
                        pattern = ((SimpleDateFormat)dateFormat).toPattern();
                        final String s = FormatCache.cDateTimeInstanceCache.putIfAbsent(multipartKey, pattern);
                        if (s != null) {
                            pattern = s;
                            return pattern;
                        }
                        return pattern;
                        Block_4: {
                            break Block_4;
                            Label_0079: {
                                dateFormat = DateFormat.getDateTimeInstance(n, n2, locale);
                            }
                            continue;
                        }
                        dateFormat = DateFormat.getDateInstance(n, locale);
                        continue;
                    }
                }
                // iftrue(Label_0079:, n2 != null)
                catch (ClassCastException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("No date time pattern for locale: ");
                    sb.append(locale);
                    throw new IllegalArgumentException(sb.toString());
                }
            }
        }
        return pattern;
    }
    
    protected abstract F createInstance(final String p0, final TimeZone p1, final Locale p2);
    
    F getDateInstance(final int i, final TimeZone timeZone, final Locale locale) {
        return this.getDateTimeInstance(i, null, timeZone, locale);
    }
    
    F getDateTimeInstance(final int i, final int j, final TimeZone timeZone, final Locale locale) {
        return this.getDateTimeInstance(Integer.valueOf(i), Integer.valueOf(j), timeZone, locale);
    }
    
    public F getInstance() {
        return this.getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
    }
    
    public F getInstance(final String s, final TimeZone timeZone, final Locale locale) {
        if (s != null) {
            TimeZone default1;
            if ((default1 = timeZone) == null) {
                default1 = TimeZone.getDefault();
            }
            Locale default2;
            if ((default2 = locale) == null) {
                default2 = Locale.getDefault();
            }
            final MultipartKey multipartKey = new MultipartKey(new Object[] { s, default1, default2 });
            Format instance;
            if ((instance = this.cInstanceCache.get(multipartKey)) == null) {
                instance = this.createInstance(s, default1, default2);
                final Format format = this.cInstanceCache.putIfAbsent(multipartKey, (F)instance);
                if (format != null) {
                    instance = format;
                }
            }
            return (F)instance;
        }
        throw new NullPointerException("pattern must not be null");
    }
    
    F getTimeInstance(final int i, final TimeZone timeZone, final Locale locale) {
        return this.getDateTimeInstance(null, i, timeZone, locale);
    }
    
    private static class MultipartKey
    {
        private int hashCode;
        private final Object[] keys;
        
        public MultipartKey(final Object... keys) {
            this.keys = keys;
        }
        
        @Override
        public boolean equals(final Object o) {
            return Arrays.equals(this.keys, ((MultipartKey)o).keys);
        }
        
        @Override
        public int hashCode() {
            if (this.hashCode == 0) {
                final Object[] keys = this.keys;
                final int length = keys.length;
                int i = 0;
                int hashCode = 0;
                while (i < length) {
                    final Object o = keys[i];
                    int n = hashCode;
                    if (o != null) {
                        n = hashCode * 7 + o.hashCode();
                    }
                    ++i;
                    hashCode = n;
                }
                this.hashCode = hashCode;
            }
            return this.hashCode;
        }
    }
}
