package co.jp.kms2.SleepTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.AlertDialog;


public class SettingSave {

	//ファイルパス
	private String save_file_path = "/data/data/co.jp.kms2.SleepTime/setting.txt";

	//スプリット用String
	private String splitter = ",";

	//sleepOFF時間設定値
	private int Off_Time1 = 1 ;
	private int Off_Time2 = 2 ;
	private int Off_Time3 = 8 ;

	//OFFtime変更時間
	private int Change_Time1 = 10 ;
	private int Change_Time2 = 16 ;

	//SleepWifi on/off
	private int Wifi_Flag = 1 ;

	//経過時間
	private int Start_time = 0;

	//SleepON経過時間
	private int Sleep_Flag = 0;

	//スプリット時の格納場所番号
	final static int splitted0 = 0;
	final static int splitted1 = 1;
	final static int splitted2 = 2;
	final static int splitted3 = 3;
	final static int splitted4 = 4;
	final static int splitted5 = 5;
	final static int splitted6 = 6;
	final static int splitted7 = 7;

	//ファイル作成処理
	public String create_file(){

		//エラーメッセージ保存用String
		String create_error = "";


		File file = new File(save_file_path);

		//ファイルがまだ作られていない場合にファイル作成
		if(!file.exists()){
			try{

				file.createNewFile();

			}catch(Exception e){

				create_error = "ファイル作成に失敗しました : " + e.getMessage();


			}

		}
		return create_error;
	}

	//ファイル保存処理
	public String output_save(String SetText){

		String output_error = "";

		try {
			//受け取ったStringをファイルに保存する
			FileOutputStream fos = new FileOutputStream(save_file_path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write(SetText);

			bw.close();
			osw.close();
			fos.close();

		} catch (Exception e) {

			output_error = "設定の保存に失敗しました : " + e.getMessage();


		}

		return output_error;
	}
	public String input_save(){

		String input_error = "";

		File file = new File(save_file_path);

		//ファイルが存在するか確認
		if(!file.exists()){
			return input_error;
		}

		try{
			//ファイルからStringを取得
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr  = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String splittedLine[];
			String line = br.readLine();

			//Stringをスプリットで設定値ごとに分割
			splittedLine = line.split(splitter);

			//分割したStringを各設定値に格納
			Off_Time1 = Integer.parseInt(splittedLine[splitted0]);
			Off_Time2 = Integer.parseInt(splittedLine[splitted1]);
			Off_Time3 = Integer.parseInt(splittedLine[splitted2]);
			Change_Time1 = Integer.parseInt(splittedLine[splitted3]);
			Change_Time2 = Integer.parseInt(splittedLine[splitted4]);
			Wifi_Flag = Integer.parseInt(splittedLine[splitted5]);
			Start_time = Integer.parseInt(splittedLine[splitted6]);
			Sleep_Flag = Integer.parseInt(splittedLine[splitted7]);

		}catch(Exception e){

			input_error = "設定の取得に失敗しました" + e.getMessage();

		}

		return input_error;

	}

	public int Off_Time1_get(){

		return Off_Time1;

	}

	public int Off_Time2_get(){

		return Off_Time2;

	}

	public int Off_Time3_get(){

		return Off_Time3;

	}

	public int Change_Time1_get(){

		return Change_Time1;

	}

	public int Change_Time2_get(){

		return Change_Time2;

	}

	public int Wifi_Flag_get(){

		return Wifi_Flag;

	}

	public int Start_Time_get(){

		return Start_time;

	}

	public int Sleep_Flag_get(){

		return Sleep_Flag;

	}

}
