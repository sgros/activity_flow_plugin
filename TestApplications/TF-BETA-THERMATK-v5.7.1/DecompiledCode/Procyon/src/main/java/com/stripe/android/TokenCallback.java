// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android;

import com.stripe.android.model.Token;

public interface TokenCallback
{
    void onError(final Exception p0);
    
    void onSuccess(final Token p0);
}
