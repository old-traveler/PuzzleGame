package com.hyc.puzzlegame.utils;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import com.hyc.puzzlegame.bean.ImagePiece;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyc on 2017/3/12 10:20
 */


public class ImageSplitterUtil
{
    /**
     * @param bitmap
     * @param piece
     *            切成piece*piece+1块
     * @return List<ImagePiece>
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece)
    {
        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pieceWidth = width / piece;
        int pieceHeight = height / 4;

        for (int i = 0; i < piece; i++){
            for (int j = 0; j < piece+1; j++){
                ImagePiece imagePiece = new ImagePiece();

                int x = i * pieceWidth;
                int y = j * pieceHeight;

                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y,
                        pieceWidth, pieceHeight));
                imagePiece.setPosition(i*4+j);
                imagePieces.add(imagePiece);

            }
        }
        return imagePieces;
    }


    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,(int) height, matrix, true);
        return bitmap;

    }


}