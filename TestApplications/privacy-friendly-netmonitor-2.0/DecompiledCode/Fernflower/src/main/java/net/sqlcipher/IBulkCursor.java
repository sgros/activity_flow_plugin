package net.sqlcipher;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import java.util.Map;

public interface IBulkCursor extends IInterface {
   int CLOSE_TRANSACTION = 12;
   int COUNT_TRANSACTION = 2;
   int DEACTIVATE_TRANSACTION = 6;
   int DELETE_ROW_TRANSACTION = 5;
   int GET_COLUMN_NAMES_TRANSACTION = 3;
   int GET_CURSOR_WINDOW_TRANSACTION = 1;
   int GET_EXTRAS_TRANSACTION = 10;
   int ON_MOVE_TRANSACTION = 8;
   int REQUERY_TRANSACTION = 7;
   int RESPOND_TRANSACTION = 11;
   int UPDATE_ROWS_TRANSACTION = 4;
   int WANTS_ON_MOVE_TRANSACTION = 9;
   String descriptor = "android.content.IBulkCursor";

   void close() throws RemoteException;

   int count() throws RemoteException;

   void deactivate() throws RemoteException;

   boolean deleteRow(int var1) throws RemoteException;

   String[] getColumnNames() throws RemoteException;

   Bundle getExtras() throws RemoteException;

   boolean getWantsAllOnMoveCalls() throws RemoteException;

   CursorWindow getWindow(int var1) throws RemoteException;

   void onMove(int var1) throws RemoteException;

   int requery(IContentObserver var1, CursorWindow var2) throws RemoteException;

   Bundle respond(Bundle var1) throws RemoteException;

   boolean updateRows(Map var1) throws RemoteException;
}
