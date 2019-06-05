package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GlideException extends Exception {
   private static final StackTraceElement[] EMPTY_ELEMENTS = new StackTraceElement[0];
   private final List causes;
   private Class dataClass;
   private DataSource dataSource;
   private Key key;

   public GlideException(String var1) {
      this(var1, Collections.emptyList());
   }

   public GlideException(String var1, Exception var2) {
      this(var1, Collections.singletonList(var2));
   }

   public GlideException(String var1, List var2) {
      super(var1);
      this.setStackTrace(EMPTY_ELEMENTS);
      this.causes = var2;
   }

   private void addRootCauses(Exception var1, List var2) {
      if (var1 instanceof GlideException) {
         Iterator var3 = ((GlideException)var1).getCauses().iterator();

         while(var3.hasNext()) {
            this.addRootCauses((Exception)var3.next(), var2);
         }
      } else {
         var2.add(var1);
      }

   }

   private static void appendCauses(List var0, Appendable var1) {
      try {
         appendCausesWrapped(var0, var1);
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static void appendCausesWrapped(List var0, Appendable var1) throws IOException {
      int var2 = var0.size();

      int var5;
      for(int var3 = 0; var3 < var2; var3 = var5) {
         Appendable var4 = var1.append("Cause (");
         var5 = var3 + 1;
         var4.append(String.valueOf(var5)).append(" of ").append(String.valueOf(var2)).append("): ");
         Exception var6 = (Exception)var0.get(var3);
         if (var6 instanceof GlideException) {
            ((GlideException)var6).printStackTrace(var1);
         } else {
            appendExceptionMessage(var6, var1);
         }
      }

   }

   private static void appendExceptionMessage(Exception var0, Appendable var1) {
      try {
         var1.append(var0.getClass().toString()).append(": ").append(var0.getMessage()).append('\n');
      } catch (IOException var2) {
         throw new RuntimeException(var0);
      }
   }

   private void printStackTrace(Appendable var1) {
      appendExceptionMessage(this, var1);
      appendCauses(this.getCauses(), new GlideException.IndentedAppendable(var1));
   }

   public Throwable fillInStackTrace() {
      return this;
   }

   public List getCauses() {
      return this.causes;
   }

   public String getMessage() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.getMessage());
      StringBuilder var2;
      String var3;
      if (this.dataClass != null) {
         var2 = new StringBuilder();
         var2.append(", ");
         var2.append(this.dataClass);
         var3 = var2.toString();
      } else {
         var3 = "";
      }

      var1.append(var3);
      if (this.dataSource != null) {
         var2 = new StringBuilder();
         var2.append(", ");
         var2.append(this.dataSource);
         var3 = var2.toString();
      } else {
         var3 = "";
      }

      var1.append(var3);
      if (this.key != null) {
         var2 = new StringBuilder();
         var2.append(", ");
         var2.append(this.key);
         var3 = var2.toString();
      } else {
         var3 = "";
      }

      var1.append(var3);
      return var1.toString();
   }

   public List getRootCauses() {
      ArrayList var1 = new ArrayList();
      this.addRootCauses(this, var1);
      return var1;
   }

   public void logRootCauses(String var1) {
      List var2 = this.getRootCauses();
      int var3 = var2.size();

      int var6;
      for(int var4 = 0; var4 < var3; var4 = var6) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Root cause (");
         var6 = var4 + 1;
         var5.append(var6);
         var5.append(" of ");
         var5.append(var3);
         var5.append(")");
         Log.i(var1, var5.toString(), (Throwable)var2.get(var4));
      }

   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream var1) {
      this.printStackTrace((Appendable)var1);
   }

   public void printStackTrace(PrintWriter var1) {
      this.printStackTrace((Appendable)var1);
   }

   void setLoggingDetails(Key var1, DataSource var2) {
      this.setLoggingDetails(var1, var2, (Class)null);
   }

   void setLoggingDetails(Key var1, DataSource var2, Class var3) {
      this.key = var1;
      this.dataSource = var2;
      this.dataClass = var3;
   }

   private static final class IndentedAppendable implements Appendable {
      private final Appendable appendable;
      private boolean printedNewLine = true;

      IndentedAppendable(Appendable var1) {
         this.appendable = var1;
      }

      private CharSequence safeSequence(CharSequence var1) {
         return (CharSequence)(var1 == null ? "" : var1);
      }

      public Appendable append(char var1) throws IOException {
         boolean var2 = this.printedNewLine;
         boolean var3 = false;
         if (var2) {
            this.printedNewLine = false;
            this.appendable.append("  ");
         }

         if (var1 == '\n') {
            var3 = true;
         }

         this.printedNewLine = var3;
         this.appendable.append(var1);
         return this;
      }

      public Appendable append(CharSequence var1) throws IOException {
         var1 = this.safeSequence(var1);
         return this.append(var1, 0, var1.length());
      }

      public Appendable append(CharSequence var1, int var2, int var3) throws IOException {
         var1 = this.safeSequence(var1);
         boolean var4 = this.printedNewLine;
         boolean var5 = false;
         if (var4) {
            this.printedNewLine = false;
            this.appendable.append("  ");
         }

         var4 = var5;
         if (var1.length() > 0) {
            var4 = var5;
            if (var1.charAt(var3 - 1) == '\n') {
               var4 = true;
            }
         }

         this.printedNewLine = var4;
         this.appendable.append(var1, var2, var3);
         return this;
      }
   }
}
