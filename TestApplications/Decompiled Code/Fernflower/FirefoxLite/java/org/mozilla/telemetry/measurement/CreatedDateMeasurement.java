package org.mozilla.telemetry.measurement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreatedDateMeasurement extends TelemetryMeasurement {
   public CreatedDateMeasurement() {
      super("created");
   }

   public Object flush() {
      return (new SimpleDateFormat("yyyy-MM-dd", Locale.US)).format(Calendar.getInstance().getTime());
   }
}
