package com.lee.my.helper.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class AppItem {

	public String appName;
	public Drawable icon;
	public String packageName;
	public String name;
	public boolean checked;
	public String remark;

	public AppItem(Context context, boolean checked, String pkn, String name) {
		super();
		// TODO Auto-generated constructor stub
		this.checked = checked;
		packageName = pkn;
		this.name = name;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageinfo = pm.getPackageInfo(packageName, 0);
			ApplicationInfo applicationinfo = packageinfo.applicationInfo;
			String loadLabel = applicationinfo.loadLabel(pm).toString();
			appName = loadLabel;
			ApplicationInfo applicationInfo = packageinfo.applicationInfo;
			Drawable loadIcon = applicationInfo.loadIcon(pm);
			icon = loadIcon;
			if (appName == null) {
				appName = "δ֪";
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}