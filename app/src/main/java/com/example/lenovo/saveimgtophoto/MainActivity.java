package com.example.lenovo.saveimgtophoto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cake);
        Button mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(new String[]{"保存图片"}, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick (DialogInterface dialog, int which){
                        if(bitmap != null){
                            saveImageToGallery(MainActivity.this, bitmap);
                        }
                    }
                });
                builder.show();
            }
        });
    }
    //指定保存路径
    public static void saveImageToGallery (Context context, Bitmap bmp){
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "webview");
        if(!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        try{
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try{
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
    }


}
