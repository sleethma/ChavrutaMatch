package com.example.micha.chavrutamatch.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by micha on 11/26/2017.
 */

public class ImgUtils {

    public static String uriToCompressedString(Context context ,Uri imgUriIn){
        Bitmap bitmap = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUriIn);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);

        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = bos.toByteArray();
        return new String(Base64.encodeToString(data, Base64.DEFAULT));
    }
}
