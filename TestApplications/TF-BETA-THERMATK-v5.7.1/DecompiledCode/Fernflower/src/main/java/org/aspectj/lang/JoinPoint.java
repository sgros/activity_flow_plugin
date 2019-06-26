package org.aspectj.lang;

public interface JoinPoint {
   Object getTarget();

   public interface StaticPart {
      String toString();
   }
}
