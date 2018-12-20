package com.yue.customcamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.yue.customcamera.activity.ShortVideoActivity;
import com.yue.customcamera.activity.ShowPicActivity;
import com.yue.customcamera.base.DefaultBaseActivity;
import com.yue.customcamera.utils.CameraUtil;

public class MainActivity extends DefaultBaseActivity {

    private Button btn_camera;
//    private ImageView img;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        btn_camera = (Button)findViewById(R.id.btn_camera);
//        img = (ImageView)findViewById(R.id.img);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //未授权，提起权限申请
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA},
                            AppConstant.PERMISSION.CAMERA);
                } else {
                    CameraUtil.getInstance().camera(MainActivity.this);
                }
            }
        });
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ShortVideoActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断请求码，确定当前申请的权限
        if (requestCode == AppConstant.PERMISSION.CAMERA) {
            //判断权限是否申请通过
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraUtil.getInstance().camera(MainActivity.this);
            } else {
                //授权失败
//                requestSdcardFailed(AppConstant.PERMISSION.REQUEST_STORAGE_SEARCH, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != AppConstant.RESULT_CODE.RESULT_OK){
            return;
        }

        if(requestCode == AppConstant.REQUEST_CODE.CAMERA){
            String img_path = data.getStringExtra(AppConstant.KEY.IMG_PATH);

            int picWidth = data.getIntExtra(AppConstant.KEY.PIC_WIDTH, 0);
            int picHeight = data.getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0);
/*
            img.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picHeight));
            img.setImageURI(Uri.parse(img_path));
            */
            Intent intent = new Intent(activity, ShowPicActivity.class);
            intent.putExtra(AppConstant.KEY.PIC_WIDTH, picWidth);
            intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight);
            intent.putExtra(AppConstant.KEY.IMG_PATH, img_path);
            startActivity(intent);
        }
    }
}
