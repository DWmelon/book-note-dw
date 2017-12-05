package com.simplenote.module.photo;

import android.annotation.TargetApi;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.simplenote.application.MyClient;
import com.simplenote.util.FileUtil;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by yuyidong on 15-1-8.
 */
@TargetApi(21)
public class JpegReaderListener implements ImageReader.OnImageAvailableListener {

    private ImageReader reader;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    @Override
    public void onImageAvailable(ImageReader reader) {
        this.reader = reader;
    }

    /**
     * 经用户确认后 保存到本地文件，并把路径保存，关闭页面
     */
    public void saveImageToFile(Runnable runnable){
        this.runnable = runnable;
        new Thread(new ImageSaver(reader)).start();
    }

    class ImageSaver implements Runnable {
        private ImageReader mImageReader;

        ImageSaver(ImageReader mImageReader) {
            this.mImageReader = mImageReader;
        }

        @Override
        public void run() {
            Image image = mImageReader.acquireLatestImage();

            String rootPath = MyClient.getMyClient().getStorageManager().getTakePhotoPath();
            if (!FileUtil.isExists(rootPath)){
                new File(rootPath).mkdirs();
            }

            File file = createJpeg(rootPath);
            try {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                FileUtil.writeByteToPath(bytes, file);
                image.close();
                MyClient.getMyClient().getAddNoteManager().getNoteImagePaths().add(file.getPath());
            } catch (Exception e) {
                e.getStackTrace();
            }
            handler.post(runnable);
        }
    }

    /**
     * 创建jpeg的文件
     *
     * @return
     */
    private File createJpeg(String rootPath) {
        long time = System.currentTimeMillis();
        int random = new Random().nextInt(1000);
        File dir = new File(rootPath);
        Log.i("JpegSaver", time + "_" + random + ".jpg");
        return new File(dir, time + "_" + random + ".jpg");
    }

}
