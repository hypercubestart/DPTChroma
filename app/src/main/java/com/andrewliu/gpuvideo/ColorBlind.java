package com.andrewliu.gpuvideo;

import com.daasuu.gpuv.egl.filter.GlFilter;

import java.util.ArrayList;
import java.util.List;

public class ColorBlind {
    public String name;
    public String url;

    ColorBlind(String name, String url) {
        this.name = name;
        this.url = url;
    }


    public static ArrayList<ColorBlind> getAll() {
        ArrayList<ColorBlind> list = new ArrayList<>();
        list.add(new ColorBlind("Deuteranope", "https://raw.githubusercontent.com/johnsonkuang/PythonDaltonAPI/master/images/Deuteranope.jpg"));
        list.add(new ColorBlind("Protanope", "https://raw.githubusercontent.com/johnsonkuang/PythonDaltonAPI/master/images/Protan.png"));
        list.add(new ColorBlind("Tritanope", "https://raw.githubusercontent.com/johnsonkuang/PythonDaltonAPI/master/images/Tritan.jpg"));
        list.add(new ColorBlind("Azure", "https://mspoweruser.com/wp-content/uploads/2017/09/azure-1.png"));
        return list;
    }

    public static GlFilter getFilter(ColorBlindTypes type, float intensity) {
        String lms21ms_deficient = "1, 0.494207, 0, 0, 0, 0, 0, 1.24827, 1";

        switch (type) {
            case Protanope:
                lms21ms_deficient = "0, 0, 0, 2.02344, 1, 0, -2.52581, 0, 1";
                break;
            case Tritanope:
                lms21ms_deficient = "1, -0.395913, 0.801109,0, 1, 0.801109,0, 0, 0";
                 break;
            default:
                //Deuteranope
                break;
        }

        return new DaltonizeFilter(lms21ms_deficient, intensity);
    }
}
