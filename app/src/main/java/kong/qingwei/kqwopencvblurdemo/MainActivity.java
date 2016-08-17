package kong.qingwei.kqwopencvblurdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

import rx.Subscriber;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private final int SELECT_PHOTO = 1;

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV Manager已安装，可以学习OpenCV啦。");
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    private ImageView mIvImage;
    private ImageView mIvImageProcessed;
    private Bitmap mSelectImage;
    private BlurUtil mBlurUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIvImage = (ImageView) findViewById(R.id.ivImage);
        mIvImageProcessed = (ImageView) findViewById(R.id.ivImageProcessed);

        // 图片模糊处理的工具类
        mBlurUtil = new BlurUtil(new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {
                // 图片处理完成
                dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                // 图片处理异常
                dismissProgressDialog();
            }

            @Override
            public void onNext(Bitmap bitmap) {
                // 获取到处理后的图片
                mIvImageProcessed.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mOpenCVCallBack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_load_image) {
            // 加载图片
            Intent photo = new Intent(Intent.ACTION_PICK);
            photo.setType("image/*");
            startActivityForResult(photo, SELECT_PHOTO);
            return true;
        } else if (id == R.id.action_blur_image) {
            // 均值模糊方法
            if (null == mSelectImage) {
                Snackbar.make(mIvImage, "请先选择一张图片", Snackbar.LENGTH_SHORT).show();
                return false;
            }
            showProgressDialog("正在使用均值模糊算法处理图片...");
            // 均值模糊算法处理图片
            mBlurUtil.blur(mSelectImage);
            return true;
        } else if (id == R.id.action_gaussian_blur_image) {
            // 高斯模糊方法
            if (null == mSelectImage) {
                Snackbar.make(mIvImage, "请先选择一张图片", Snackbar.LENGTH_SHORT).show();
                return false;
            }
            showProgressDialog("正在使用高斯模糊算法处理图片...");
            // 高斯模糊算法处理图片
            mBlurUtil.gaussianBlur(mSelectImage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        // 获取选中的图片
                        mSelectImage = BitmapFactory.decodeStream(imageStream);
                        mIvImage.setImageBitmap(mSelectImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
