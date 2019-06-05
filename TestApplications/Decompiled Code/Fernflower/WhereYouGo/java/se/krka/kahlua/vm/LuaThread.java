package se.krka.kahlua.vm;

import java.util.Vector;
import se.krka.kahlua.stdlib.BaseLib;

public class LuaThread {
   private static final int INITIAL_CALL_FRAME_STACK_SIZE = 10;
   public static final int INITIAL_STACK_SIZE = 10;
   private static final int MAX_CALL_FRAME_STACK_SIZE = 100;
   public static final int MAX_STACK_SIZE = 1000;
   public LuaCallFrame[] callFrameStack;
   public int callFrameTop;
   public LuaTable environment;
   public int expectedResults;
   public Vector liveUpvalues;
   public Object[] objectStack;
   public LuaThread parent;
   public String stackTrace = "";
   public LuaState state;
   public int top;

   public LuaThread(LuaState var1, LuaTable var2) {
      this.state = var1;
      this.environment = var2;
      this.objectStack = new Object[10];
      this.callFrameStack = new LuaCallFrame[10];
      this.liveUpvalues = new Vector();
   }

   private void callFrameStackClear(int var1, int var2) {
      for(; var1 <= var2; ++var1) {
         if (this.callFrameStack[var1] != null) {
            this.callFrameStack[var1].closure = null;
            this.callFrameStack[var1].javaFunction = null;
         }
      }

   }

   private final void ensureCallFrameStackSize(int var1) {
      if (var1 > 100) {
         throw new RuntimeException("Stack overflow");
      } else {
         int var2 = this.callFrameStack.length;

         int var3;
         for(var3 = var2; var3 <= var1; var3 *= 2) {
         }

         if (var3 > var2) {
            LuaCallFrame[] var4 = new LuaCallFrame[var3];
            System.arraycopy(this.callFrameStack, 0, var4, 0, var2);
            this.callFrameStack = var4;
         }

      }
   }

   private final void ensureStacksize(int var1) {
      if (var1 > 1000) {
         throw new RuntimeException("Stack overflow");
      } else {
         int var2 = this.objectStack.length;

         int var3;
         for(var3 = var2; var3 <= var1; var3 *= 2) {
         }

         if (var3 > var2) {
            Object[] var4 = new Object[var3];
            System.arraycopy(this.objectStack, 0, var4, 0, var2);
            this.objectStack = var4;
         }

      }
   }

   private String getStackTrace(LuaCallFrame var1) {
      String var4;
      if (var1.isLua()) {
         int[] var2 = var1.closure.prototype.lines;
         if (var2 != null) {
            int var3 = var1.pc - 1;
            if (var3 >= 0 && var3 < var2.length) {
               var4 = "at " + var1.closure.prototype + ":" + var2[var3] + "\n";
               return var4;
            }
         }

         var4 = "";
      } else {
         var4 = "at " + var1.javaFunction;
      }

      return var4;
   }

   public void addStackTrace(LuaCallFrame var1) {
      this.stackTrace = this.stackTrace + this.getStackTrace(var1);
   }

   public void cleanCallFrames(LuaCallFrame var1) {
      while(true) {
         LuaCallFrame var2 = this.currentCallFrame();
         if (var2 == null || var2 == var1) {
            return;
         }

         this.addStackTrace(var2);
         this.popCallFrame();
      }
   }

   public final void closeUpvalues(int var1) {
      int var2 = this.liveUpvalues.size();

      while(true) {
         --var2;
         if (var2 < 0) {
            break;
         }

         UpValue var3 = (UpValue)this.liveUpvalues.elementAt(var2);
         if (var3.index < var1) {
            break;
         }

         var3.value = this.objectStack[var3.index];
         var3.thread = null;
         this.liveUpvalues.removeElementAt(var2);
      }

   }

   public final LuaCallFrame currentCallFrame() {
      LuaCallFrame var1;
      if (this.isDead()) {
         var1 = null;
      } else {
         LuaCallFrame var2 = this.callFrameStack[this.callFrameTop - 1];
         var1 = var2;
         if (var2 == null) {
            var1 = new LuaCallFrame(this);
            this.callFrameStack[this.callFrameTop - 1] = var1;
         }
      }

      return var1;
   }

   public final UpValue findUpvalue(int var1) {
      int var2 = this.liveUpvalues.size();

      UpValue var4;
      while(true) {
         int var3 = var2 - 1;
         if (var3 >= 0) {
            var4 = (UpValue)this.liveUpvalues.elementAt(var3);
            if (var4.index == var1) {
               break;
            }

            var2 = var3;
            if (var4.index >= var1) {
               continue;
            }
         }

         var4 = new UpValue();
         var4.thread = this;
         var4.index = var1;
         this.liveUpvalues.insertElementAt(var4, var3 + 1);
         break;
      }

      return var4;
   }

   public String getCurrentStackTrace(int var1, int var2, int var3) {
      int var4 = var1;
      if (var1 < 0) {
         var4 = 0;
      }

      var1 = var2;
      if (var2 < 0) {
         var1 = 0;
      }

      StringBuffer var5 = new StringBuffer();

      for(var2 = this.callFrameTop - 1 - var4; var2 >= var3 && var1 > 0; --var1) {
         var5.append(this.getStackTrace(this.callFrameStack[var2]));
         --var2;
      }

      return var5.toString();
   }

   public LuaCallFrame getParent(int var1) {
      boolean var2 = true;
      boolean var3;
      if (var1 >= 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "Level must be non-negative");
      var1 = this.callFrameTop - var1 - 1;
      if (var1 >= 0) {
         var3 = var2;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "Level too high");
      return this.callFrameStack[var1];
   }

   public int getTop() {
      return this.top;
   }

   public boolean isDead() {
      boolean var1;
      if (this.callFrameTop == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void popCallFrame() {
      if (this.isDead()) {
         throw new RuntimeException("Stack underflow");
      } else {
         this.setCallFrameStackTop(this.callFrameTop - 1);
      }
   }

   public final LuaCallFrame pushNewCallFrame(LuaClosure var1, JavaFunction var2, int var3, int var4, int var5, boolean var6, boolean var7) {
      this.setCallFrameStackTop(this.callFrameTop + 1);
      LuaCallFrame var8 = this.currentCallFrame();
      var8.localBase = var3;
      var8.returnBase = var4;
      var8.nArguments = var5;
      var8.fromLua = var6;
      var8.insideCoroutine = var7;
      var8.closure = var1;
      var8.javaFunction = var2;
      return var8;
   }

   public final void setCallFrameStackTop(int var1) {
      if (var1 > this.callFrameTop) {
         this.ensureCallFrameStackSize(var1);
      } else {
         this.callFrameStackClear(var1, this.callFrameTop - 1);
      }

      this.callFrameTop = var1;
   }

   public final void setTop(int var1) {
      if (this.top < var1) {
         this.ensureStacksize(var1);
      } else {
         this.stackClear(var1, this.top - 1);
      }

      this.top = var1;
   }

   public final void stackClear(int var1, int var2) {
      while(var1 <= var2) {
         this.objectStack[var1] = null;
         ++var1;
      }

   }

   public final void stackCopy(int var1, int var2, int var3) {
      if (var3 > 0 && var1 != var2) {
         System.arraycopy(this.objectStack, var1, this.objectStack, var2, var3);
      }

   }
}
