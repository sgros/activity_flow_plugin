// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.exception;

public class CardException extends StripeException
{
    private String charge;
    private String code;
    private String declineCode;
    private String param;
    
    public CardException(final String s, final String s2, final String code, final String param, final String declineCode, final String charge, final Integer n, final Throwable t) {
        super(s, s2, n, t);
        this.code = code;
        this.param = param;
        this.declineCode = declineCode;
        this.charge = charge;
    }
}
