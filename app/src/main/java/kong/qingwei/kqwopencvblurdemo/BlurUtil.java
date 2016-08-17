package kong.qingwei.kqwopencvblurdemo;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by kqw on 2016/8/17.
 * 图片虚化的工具类
 */
public class BlurUtil {

    private Subscriber<Bitmap> mSubscriber;

    public BlurUtil(Subscriber<Bitmap> subscriber) {
        mSubscriber = subscriber;
    }

    /**
     * 均值模糊方法
     *
     * @param bitmap 要处理的图片
     */
    public void blur(Bitmap bitmap) {
        // RxJava处理图片虚化
        if (null != mSubscriber)
            Observable
                    .just(bitmap)
                    .map(new Func1<Bitmap, Bitmap>() {

                        @Override
                        public Bitmap call(Bitmap bitmap) {
                            // Bitmap转为Mat
                            Mat src = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
                            Utils.bitmapToMat(bitmap, src);

                            // 均值模糊方法
                            Imgproc.blur(src, src, new Size(100, 100));

                            // Mat转Bitmap
                            Bitmap processedImage = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(src, processedImage);

                            return processedImage;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mSubscriber);
    }

    /**
     * 高斯模糊方法
     *
     * @param bitmap 要处理的图片
     */
    public void gaussianBlur(Bitmap bitmap) {
        // RxJava处理图片虚化
        if (null != mSubscriber)
            Observable
                    .just(bitmap)
                    .map(new Func1<Bitmap, Bitmap>() {

                        @Override
                        public Bitmap call(Bitmap bitmap) {
                            // Bitmap转为Mat
                            Mat src = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
                            Utils.bitmapToMat(bitmap, src);

                            // 高斯模糊方法
                            Imgproc.GaussianBlur(src, src, new Size(91, 91), 0);

                            // Mat转Bitmap
                            Bitmap processedImage = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(src, processedImage);

                            return processedImage;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mSubscriber);
    }
}
