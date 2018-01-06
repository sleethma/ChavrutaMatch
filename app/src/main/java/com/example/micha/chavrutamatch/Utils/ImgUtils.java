package com.example.micha.chavrutamatch.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by micha on 11/26/2017.
 */

public class ImgUtils {

    public static int rotateImgNeededCk(Context context, Uri uri) throws IOException {

        int rotation = 0;

        //only executes on API>24
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            InputStream in = context.getContentResolver().openInputStream(uri);

            try {
                ExifInterface exifInterface = new ExifInterface(in);

                int orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotation = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotation = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotation = 270;
                        break;
                }
                // Now you can extract any Exif tag you want
                // Assuming the image is a JPEG or supported raw format
            } catch (IOException e) {
                e.printStackTrace();

                // Handle any errors
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        return rotation;
    }


    public static Bitmap rotateImg(Bitmap bitmapToRotate, int degreesToRotate) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degreesToRotate);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapToRotate, 0, 0, bitmapToRotate.getWidth(),
                bitmapToRotate.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public static String bitmapToCompressedBase64String(Context context, Bitmap bitmapToBase64) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapToBase64.compress(Bitmap.CompressFormat.JPEG, 50, bos);

        byte[] data = bos.toByteArray();
        long testSize = data.length;

        return resizeBase64Image(Base64.encodeToString(data, Base64.DEFAULT));
    }

    public static String uriToCompressedBase64String(Context context, Uri imgUriIn) {
        Bitmap bitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUriIn);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);

        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = bos.toByteArray();
        return resizeBase64Image(Base64.encodeToString(data, Base64.DEFAULT));
    }

    private static String resizeBase64Image(String base64image) {
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);
        int IMG_WIDTH = 400;
        int IMG_HEIGHT = 400;


        if (image.getHeight() <= 300 && image.getWidth() <= 300) {
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] b = baos.toByteArray();
        long testReducedLength = b.length;
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);
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
