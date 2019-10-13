package com.andrewliu.gpuvideo;

import com.daasuu.gpuv.egl.filter.GlFilter;

public class DaltonizeFilter extends GlFilter {
    private static final String VERTEX_SHADER = "";


    private static final String FRAGMENT_SHADER = "precision mediump float;\n" +

            "varying highp vec2 vTextureCoord;\n" +

            "uniform lowp sampler2D sTexture;\n" +

            "\n" +

            "const highp mat3 rgb2lms = mat3(\n" +

            "17.8824,3.45565,0.0299566,\n" +

            "43.5161,27.1554,0.184309,\n" +

            "4.11935,3.86714,1.46709\n" +

            ");\n" +

            "\n" +

            "const highp mat3 lms2rgb = mat3(\n" +

            "0.0809444479, -0.0102485335, -.000365296938,\n" +

            "-0.130504409, 0.0540193266, -0.00412161469,\n" +

            "0.116721066, -0.113614708, 0.693511405\n" +

            ");\n" +

            "\n" +

            "const highp mat3 lms21ms_deficient = mat3(\n" +

            "1, 0.494207, 0,\n" +

            "0, 0, 0,\n" +

            "0, 1.24827, 1\n" +

            ");\n" +

            "\n" +

            "const highp mat3 err2mod = mat3(\n" +

            "0, 0.7, 0.7,\n" +

            "0,1,0,\n" +

            "0,0,1\n" +

            ");\n" +

            "\n" +

            "void main() {\n" +

            "lowp vec4 color = texture2D(sTexture, vTextureCoord);\n" +

            "\n" +

            "lowp vec3 rgb = color.rgb;\n" +

            "lowp vec3 lms = rgb2lms * rgb;\n" +

            "\n" +

            "lowp vec3 _lms = lms21ms_deficient * lms;\n" +

            "\n" +

            "lowp vec3 _rgb = lms2rgb * _lms;\n" +

            "\n" +

            "lowp vec3 error = rgb - _rgb;\n" +

            "lowp vec3 err = (err2mod * 2.0) * error;\n" +

            "\n" +

            "lowp vec3 dtpn = err + rgb;\n" +

            "\n" +

            "dtpn[0] = max(0.0, dtpn[0]);\n" +

            "dtpn[0] = min(255.0, dtpn[0]);\n" +

            "dtpn[1] = max(0.0, dtpn[1]);\n" +

            "dtpn[1] = min(255.0, dtpn[1]);\n" +

            "dtpn[2] = max(0.0, dtpn[2]);\n" +

            "dtpn[2] = min(255.0, dtpn[2]);\n" +

            "\n" +

            "gl_FragColor = vec4(dtpn.rgb, color.a);\n" +

            "}\n";

    public DaltonizeFilter(String lms21ms_deficient) {
        //super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
        this(lms21ms_deficient, 1);
    }

    public DaltonizeFilter(String lms21ms_deficient, float intensity) {
        super(DEFAULT_VERTEX_SHADER, createFragmentShader(lms21ms_deficient, intensity));
    }

    public static String createFragmentShader(String lms21ms_deficient, float intensity) {
        return "precision mediump float;\n" +

                "varying highp vec2 vTextureCoord;\n" +

                "uniform lowp sampler2D sTexture;\n" +

                "\n" +

                "const highp mat3 rgb2lms = mat3(\n" +

                "17.8824,3.45565,0.0299566,\n" +

                "43.5161,27.1554,0.184309,\n" +

                "4.11935,3.86714,1.46709\n" +

                ");\n" +

                "\n" +

                "const highp mat3 lms2rgb = mat3(\n" +

                "0.0809444479, -0.0102485335, -.000365296938,\n" +

                "-0.130504409, 0.0540193266, -0.00412161469,\n" +

                "0.116721066, -0.113614708, 0.693511405\n" +

                ");\n" +

                "\n" +

                "const highp mat3 lms21ms_deficient = mat3(\n" +

                lms21ms_deficient +

                ");\n" +

                "\n" +

                "const highp mat3 err2mod = mat3(\n" +

                "0, 0.7, 0.7,\n" +

                "0,1,0,\n" +

                "0,0,1\n" +

                ");\n" +

                "\n" +

                "void main() {\n" +

                "lowp vec4 color = texture2D(sTexture, vTextureCoord);\n" +

                "\n" +

                "lowp vec3 rgb = color.rgb;\n" +

                "lowp vec3 lms = rgb2lms * rgb;\n" +

                "\n" +

                "lowp vec3 _lms = lms21ms_deficient * lms;\n" +

                "\n" +

                "lowp vec3 _rgb = lms2rgb * _lms;\n" +

                "\n" +

                "lowp vec3 error = rgb - _rgb;\n" +

                "lowp vec3 err = (err2mod * " + intensity + ") * error;\n" +

                "\n" +

                "lowp vec3 dtpn = err + rgb;\n" +

                "\n" +

                "dtpn[0] = max(0.0, dtpn[0]);\n" +

                "dtpn[0] = min(255.0, dtpn[0]);\n" +

                "dtpn[1] = max(0.0, dtpn[1]);\n" +

                "dtpn[1] = min(255.0, dtpn[1]);\n" +

                "dtpn[2] = max(0.0, dtpn[2]);\n" +

                "dtpn[2] = min(255.0, dtpn[2]);\n" +

                "\n" +

                "gl_FragColor = vec4(dtpn.rgb, color.a);\n" +

                "}\n";
    }
}
