package com.tz.fileexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.R.anim;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tz.fileexplorer.adapter.FileAdapter;
import com.tz.fileexplorer.bean.MyFile;
import com.tz.fileexplorer.util.FileUtil;
/**
 *�������ļ������ 
 *(�������ͼƬ)
 */
public class MainActivity extends Activity {

	private ListView lv;
	private String rootPath;
	private List<MyFile> list = new ArrayList<MyFile>();
	private FileAdapter adapter;

	private RelativeLayout main;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		main=(RelativeLayout) findViewById(R.id.main);
		lv = (ListView)findViewById(R.id.lv);
		//�ж�SD���Ƿ���������
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//�õ�sd����Ŀ¼   /storage/sdcard
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}else{
			Toast.makeText(this, "SD Error", 0).show();
		}
		
	    Log.i("1111", ""+new File(rootPath).getParentFile());
		initData(rootPath);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 
				MyFile myFile = (MyFile) adapter.getItem(position);
				final File file = new File(myFile.getPath());
				if(file.isDirectory()){
					if (file.getParentFile()==null) {
						Toast.makeText(MainActivity.this, "û����һ��Ŀ¼��"+file.getParentFile(), Toast.LENGTH_LONG).show();
					}else {
						initData(file.getAbsolutePath());
					}
				}
				else {
					FileUtil.openFile(MainActivity.this, file);	
				}
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				MyFile myFile = (MyFile) adapter.getItem(position);
				final File file = new File(myFile.getPath());
				View contentView=LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_detail, null);
				final PopupWindow popupWindow=new PopupWindow(contentView, 200, LayoutParams.WRAP_CONTENT, true);
				TextView delete=(TextView) contentView.findViewById(R.id.tv_delete);
				TextView detail=(TextView) contentView.findViewById(R.id.tv_detail);
				int[] location=new int[2];
				view.getLocationInWindow(location);	
				popupWindow.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
				popupWindow.showAtLocation(main, Gravity.LEFT|Gravity.LEFT, location[0]+20, location[1]);
				
				delete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Builder builder=new Builder(MainActivity.this).setIcon(R.drawable.ic_launcher);
						builder.setMessage("�Ƿ�ɾ����");
						builder.setTitle("��ʾ");
						builder.setPositiveButton("ɾ��", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (FileUtil.deleteFile(file)) {
									Toast.makeText(MainActivity.this, "ɾ���ɹ�", Toast.LENGTH_LONG).show();
									Log.i("1111", "" + file);
								}else {
									Toast.makeText(MainActivity.this, "ɾ��ʧ��", Toast.LENGTH_LONG).show();
								}
								dialog.dismiss();
								popupWindow.dismiss();
							}
						});
						builder.setNegativeButton("ȡ��", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
		                         dialog.dismiss();	
		                     	 popupWindow.dismiss();
							}
						});
						builder.create().show();
					}
				});
				detail.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
						String path=file.getAbsolutePath();
//						long lL=file.getFreeSpace();
						View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_item, null);
						TextView textView=(TextView) view.findViewById(R.id.tv_detail);
						textView.setText("·���ǣ�"+path+"\n"+"�ռ��СΪ��");
					    PopupWindow	popupWindow2=new PopupWindow(view, 400, 400);
						popupWindow2.setBackgroundDrawable(new ColorDrawable(Color.GREEN));
						popupWindow2.showAtLocation(main, Gravity.TOP, 10, 50);
					}
				});
				return false;
			}
		});
		
	}

	private void initData(String path) {
		list.clear();
		//����SD��
		File file = new File(path);
		File[] listFiles = file.listFiles();

		MyFile file_back = new MyFile(
				"����",
				BitmapFactory.decodeResource(getResources(), R.drawable.dirs), 
				file.getParentFile().getAbsolutePath(),
				false);
		list.add(file_back);
		for (File f : listFiles) {
			MyFile myFile = new MyFile();
			myFile.setName(f.getName());
			myFile.setPath(f.getAbsolutePath());
			if(f.isDirectory()){//���ļ���
				myFile.setIcon(false);
				myFile.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.dirs));
			}else{
				//�ļ�
				//ͼƬ
				if(f.getName().toLowerCase().endsWith(".png")||f.getName().toLowerCase().endsWith(".jpg")||f.getName().toLowerCase().endsWith(".jpeg")){
					myFile.setIcon(true);
					myFile.setIcon(null);
//					myFile.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.file));
				}else{//�����ļ�
					myFile.setIcon(false);
					myFile.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.file));
				}
			}
			list.add(myFile);
		}
		adapter = new FileAdapter(list, this);
		lv.setAdapter(adapter);
	}
}
