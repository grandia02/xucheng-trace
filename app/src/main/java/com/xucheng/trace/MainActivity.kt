package com.xucheng.trace
import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.widget.*
class MainActivity:Activity(){
 private lateinit var output:TextView
 private lateinit var db:TraceDb
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);db=TraceDb(this);val layout=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(32,40,32,24)}
 val title=TextView(this).apply{text="序程·行迹 · 定位验证版";textSize=23f};layout.addView(title)
 fun btn(label:String,run:()->Unit){layout.addView(Button(this).apply{text=label;setOnClickListener{run()}})}
 btn("开始记录"){startCapture()};btn("停止记录"){startService(Intent(this,CaptureService::class.java).setAction(CaptureService.STOP))}
 btn("刷新记录"){refresh()}
 output=TextView(this).apply{textSize=14f};layout.addView(output)
 layout.addView(TextView(this).apply{text="仅用户主动启动定位；数据保存在本机，卸载后丢失。此版尚未经过荣耀手机实测。"})
 setContentView(ScrollView(this).apply{addView(layout)});refresh()}
 private fun hasLocation()=checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
 private fun startCapture(){if(!hasLocation()){requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),21);return};try{startForegroundService(Intent(this,CaptureService::class.java))}catch(e:Exception){Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()}}
 override fun onRequestPermissionsResult(requestCode:Int,permissions:Array<out String>,grantResults:IntArray){super.onRequestPermissionsResult(requestCode,permissions,grantResults);if(requestCode==21&&hasLocation())startCapture()}
 private fun refresh(){output.text=db.summary()}
 override fun onDestroy(){db.close();super.onDestroy()}
}