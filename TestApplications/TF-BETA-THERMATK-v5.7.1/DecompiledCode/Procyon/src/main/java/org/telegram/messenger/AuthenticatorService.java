// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.accounts.Account;
import android.accounts.NetworkErrorException;
import android.os.Bundle;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AbstractAccountAuthenticator;
import android.os.IBinder;
import android.content.Intent;
import android.content.Context;
import android.app.Service;

public class AuthenticatorService extends Service
{
    private static Authenticator authenticator;
    
    protected Authenticator getAuthenticator() {
        if (AuthenticatorService.authenticator == null) {
            AuthenticatorService.authenticator = new Authenticator((Context)this);
        }
        return AuthenticatorService.authenticator;
    }
    
    public IBinder onBind(final Intent intent) {
        if (intent.getAction().equals("android.accounts.AccountAuthenticator")) {
            return this.getAuthenticator().getIBinder();
        }
        return null;
    }
    
    private static class Authenticator extends AbstractAccountAuthenticator
    {
        private final Context context;
        
        public Authenticator(final Context context) {
            super(context);
            this.context = context;
        }
        
        public Bundle addAccount(final AccountAuthenticatorResponse accountAuthenticatorResponse, final String s, final String s2, final String[] array, final Bundle bundle) throws NetworkErrorException {
            return null;
        }
        
        public Bundle confirmCredentials(final AccountAuthenticatorResponse accountAuthenticatorResponse, final Account account, final Bundle bundle) throws NetworkErrorException {
            return null;
        }
        
        public Bundle editProperties(final AccountAuthenticatorResponse accountAuthenticatorResponse, final String s) {
            return null;
        }
        
        public Bundle getAccountRemovalAllowed(final AccountAuthenticatorResponse accountAuthenticatorResponse, final Account account) throws NetworkErrorException {
            return super.getAccountRemovalAllowed(accountAuthenticatorResponse, account);
        }
        
        public Bundle getAuthToken(final AccountAuthenticatorResponse accountAuthenticatorResponse, final Account account, final String s, final Bundle bundle) throws NetworkErrorException {
            return null;
        }
        
        public String getAuthTokenLabel(final String s) {
            return null;
        }
        
        public Bundle hasFeatures(final AccountAuthenticatorResponse accountAuthenticatorResponse, final Account account, final String[] array) throws NetworkErrorException {
            return null;
        }
        
        public Bundle updateCredentials(final AccountAuthenticatorResponse accountAuthenticatorResponse, final Account account, final String s, final Bundle bundle) throws NetworkErrorException {
            return null;
        }
    }
}
