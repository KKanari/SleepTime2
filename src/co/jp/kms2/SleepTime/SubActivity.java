package co.jp.kms2.SleepTime;

import co.jp.kms2.SleepTime.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity extends Activity{

	TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sleep_time);

        //表示用レイアウトインスタンス取得
        LinearLayout sv = (LinearLayout)findViewById(R.id.View);


        try{
        	//データベースクラスインスタンス作成
        	TimeDB time = new TimeDB(this);

        	SQLiteDatabase db = time.getWritableDatabase();

        	//データ取得
        	Cursor c = db.rawQuery("select DispStr from SleepDB", null);

        	//カーソルを先頭に持ってくる
        	boolean isEof  = c.moveToFirst();

        	//カーソルにデータがあるか
        	while(isEof){

        		//テキストビュー作成
        		tv = new TextView(this);
        		//Stringをテキストビューに設定
        		tv.setText(c.getString(0));
        		//文字サイズ設定
        		tv.setTextSize(20);
        		//フォントを等幅フォントに変更する
        		tv.setTypeface(Typeface.MONOSPACE);
        		//テキストビューをレイアウトに反映
        		sv.addView(tv);
        		//カーソルを次へ
        		isEof = c.moveToNext();

        	}
        	c.close();
        	db.close();

        }catch(Exception e){
        	//エラー表示
        	AlertDialog.Builder dlg;
        	dlg = new AlertDialog.Builder(this);
        	dlg.setMessage("データを取得できませんでした : " + e.getMessage());
        	dlg.setPositiveButton("OK", null);
        	dlg.show();
        	finish();

        }

        //戻るボタンクリック処理
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				Back_Activity();
			}
		});

    }
    public void Back_Activity(){
    	//アクティビティを終了
    	finish();

    }
}
