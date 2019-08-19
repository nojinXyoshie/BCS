//package com.annisa.bcs;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//
//import com.annisa.bcs.Helper.PermissionHelper;
//import com.annisa.bcs.helper.PermissionHelper;
//
//
//
//public class CameraPermission extends AppCompatActivity {
//
//    PermissionHelper permissionHelper;
//    Intent intent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.camera_pemission);
//        permissionHelper = new PermissionHelper(this);
//
//        checkAndRequestPermissions();
//    }
//
//    private boolean checkAndRequestPermissions() {
//        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
//            @Override
//            public void onPermissionCheckDone() {
//                intent = new Intent(CameraPermission.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        permissionHelper.checkAndRequestPermissions();
//
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
//    }
//}