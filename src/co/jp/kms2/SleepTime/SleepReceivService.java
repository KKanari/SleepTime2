package co.jp.kms2.SleepTime;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;

public class SleepReceivService extends Service{

	//AlarmManagerのintent
	private PendingIntent alarmSender;

	//設定保存ファイル
	private SettingSave save;

	//sleepOFF時間設定値
	private int Off_Time1;
	private int Off_Time2;
	private int Off_Time3;

	//OFFtime変更時間設定値
	private int Change_Time1;
	private int Change_Time2;

	//SleepWifi on/off設定値
	private int Wifi_Flag;

	//経過時間
	private int Start_time;
	//経過時間リセット用変数
	private final static int Start_time_Reset = 0;

	//sleepON経過時間
	private int Sleep_Flag;
	//sleepON経過時間リセット用変数
	private final static int Sleep_Flag_Reset = 0;

	//ステータスバー表示用のID
	private final static int NOTIFICATION_ID = 0;
	//秒
	private final static int SecondTime = 1000;
	//分
	private final static int MinuteTime = 60;
	//時間
	private final static int HourTime = 60;
	//アラームマネージャー実行時間
	private final static int AlarmTime = HourTime * MinuteTime * SecondTime;
	//バッテリー取得値
	final static int BatteryGet = 0;
	//スタートから24時間経過しているかを比較するための変数
	final static int End24 = 24;
	//画面ON時間
	//static int OnTime = 0;
	static int memo = 1024;



	Context c ;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


