package com.tz.fileexplorer.util;

import android.graphics.Color;

public class ColorUtil {

	/**
	 * ����һ�����ɫ
	 * @return ��ɫֵ
	 */
	public static int generateRandomColor(){
		//����ڡ�����ɫ��Ӧ����ֵ 
		long black = Long.parseLong("ff000000", 16);
		//�����ɫ��Ӧ������
		long white = Long.parseLong("ffffffff", 16);
		long colorLong =(long) (Math.random()*(black-white+1)+white);
		String colorhex="#"+Long.toHexString(colorLong);
		return Color.parseColor(colorhex);
	}
	
}