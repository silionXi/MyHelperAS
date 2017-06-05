package com.lee.my.helper.adapter;

import android.graphics.drawable.Drawable;

public class ApplicationItem {

	public Drawable icon;
	public String packageName;
	public String appName;
	public String sourceDir;
	public String apkPath;
	public String remark;
	public String version;
	public boolean checked;
	public boolean same;
	public int flags;

	public ApplicationItem(Drawable icon, String packageName, String appName,
			String sourceDir) {
		this.icon = icon;
		this.packageName = packageName;
		this.appName = appName;
		this.sourceDir = sourceDir;
	}

}
