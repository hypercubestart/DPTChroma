package com.andrewliu.gpuvideo;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ImageClassifier {
//    Interpreter tflite;
//    ImageClassifier(Activity activity) throws IOException {
//        tflite = new Interpreter(loadModelFile(activity));
//        labelList = 6;
//        imgData =
//                ByteBuffer.allocateDirect(
//                        4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
//        imgData.order(ByteOrder.nativeOrder());
//        labelProbArray = new float[1][labelList.size()];
//        // Log.d(TAG, "Created a Tensorflow Lite Image Classifier.");
//    }
//
//    /** Memory-map the model file in Assets. */
//    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
//        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
//        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
//        FileChannel fileChannel = inputStream.getChannel();
//        long startOffset = fileDescriptor.getStartOffset();
//        long declaredLength = fileDescriptor.getDeclaredLength();
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
//    }
//
//    private String getModelPath() {
//        return "model.tflite";
//    }
}