	public void onCreate(){
		super.onCreate();

		//contextのインスタンス取得
		c =  SleepReceivService.this.getApplicationContext();

		//スリープONのインテントフィルター設定
		c.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		//追加
		c.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));

	}
	 public int onStartCommand(Intent intent, int flags, int startId){
		  onStart(intent, startId);
		  return 1;
		 }
	public void onStart(Intent intent,int StartId){

		System.out.println("起動");
		System.out.println("アプリメモリ");
		// アプリのメモリ情報を取得
		Runtime runtime = Runtime.getRuntime();
		// トータルメモリ
		System.out.println("totalMemory[KB] = " + (int)(runtime.totalMemory()/memo));
		// 空きメモリ
		System.out.println("freeMemory[KB] = " + (int)(runtime.freeMemory()/memo));
		// 現在使用しているメモリ
		System.out.println("usedMemory[KB] = " + (int)( (runtime.totalMemory() - runtime.freeMemory())/memo) );
		// Dalvikで使用できる最大メモリ
		System.out.println("maxMemory[KB] = " + (int)(runtime.maxMemory()/memo));

		ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		System.out.println("システムメモリ");
		// システムの利用可能な空きメモリ
		System.out.println("memoryInfo.availMem[MB] = " + (int)(memoryInfo.availMem/memo/memo));
		// 低メモリ(LowMemory)状態の閾値
		System.out.println("memoryInfo.threshold[MB] = " + (int)(memoryInfo.threshold/memo/memo));
		// 低メモリ状態を示すフラグ(trueでメモリ不足状態)
		System.out.println("memoryInfo.lowMemory = " + memoryInfo.lowMemory);

		//メモリここまで。



		//設定保存ファイル読み込み
		save = new SettingSave();
		save.input_save();

		//設定値取得
		Off_Time1 = save.Off_Time1_get();
		Off_Time2 = save.Off_Time2_get();
		Off_Time3 = save.Off_Time3_get();
		Change_Time1 = save.Change_Time1_get();
		Change_Time2 = save.Change_Time2_get();
		Wifi_Flag = save.Wifi_Flag_get();
		Start_time = save.Start_Time_get();
		Sleep_Flag = save.Sleep_Flag_get();

		System.out.println(Off_Time1+":"+Off_Time2+":"
				+Off_Time3+":"+Change_Time1+":"+Change_Time2+":"+Start_time+":"+Sleep_Flag);

		startNotification();

		//sleepOFF時間1を使用するか比較
		if(Start_time < Change_Time1 ){

			Sleep_Flag_Check(Off_Time1);

		//sleepOFF時間2を使う時間になっているか比較
		}else if(Start_time >= Change_Time1&&Start_time < Change_Time2){

			Sleep_Flag_Check(Off_Time2);

		//sleepOFF時間3を使う時間になっているか比較
		}else if(Start_time >= Change_Time2&&Start_time < End24){

			Sleep_Flag_Check(Off_Time3);

		//バックグランド処理開始から24時間経っているか比較
		}else if(Start_time >= End24){

			Start_time = Start_time_Reset;
			Sleep_Flag_Check(Off_Time1);

		}

	}
	public void startNotification(){

		//ステータスバーにアイコン表示

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher, getString(R.string.app_name), System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SleepTimeActivity.class ), Intent.FLAG_ACTIVITY_NEW_TASK);

		notification.setLatestEventInfo(this, getString(R.string.app_name), "実行中", contentIntent);

		notificationManager.notify(NOTIFICATION_ID, notification);

	}

	public void Sleep_Flag_Check(int OffTime){

		//sleep経過時間がsleepOFF時間経過していれば画面をONにする
		if(Sleep_Flag >= OffTime){

			Sleep_Flag = Sleep_Flag_Reset;
			Wake_Up();


		}
		//まだ画面変更のタイミングに達していない場合
		if(Sleep_Flag > 0 & Sleep_Flag < OffTime){

			//AlarmManager設定処理呼び出し
			Thread thr = new Thread(null, task, "AlarmService_Service");
	        thr.start();

		}

	}

	BroadcastReceiver mReceiver = new BroadcastReceiver(){

		//SleepON取得処理

		@Override
		public synchronized void onReceive(Context context, Intent intent) {
			// TODO 自動生成されたメソッド・スタブ

			//Sleep状態になった場合
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

				int level = BatteryGet();

				//保存String作成クラスに値を送る
				SetString setstring = new SetString("OFF　　 " , level);

				//完成したStringを取得
				String SetStr = setstring.getString();

				//データベース作成
				TimeDB db = new TimeDB(context);

				//データベース保存
				db.InsertTime(SetStr);

				System.out.println("画面OFFキャッチ");

				//AlarmManager設定処理呼び出し
				Thread thr = new Thread(null, task, "AlarmService_Service");
		        thr.start();
		        //追加
			}else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
				System.out.println("画面ONキャッチ");
			}
		}

	};

	public int BatteryGet(){

		//バッテリー残量取得メソッド

		Intent bat = SleepReceivService.this.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

		return bat.getIntExtra("level", BatteryGet);

	}

	 public void Wake_Up(){

		 //画面ON処理メソッド

		 //画面をONにする
		 PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
			PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP
					| PowerManager.ON_AFTER_RELEASE, "my tag");

			wakelock.acquire();
			System.out.println("画面 ON");



			int level = BatteryGet();

			//データベース保存用String作成
			SetString setstring = new SetString("ON　　  " , level);
			String SetStr = setstring.getString();

			//データベースにデータベース保存用Stringを保存
			TimeDB db = new TimeDB(SleepReceivService.this);
			db.InsertTime(SetStr);
			//OnTime = 15000;
			//try{
			//Thread.sleep(OnTime);
			//}catch(Exception e){}
			wakelock.release();

			System.out.println("画面 ロック解除");

	}
	 private Runnable task = new Runnable() {
	        @Override
	        public void run() {



	        	// 次回起動登録
	   		 	long now = System.currentTimeMillis();

	   		 	//インテント設定
	   		 	Intent intent = new Intent(SleepReceivService.this, SleepReceivService.class);
	   		 	alarmSender = PendingIntent.getService(SleepReceivService.this, 0,intent, 0);

	   		 	//次回バックグラウンド処理の開始時間を設定
	   		 	AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	   		 	am.set(AlarmManager.RTC, (now + AlarmTime), alarmSender);


	   		 	//経過時間とsleep経過時間に一時間経過を加える
	   		 	++Start_time;
	   		 	++Sleep_Flag;

	   		 	//設定保存ファイルに保存するString作成
	   			String SetText = Off_Time1+","+ Off_Time2+","+ Off_Time3+","+ Change_Time1+","
	   						   + Change_Time2 +","+ Wifi_Flag +","+Start_time+","+Sleep_Flag;
	   			//設定保存
	   			save.output_save(SetText);


	   			System.out.println("終了前");
	   			System.out.println("アプリメモリ");
	   		    // アプリのメモリ情報を取得
	   			Runtime runtime = Runtime.getRuntime();
	   			// トータルメモリ
	   			System.out.println("totalMemory[KB] = " + (int)(runtime.totalMemory()/memo));
	   			// 空きメモリ
	   			System.out.println("freeMemory[KB] = " + (int)(runtime.freeMemory()/memo));
	   			// 現在使用しているメモリ
	   			System.out.println("usedMemory[KB] = " + (int)( (runtime.totalMemory() - runtime.freeMemory())/memo) );
	   			// Dalvikで使用できる最大メモリ
	   			System.out.println("maxMemory[KB] = " + (int)(runtime.maxMemory()/memo));

	   			ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
	   			ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
	   			activityManager.getMemoryInfo(memoryInfo);
	   			System.out.println("システムメモリ");
	   			// システムの利用可能な空きメモリ
	   			System.out.println("memoryInfo.availMem[MB] = " + (int)(memoryInfo.availMem/memo/memo));
	   			// 低メモリ(LowMemory)状態の閾値
	   			System.out.println("memoryInfo.threshold[MB] = " + (int)(memoryInfo.threshold/memo/memo));
	   			// 低メモリ状態を示すフラグ(trueでメモリ不足状態)
	   			System.out.println("memoryInfo.lowMemory = " + memoryInfo.lowMemory);
	   			//メモリここまで。



	   			// サービス終了
	   			SleepReceivService.this.stopSelf();
	        }
	    };



	 public void onDestroy() {
		super.onDestroy();
		//サービス終了時処理

		//ステータスバーアイコン終了
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
  		notificationManager.cancel(NOTIFICATION_ID);

  		//SleepONインテントフィルターの解放
		c.unregisterReceiver(mReceiver);

		System.out.println("終了");

	}
}
