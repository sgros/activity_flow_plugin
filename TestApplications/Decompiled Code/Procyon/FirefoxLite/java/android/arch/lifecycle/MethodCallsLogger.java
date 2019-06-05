// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Map;

public class MethodCallsLogger
{
    private Map<String, Integer> mCalledMethods;
    
    public MethodCallsLogger() {
        this.mCalledMethods = new HashMap<String, Integer>();
    }
}
