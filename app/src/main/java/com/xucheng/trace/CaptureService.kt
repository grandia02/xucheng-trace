package com.xucheng.trace
import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.location.*
import android.os.*
class CaptureService: Service(), LocationListener {
 companion object { const val STOP="com.xucheng.trace.STOP"; const val CHANNEL="trace_capture" }
 private lateinit var manager:LocationManager
 private lateinit var db:TraceDb
 override fun onCreate(){super.onCreate();manager=getSystemService(LOCATION_SERVICE) as LocationManager;db=TraceDb(this); val ch=NotificationChannel(CHANNEL,"位置记录",NotificationManager.IMPORTANCE_LOW);(getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(ch)}
 override fun onStartCommand(intent:Intent?,flags:Int,startId:Int):Int {
  if(intent?.action==STOP){stopSelf();return START_NOT_STICKY}
  val notification=Notification.Builder(this,CHANNEL).setSmallIcon(android.R.drawable.ic_menu_mylocation).setContentTitle("序程·行迹正在记录位置").setContentText("点击应用停止采集").setContentIntent(PendingIntent.getActivity(this,0,Intent(this,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE)).build()
  try {startForeground(11,notification); if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000L,50f,this)
   if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED && manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60000L,50f,this)
  } catch(e:Exception){stopSelf();return START_NOT_STICKY}
  return START_STICKY
 }
 override fun onLocationChanged(location:Location){db.add(location)}
 override fun onBind(intent:Intent?):IBinder?=null
 override fun onDestroy(){manager.removeUpdates(this);db.close();super.onDestroy()}
}