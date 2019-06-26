// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.graphics.Color;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import android.opengl.GLES20;
import java.util.HashMap;
import java.util.Map;

public class Shader
{
    private int fragmentShader;
    protected int program;
    protected Map<String, Integer> uniformsMap;
    private int vertexShader;
    
    public Shader(final String s, final String s2, final String[] array, final String[] array2) {
        this.uniformsMap = new HashMap<String, Integer>();
        this.program = GLES20.glCreateProgram();
        final CompilationResult compileShader = this.compileShader(35633, s);
        final int status = compileShader.status;
        final int n = 0;
        if (status == 0) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Vertex shader compilation failed");
            }
            this.destroyShader(compileShader.shader, 0, this.program);
            return;
        }
        final CompilationResult compileShader2 = this.compileShader(35632, s2);
        if (compileShader2.status == 0) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Fragment shader compilation failed");
            }
            this.destroyShader(compileShader.shader, compileShader2.shader, this.program);
            return;
        }
        GLES20.glAttachShader(this.program, compileShader.shader);
        GLES20.glAttachShader(this.program, compileShader2.shader);
        for (int i = 0; i < array.length; ++i) {
            GLES20.glBindAttribLocation(this.program, i, array[i]);
        }
        if (this.linkProgram(this.program) == 0) {
            this.destroyShader(compileShader.shader, compileShader2.shader, this.program);
            return;
        }
        for (int length = array2.length, j = n; j < length; ++j) {
            final String s3 = array2[j];
            this.uniformsMap.put(s3, GLES20.glGetUniformLocation(this.program, s3));
        }
        final int shader = compileShader.shader;
        if (shader != 0) {
            GLES20.glDeleteShader(shader);
        }
        final int shader2 = compileShader2.shader;
        if (shader2 != 0) {
            GLES20.glDeleteShader(shader2);
        }
    }
    
    public static void SetColorUniform(final int n, final int n2) {
        GLES20.glUniform4f(n, Color.red(n2) / 255.0f, Color.green(n2) / 255.0f, Color.blue(n2) / 255.0f, Color.alpha(n2) / 255.0f);
    }
    
    private CompilationResult compileShader(int glCreateShader, final String s) {
        glCreateShader = GLES20.glCreateShader(glCreateShader);
        GLES20.glShaderSource(glCreateShader, s);
        GLES20.glCompileShader(glCreateShader);
        final int[] array = { 0 };
        GLES20.glGetShaderiv(glCreateShader, 35713, array, 0);
        if (array[0] == 0 && BuildVars.LOGS_ENABLED) {
            FileLog.e(GLES20.glGetShaderInfoLog(glCreateShader));
        }
        return new CompilationResult(glCreateShader, array[0]);
    }
    
    private void destroyShader(final int n, final int n2, final int n3) {
        if (n != 0) {
            GLES20.glDeleteShader(n);
        }
        if (n2 != 0) {
            GLES20.glDeleteShader(n2);
        }
        if (n3 != 0) {
            GLES20.glDeleteProgram(n);
        }
    }
    
    private int linkProgram(final int n) {
        GLES20.glLinkProgram(n);
        final int[] array = { 0 };
        GLES20.glGetProgramiv(n, 35714, array, 0);
        if (array[0] == 0 && BuildVars.LOGS_ENABLED) {
            FileLog.e(GLES20.glGetProgramInfoLog(n));
        }
        return array[0];
    }
    
    public void cleanResources() {
        if (this.program != 0) {
            GLES20.glDeleteProgram(this.vertexShader);
            this.program = 0;
        }
    }
    
    public int getUniform(final String s) {
        return this.uniformsMap.get(s);
    }
    
    private class CompilationResult
    {
        int shader;
        int status;
        
        CompilationResult(final int shader, final int status) {
            this.shader = shader;
            this.status = status;
        }
    }
}
