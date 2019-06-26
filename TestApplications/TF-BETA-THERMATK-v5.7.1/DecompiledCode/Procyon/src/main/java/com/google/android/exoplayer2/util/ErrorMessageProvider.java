// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import android.util.Pair;

public interface ErrorMessageProvider<T extends Throwable>
{
    Pair<Integer, String> getErrorMessage(final T p0);
}
