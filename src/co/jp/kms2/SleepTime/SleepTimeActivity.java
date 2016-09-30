package co.jp.kms2.SleepTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SleepTimeActivity extends Activity {
	/**
	 * メイン画面処理
	 */

	//sleepOFF時間
	EditText OffTime1;
	EditText OffTime2;
	EditText OffTime3;

	//OFFtime変更時間
	EditText ChangeTime1;
	EditText ChangeTime2;

	//SleepWifi on/off
	EditText WifiFlag;

	//設定保存ファイルクラス
	SettingSave save ;

	Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sleeptime_set);

        //EditTextのインスタンス取得
        OffTime1 = (EditText)findViewById(R.id.OffTime1);
        OffTime2 = (EditText)findViewById(R.id.OffTime2);
        OffTime3 = (EditText)findViewById(R.id.OffTime3);
        ChangeTime1 = (EditText)findViewById(R.id.ChangeTime1);
        ChangeTime2 = (EditText)findViewById(R.id.ChangeTime2);
        WifiFlag = (EditText)findViewById(R.id.flag);
        intent = new Intent(this , SleepReceivService.class);

        TimeDB time = new TimeDB(this);

        //設定保存ファイルのインスタンス取得
        save = new SettingSave();

        //ボタンのインスタンス取得
        Button StartBt = (Button)findViewById(R.id.StartBt);
        Button DisplayBt = (Button)findViewById(R.id.DisplayBt);

        //startボタンクリック処理
        StartBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ

				//sleepOFF時間設定値取得
				String OffT1 = OffTime1.getText().toString();
				String OffT2 = OffTime2.getText().toString();
				String OffT3 = OffTime3.getText().toString();

				//OFFtime変更時間設定値取得
				String ChangeT1 = ChangeTime1.getText().toString();
				String ChangeT2 = ChangeTime2.getText().toString();

				//SleepWifi on/off設定値取得
				String WifiF = WifiFlag.getText().toString();

				//全項目が埋まっているかチェック
				if((OffT1.equals(""))||(OffT2.equals(""))||(OffT3.equals(""))
						||(ChangeT1.equals(""))||(ChangeT2.equals(""))||(WifiF.equals(""))){
					ToastSet("全ての項目を埋めてください");
					return;
				}

				int CT1 = Integer.parseInt(ChangeT1);
				int CT2 = Integer.parseInt(ChangeT2);

				//OFFtime変更時間が範囲内かのチェック
				if((CT1 == 0 || CT1 > 23)||(CT2 == 0 || CT2 > 23)){
					ToastSet("変更時間は1～23");
					return;
				}
				//OFFtime変更時間1がOFFtime変更時間2以下かチェック
				if(CT1 >= CT2){
					ToastSet("変更時間1は変更時間2より小さくしてください");
					return;
				}

				//経過時間
				int StartTime = 0;

				//SleepON経過時間
				int SleepFlag = 0;

				//設定保存用にString連結
				String SetText = OffT1+","+ OffT2+","+ OffT3+","+ CT1+","
						       + CT2 +","+ WifiF +","+StartTime+","+SleepFlag;

				//設定保存ファイル作成
				String create = save.create_file();
				if(!create.equals("")){
					ToastSetFile(create);
				}
				//設定値保存
				String out = save.output_save(SetText);
				if(!out.equals("")){
					ToastSetFile(out);
				}
				start_Service();

			}
		});

        //表示ボタンクリック処理
        DisplayBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				sub_activity();
			}
		});


    }

    //エラー表示処理
    public void ToastSet(String str){
    	AlertDialog.Builder dlg;
    	dlg = new AlertDialog.Builder(this);
    	dlg.setMessage(str);
    	dlg.setPositiveButton("OK", null);
    	dlg.show();


    }
    public void ToastSetFile(String str){
    	AlertDialog.Builder dlg;
    	dlg = new AlertDialog.Builder(this);
    	dlg.setMessage(str);
    	dlg.setPositiveButton("OK", null);
    	dlg.show();
    	finish();
    }

    //サブActivity移行処理
    public void sub_activity(){
    	//intent設定
    	Intent intent = new Intent(this , SubActivity.class);

    	//サブActivityスタート
    	startActivity(intent);

    }

    //バックグラウンド処理スタート処理
    public void start_Service(){

    	//サービスを終了
    	stopService(intent);

    	//サービスの開始
    	startService(intent);
    }
}
