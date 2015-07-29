package com.tz.auto;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

import com.tz.contactadapter.ContactAdapter;
import com.tz.remind.Contact;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		final AutoCompleteTextView autoTip=(AutoCompleteTextView) this.findViewById(R.id.autoTip);
		autoTip.setThreshold(1);
		
		//初始化数据
		ArrayList<Contact> Contacts=Contact.generateContact();	
		
		//自定义adapter
		ContactAdapter adapter=new ContactAdapter(this,Contacts);
		autoTip.setAdapter(adapter);		
		
		//设置点击事件获取文本		
		autoTip.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Contact contact=(Contact) parent.getItemAtPosition(position);
				autoTip.setText(contact.getPhoneNum());
	
			}
		});

	}
}
