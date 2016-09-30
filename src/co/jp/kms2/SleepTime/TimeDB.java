package co.jp.kms2.SleepTime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class TimeDB extends SQLiteOpenHelper{

	public TimeDB(Context context){

			//データベース作成
			super(context, "SleepTimeDB", null , 1);

	}
	public void onCreate(SQLiteDatabase db){

			//テーブル、カラム作成
			db.execSQL("create table SleepDB(DispStr text not null)");

	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

	}

	public void InsertTime(String str){

		//渡されたStringをデータベースへ保存

		SQLiteDatabase db = getWritableDatabase();

		db.execSQL("insert into SleepDB(DispStr)values('"+str+"');");

		db.close();
	}

}
