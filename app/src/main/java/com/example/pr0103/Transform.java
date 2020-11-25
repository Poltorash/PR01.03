package com.example.pr0103;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Vibrator;

import static com.example.pr0103.UserStaticInfo.LOGIN;
import static com.example.pr0103.UserStaticInfo.PASSWORD;

public class Transform {

    //это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";

    public static boolean StringNoNull(String string){
        if(string==null)
            return false;
        else return string.length() != 0;
    }

    public static void Vibrate(Context context){
        //секунда
        long mills = 1000L;
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //проверка возможности вибрации
        if(vibrator.hasVibrator()){
            vibrator.vibrate(mills);
        }
    }

    public static int parseIntOrDefault(String whatParse, int defaultValue){
        int parse;
        //проверяем на возможность перевода
        try{
            parse = Integer.parseInt(whatParse);
        }
        catch (Exception NumberFormatException){
            parse = defaultValue;
        }
        return parse;
    }

    public static Bitmap getRoundedCornerCubeBitmap(Bitmap bitmap,float radiusInPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, radiusInPx, radiusInPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void SaveUser(SharedPreferences sp,String login,String password){
        SharedPreferences.Editor e = sp.edit();
        e.putString(LOGIN, login);
        e.putString(PASSWORD, password);
        e.apply();//подтверждение изменений
    }

    public static Bitmap getRoundedMapBitmap(Bitmap bitmap){
        int iconSize = 100;
        Bitmap output = Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false);
        return getRoundedCornerCubeBitmap(output, iconSize);
    }
}
