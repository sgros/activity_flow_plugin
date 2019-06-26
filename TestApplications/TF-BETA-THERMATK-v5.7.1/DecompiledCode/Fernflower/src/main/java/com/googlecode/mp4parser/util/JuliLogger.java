package com.googlecode.mp4parser.util;

import java.util.logging.Level;

public class JuliLogger extends Logger {
   java.util.logging.Logger logger;

   public JuliLogger(String var1) {
      this.logger = java.util.logging.Logger.getLogger(var1);
   }

   public void logDebug(String var1) {
      this.logger.log(Level.FINE, var1);
   }
}
