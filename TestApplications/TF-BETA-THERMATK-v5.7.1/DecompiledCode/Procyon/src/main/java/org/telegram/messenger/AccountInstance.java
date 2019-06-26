// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.ConnectionsManager;

public class AccountInstance
{
    private static volatile AccountInstance[] Instance;
    private int currentAccount;
    
    static {
        AccountInstance.Instance = new AccountInstance[3];
    }
    
    public AccountInstance(final int currentAccount) {
        this.currentAccount = currentAccount;
    }
    
    public static AccountInstance getInstance(final int n) {
        final AccountInstance accountInstance;
        if ((accountInstance = AccountInstance.Instance[n]) == null) {
            synchronized (AccountInstance.class) {
                if (AccountInstance.Instance[n] == null) {
                    AccountInstance.Instance[n] = new AccountInstance(n);
                }
            }
        }
        return accountInstance;
    }
    
    public ConnectionsManager getConnectionsManager() {
        return ConnectionsManager.getInstance(this.currentAccount);
    }
    
    public ContactsController getContactsController() {
        return ContactsController.getInstance(this.currentAccount);
    }
    
    public DataQuery getDataQuery() {
        return DataQuery.getInstance(this.currentAccount);
    }
    
    public MessagesController getMessagesController() {
        return MessagesController.getInstance(this.currentAccount);
    }
    
    public MessagesStorage getMessagesStorage() {
        return MessagesStorage.getInstance(this.currentAccount);
    }
    
    public NotificationCenter getNotificationCenter() {
        return NotificationCenter.getInstance(this.currentAccount);
    }
    
    public NotificationsController getNotificationsController() {
        return NotificationsController.getInstance(this.currentAccount);
    }
    
    public UserConfig getUserConfig() {
        return UserConfig.getInstance(this.currentAccount);
    }
}
