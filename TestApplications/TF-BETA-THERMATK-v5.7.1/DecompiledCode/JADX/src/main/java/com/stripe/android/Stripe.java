package com.stripe.android;

import android.os.AsyncTask;
import android.os.Build.VERSION;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.StripeException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.net.RequestOptions;
import com.stripe.android.net.StripeApiHandler;
import com.stripe.android.util.StripeNetworkUtils;
import java.util.concurrent.Executor;

public class Stripe {
    private String defaultPublishableKey;
    TokenCreator tokenCreator = new C02491();

    private class ResponseWrapper {
        final Exception error;
        final Token token;

        /* synthetic */ ResponseWrapper(Stripe stripe, Token token, Exception exception, C02491 c02491) {
            this(token, exception);
        }

        private ResponseWrapper(Token token, Exception exception) {
            this.error = exception;
            this.token = token;
        }
    }

    interface TokenCreator {
        void create(Card card, String str, Executor executor, TokenCallback tokenCallback);
    }

    /* renamed from: com.stripe.android.Stripe$1 */
    class C02491 implements TokenCreator {
        C02491() {
        }

        public void create(final Card card, final String str, Executor executor, final TokenCallback tokenCallback) {
            Stripe.this.executeTokenTask(executor, new AsyncTask<Void, Void, ResponseWrapper>() {
                /* Access modifiers changed, original: protected|varargs */
                public ResponseWrapper doInBackground(Void... voidArr) {
                    try {
                        return new ResponseWrapper(Stripe.this, StripeApiHandler.createToken(StripeNetworkUtils.hashMapFromCard(card), RequestOptions.builder(str).build()), null, null);
                    } catch (StripeException e) {
                        return new ResponseWrapper(Stripe.this, null, e, null);
                    }
                }

                /* Access modifiers changed, original: protected */
                public void onPostExecute(ResponseWrapper responseWrapper) {
                    Stripe.this.tokenTaskPostExecution(responseWrapper, tokenCallback);
                }
            });
        }
    }

    public Stripe(String str) throws AuthenticationException {
        setDefaultPublishableKey(str);
    }

    public void createToken(Card card, TokenCallback tokenCallback) {
        createToken(card, this.defaultPublishableKey, tokenCallback);
    }

    public void createToken(Card card, String str, TokenCallback tokenCallback) {
        createToken(card, str, null, tokenCallback);
    }

    public void createToken(Card card, String str, Executor executor, TokenCallback tokenCallback) {
        if (card == null) {
            throw new RuntimeException("Required Parameter: 'card' is required to create a token");
        } else if (tokenCallback != null) {
            try {
                validateKey(str);
                this.tokenCreator.create(card, str, executor, tokenCallback);
            } catch (AuthenticationException e) {
                tokenCallback.onError(e);
            }
        } else {
            throw new RuntimeException("Required Parameter: 'callback' is required to use the created token and handle errors");
        }
    }

    public void setDefaultPublishableKey(String str) throws AuthenticationException {
        validateKey(str);
        this.defaultPublishableKey = str;
    }

    private void validateKey(String str) throws AuthenticationException {
        Integer valueOf = Integer.valueOf(0);
        if (str == null || str.length() == 0) {
            throw new AuthenticationException("Invalid Publishable Key: You must use a valid publishable key to create a token.  For more info, see https://stripe.com/docs/stripe.js.", null, valueOf);
        } else if (str.startsWith("sk_")) {
            throw new AuthenticationException("Invalid Publishable Key: You are using a secret key to create a token, instead of the publishable one. For more info, see https://stripe.com/docs/stripe.js", null, valueOf);
        }
    }

    private void tokenTaskPostExecution(ResponseWrapper responseWrapper, TokenCallback tokenCallback) {
        Token token = responseWrapper.token;
        if (token != null) {
            tokenCallback.onSuccess(token);
            return;
        }
        Exception exception = responseWrapper.error;
        if (exception != null) {
            tokenCallback.onError(exception);
        } else {
            tokenCallback.onError(new RuntimeException("Somehow got neither a token response or an error response"));
        }
    }

    private void executeTokenTask(Executor executor, AsyncTask<Void, Void, ResponseWrapper> asyncTask) {
        if (executor == null || VERSION.SDK_INT <= 11) {
            asyncTask.execute(new Void[0]);
        } else {
            asyncTask.executeOnExecutor(executor, new Void[0]);
        }
    }
}
