// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.Closeable;
import android.support.v4.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.graphics.Typeface;
import android.content.res.Resources;
import android.content.Context;
import android.support.v4.content.res.FontResourcesParserCompat;

class TypefaceCompatBaseImpl
{
    private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final int n) {
        return findBestFont(fontFamilyFilesResourceEntry.getEntries(), n, (StyleExtractor<FontResourcesParserCompat.FontFileResourceEntry>)new StyleExtractor<FontResourcesParserCompat.FontFileResourceEntry>() {
            public int getWeight(final FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.getWeight();
            }
            
            public boolean isItalic(final FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.isItalic();
            }
        });
    }
    
    private static <T> T findBestFont(final T[] array, int i, final StyleExtractor<T> styleExtractor) {
        int n;
        if ((i & 0x1) == 0x0) {
            n = 400;
        }
        else {
            n = 700;
        }
        final boolean b = (i & 0x2) != 0x0;
        final int length = array.length;
        T t = null;
        i = 0;
        int n2 = Integer.MAX_VALUE;
        while (i < length) {
            final T t2 = array[i];
            final int abs = Math.abs(styleExtractor.getWeight(t2) - n);
            int n3;
            if (styleExtractor.isItalic(t2) == b) {
                n3 = 0;
            }
            else {
                n3 = 1;
            }
            final int n4 = abs * 2 + n3;
            int n5;
            if (t == null || (n5 = n2) > n4) {
                t = t2;
                n5 = n4;
            }
            ++i;
            n2 = n5;
        }
        return t;
    }
    
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, final int n) {
        final FontResourcesParserCompat.FontFileResourceEntry bestEntry = this.findBestEntry(fontFamilyFilesResourceEntry, n);
        if (bestEntry == null) {
            return null;
        }
        return TypefaceCompat.createFromResourcesFontFile(context, resources, bestEntry.getResourceId(), bestEntry.getFileName(), n);
    }
    
    public Typeface createFromFontInfo(final Context context, final CancellationSignal cancellationSignal, final FontsContractCompat.FontInfo[] array, final int n) {
        if (array.length < 1) {
            return null;
        }
        final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(array, n);
        try {
            final InputStream openInputStream = context.getContentResolver().openInputStream(bestInfo.getUri());
            try {
                final Typeface fromInputStream = this.createFromInputStream(context, openInputStream);
                TypefaceCompatUtil.closeQuietly(openInputStream);
                return fromInputStream;
            }
            catch (IOException ex) {}
        }
        catch (IOException ex2) {}
    }
    
    protected Typeface createFromInputStream(Context tempFile, final InputStream inputStream) {
        tempFile = (Context)TypefaceCompatUtil.getTempFile(tempFile);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile((File)tempFile, inputStream)) {
                return null;
            }
            return Typeface.createFromFile(((File)tempFile).getPath());
        }
        catch (RuntimeException ex) {
            return null;
        }
        finally {
            ((File)tempFile).delete();
        }
    }
    
    public Typeface createFromResourcesFontFile(Context tempFile, final Resources resources, final int n, final String s, final int n2) {
        tempFile = (Context)TypefaceCompatUtil.getTempFile(tempFile);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile((File)tempFile, resources, n)) {
                return null;
            }
            return Typeface.createFromFile(((File)tempFile).getPath());
        }
        catch (RuntimeException ex) {
            return null;
        }
        finally {
            ((File)tempFile).delete();
        }
    }
    
    protected FontsContractCompat.FontInfo findBestInfo(final FontsContractCompat.FontInfo[] array, final int n) {
        return findBestFont(array, n, (StyleExtractor<FontsContractCompat.FontInfo>)new StyleExtractor<FontsContractCompat.FontInfo>() {
            public int getWeight(final FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.getWeight();
            }
            
            public boolean isItalic(final FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.isItalic();
            }
        });
    }
    
    private interface StyleExtractor<T>
    {
        int getWeight(final T p0);
        
        boolean isItalic(final T p0);
    }
}
