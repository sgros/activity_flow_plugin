package com.bumptech.glide.disklrucache;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache implements Closeable {
   private final int appVersion;
   private final Callable cleanupCallable;
   private final File directory;
   final ThreadPoolExecutor executorService;
   private final File journalFile;
   private final File journalFileBackup;
   private final File journalFileTmp;
   private Writer journalWriter;
   private final LinkedHashMap lruEntries = new LinkedHashMap(0, 0.75F, true);
   private long maxSize;
   private long nextSequenceNumber = 0L;
   private int redundantOpCount;
   private long size = 0L;
   private final int valueCount;

   private DiskLruCache(File var1, int var2, int var3, long var4) {
      this.executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DiskLruCache.DiskLruCacheThreadFactory());
      this.cleanupCallable = new Callable() {
         public Void call() throws Exception {
            DiskLruCache var1 = DiskLruCache.this;
            synchronized(var1){}

            Throwable var10000;
            boolean var10001;
            label197: {
               try {
                  if (DiskLruCache.this.journalWriter == null) {
                     return null;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label197;
               }

               try {
                  DiskLruCache.this.trimToSize();
                  if (DiskLruCache.this.journalRebuildRequired()) {
                     DiskLruCache.this.rebuildJournal();
                     DiskLruCache.this.redundantOpCount = 0;
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label197;
               }

               label187:
               try {
                  return null;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label187;
               }
            }

            while(true) {
               Throwable var2 = var10000;

               try {
                  throw var2;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  continue;
               }
            }
         }
      };
      this.directory = var1;
      this.appVersion = var2;
      this.journalFile = new File(var1, "journal");
      this.journalFileTmp = new File(var1, "journal.tmp");
      this.journalFileBackup = new File(var1, "journal.bkp");
      this.valueCount = var3;
      this.maxSize = var4;
   }

   private void checkNotClosed() {
      if (this.journalWriter == null) {
         throw new IllegalStateException("cache is closed");
      }
   }

   private void completeEdit(DiskLruCache.Editor var1, boolean var2) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label1853: {
         IllegalStateException var194;
         DiskLruCache.Entry var3;
         boolean var10001;
         label1848: {
            try {
               var3 = var1.entry;
               if (var3.currentEditor == var1) {
                  break label1848;
               }
            } catch (Throwable var193) {
               var10000 = var193;
               var10001 = false;
               break label1853;
            }

            try {
               var194 = new IllegalStateException();
               throw var194;
            } catch (Throwable var187) {
               var10000 = var187;
               var10001 = false;
               break label1853;
            }
         }

         int var6;
         label1860: {
            byte var4 = 0;
            int var5 = var4;
            if (var2) {
               label1859: {
                  var5 = var4;

                  try {
                     if (var3.readable) {
                        break label1859;
                     }
                  } catch (Throwable var191) {
                     var10000 = var191;
                     var10001 = false;
                     break label1853;
                  }

                  var6 = 0;

                  while(true) {
                     var5 = var4;

                     try {
                        if (var6 >= this.valueCount) {
                           break;
                        }

                        if (!var1.written[var6]) {
                           break label1860;
                        }

                        if (!var3.getDirtyFile(var6).exists()) {
                           var1.abort();
                           return;
                        }
                     } catch (Throwable var192) {
                        var10000 = var192;
                        var10001 = false;
                        break label1853;
                     }

                     ++var6;
                  }
               }
            }

            while(true) {
               File var195;
               long var8;
               label1866: {
                  try {
                     if (var5 < this.valueCount) {
                        var195 = var3.getDirtyFile(var5);
                        break label1866;
                     }
                  } catch (Throwable var190) {
                     var10000 = var190;
                     var10001 = false;
                     break label1853;
                  }

                  label1801: {
                     label1800: {
                        try {
                           ++this.redundantOpCount;
                           var3.currentEditor = null;
                           if (var3.readable | var2) {
                              var3.readable = true;
                              this.journalWriter.append("CLEAN");
                              this.journalWriter.append(' ');
                              this.journalWriter.append(var3.key);
                              this.journalWriter.append(var3.getLengths());
                              this.journalWriter.append('\n');
                              break label1800;
                           }
                        } catch (Throwable var186) {
                           var10000 = var186;
                           var10001 = false;
                           break label1853;
                        }

                        try {
                           this.lruEntries.remove(var3.key);
                           this.journalWriter.append("REMOVE");
                           this.journalWriter.append(' ');
                           this.journalWriter.append(var3.key);
                           this.journalWriter.append('\n');
                           break label1801;
                        } catch (Throwable var184) {
                           var10000 = var184;
                           var10001 = false;
                           break label1853;
                        }
                     }

                     if (var2) {
                        try {
                           var8 = (long)(this.nextSequenceNumber++);
                           var3.sequenceNumber = var8;
                        } catch (Throwable var185) {
                           var10000 = var185;
                           var10001 = false;
                           break label1853;
                        }
                     }
                  }

                  try {
                     this.journalWriter.flush();
                     if (this.size <= this.maxSize && !this.journalRebuildRequired()) {
                        return;
                     }
                  } catch (Throwable var183) {
                     var10000 = var183;
                     var10001 = false;
                     break label1853;
                  }

                  try {
                     this.executorService.submit(this.cleanupCallable);
                  } catch (Throwable var182) {
                     var10000 = var182;
                     var10001 = false;
                     break label1853;
                  }

                  return;
               }

               if (var2) {
                  try {
                     if (var195.exists()) {
                        File var7 = var3.getCleanFile(var5);
                        var195.renameTo(var7);
                        var8 = var3.lengths[var5];
                        long var10 = var7.length();
                        var3.lengths[var5] = var10;
                        this.size = this.size - var8 + var10;
                     }
                  } catch (Throwable var188) {
                     var10000 = var188;
                     var10001 = false;
                     break label1853;
                  }
               } else {
                  try {
                     deleteIfExists(var195);
                  } catch (Throwable var189) {
                     var10000 = var189;
                     var10001 = false;
                     break label1853;
                  }
               }

               ++var5;
            }
         }

         label1778:
         try {
            var1.abort();
            StringBuilder var197 = new StringBuilder();
            var197.append("Newly created entry didn't create value for index ");
            var197.append(var6);
            var194 = new IllegalStateException(var197.toString());
            throw var194;
         } catch (Throwable var181) {
            var10000 = var181;
            var10001 = false;
            break label1778;
         }
      }

      Throwable var196 = var10000;
      throw var196;
   }

   private static void deleteIfExists(File var0) throws IOException {
      if (var0.exists() && !var0.delete()) {
         throw new IOException();
      }
   }

   private DiskLruCache.Editor edit(String var1, long var2) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label339: {
         DiskLruCache.Entry var4;
         boolean var10001;
         try {
            this.checkNotClosed();
            var4 = (DiskLruCache.Entry)this.lruEntries.get(var1);
         } catch (Throwable var37) {
            var10000 = var37;
            var10001 = false;
            break label339;
         }

         if (var2 != -1L) {
            if (var4 == null) {
               return null;
            }

            long var5;
            try {
               var5 = var4.sequenceNumber;
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label339;
            }

            if (var5 != var2) {
               return null;
            }
         }

         DiskLruCache.Editor var7;
         if (var4 == null) {
            try {
               var4 = new DiskLruCache.Entry(var1);
               this.lruEntries.put(var1, var4);
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label339;
            }
         } else {
            try {
               var7 = var4.currentEditor;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label339;
            }

            if (var7 != null) {
               return null;
            }
         }

         try {
            var7 = new DiskLruCache.Editor(var4);
            var4.currentEditor = var7;
            this.journalWriter.append("DIRTY");
            this.journalWriter.append(' ');
            this.journalWriter.append(var1);
            this.journalWriter.append('\n');
            this.journalWriter.flush();
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label339;
         }

         return var7;
      }

      Throwable var38 = var10000;
      throw var38;
   }

   private boolean journalRebuildRequired() {
      boolean var1;
      if (this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static DiskLruCache open(File var0, int var1, int var2, long var3) throws IOException {
      if (var3 <= 0L) {
         throw new IllegalArgumentException("maxSize <= 0");
      } else if (var2 > 0) {
         File var5 = new File(var0, "journal.bkp");
         if (var5.exists()) {
            File var6 = new File(var0, "journal");
            if (var6.exists()) {
               var5.delete();
            } else {
               renameTo(var5, var6, false);
            }
         }

         DiskLruCache var7 = new DiskLruCache(var0, var1, var2, var3);
         if (var7.journalFile.exists()) {
            try {
               var7.readJournal();
               var7.processJournal();
               return var7;
            } catch (IOException var9) {
               PrintStream var8 = System.out;
               StringBuilder var11 = new StringBuilder();
               var11.append("DiskLruCache ");
               var11.append(var0);
               var11.append(" is corrupt: ");
               var11.append(var9.getMessage());
               var11.append(", removing");
               var8.println(var11.toString());
               var7.delete();
            }
         }

         var0.mkdirs();
         DiskLruCache var10 = new DiskLruCache(var0, var1, var2, var3);
         var10.rebuildJournal();
         return var10;
      } else {
         throw new IllegalArgumentException("valueCount <= 0");
      }
   }

   private void processJournal() throws IOException {
      deleteIfExists(this.journalFileTmp);
      Iterator var1 = this.lruEntries.values().iterator();

      while(true) {
         while(var1.hasNext()) {
            DiskLruCache.Entry var2 = (DiskLruCache.Entry)var1.next();
            DiskLruCache.Editor var3 = var2.currentEditor;
            byte var4 = 0;
            int var5 = 0;
            if (var3 == null) {
               while(var5 < this.valueCount) {
                  this.size += var2.lengths[var5];
                  ++var5;
               }
            } else {
               var2.currentEditor = null;

               for(var5 = var4; var5 < this.valueCount; ++var5) {
                  deleteIfExists(var2.getCleanFile(var5));
                  deleteIfExists(var2.getDirtyFile(var5));
               }

               var1.remove();
            }
         }

         return;
      }
   }

   private void readJournal() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void readJournalLine(String var1) throws IOException {
      int var2 = var1.indexOf(32);
      StringBuilder var6;
      if (var2 != -1) {
         int var3 = var2 + 1;
         int var4 = var1.indexOf(32, var3);
         String var10;
         if (var4 == -1) {
            String var5 = var1.substring(var3);
            var10 = var5;
            if (var2 == "REMOVE".length()) {
               var10 = var5;
               if (var1.startsWith("REMOVE")) {
                  this.lruEntries.remove(var5);
                  return;
               }
            }
         } else {
            var10 = var1.substring(var3, var4);
         }

         DiskLruCache.Entry var7 = (DiskLruCache.Entry)this.lruEntries.get(var10);
         DiskLruCache.Entry var9 = var7;
         if (var7 == null) {
            var9 = new DiskLruCache.Entry(var10);
            this.lruEntries.put(var10, var9);
         }

         if (var4 != -1 && var2 == "CLEAN".length() && var1.startsWith("CLEAN")) {
            String[] var8 = var1.substring(var4 + 1).split(" ");
            var9.readable = true;
            var9.currentEditor = null;
            var9.setLengths(var8);
         } else if (var4 == -1 && var2 == "DIRTY".length() && var1.startsWith("DIRTY")) {
            var9.currentEditor = new DiskLruCache.Editor(var9);
         } else if (var4 != -1 || var2 != "READ".length() || !var1.startsWith("READ")) {
            var6 = new StringBuilder();
            var6.append("unexpected journal line: ");
            var6.append(var1);
            throw new IOException(var6.toString());
         }

      } else {
         var6 = new StringBuilder();
         var6.append("unexpected journal line: ");
         var6.append(var1);
         throw new IOException(var6.toString());
      }
   }

   private void rebuildJournal() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label712: {
         boolean var10001;
         try {
            if (this.journalWriter != null) {
               this.journalWriter.close();
            }
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label712;
         }

         BufferedWriter var1;
         try {
            FileOutputStream var3 = new FileOutputStream(this.journalFileTmp);
            OutputStreamWriter var2 = new OutputStreamWriter(var3, Util.US_ASCII);
            var1 = new BufferedWriter(var2);
         } catch (Throwable var75) {
            var10000 = var75;
            var10001 = false;
            break label712;
         }

         label713: {
            label699: {
               Iterator var79;
               try {
                  var1.write("libcore.io.DiskLruCache");
                  var1.write("\n");
                  var1.write("1");
                  var1.write("\n");
                  var1.write(Integer.toString(this.appVersion));
                  var1.write("\n");
                  var1.write(Integer.toString(this.valueCount));
                  var1.write("\n");
                  var1.write("\n");
                  var79 = this.lruEntries.values().iterator();
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label699;
               }

               label696:
               while(true) {
                  while(true) {
                     StringBuilder var4;
                     DiskLruCache.Entry var82;
                     try {
                        if (!var79.hasNext()) {
                           break label713;
                        }

                        var82 = (DiskLruCache.Entry)var79.next();
                        if (var82.currentEditor != null) {
                           var4 = new StringBuilder();
                           var4.append("DIRTY ");
                           var4.append(var82.key);
                           var4.append('\n');
                           var1.write(var4.toString());
                           continue;
                        }
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label696;
                     }

                     try {
                        var4 = new StringBuilder();
                        var4.append("CLEAN ");
                        var4.append(var82.key);
                        var4.append(var82.getLengths());
                        var4.append('\n');
                        var1.write(var4.toString());
                     } catch (Throwable var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label696;
                     }
                  }
               }
            }

            Throwable var80 = var10000;

            try {
               var1.close();
               throw var80;
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label712;
            }
         }

         try {
            var1.close();
            if (this.journalFile.exists()) {
               renameTo(this.journalFile, this.journalFileBackup, true);
            }
         } catch (Throwable var71) {
            var10000 = var71;
            var10001 = false;
            break label712;
         }

         label677:
         try {
            renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            FileOutputStream var78 = new FileOutputStream(this.journalFile, true);
            OutputStreamWriter var83 = new OutputStreamWriter(var78, Util.US_ASCII);
            BufferedWriter var81 = new BufferedWriter(var83);
            this.journalWriter = var81;
            return;
         } catch (Throwable var70) {
            var10000 = var70;
            var10001 = false;
            break label677;
         }
      }

      Throwable var77 = var10000;
      throw var77;
   }

   private static void renameTo(File var0, File var1, boolean var2) throws IOException {
      if (var2) {
         deleteIfExists(var1);
      }

      if (!var0.renameTo(var1)) {
         throw new IOException();
      }
   }

   private void trimToSize() throws IOException {
      while(this.size > this.maxSize) {
         this.remove((String)((java.util.Map.Entry)this.lruEntries.entrySet().iterator().next()).getKey());
      }

   }

   public void close() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label229: {
         Writer var1;
         boolean var10001;
         try {
            var1 = this.journalWriter;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label229;
         }

         if (var1 == null) {
            return;
         }

         Iterator var24;
         try {
            ArrayList var23 = new ArrayList(this.lruEntries.values());
            var24 = var23.iterator();
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label229;
         }

         while(true) {
            try {
               if (!var24.hasNext()) {
                  break;
               }

               DiskLruCache.Entry var2 = (DiskLruCache.Entry)var24.next();
               if (var2.currentEditor != null) {
                  var2.currentEditor.abort();
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label229;
            }
         }

         try {
            this.trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label229;
         }

         return;
      }

      Throwable var25 = var10000;
      throw var25;
   }

   public void delete() throws IOException {
      this.close();
      Util.deleteContents(this.directory);
   }

   public DiskLruCache.Editor edit(String var1) throws IOException {
      return this.edit(var1, -1L);
   }

   public DiskLruCache.Value get(String var1) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label475: {
         DiskLruCache.Entry var2;
         boolean var10001;
         try {
            this.checkNotClosed();
            var2 = (DiskLruCache.Entry)this.lruEntries.get(var1);
         } catch (Throwable var48) {
            var10000 = var48;
            var10001 = false;
            break label475;
         }

         if (var2 == null) {
            return null;
         }

         boolean var3;
         try {
            var3 = var2.readable;
         } catch (Throwable var47) {
            var10000 = var47;
            var10001 = false;
            break label475;
         }

         if (!var3) {
            return null;
         }

         File[] var4;
         int var5;
         try {
            var4 = var2.cleanFiles;
            var5 = var4.length;
         } catch (Throwable var46) {
            var10000 = var46;
            var10001 = false;
            break label475;
         }

         for(int var6 = 0; var6 < var5; ++var6) {
            try {
               var3 = var4[var6].exists();
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label475;
            }

            if (!var3) {
               return null;
            }
         }

         try {
            ++this.redundantOpCount;
            this.journalWriter.append("READ");
            this.journalWriter.append(' ');
            this.journalWriter.append(var1);
            this.journalWriter.append('\n');
            if (this.journalRebuildRequired()) {
               this.executorService.submit(this.cleanupCallable);
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label475;
         }

         DiskLruCache.Value var50;
         try {
            var50 = new DiskLruCache.Value(var1, var2.sequenceNumber, var2.cleanFiles, var2.lengths);
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label475;
         }

         return var50;
      }

      Throwable var49 = var10000;
      throw var49;
   }

   public boolean remove(String var1) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label488: {
         DiskLruCache.Entry var2;
         boolean var10001;
         try {
            this.checkNotClosed();
            var2 = (DiskLruCache.Entry)this.lruEntries.get(var1);
         } catch (Throwable var46) {
            var10000 = var46;
            var10001 = false;
            break label488;
         }

         int var3 = 0;
         if (var2 == null) {
            return false;
         }

         try {
            if (var2.currentEditor != null) {
               return false;
            }
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label488;
         }

         File var4;
         label489: {
            while(true) {
               try {
                  if (var3 >= this.valueCount) {
                     break;
                  }

                  var4 = var2.getCleanFile(var3);
                  if (var4.exists() && !var4.delete()) {
                     break label489;
                  }
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label488;
               }

               try {
                  this.size -= var2.lengths[var3];
                  var2.lengths[var3] = 0L;
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label488;
               }

               ++var3;
            }

            try {
               ++this.redundantOpCount;
               this.journalWriter.append("REMOVE");
               this.journalWriter.append(' ');
               this.journalWriter.append(var1);
               this.journalWriter.append('\n');
               this.lruEntries.remove(var1);
               if (this.journalRebuildRequired()) {
                  this.executorService.submit(this.cleanupCallable);
               }
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label488;
            }

            return true;
         }

         label453:
         try {
            StringBuilder var49 = new StringBuilder();
            var49.append("failed to delete ");
            var49.append(var4);
            IOException var47 = new IOException(var49.toString());
            throw var47;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label453;
         }
      }

      Throwable var48 = var10000;
      throw var48;
   }

   private static final class DiskLruCacheThreadFactory implements ThreadFactory {
      private DiskLruCacheThreadFactory() {
      }

      // $FF: synthetic method
      DiskLruCacheThreadFactory(Object var1) {
         this();
      }

      public Thread newThread(Runnable var1) {
         synchronized(this){}

         Thread var2;
         try {
            var2 = new Thread(var1, "glide-disk-lru-cache-thread");
            var2.setPriority(1);
         } finally {
            ;
         }

         return var2;
      }
   }

   public final class Editor {
      private boolean committed;
      private final DiskLruCache.Entry entry;
      private final boolean[] written;

      private Editor(DiskLruCache.Entry var2) {
         this.entry = var2;
         boolean[] var3;
         if (var2.readable) {
            var3 = null;
         } else {
            var3 = new boolean[DiskLruCache.this.valueCount];
         }

         this.written = var3;
      }

      // $FF: synthetic method
      Editor(DiskLruCache.Entry var2, Object var3) {
         this(var2);
      }

      public void abort() throws IOException {
         DiskLruCache.this.completeEdit(this, false);
      }

      public void abortUnlessCommitted() {
         if (!this.committed) {
            try {
               this.abort();
            } catch (IOException var2) {
            }
         }

      }

      public void commit() throws IOException {
         DiskLruCache.this.completeEdit(this, true);
         this.committed = true;
      }

      public File getFile(int var1) throws IOException {
         DiskLruCache var2 = DiskLruCache.this;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label295: {
            label290: {
               try {
                  if (this.entry.currentEditor == this) {
                     if (!this.entry.readable) {
                        this.written[var1] = true;
                     }
                     break label290;
                  }
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label295;
               }

               try {
                  IllegalStateException var3 = new IllegalStateException();
                  throw var3;
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label295;
               }
            }

            File var34;
            try {
               var34 = this.entry.getDirtyFile(var1);
               if (!DiskLruCache.this.directory.exists()) {
                  DiskLruCache.this.directory.mkdirs();
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label295;
            }

            label278:
            try {
               return var34;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label278;
            }
         }

         while(true) {
            Throwable var35 = var10000;

            try {
               throw var35;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               continue;
            }
         }
      }
   }

   private final class Entry {
      File[] cleanFiles;
      private DiskLruCache.Editor currentEditor;
      File[] dirtyFiles;
      private final String key;
      private final long[] lengths;
      private boolean readable;
      private long sequenceNumber;

      private Entry(String var2) {
         this.key = var2;
         this.lengths = new long[DiskLruCache.this.valueCount];
         this.cleanFiles = new File[DiskLruCache.this.valueCount];
         this.dirtyFiles = new File[DiskLruCache.this.valueCount];
         StringBuilder var5 = new StringBuilder(var2);
         var5.append('.');
         int var3 = var5.length();

         for(int var4 = 0; var4 < DiskLruCache.this.valueCount; ++var4) {
            var5.append(var4);
            this.cleanFiles[var4] = new File(DiskLruCache.this.directory, var5.toString());
            var5.append(".tmp");
            this.dirtyFiles[var4] = new File(DiskLruCache.this.directory, var5.toString());
            var5.setLength(var3);
         }

      }

      // $FF: synthetic method
      Entry(String var2, Object var3) {
         this(var2);
      }

      private IOException invalidLengths(String[] var1) throws IOException {
         StringBuilder var2 = new StringBuilder();
         var2.append("unexpected journal line: ");
         var2.append(Arrays.toString(var1));
         throw new IOException(var2.toString());
      }

      private void setLengths(String[] var1) throws IOException {
         if (var1.length != DiskLruCache.this.valueCount) {
            throw this.invalidLengths(var1);
         } else {
            int var2 = 0;

            while(true) {
               try {
                  if (var2 >= var1.length) {
                     return;
                  }

                  this.lengths[var2] = Long.parseLong(var1[var2]);
               } catch (NumberFormatException var4) {
                  throw this.invalidLengths(var1);
               }

               ++var2;
            }
         }
      }

      public File getCleanFile(int var1) {
         return this.cleanFiles[var1];
      }

      public File getDirtyFile(int var1) {
         return this.dirtyFiles[var1];
      }

      public String getLengths() throws IOException {
         StringBuilder var1 = new StringBuilder();
         long[] var2 = this.lengths;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            long var5 = var2[var4];
            var1.append(' ');
            var1.append(var5);
         }

         return var1.toString();
      }
   }

   public final class Value {
      private final File[] files;
      private final String key;
      private final long[] lengths;
      private final long sequenceNumber;

      private Value(String var2, long var3, File[] var5, long[] var6) {
         this.key = var2;
         this.sequenceNumber = var3;
         this.files = var5;
         this.lengths = var6;
      }

      // $FF: synthetic method
      Value(String var2, long var3, File[] var5, long[] var6, Object var7) {
         this(var2, var3, var5, var6);
      }

      public File getFile(int var1) {
         return this.files[var1];
      }
   }
}
