// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import java.util.List;

public abstract class InputMerger
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("InputMerger");
    }
    
    public static InputMerger fromClassName(final String s) {
        try {
            return (InputMerger)Class.forName(s).newInstance();
        }
        catch (Exception ex) {
            final Logger value = Logger.get();
            final String tag = InputMerger.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Trouble instantiating + ");
            sb.append(s);
            value.error(tag, sb.toString(), ex);
            return null;
        }
    }
    
    public abstract Data merge(final List<Data> p0);
}
