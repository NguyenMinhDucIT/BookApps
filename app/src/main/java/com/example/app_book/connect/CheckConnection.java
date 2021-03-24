package com.example.app_book.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckConnection {
    //https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    public static boolean haveNetworkConnection(Context context) {
        // mặc định khi kết nối đc wifi, mobile là false
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        // Lấy network info
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            // dùng vòng lặp for cho đến khi bắt đc các tín hiệu
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;  // có kết nối wifi
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true; // có kết nối với mobile
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public  static  void ShowToast_short(Context context,String thongbao){
        Toast.makeText(context,thongbao, Toast.LENGTH_SHORT).show();
    }
}
