package org.greenrobot.greendao.converter;

public interface PropertyConverter {
   Object convertToDatabaseValue(Object var1);

   Object convertToEntityProperty(Object var1);
}
