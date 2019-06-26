// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import android.os.Environment;
import com.github.mikephil.charting.data.Entry;
import android.util.Log;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.github.mikephil.charting.data.BarEntry;
import java.util.List;
import android.content.res.AssetManager;

public class FileUtils
{
    private static final String LOG = "MPChart-FileUtils";
    
    public static List<BarEntry> loadBarEntriesFromAssets(final AssetManager assetManager, String split) {
        final ArrayList<BarEntry> list = new ArrayList<BarEntry>();
        final BufferedReader bufferedReader = null;
        BufferedReader bufferedReader3;
        final BufferedReader bufferedReader2 = bufferedReader3 = null;
        BufferedReader bufferedReader4 = null;
        try {
            try {
                bufferedReader3 = bufferedReader2;
                bufferedReader3 = bufferedReader2;
                final InputStreamReader in = new InputStreamReader(assetManager.open((String)split), "UTF-8");
                bufferedReader3 = bufferedReader2;
                bufferedReader4 = new BufferedReader(in);
                try {
                    for (String s = bufferedReader4.readLine(); s != null; s = bufferedReader4.readLine()) {
                        split = (IOException)(Object)s.split("#");
                        list.add(new BarEntry(Float.parseFloat(split[1]), Float.parseFloat(split[0])));
                    }
                    if (bufferedReader4 != null) {
                        final BufferedReader bufferedReader5 = bufferedReader4;
                        bufferedReader5.close();
                        return list;
                    }
                    return list;
                }
                catch (IOException split) {}
                finally {
                    final Object o;
                    final BufferedReader bufferedReader6 = (BufferedReader)o;
                }
            }
            finally {
                bufferedReader4 = bufferedReader3;
            }
        }
        catch (IOException split) {
            final BufferedReader bufferedReader6 = bufferedReader;
        }
        try {
            final BufferedReader bufferedReader5 = bufferedReader4;
            bufferedReader5.close();
            return list;
            final BufferedReader bufferedReader6;
            bufferedReader6.close();
        }
        catch (IOException ex) {
            Log.e("MPChart-FileUtils", ex.toString());
        }
        return list;
        if (bufferedReader4 != null) {
            try {
                bufferedReader4.close();
            }
            catch (IOException ex2) {
                Log.e("MPChart-FileUtils", ex2.toString());
            }
        }
    }
    
    public static List<Entry> loadEntriesFromAssets(final AssetManager assetManager, String ex) {
        final ArrayList<Entry> list = new ArrayList<Entry>();
        final BufferedReader bufferedReader = null;
        BufferedReader bufferedReader3;
        final BufferedReader bufferedReader2 = bufferedReader3 = null;
        BufferedReader bufferedReader4 = null;
        try {
            try {
                bufferedReader3 = bufferedReader2;
                bufferedReader3 = bufferedReader2;
                final InputStreamReader in = new InputStreamReader(assetManager.open((String)ex), "UTF-8");
                bufferedReader3 = bufferedReader2;
                bufferedReader4 = new BufferedReader(in);
                try {
                    for (String s = bufferedReader4.readLine(); s != null; s = bufferedReader4.readLine()) {
                        final String[] split = s.split("#");
                        final int length = split.length;
                        int i = 0;
                        if (length <= 2) {
                            ex = (IOException)new Entry(Float.parseFloat(split[1]), Float.parseFloat(split[0]));
                            list.add((BarEntry)ex);
                        }
                        else {
                            for (ex = (IOException)(Object)new float[split.length - 1]; i < ex.length; ++i) {
                                ex[i] = Float.parseFloat(split[i]);
                            }
                            list.add(new BarEntry((float)Integer.parseInt(split[split.length - 1]), (float[])(Object)ex));
                        }
                    }
                    if (bufferedReader4 != null) {
                        final BufferedReader bufferedReader5 = bufferedReader4;
                        bufferedReader5.close();
                        return list;
                    }
                    return list;
                }
                catch (IOException ex) {}
                finally {
                    final Object o;
                    final BufferedReader bufferedReader6 = (BufferedReader)o;
                }
            }
            finally {
                bufferedReader4 = bufferedReader3;
            }
        }
        catch (IOException ex) {
            final BufferedReader bufferedReader6 = bufferedReader;
        }
        try {
            final BufferedReader bufferedReader5 = bufferedReader4;
            bufferedReader5.close();
            return list;
            final BufferedReader bufferedReader6;
            bufferedReader6.close();
        }
        catch (IOException ex2) {
            Log.e("MPChart-FileUtils", ex2.toString());
        }
        return list;
        if (bufferedReader4 != null) {
            try {
                bufferedReader4.close();
            }
            catch (IOException ex3) {
                Log.e("MPChart-FileUtils", ex3.toString());
            }
        }
    }
    
    public static List<Entry> loadEntriesFromFile(String child) {
        final File file = new File(Environment.getExternalStorageDirectory(), child);
        child = (String)new ArrayList();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                final String[] split = line.split("#");
                final int length = split.length;
                int i = 0;
                if (length <= 2) {
                    ((List<Entry>)child).add(new Entry(Float.parseFloat(split[0]), (float)Integer.parseInt(split[1])));
                }
                else {
                    float[] array;
                    for (array = new float[split.length - 1]; i < array.length; ++i) {
                        array[i] = Float.parseFloat(split[i]);
                    }
                    ((List<BarEntry>)child).add(new BarEntry((float)Integer.parseInt(split[split.length - 1]), array));
                }
            }
        }
        catch (IOException ex) {
            Log.e("MPChart-FileUtils", ex.toString());
        }
        return (List<Entry>)child;
    }
    
    public static void saveToSdCard(final List<Entry> list, final String child) {
        final File file = new File(Environment.getExternalStorageDirectory(), child);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException ex) {
                Log.e("MPChart-FileUtils", ex.toString());
            }
        }
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            for (final Entry entry : list) {
                final StringBuilder sb = new StringBuilder();
                sb.append(entry.getY());
                sb.append("#");
                sb.append(entry.getX());
                bufferedWriter.append((CharSequence)sb.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (IOException ex2) {
            Log.e("MPChart-FileUtils", ex2.toString());
        }
    }
}
