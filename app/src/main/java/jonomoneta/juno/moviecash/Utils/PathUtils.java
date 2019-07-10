package jonomoneta.juno.moviecash.Utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.loader.content.CursorLoader;

public class PathUtils {

//    @SuppressLint("NewApi")
//    public static String getRealPathFromURI_API19(Context context, Uri uri){
//        String filePath = "";
//        String wholeID = DocumentsContract.getDocumentId(uri);
//        Log.e("wholeId", wholeID);
//        // Split at colon, use second item in the array
//        String id = wholeID.split(":")[1];
////        if (wholeID.contains(":")){
////            id = wholeID.split(":")[1];
////        } else {
////            id = wholeID;
////        }
//
//        String[] column = { MediaStore.Images.Media.DATA };
//
//        // where id is equal to
//        String sel = MediaStore.Images.Media._ID + "=?";
//
//        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                column, sel, new String[]{ id }, null);
//
//        int columnIndex = cursor.getColumnIndex(column[0]);
//
//        if (cursor.moveToFirst()) {
//            filePath = cursor.getString(columnIndex);
//        }
//        cursor.close();
//        return filePath;
//    }
//
//
//    @SuppressLint("NewApi")
//    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        String result = null;
//
//        CursorLoader cursorLoader = new CursorLoader(
//                context,
//                contentUri, proj, null, null, null);
//        Cursor cursor = cursorLoader.loadInBackground();
//
//        if(cursor != null){
//            int column_index =
//                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            result = cursor.getString(column_index);
//        }
//        return result;
//    }
//
//    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
//        int column_index
//                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

    public static String getPath(final Context context, final Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
