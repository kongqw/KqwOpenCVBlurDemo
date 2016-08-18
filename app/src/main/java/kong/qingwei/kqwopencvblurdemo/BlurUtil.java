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

    /**
     * 中值模糊方法
     *
     * @param bitmap 要处理的图片
     */
    public void medianBlur(Bitmap bitmap) {
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

                            // 中值模糊方法
                            Imgproc.medianBlur(src, src, 33);

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
     * 锐化图片
     *
     * @param bitmap 要处理的图片
     */
    public void filter2D(Bitmap bitmap) {
        // 使用RxJava处理图片
        if (null != mSubscriber)
            Observable
                    .just(bitmap)
                    .map(new Func1<Bitmap, Bitmap>() {

                        @Override
                        public Bitmap call(Bitmap bitmap) {
                            // Bitmap转为Mat
                            Mat src = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
                            Utils.bitmapToMat(bitmap, src);

                            /*
                                自定义核
                                0   -1  0
                                -1  5   -1
                                0   -1  0
                              */
                            Mat kernel = new Mat(3, 3, CvType.CV_16SC1);
                            kernel.put(0, 0, 0, -1, 0, -1, 5, -1, 0, -1, 0);
                            // 对图像和自定义核做卷积
                            Imgproc.filter2D(src, src, src.depth(), kernel);

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
