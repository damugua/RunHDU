package com.cxsj.runhdu.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.cxsj.runhdu.constant.Types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sail on 2017/3/15 0030.
 * 工具类集合
 */
public class Utility {
    /**
     * 获取手机IMEI
     *
     * @param context
     * @return IMEI
     */
    public static String getDeviceId(Context context) {
        String Imei = "NULL";
        try {
            Imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            Toast.makeText(context, "获取IMEI码失败", Toast.LENGTH_SHORT);
            Imei = "NULL";
        }
        return Imei;
    }


    /**
     * GPS是否打开
     *
     * @param context
     * @return GPS开启状态
     */
    public static boolean isGPSOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return result;
    }

    /**
     * 位置集合里每连续两点距离的总和
     *
     * @param points 位置集合
     * @return 距离总和
     */
    public static int getRunningDistance(List<LatLng> points) {
        if (points.size() == 0 || points.size() == 1) return 0;
        double result = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            result += DistanceUtil.getDistance(points.get(i), points.get(i + 1));
        }
        return (int) result;
    }


    /**
     * 是否有网络
     *
     * @param context
     * @return 网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param d 需要处理的数字
     * @param num 保留位数(只能是1或者2)
     * @return 保留num位小数后的字符串
     */
    public static String formatDecimal(double d, int num) {
        if (num == 2) {
            return String.format("%.2f", d);
        } else {
            return String.format("%.1f", d);
        }

    }

    /**
     * 获取AlertDialog.Builder实例。
     *
     * @param context
     * @param title
     * @param message
     * @return
     */
    public static AlertDialog.Builder getDialogBuilder(Context context, String title, String message) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle(title);
        b.setMessage(message);
        b.setCancelable(false);
        return b;
    }

    /**
     * @param type 获取的时间类型，
     *             Types.TYPE_AM_PM：返回上午/下午/晚上
     *             Types.TYPE_MONTH_DATE：返回日期，例如2月15日
     *             Types.TYPE_CURRENT_TIME：返回时间，例如18：23
     *             其他类型：Calendar.TYPE
     * @return 类型字符串
     */
    public static String getTime(int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (type == Types.TYPE_AM_PM) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour >= 6 && hour < 9) {
                return "早上";
            } else if (hour >= 9 && hour < 12) {
                return "上午";
            } else if (hour >= 12 && hour < 13) {
                return "中午";
            } else if (hour >= 13 && hour < 19) {
                return "下午";
            } else {
                return "晚上";
            }
        } else if (type == Types.TYPE_MONTH_DATE) {
            return new SimpleDateFormat("M月d日", Locale.CHINA).format(new Date());
        } else if (type == Types.TYPE_CURRENT_TIME) {
            return new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date());
        } else {
            return String.valueOf(calendar.get(type));
        }
    }

    /**
     * 返回集合里是否全部为同一个数据。
     * @param list 检测的list
     * @param value 值
     * @param <T> 集合泛型
     * @return 是否为同一个数据。
     */
    public static <T> boolean isAllSame(List<T> list, T value) {
        if(list.isEmpty()) return false;
        for (T t : list) {
            if (t != value) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回两个时间差
     * @param oldTime 较早的时间
     * @param newTime 新的时间
     * @return 两个时间间隔的分钟数
     */
    public static int getTimeDiff(String oldTime, String newTime) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.CHINA);

        Date now = null;
        Date past = null;
        try {
            now = df.parse(newTime);
            past = df.parse(oldTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        long l = now.getTime() - past.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        return (int) min;
    }
}
