package com.hyc.puzzlegame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.hyc.puzzlegame.utils.ImageSplitterUtil;
import com.hyc.puzzlegame.view.PuzzleView;



public class MainActivity extends AppCompatActivity {

    private static final int IMAGE = 1;
    private static final int  CAMERA_WITH_DATA= 12;

    private PuzzleView puzzleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onClick();
    }

    public void onClick() {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取被选择的图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
        //获取拍照后的bitmap
        if(resultCode!=RESULT_OK)
            return;
        switch(requestCode) {
            case CAMERA_WITH_DATA:
                final Bitmap photo = data.getParcelableExtra("data");
                if (photo != null) {
                    Bitmap bm = photo;
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    float screenWidth = dm.widthPixels;
                    float screenHeight = screenWidth*bm.getHeight()/bm.getWidth();
                    puzzleView=new PuzzleView(this);
                    puzzleView.setList(ImageSplitterUtil.splitImage(ImageSplitterUtil.zoomImage(bm,screenWidth,screenHeight),3));
                    setContentView(puzzleView);
                    bm.recycle();
                }
        }

    }

    //加载图片
    private void showImage(String imagePath){
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float screenWidth = dm.widthPixels;
        float screenHeight = screenWidth*bm.getHeight()/bm.getWidth();
        puzzleView=new PuzzleView(this);
        puzzleView.setList(ImageSplitterUtil.splitImage(ImageSplitterUtil.zoomImage(bm,screenWidth,screenHeight),3));
        setContentView(puzzleView);
        bm.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.restart:
                onClick();
                break;
            case R.id.camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_WITH_DATA);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}



