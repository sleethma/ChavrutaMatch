package com.example.micha.chavrutamatch.Utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by micha on 11/26/2017.
 */

public class ImgUtils {

    public static void compressImageBitmap(Context context, Uri uriIn) {
//            Take in uri

//    compress image based on type
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uriIn));
        Toast.makeText(context, type, Toast.LENGTH_LONG).show();
//    store image in cache?
        //get ImageView dementions (pass in as params?)
        //call createScaledBitmap( pass in dimensions here from imageview)
//            return image
    }

    public static String uriToCompressedBase64String(Context context, Uri imgUriIn) {
        Bitmap bitmap = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUriIn);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);

        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = bos.toByteArray();
        return new String(Base64.encodeToString(data, Base64.DEFAULT));
    }

    //todo:delete below
    public static byte[] uriToByteArray(Context context, Uri imgUriIn, String filePath) throws URISyntaxException {
        Bitmap bitmap = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUriIn);
            Bitmap rotatedBitmap = rotateImgIfNeeded(filePath, bitmap);

            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);

        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = bos.toByteArray();
        return Base64.encode(data, Base64.DEFAULT);
    }

    //todo:delete if no need
    public static Bitmap rotateImgIfNeeded(String fileName, Bitmap bitmapToRotate) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapToRotate, 0, 0, bitmapToRotate.getWidth(),
                bitmapToRotate.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    //todo: delete if no need
    //method returns real fully qual fileName for use in ExifInterface
    public static String getFilePath(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap base64StringToBitmap(String encodedString) {
        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    //todo: close bytebuffer and inputStream
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    /**
     * Creates the temporary image file in the cache directory.
     *
     * @return The temporary image file.
     * @throws IOException Thrown if there is an error creating the file
     */
    public static File createTempImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalCacheDir();

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }


    static Bitmap resamplePic(Context context, String imagePath) {

        // Get device screen size information
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        int targetH = metrics.heightPixels;
        int targetW = metrics.widthPixels;

        // Get the dimensions of the original bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath);
    }

    /**
     * Deletes image file for a given path.
     *
     * @param context   The application context.
     * @param imagePath The path of the photo to be deleted.
     */
    public static boolean deleteImageFile(Context context, String imagePath) {
        // Get the file
        File imageFile = new File(imagePath);

        // Delete the image
        boolean deleted = imageFile.delete();

        // If there is an error deleting the file, show a Toast
        if (!deleted) {
            Toast.makeText(context, "Error finding image", Toast.LENGTH_SHORT).show();
        }

        return deleted;
    }


}
