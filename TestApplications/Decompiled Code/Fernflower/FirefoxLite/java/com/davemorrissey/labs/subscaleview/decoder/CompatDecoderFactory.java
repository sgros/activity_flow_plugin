package com.davemorrissey.labs.subscaleview.decoder;

public class CompatDecoderFactory implements DecoderFactory {
   private Class clazz;

   public CompatDecoderFactory(Class var1) {
      this.clazz = var1;
   }

   public Object make() throws IllegalAccessException, InstantiationException {
      return this.clazz.newInstance();
   }
}
