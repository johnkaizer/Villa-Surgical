package com.example.villasurgical.Fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.villasurgical.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;


public class HomeFragment extends Fragment {

    private ImageView qrCodeImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        qrCodeImageView = rootView.findViewById(R.id.qrCodeImageView);

        // Generate QR code
        String qrCodeValue = "Products"; // Replace with your desired value
        Bitmap qrCodeBitmap = generateQRCode(qrCodeValue);

        // Set the QR code bitmap in the ImageView
        if (qrCodeBitmap != null) {
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        }

        return rootView;
    }

    private Bitmap generateQRCode(String value) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    value, BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            qrCodeBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return qrCodeBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
