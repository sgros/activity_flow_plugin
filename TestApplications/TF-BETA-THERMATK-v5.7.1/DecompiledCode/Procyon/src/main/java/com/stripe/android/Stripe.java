// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android;

import android.os.Build$VERSION;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.StripeException;
import com.stripe.android.model.Token;
import com.stripe.android.net.StripeApiHandler;
import com.stripe.android.net.RequestOptions;
import com.stripe.android.util.StripeNetworkUtils;
import android.os.AsyncTask;
import java.util.concurrent.Executor;
import com.stripe.android.model.Card;

public class Stripe
{
    private String defaultPublishableKey;
    TokenCreator tokenCreator;
    
    public Stripe(final String defaultPublishableKey) throws AuthenticationException {
        this.tokenCreator = (TokenCreator)new TokenCreator() {
            @Override
            public void create(final Card card, final String s, final Executor executor, final TokenCallback tokenCallback) {
                Stripe.this.executeTokenTask(executor, new AsyncTask<Void, Void, ResponseWrapper>() {
                    protected ResponseWrapper doInBackground(final Void... array) {
                        try {
                            return new ResponseWrapper(StripeApiHandler.createToken(StripeNetworkUtils.hashMapFromCard(card), RequestOptions.builder(s).build()), (Exception)null);
                        }
                        catch (StripeException ex) {
                            return new ResponseWrapper((Token)null, (Exception)ex);
                        }
                    }
                    
                    protected void onPostExecute(final ResponseWrapper responseWrapper) {
                        Stripe.this.tokenTaskPostExecution(responseWrapper, tokenCallback);
                    }
                });
            }
        };
        this.setDefaultPublishableKey(defaultPublishableKey);
    }
    
    private void executeTokenTask(final Executor executor, final AsyncTask<Void, Void, ResponseWrapper> asyncTask) {
        if (executor != null && Build$VERSION.SDK_INT > 11) {
            asyncTask.executeOnExecutor(executor, (Object[])new Void[0]);
        }
        else {
            asyncTask.execute((Object[])new Void[0]);
        }
    }
    
    private void tokenTaskPostExecution(final ResponseWrapper responseWrapper, final TokenCallback tokenCallback) {
        final Token token = responseWrapper.token;
        if (token != null) {
            tokenCallback.onSuccess(token);
        }
        else {
            final Exception error = responseWrapper.error;
            if (error != null) {
                tokenCallback.onError(error);
            }
            else {
                tokenCallback.onError(new RuntimeException("Somehow got neither a token response or an error response"));
            }
        }
    }
    
    private void validateKey(final String s) throws AuthenticationException {
        final Integer value = 0;
        if (s == null || s.length() == 0) {
            throw new AuthenticationException("Invalid Publishable Key: You must use a valid publishable key to create a token.  For more info, see https://stripe.com/docs/stripe.js.", null, value);
        }
        if (!s.startsWith("sk_")) {
            return;
        }
        throw new AuthenticationException("Invalid Publishable Key: You are using a secret key to create a token, instead of the publishable one. For more info, see https://stripe.com/docs/stripe.js", null, value);
    }
    
    public void createToken(final Card card, final TokenCallback tokenCallback) {
        this.createToken(card, this.defaultPublishableKey, tokenCallback);
    }
    
    public void createToken(final Card card, final String s, final TokenCallback tokenCallback) {
        this.createToken(card, s, null, tokenCallback);
    }
    
    public void createToken(final Card card, final String s, final Executor executor, final TokenCallback tokenCallback) {
        Label_0043: {
            if (card == null) {
                break Label_0043;
            }
            Label_0031: {
                if (tokenCallback == null) {
                    break Label_0031;
                }
                try {
                    this.validateKey(s);
                    this.tokenCreator.create(card, s, executor, tokenCallback);
                    return;
                    throw new RuntimeException("Required Parameter: 'card' is required to create a token");
                    throw new RuntimeException("Required Parameter: 'callback' is required to use the created token and handle errors");
                }
                catch (AuthenticationException ex) {
                    tokenCallback.onError(ex);
                }
            }
        }
    }
    
    public void setDefaultPublishableKey(final String defaultPublishableKey) throws AuthenticationException {
        this.validateKey(defaultPublishableKey);
        this.defaultPublishableKey = defaultPublishableKey;
    }
    
    private class ResponseWrapper
    {
        final Exception error;
        final Token token;
        
        private ResponseWrapper(final Token token, final Exception error) {
            this.error = error;
            this.token = token;
        }
    }
    
    interface TokenCreator
    {
        void create(final Card p0, final String p1, final Executor p2, final TokenCallback p3);
    }
}
