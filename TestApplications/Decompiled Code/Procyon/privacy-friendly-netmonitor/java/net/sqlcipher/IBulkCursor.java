// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import java.util.Map;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.IInterface;

public interface IBulkCursor extends IInterface
{
    public static final int CLOSE_TRANSACTION = 12;
    public static final int COUNT_TRANSACTION = 2;
    public static final int DEACTIVATE_TRANSACTION = 6;
    public static final int DELETE_ROW_TRANSACTION = 5;
    public static final int GET_COLUMN_NAMES_TRANSACTION = 3;
    public static final int GET_CURSOR_WINDOW_TRANSACTION = 1;
    public static final int GET_EXTRAS_TRANSACTION = 10;
    public static final int ON_MOVE_TRANSACTION = 8;
    public static final int REQUERY_TRANSACTION = 7;
    public static final int RESPOND_TRANSACTION = 11;
    public static final int UPDATE_ROWS_TRANSACTION = 4;
    public static final int WANTS_ON_MOVE_TRANSACTION = 9;
    public static final String descriptor = "android.content.IBulkCursor";
    
    void close() throws RemoteException;
    
    int count() throws RemoteException;
    
    void deactivate() throws RemoteException;
    
    boolean deleteRow(final int p0) throws RemoteException;
    
    String[] getColumnNames() throws RemoteException;
    
    Bundle getExtras() throws RemoteException;
    
    boolean getWantsAllOnMoveCalls() throws RemoteException;
    
    CursorWindow getWindow(final int p0) throws RemoteException;
    
    void onMove(final int p0) throws RemoteException;
    
    int requery(final IContentObserver p0, final CursorWindow p1) throws RemoteException;
    
    Bundle respond(final Bundle p0) throws RemoteException;
    
    boolean updateRows(final Map<? extends Long, ? extends Map<String, Object>> p0) throws RemoteException;
}
