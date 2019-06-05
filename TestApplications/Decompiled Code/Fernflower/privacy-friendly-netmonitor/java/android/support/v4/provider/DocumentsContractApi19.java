package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

@RequiresApi(19)
class DocumentsContractApi19 {
   private static final int FLAG_VIRTUAL_DOCUMENT = 512;
   private static final String TAG = "DocumentFile";

   public static boolean canRead(Context var0, Uri var1) {
      if (var0.checkCallingOrSelfUriPermission(var1, 1) != 0) {
         return false;
      } else {
         return !TextUtils.isEmpty(getRawType(var0, var1));
      }
   }

   public static boolean canWrite(Context var0, Uri var1) {
      if (var0.checkCallingOrSelfUriPermission(var1, 2) != 0) {
         return false;
      } else {
         String var2 = getRawType(var0, var1);
         int var3 = queryForInt(var0, var1, "flags", 0);
         if (TextUtils.isEmpty(var2)) {
            return false;
         } else if ((var3 & 4) != 0) {
            return true;
         } else if ("vnd.android.document/directory".equals(var2) && (var3 & 8) != 0) {
            return true;
         } else {
            return !TextUtils.isEmpty(var2) && (var3 & 2) != 0;
         }
      }
   }

   private static void closeQuietly(AutoCloseable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }

   }

   public static boolean delete(Context var0, Uri var1) {
      try {
         boolean var2 = DocumentsContract.deleteDocument(var0.getContentResolver(), var1);
         return var2;
      } catch (Exception var3) {
         return false;
      }
   }

   public static boolean exists(Context var0, Uri var1) {
      ContentResolver var2 = var0.getContentResolver();
      boolean var3 = true;
      StringBuilder var4 = null;
      Cursor var78 = null;

      int var5;
      Cursor var79;
      label567: {
         Throwable var80;
         label566: {
            Throwable var10000;
            label571: {
               Exception var81;
               boolean var10001;
               label564: {
                  label563: {
                     try {
                        try {
                           var79 = var2.query(var1, new String[]{"document_id"}, (String)null, (String[])null, (String)null);
                           break label563;
                        } catch (Exception var76) {
                           var81 = var76;
                        }
                     } catch (Throwable var77) {
                        var10000 = var77;
                        var10001 = false;
                        break label571;
                     }

                     var79 = var4;
                     break label564;
                  }

                  try {
                     try {
                        var5 = var79.getCount();
                        break label567;
                     } catch (Exception var70) {
                        var81 = var70;
                     }
                  } catch (Throwable var71) {
                     var78 = var79;
                     var80 = var71;
                     break label566;
                  }
               }

               var78 = var79;

               try {
                  var4 = new StringBuilder;
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label571;
               }

               var78 = var79;

               try {
                  var4.<init>();
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label571;
               }

               var78 = var79;

               try {
                  var4.append("Failed query: ");
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label571;
               }

               var78 = var79;

               try {
                  var4.append(var81);
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label571;
               }

               var78 = var79;

               try {
                  Log.w("DocumentFile", var4.toString());
               } catch (Throwable var69) {
                  var10000 = var69;
                  var10001 = false;
                  break label571;
               }

               closeQuietly(var79);
               return false;
            }

            var80 = var10000;
         }

         closeQuietly(var78);
         throw var80;
      }

      if (var5 <= 0) {
         var3 = false;
      }

      closeQuietly(var79);
      return var3;
   }

   public static long getFlags(Context var0, Uri var1) {
      return queryForLong(var0, var1, "flags", 0L);
   }

   public static String getName(Context var0, Uri var1) {
      return queryForString(var0, var1, "_display_name", (String)null);
   }

   private static String getRawType(Context var0, Uri var1) {
      return queryForString(var0, var1, "mime_type", (String)null);
   }

   public static String getType(Context var0, Uri var1) {
      String var2 = getRawType(var0, var1);
      return "vnd.android.document/directory".equals(var2) ? null : var2;
   }

   public static boolean isDirectory(Context var0, Uri var1) {
      return "vnd.android.document/directory".equals(getRawType(var0, var1));
   }

   public static boolean isDocumentUri(Context var0, Uri var1) {
      return DocumentsContract.isDocumentUri(var0, var1);
   }

   public static boolean isFile(Context var0, Uri var1) {
      String var2 = getRawType(var0, var1);
      return !"vnd.android.document/directory".equals(var2) && !TextUtils.isEmpty(var2);
   }

   public static boolean isVirtual(Context var0, Uri var1) {
      boolean var2 = isDocumentUri(var0, var1);
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         if ((getFlags(var0, var1) & 512L) != 0L) {
            var3 = true;
         }

         return var3;
      }
   }

   public static long lastModified(Context var0, Uri var1) {
      return queryForLong(var0, var1, "last_modified", 0L);
   }

   public static long length(Context var0, Uri var1) {
      return queryForLong(var0, var1, "_size", 0L);
   }

   private static int queryForInt(Context var0, Uri var1, String var2, int var3) {
      return (int)queryForLong(var0, var1, var2, (long)var3);
   }

   private static long queryForLong(Context var0, Uri var1, String var2, long var3) {
      label618: {
         ContentResolver var5 = var0.getContentResolver();
         StringBuilder var6 = null;
         Throwable var81 = null;

         label615: {
            label614: {
               Object var82;
               label613: {
                  Throwable var10000;
                  label619: {
                     boolean var10001;
                     Exception var83;
                     label611: {
                        label610: {
                           try {
                              try {
                                 var82 = var5.query(var1, new String[]{var2}, (String)null, (String[])null, (String)null);
                                 break label610;
                              } catch (Exception var79) {
                                 var83 = var79;
                              }
                           } catch (Throwable var80) {
                              var10000 = var80;
                              var10001 = false;
                              break label619;
                           }

                           var1 = var6;
                           break label611;
                        }

                        try {
                           if (!((Cursor)var82).moveToFirst() || ((Cursor)var82).isNull(0)) {
                              break label615;
                           }

                           long var7 = ((Cursor)var82).getLong(0);
                           break label614;
                        } catch (Exception var73) {
                        } finally {
                           break label613;
                        }
                     }

                     var81 = var6;

                     try {
                        var6 = new StringBuilder;
                     } catch (Throwable var78) {
                        var10000 = var78;
                        var10001 = false;
                        break label619;
                     }

                     var81 = var1;

                     try {
                        var6.<init>();
                     } catch (Throwable var77) {
                        var10000 = var77;
                        var10001 = false;
                        break label619;
                     }

                     var81 = var1;

                     try {
                        var6.append("Failed query: ");
                     } catch (Throwable var76) {
                        var10000 = var76;
                        var10001 = false;
                        break label619;
                     }

                     var81 = var1;

                     try {
                        var6.append(var83);
                     } catch (Throwable var75) {
                        var10000 = var75;
                        var10001 = false;
                        break label619;
                     }

                     var81 = var1;

                     try {
                        Log.w("DocumentFile", var6.toString());
                     } catch (Throwable var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label619;
                     }

                     closeQuietly(var1);
                     return var3;
                  }

                  Throwable var84 = var10000;
                  var82 = var81;
                  var81 = var84;
               }

               closeQuietly((AutoCloseable)var82);
               throw var81;
            }

         }

      }
   }

   private static String queryForString(Context var0, Uri var1, String var2, String var3) {
      label618: {
         ContentResolver var4 = var0.getContentResolver();
         StringBuilder var5 = null;
         Object var78 = null;

         label615: {
            label614: {
               Object var79;
               label613: {
                  Throwable var10000;
                  label619: {
                     Exception var80;
                     boolean var10001;
                     label611: {
                        label610: {
                           try {
                              try {
                                 var79 = var4.query(var1, new String[]{var2}, (String)null, (String[])null, (String)null);
                                 break label610;
                              } catch (Exception var76) {
                                 var80 = var76;
                              }
                           } catch (Throwable var77) {
                              var10000 = var77;
                              var10001 = false;
                              break label619;
                           }

                           var1 = var5;
                           break label611;
                        }

                        try {
                           if (!((Cursor)var79).moveToFirst() || ((Cursor)var79).isNull(0)) {
                              break label615;
                           }

                           var78 = ((Cursor)var79).getString(0);
                           break label614;
                        } catch (Exception var70) {
                        } finally {
                           break label613;
                        }
                     }

                     var78 = var5;

                     try {
                        var5 = new StringBuilder;
                     } catch (Throwable var75) {
                        var10000 = var75;
                        var10001 = false;
                        break label619;
                     }

                     var78 = var1;

                     try {
                        var5.<init>();
                     } catch (Throwable var74) {
                        var10000 = var74;
                        var10001 = false;
                        break label619;
                     }

                     var78 = var1;

                     try {
                        var5.append("Failed query: ");
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label619;
                     }

                     var78 = var1;

                     try {
                        var5.append(var80);
                     } catch (Throwable var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label619;
                     }

                     var78 = var1;

                     try {
                        Log.w("DocumentFile", var5.toString());
                     } catch (Throwable var69) {
                        var10000 = var69;
                        var10001 = false;
                        break label619;
                     }

                     closeQuietly(var1);
                     return var3;
                  }

                  Throwable var81 = var10000;
                  var79 = var78;
                  var78 = var81;
               }

               closeQuietly((AutoCloseable)var79);
               throw var78;
            }

         }

      }
   }
}
