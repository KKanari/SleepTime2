package co.jp.kms2.SleepTime;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

//端末起動時に処理されるクラス
public class StartupReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		//アクションを取得
		String action = intent.getAction();

		//取得したアクションが端末起動ONであるか確認
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {

			//バッテリーサービスを起動する
			Intent serviceIntent = new Intent(context.getApplicationContext() , BatteryService.class);
			context.startService(serviceIntent);
		}

	}

}
