// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import java.util.regex.Matcher;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

public final class CalendarParsedResult extends ParsedResult
{
    private static final Pattern DATE_TIME;
    private static final Pattern RFC2445_DURATION;
    private static final long[] RFC2445_DURATION_FIELD_UNITS;
    private final String[] attendees;
    private final String description;
    private final Date end;
    private final boolean endAllDay;
    private final double latitude;
    private final String location;
    private final double longitude;
    private final String organizer;
    private final Date start;
    private final boolean startAllDay;
    private final String summary;
    
    static {
        RFC2445_DURATION = Pattern.compile("P(?:(\\d+)W)?(?:(\\d+)D)?(?:T(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?)?");
        RFC2445_DURATION_FIELD_UNITS = new long[] { 604800000L, 86400000L, 3600000L, 60000L, 1000L };
        DATE_TIME = Pattern.compile("[0-9]{8}(T[0-9]{6}Z?)?");
    }
    
    public CalendarParsedResult(final String summary, final String s, final String s2, final String s3, final String location, final String organizer, final String[] attendees, final String description, final double latitude, final double longitude) {
        long durationMS;
        Date end;
        boolean startAllDay;
        boolean endAllDay;
        Label_0045_Outer:Label_0057_Outer:Label_0079_Outer:
        while (true) {
            super(ParsedResultType.CALENDAR);
            this.summary = summary;
            while (true) {
            Label_0186:
                while (true) {
                    Label_0156: {
                        while (true) {
                            while (true) {
                                try {
                                    this.start = parseDate(s);
                                    if (s2 != null) {
                                        break Label_0156;
                                    }
                                    durationMS = parseDurationMS(s3);
                                    if (durationMS < 0L) {
                                        end = null;
                                        this.end = end;
                                        if (s.length() != 8) {
                                            break Label_0156;
                                        }
                                        startAllDay = true;
                                        this.startAllDay = startAllDay;
                                        if (s2 != null && s2.length() == 8) {
                                            endAllDay = true;
                                            this.endAllDay = endAllDay;
                                            this.location = location;
                                            this.organizer = organizer;
                                            this.attendees = attendees;
                                            this.description = description;
                                            this.latitude = latitude;
                                            this.longitude = longitude;
                                            return;
                                        }
                                        break Label_0186;
                                    }
                                }
                                catch (ParseException ex) {
                                    throw new IllegalArgumentException(ex.toString());
                                }
                                end = new Date(this.start.getTime() + durationMS);
                                continue Label_0045_Outer;
                            }
                            try {
                                this.end = parseDate(s2);
                                continue Label_0057_Outer;
                            }
                            catch (ParseException ex2) {
                                throw new IllegalArgumentException(ex2.toString());
                            }
                            break;
                        }
                    }
                    startAllDay = false;
                    continue Label_0079_Outer;
                }
                endAllDay = false;
                continue;
            }
        }
    }
    
    private static DateFormat buildDateFormat() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat;
    }
    
    private static DateFormat buildDateTimeFormat() {
        return new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH);
    }
    
    private static String format(final boolean b, final Date date) {
        String format;
        if (date == null) {
            format = null;
        }
        else {
            DateFormat dateFormat;
            if (b) {
                dateFormat = DateFormat.getDateInstance(2);
            }
            else {
                dateFormat = DateFormat.getDateTimeInstance(2, 2);
            }
            format = dateFormat.format(date);
        }
        return format;
    }
    
    private static Date parseDate(final String s) throws ParseException {
        if (!CalendarParsedResult.DATE_TIME.matcher(s).matches()) {
            throw new ParseException(s, 0);
        }
        Date date;
        if (s.length() == 8) {
            date = buildDateFormat().parse(s);
        }
        else if (s.length() == 16 && s.charAt(15) == 'Z') {
            final Date parse = buildDateTimeFormat().parse(s.substring(0, 15));
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            final long date2 = parse.getTime() + gregorianCalendar.get(15);
            gregorianCalendar.setTime(new Date(date2));
            date = new Date(date2 + gregorianCalendar.get(16));
        }
        else {
            date = buildDateTimeFormat().parse(s);
        }
        return date;
    }
    
    private static long parseDurationMS(final CharSequence input) {
        long n = -1L;
        if (input != null) {
            final Matcher matcher = CalendarParsedResult.RFC2445_DURATION.matcher(input);
            if (matcher.matches()) {
                long n2 = 0L;
                int n3 = 0;
                while (true) {
                    n = n2;
                    if (n3 >= CalendarParsedResult.RFC2445_DURATION_FIELD_UNITS.length) {
                        break;
                    }
                    final String group = matcher.group(n3 + 1);
                    long n4 = n2;
                    if (group != null) {
                        n4 = n2 + CalendarParsedResult.RFC2445_DURATION_FIELD_UNITS[n3] * Integer.parseInt(group);
                    }
                    ++n3;
                    n2 = n4;
                }
            }
        }
        return n;
    }
    
    public String[] getAttendees() {
        return this.attendees;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public String getDisplayResult() {
        final StringBuilder sb = new StringBuilder(100);
        ParsedResult.maybeAppend(this.summary, sb);
        ParsedResult.maybeAppend(format(this.startAllDay, this.start), sb);
        ParsedResult.maybeAppend(format(this.endAllDay, this.end), sb);
        ParsedResult.maybeAppend(this.location, sb);
        ParsedResult.maybeAppend(this.organizer, sb);
        ParsedResult.maybeAppend(this.attendees, sb);
        ParsedResult.maybeAppend(this.description, sb);
        return sb.toString();
    }
    
    public Date getEnd() {
        return this.end;
    }
    
    public double getLatitude() {
        return this.latitude;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public double getLongitude() {
        return this.longitude;
    }
    
    public String getOrganizer() {
        return this.organizer;
    }
    
    public Date getStart() {
        return this.start;
    }
    
    public String getSummary() {
        return this.summary;
    }
    
    public boolean isEndAllDay() {
        return this.endAllDay;
    }
    
    public boolean isStartAllDay() {
        return this.startAllDay;
    }
}
