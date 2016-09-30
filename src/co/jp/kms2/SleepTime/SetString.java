package co.jp.kms2.SleepTime;

import java.util.Calendar;

public class SetString {

	//ON or OFF or 起動ONを格納する変数
	String StartDisp;

	//バッテリー残量を格納する変数
	String BatteryLevel;

	SetString(String Start , int Battery){

		StartDisp = Start;

		BatteryLevel = Integer.toString(Battery);

		//バッテリー残量が一桁だったらスペースを2つ追加
		if(BatteryLevel.length() == 1){

			BatteryLevel = "  " + BatteryLevel;

		//バッテリー残量が二桁だったらスペースを1つ追加
		}else if(BatteryLevel.length() == 2){

			BatteryLevel = " " + BatteryLevel;
		}

	}
	public String getString(){
		Calendar calendar = Calendar.getInstance();

		//月を取得
		String month = Integer.toString(calendar.get(Calendar.MONTH)+1);

		//月が一桁だったら先頭に0を追加
		if(month.length() == 1){
			month = "0" + month;
		}

		//日にちを取得
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

		//日にちが一桁だったら先頭に0を追加
		if(day.length() == 1){
			day = "0" + day;
		}

		//時間を取得
		String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));

		//時間が一桁だったら先頭に0を追加
		if(hour.length() == 1){
			hour = "0" + hour;
		}

		//分を取得
		String minute = Integer.toString(calendar.get(Calendar.MINUTE));

		//分が一桁だったら先頭に0を追加
		if(minute.length() == 1){
			minute = "0" + minute;
		}
		//設定保存ファイル読み込み
		SettingSave save = new SettingSave();
		save.input_save();

		//WiFi設定フラグ
		int Wifi_Flag = save.Wifi_Flag_get();


		return StartDisp+" "+month+"/"+day+" "+hour+":"+minute+" "+BatteryLevel+"%"+" "+Wifi_Flag;
	}

}
