package com.hyc.puzzlegame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.hyc.puzzlegame.bean.ImagePiece;
import java.util.Collections;
import java.util.List;


/**
 * Created by hyc on 2017/3/12 10:20
 */

public class PuzzleView extends View {

    private float imageWidth;

    private float imageHeight;

    private int currentDownPosition;

    private int relativePosition=-1;

    private boolean isUp=false;

    private List<ImagePiece> list;

    private Paint paint;

    private boolean isWin=false;


    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public PuzzleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public PuzzleView(Context context) {
        this(context,null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint==null){
            paint=new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(Color.WHITE);
        }

        if (list!=null){
            drawPuzzle(canvas);
        }
    }

    private void drawPuzzle(Canvas canvas) {


        for (int i=0;i<3;i++){
            for (int j=0;j<4;j++){
                if (i*4+j==relativePosition&&!isUp){
                    canvas.drawLine(i*imageWidth,(j+1)*imageHeight,(i+1)*imageWidth,(j+1)*imageHeight,paint);
                    if(i!=2){
                        canvas.drawLine((i+1)*imageWidth,j*imageHeight,(i+1)*imageWidth,(j+1)*imageHeight,paint);
                    }
                    continue;
                }
                if (i*4+j==currentDownPosition&&!isUp){
                    if (isVertical){
                        canvas.drawBitmap(list.get(i*4+j).getBitmap(),i*imageWidth,j*imageHeight+moveDistance,paint);
                    }else {
                        canvas.drawBitmap(list.get(i*4+j).getBitmap(),i*imageWidth+moveDistance,j*imageHeight,paint);
                    }
                    drawMoveRelative(canvas,i,j);

                }else{
                    canvas.drawBitmap(list.get(i*4+j).getBitmap(),i*imageWidth,j*imageHeight,paint);
                }

                canvas.drawLine(i*imageWidth,(j+1)*imageHeight,(i+1)*imageWidth,(j+1)*imageHeight,paint);
                if(i!=2){
                    canvas.drawLine((i+1)*imageWidth,j*imageHeight,(i+1)*imageWidth,(j+1)*imageHeight,paint);
                }
            }
        }
    }

    private void drawMoveRelative(Canvas canvas,int i,int j) {
        switch (moveDirection){
            case LEFT:
                if (currentDownPosition!=0&currentDownPosition!=1&currentDownPosition!=2&currentDownPosition!=3){
                    relativePosition=currentDownPosition-4;
                    canvas.drawBitmap(list.get(relativePosition).getBitmap(),(i-1)*imageWidth+Math.abs(moveDistance),j*imageHeight,paint);
                }else {
                    relativePosition=-1;
                }
                break;
            case RIGHT:
                if (currentDownPosition!=8&currentDownPosition!=9&currentDownPosition!=10&currentDownPosition!=11){
                    relativePosition=currentDownPosition+4;
                    canvas.drawBitmap(list.get(relativePosition).getBitmap(),(i+1)*imageWidth-Math.abs(moveDistance),j*imageHeight,paint);
                }else {
                    relativePosition=-1;
                }
                break;
            case TOP:
                if (currentDownPosition!=0&currentDownPosition!=4&currentDownPosition!=8){
                    relativePosition=currentDownPosition-1;
                    canvas.drawBitmap(list.get(relativePosition).getBitmap(),i*imageWidth,(j-1)*imageHeight+Math.abs(moveDistance),paint);
                }else {
                    relativePosition=-1;
                }
                break;
            case BOTTOM:
                if (currentDownPosition!=3&currentDownPosition!=7&currentDownPosition!=11){
                    relativePosition=currentDownPosition+1;
                    canvas.drawBitmap(list.get(relativePosition).getBitmap(),i*imageWidth,(j+1)*imageHeight-Math.abs(moveDistance),paint);
                }else {
                    relativePosition=-1;
                }
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setList(List<ImagePiece> list) {

        this.list = list;
        imageWidth=list.get(0).getBitmap().getWidth();
        imageHeight=list.get(0).getBitmap().getHeight();
        Collections.shuffle(list);
        isWin=false;
    }

    float x;
    float y;
    float moveDistance=0;
    boolean isVertical;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isUp=false;
                x=event.getX();
                y=event.getY();
                currentDownPosition=(int)(x/imageWidth)*4+(int)(y/imageHeight);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX=event.getX();
                float moveY=event.getY();
                moveCurrentImage(x,y,moveX,moveY);

                break;
            case MotionEvent.ACTION_UP:
                judgeIsChange();
                moveDistance=0;
                isUp=true;
                relativePosition=-1;
                currentDownPosition=-1;
                invalidate();
                checkIsWin();
                break;
        }
        return true;
    }

    private void checkIsWin() {
        if (isWin){
            return;
        }
        for (int i=0;i<list.size();i++){

            if (list.get(i).getPosition()!=i){
                isWin=false;
                return;
            }
        }
        isWin=true;
        Toast.makeText(getContext(), "拼图完成！", Toast.LENGTH_SHORT).show();
    }

    private void judgeIsChange() {
        if ((isVertical&&Math.abs(moveDistance)>imageHeight/2)|(!isVertical&&Math.abs(moveDistance)>imageWidth/2)){
            if (relativePosition==-1){
                return;
            }
            ImagePiece img1=new ImagePiece();
            img1.setBitmap(list.get(currentDownPosition).getBitmap());
            img1.setPosition(list.get(currentDownPosition).getPosition());
            ImagePiece img2=new ImagePiece();
            img2.setBitmap(list.get(relativePosition).getBitmap());
            img2.setPosition(list.get(relativePosition).getPosition());
            list.remove(currentDownPosition);
            list.add(currentDownPosition,img2);
            list.remove(relativePosition);
            list.add(relativePosition,img1);
        }
    }

    /**
     * 手指滑动的方向，其中
     */
    private int moveDirection;
    public final int LEFT=63045;
    public final int RIGHT=63044;
    public final int TOP=63043;
    public final int BOTTOM=63042;



    private void moveCurrentImage(float x, float y, float moveX, float moveY) {
        if (Math.max(Math.abs(moveX-x),Math.abs(moveY-y))<80f||isWin)
            return;
        isVertical=Math.max(Math.abs(moveX-x),Math.abs(moveY-y))==Math.abs(moveY-y);
        if (isVertical){
            moveDirection=(moveY-y)>0?BOTTOM:TOP;
            moveDistance=Math.abs(moveY-y)>imageHeight?imageHeight:(moveY-y);
            if (moveDistance==imageHeight&&moveY-y<0)
                moveDistance=-imageHeight;
        }else {
            moveDirection=(moveX-x)>0?RIGHT:LEFT;
            moveDistance=(Math.abs(moveX-x)>imageWidth)?imageWidth:(moveX-x);
            if (moveDistance==imageWidth&&(moveX-x)<0)
                moveDistance=-imageWidth;
        }
        invalidate();
    }




}
