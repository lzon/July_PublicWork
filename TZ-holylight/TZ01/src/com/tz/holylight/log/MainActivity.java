package com.tz.holylight.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   public void getlog(View v){
	  StringBuffer sb= getLogFromProcess();
	   writeLogtoSDCard(sb);
   }
   
   public void call(View v){
	   Intent intent=new Intent();
	   intent.setAction(Intent.ACTION_CALL);
	   intent.setData(Uri.parse("tel:"+ "10086"));
	   startActivity(intent);
   }
   public void sendsms(View v){
	   Uri smsToUri = Uri.parse("smsto:10086");   
	   Intent intent=new Intent(Intent.ACTION_SENDTO,smsToUri);
	  intent.putExtra("sms_body", "��������...");
	   startActivity(intent);
   }

private void writeLogtoSDCard(StringBuffer sb) {
	String path=Environment.getExternalStorageDirectory().getAbsolutePath();
	    File file=new File(path+"/logcat.log");
	    FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			 fos.write(sb.toString().getBytes("utf-8"));
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

private StringBuffer getLogFromProcess() {
	ArrayList<String> cmdline=new ArrayList<String>();
	   cmdline.add("logcat");
	   cmdline.add("-v");
	   cmdline.add("-s");
	   cmdline.add("MY_INFO");
	   StringBuffer sb=new StringBuffer();
	   String[] strs=new String[cmdline.size()];
	   try {
		Process process=Runtime.getRuntime().exec(cmdline.toArray(strs));
		InputStream is=process.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String log=null;
		
	    while((log=br.readLine())!=null){
	    	sb.append(log);
	    	sb.append("\n");
	    }
	   
	  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return sb;
}
}
