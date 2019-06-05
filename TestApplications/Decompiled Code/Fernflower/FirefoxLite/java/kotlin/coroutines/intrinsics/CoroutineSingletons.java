package kotlin.coroutines.intrinsics;

public enum CoroutineSingletons {
   COROUTINE_SUSPENDED,
   RESUMED,
   UNDECIDED;

   static {
      CoroutineSingletons var0 = new CoroutineSingletons("COROUTINE_SUSPENDED", 0);
      COROUTINE_SUSPENDED = var0;
      CoroutineSingletons var1 = new CoroutineSingletons("UNDECIDED", 1);
      UNDECIDED = var1;
      CoroutineSingletons var2 = new CoroutineSingletons("RESUMED", 2);
      RESUMED = var2;
   }
}
