package com.liangmayong.photoselector;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.security.MessageDigest;

/**
 * PhotoSelector
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class PhotoSelector {

    private static volatile PhotoSelector selector;
    private CustomSelectorDialogListener dialogListener;

    /**
     * OnShowSelectorDialogListener
     */
    public static interface CustomSelectorDialogListener {
        void showSelectorDialog(final Activity activity, final int id, String takeName, String selectName);
    }

    /**
     * setCustomDialog
     *
     * @param dialogListener dialogListener
     */
    public void setCustomDialog(CustomSelectorDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    /**
     * getInstance
     *
     * @return PhotoSelector
     */
    public static PhotoSelector getInstance() {
        if (selector == null) {
            synchronized (PhotoSelector.class) {
                selector = new PhotoSelector();
            }
        }
        return selector;
    }

    /**
     * selectDialog
     *
     * @param activity   activity
     * @param id         id
     * @param takeName   takeName
     * @param selectName selectName
     */
    public void showSelectorDialog(final Activity activity, final int id, String takeName, String selectName) {
        if (dialogListener != null) {
            dialogListener.showSelectorDialog(activity, id, takeName, selectName);
            return;
        }
        new AlertDialog.Builder(activity)
                .setItems(new CharSequence[]{takeName, selectName}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            startTakePhoto(activity, id);
                        } else {
                            startSelectPhoto(activity, id);
                        }
                    }
                }).show();
    }

    /**
     *
     */
    private PhotoSelector() {
    }

    private static String tempDir = "/photoselector/temp";

    /**
     * takePhoto
     *
     * @param activity activity
     * @param id       id
     */
    public void startTakePhoto(final Activity activity, final int id) {
        activity.startActivityForResult(getTakeIntent(id + 0xAA), id + 0xAA);
    }

    /**
     * selectPhoto
     *
     * @param activity activity
     * @param id       id
     */
    public void startSelectPhoto(final Activity activity, final int id) {
        activity.startActivityForResult(getSelectIntent(), id + 0xFF);
    }

    /**
     * handleResult
     *
     * @param id          id
     * @param width       width
     * @param height      height
     * @param activity    activity
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     * @return bitmap
     */
    public Bitmap handleResult(final int id, int width, int height, Activity activity, int requestCode, int resultCode,
                               Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == id + 0xAA) {
                activity.startActivityForResult(getCropIntent(getTempUri(id + 0xAA), id, width, height), id);
            } else if (requestCode == id + 0xFF) {
                activity.startActivityForResult(getCropIntent(data.getData(), id, width, height), id);
            } else if (requestCode == id) {
                // delete take photo
                File file = new File(getTempPath(id + 0xAA));
                if (file.exists()) {
                    file.delete();
                }
                return getTempThumbnail(id, width, height);
            }
        }
        return null;
    }

    /**
     * setTempDir
     *
     * @param tempDir tempDir
     */
    public void setTempDir(String tempDir) {
        PhotoSelector.tempDir = tempDir;
    }

    /**
     * clearTemp
     */
    public void clearTemp() {
        String dirName = tempDir;
        String fileDir = Environment.getExternalStorageDirectory().getPath() + "/" + dirName;
        if (!createDir(fileDir)) {
            dirName = "";
            fileDir = Environment.getExternalStorageDirectory().getPath() + "/" + dirName;
        }
        File file = new File(fileDir);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * getTempFilename
     *
     * @param id name
     * @return temp filename
     */
    public String getTempPath(int id) {
        String dirName = tempDir;
        String fileDir = Environment.getExternalStorageDirectory().getPath() + "/" + dirName;
        if (!createDir(fileDir)) {
            dirName = "";
            fileDir = Environment.getExternalStorageDirectory().getPath() + "/" + dirName;
        }
        return fileDir + "/" + "temp_photoselector_" + encrypt(id + "");
    }

    /**
     * getImageThumbnail
     *
     * @param id     id
     * @param width  width
     * @param height height
     * @return Bitmap
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public Bitmap getTempThumbnail(int id, int width, int height) {
        String imagePath = getTempPath(id);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        if (width == 0 || height == 0) {
            width = w;
            height = h;
        }
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * getImageSelectIntent
     *
     * @return Intent
     */
    private Intent getSelectIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /**
     * getImageTakeIntent
     *
     * @return Intent
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private Intent getTakeIntent(int id) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri(id));
        return intent;
    }

    /**
     * getImageCropIntent
     *
     * @param uri    old file uri
     * @param id     id
     * @param width  bitmap width
     * @param height bitmap height
     * @return Intent
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private Intent getCropIntent(Uri uri, int id, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri(id));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /**
     * getImageTempUri
     *
     * @param id id
     * @return Uri
     */
    private Uri getTempUri(int id) {
        return Uri.parse("file://" + "/" + getTempPath(id));
    }

    /**
     * createDir
     *
     * @param dirName dirName
     * @return boolean
     */
    private boolean createDir(String dirName) {
        File file = new File(dirName);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * MD5 encrypt
     *
     * @param str string
     * @return encrypt string
     */
    @SuppressLint("DefaultLocale")
    private String encrypt(String str) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte tmp[] = mdTemp.digest();
            char strs[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf];
                strs[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(strs).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

}
