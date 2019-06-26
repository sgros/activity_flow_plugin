// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.model;

import java.util.Date;

public class Token
{
    private final Card mCard;
    private final Date mCreated;
    private final String mId;
    private final boolean mLivemode;
    private final String mType;
    private final boolean mUsed;
    
    public Token(final String mId, final boolean mLivemode, final Date mCreated, final Boolean b, final Card mCard, final String mType) {
        this.mId = mId;
        this.mType = mType;
        this.mCreated = mCreated;
        this.mLivemode = mLivemode;
        this.mCard = mCard;
        this.mUsed = b;
    }
    
    public String getId() {
        return this.mId;
    }
    
    public String getType() {
        return this.mType;
    }
}
