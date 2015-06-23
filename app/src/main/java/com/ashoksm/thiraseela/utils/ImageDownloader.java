package com.ashoksm.thiraseela.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * This Class is used to download image async.
 * Created by Ashok Saravanan on 19/5/15.
 */
public class ImageDownloader {

    private static LruCache<String, Bitmap> mMemoryCache;
    private static ImageDownloader imageDownloader;

    private ImageDownloader(int memClassBytesIn) {
        if (mMemoryCache == null) {
            // Get max available VM memory, exceeding this amount will throw an
            // OutOfMemory exception. Stored in kilobytes as LruCache takes an
            // int in its constructor.
            final int maxMemory = memClassBytesIn * 1024 * 1024;

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                        return bitmap.getByteCount();
                    } else {
                        // Pre HC-MR1
                        return bitmap.getRowBytes() * bitmap.getHeight();
                    }
                }
            };

        }
    }

    public static ImageDownloader getInstance(int memClassBytesIn) {
        if (imageDownloader == null) {
            imageDownloader = new ImageDownloader(memClassBytesIn);
            return imageDownloader;
        } else {
            return imageDownloader;
        }
    }

    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable downloadedDrawable = (AsyncDrawable) drawable;
                return downloadedDrawable.getBitmapWorkerTask();
            } else if (drawable instanceof AsyncColorDrawable) {
                AsyncColorDrawable colorDrawable = (AsyncColorDrawable) drawable;
                return colorDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    public void download(String url, ImageView imageView, Resources res, Bitmap bitmap) {
        if (cancelPotentialDownload(url, imageView)) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
            if (url.contains("home_bg.png") || url.contains("inner_bg.png")) {
                AsyncColorDrawable colorDrawable = new AsyncColorDrawable(task);
                imageView.setImageDrawable(colorDrawable);
            } else {
                AsyncDrawable downloadedDrawable = new AsyncDrawable(res, bitmap, task);
                imageView.setImageDrawable(downloadedDrawable);
            }
            task.execute(url);
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
    }

    public Bitmap getBitmapFromMemCache(String key) {
        Bitmap bitmap = null;
        if (mMemoryCache != null) {
            bitmap = mMemoryCache.get(key);
        }
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapDownloaderTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapDownloaderTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    static class AsyncColorDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public AsyncColorDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.BLACK);
            bitmapDownloaderTaskReference =
                    new WeakReference<>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }

    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String url;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            try {
                url = params[0];
                Bitmap bitmap = BitmapFactory.decodeStream(new URL((params[0])).openStream());
                if (bitmap.getWidth() > 300 && bitmap.getHeight() > 400) {
                    bitmap = getResizedBitmap(bitmap, 300, 400);
                }
                addBitmapToMemoryCache(url, bitmap);
                return bitmap;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                if (this == bitmapDownloaderTask & bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }

            }

        }
    }
}

