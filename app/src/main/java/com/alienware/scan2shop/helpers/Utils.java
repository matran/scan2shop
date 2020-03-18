package com.alienware.scan2shop.helpers;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alienware.scan2shop.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by henry cheruiyot on 2/3/2018.
 */

public class Utils {

    public static void setUpToolbar(AppCompatActivity paramAppCompatActivity)
    {
        paramAppCompatActivity.setSupportActionBar((Toolbar)paramAppCompatActivity.findViewById(R.id.toolbar));
        Objects.requireNonNull(paramAppCompatActivity.getSupportActionBar()).setHomeButtonEnabled(true);
        paramAppCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        paramAppCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        paramAppCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public  static  Bitmap createBarcodeBitmap(String data, int width, int height) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        String finalData = Uri.encode(data);
        BitMatrix bm = writer.encode(finalData, BarcodeFormat.EAN_13, width, 1);
        int bmWidth = bm.getWidth();
        Bitmap imageBitmap = Bitmap.createBitmap(bmWidth, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bmWidth; i++) {
            int[] column = new int[height];
            Arrays.fill(column, bm.get(i, 0) ? Color.BLACK : Color.WHITE);
            imageBitmap.setPixels(column, 0, 1, i, 0, 1, height);
        }
        return imageBitmap;
    }


}

