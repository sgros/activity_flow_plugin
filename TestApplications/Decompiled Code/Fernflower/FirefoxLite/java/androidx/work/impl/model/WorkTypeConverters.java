package androidx.work.impl.model;

import androidx.work.BackoffPolicy;
import androidx.work.ContentUriTriggers;
import androidx.work.NetworkType;
import androidx.work.WorkInfo;

public class WorkTypeConverters {
   public static int backoffPolicyToInt(BackoffPolicy var0) {
      switch(var0) {
      case EXPONENTIAL:
         return 0;
      case LINEAR:
         return 1;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Could not convert ");
         var1.append(var0);
         var1.append(" to int");
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static ContentUriTriggers byteArrayToContentUriTriggers(byte[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static byte[] contentUriTriggersToByteArray(ContentUriTriggers param0) {
      // $FF: Couldn't be decompiled
   }

   public static BackoffPolicy intToBackoffPolicy(int var0) {
      switch(var0) {
      case 0:
         return BackoffPolicy.EXPONENTIAL;
      case 1:
         return BackoffPolicy.LINEAR;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Could not convert ");
         var1.append(var0);
         var1.append(" to BackoffPolicy");
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static NetworkType intToNetworkType(int var0) {
      switch(var0) {
      case 0:
         return NetworkType.NOT_REQUIRED;
      case 1:
         return NetworkType.CONNECTED;
      case 2:
         return NetworkType.UNMETERED;
      case 3:
         return NetworkType.NOT_ROAMING;
      case 4:
         return NetworkType.METERED;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Could not convert ");
         var1.append(var0);
         var1.append(" to NetworkType");
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static WorkInfo.State intToState(int var0) {
      switch(var0) {
      case 0:
         return WorkInfo.State.ENQUEUED;
      case 1:
         return WorkInfo.State.RUNNING;
      case 2:
         return WorkInfo.State.SUCCEEDED;
      case 3:
         return WorkInfo.State.FAILED;
      case 4:
         return WorkInfo.State.BLOCKED;
      case 5:
         return WorkInfo.State.CANCELLED;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Could not convert ");
         var1.append(var0);
         var1.append(" to State");
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static int networkTypeToInt(NetworkType var0) {
      switch(var0) {
      case NOT_REQUIRED:
         return 0;
      case CONNECTED:
         return 1;
      case UNMETERED:
         return 2;
      case NOT_ROAMING:
         return 3;
      case METERED:
         return 4;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Could not convert ");
         var1.append(var0);
         var1.append(" to int");
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static int stateToInt(WorkInfo.State var0) {
      switch(var0) {
      case ENQUEUED:
         return 0;
      case RUNNING:
         return 1;
      case SUCCEEDED:
         return 2;
      case FAILED:
         return 3;
      case BLOCKED:
         return 4;
      case CANCELLED:
         return 5;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Could not convert ");
         var1.append(var0);
         var1.append(" to int");
         throw new IllegalArgumentException(var1.toString());
      }
   }
}
