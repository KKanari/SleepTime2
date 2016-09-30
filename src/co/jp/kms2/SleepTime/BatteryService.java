package co.jp.kms2.SleepTime;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BatteryService extends Service{

	final static int BatteryGet = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public void onCreate(){
		super.onCreate();

		//バッテリー残量取得
		Intent bat = this.registerReceiver(null,
               new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = bat.getIntExtra("level", BatteryGet);

		//データベース保存用String作成
		SetString setstring = new SetString("起動ON  " , level);
		String SetStr = setstring.getString();

		//データベース保存用Stringデータベース保存
		TimeDB db = new TimeDB(this);
		db.InsertTime(SetStr);

		//バックグラウンド処理開始
		Intent serviceIntent = new Intent(this , SleepReceivService.class);
		startService(serviceIntent);

		this.stopSelf();

	}

}
