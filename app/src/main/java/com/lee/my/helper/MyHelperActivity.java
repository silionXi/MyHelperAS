package com.lee.my.helper;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.lee.my.helper.adapter.ApplicationItem;
import com.lee.my.helper.tools.Tools;

public class MyHelperActivity extends TabActivity {

	protected static final String backupPath = "/sdcard/Android/backup";
	public static ArrayList<ApplicationItem> installApps = new ArrayList<ApplicationItem>();
	public static ArrayList<ApplicationItem> restoreApps = new ArrayList<ApplicationItem>();
	public static float block;
	public static float totalBlocks;
	public static float availaBlock;
	private static MyHelperActivity mainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getDecorView().setKeepScreenOn(true);
		mainActivity = this;
		TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost
				.newTabSpec("run")
				.setIndicator("清理",
						getResources().getDrawable(R.drawable.cache))
				.setContent(new Intent(this, RunAppActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("user")
				.setIndicator("用户", getResources().getDrawable(R.drawable.user))
				.setContent(new Intent(this, UsersActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("system")
				.setIndicator("系统",
						getResources().getDrawable(R.drawable.system))
				.setContent(new Intent(this, SystemActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("backup")
				.setIndicator("备份",
						getResources().getDrawable(R.drawable.backup))
				.setContent(new Intent(this, BackUpActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("restore")
				.setIndicator("还原",
						getResources().getDrawable(R.drawable.restore))
				.setContent(new Intent(this, RestoreActivity.class)));
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(1);
			}
		});
		Tools.runRootCommand("");// 取得ROOT权限
		init();
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (BackUpActivity.adapter != null)
					BackUpActivity.adapter.notifyDataSetChanged();
				if (RestoreActivity.adapter != null)
					RestoreActivity.adapter.notifyDataSetChanged();
				break;
			case 2:
				if (dialog != null)
					dialog.dismiss();
				break;
			}
		}

	};

	public static void flush() {
		installApps.clear();
		restoreApps.clear();
		mainActivity.getInstalledApps(false);
		mainActivity.getRestoreApps();
		mainActivity.getSDInfo();
		mainActivity.process();
	}

	protected ProgressDialog dialog;

	public void init() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("初始化中，请稍后……");
		dialog.show();
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				installApps.clear();
				restoreApps.clear();
				getInstalledApps(false);
				getRestoreApps();
				getSDInfo();
				process();
			}

		}.start();
	}
	
	public static void flushAppList(){
		for (int i = 0; i < installApps.size(); i++) {
			ApplicationItem item = installApps.get(i);
			item.remark = "可备份";
			item.same = false;
		}
		for (int j = 0; j < restoreApps.size(); j++) {
			ApplicationItem item = restoreApps.get(j);
			item.remark = "可还原";
			item.same = false;
		}

		for (int i = 0; i < installApps.size(); i++) {
			ApplicationItem item = installApps.get(i);
			for (int j = 0; j < restoreApps.size(); j++) {
				ApplicationItem item2 = restoreApps.get(j);
				if (item.appName.equals(item2.appName)) {
					item.same = true;
					item2.same = true;
					item.remark = "已备份";
					item2.remark = "已安装";
					break;
				}
			}
		}
		if (BackUpActivity.adapter != null)
			BackUpActivity.adapter.notifyDataSetChanged();
		if (RestoreActivity.adapter != null)
			RestoreActivity.adapter.notifyDataSetChanged();
	}

	public void process() {
		for (int i = 0; i < installApps.size(); i++) {
			ApplicationItem item = installApps.get(i);
			item.remark = "可备份";
			item.same = false;
		}
		for (int j = 0; j < restoreApps.size(); j++) {
			ApplicationItem item = restoreApps.get(j);
			item.remark = "可还原";
			item.same = false;
		}

		for (int i = 0; i < installApps.size(); i++) {
			ApplicationItem item = installApps.get(i);
			for (int j = 0; j < restoreApps.size(); j++) {
				ApplicationItem item2 = restoreApps.get(j);
				if (item.appName.equals(item2.appName)) {
					item.same = true;
					item2.same = true;
					item.remark = "已备份";
					item2.remark = "已安装";
					break;
				}
			}
		}
		handler.sendEmptyMessage(2);
	}

	public void getSDInfo() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File path = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(path.getPath());
			block = statfs.getBlockSize();
			totalBlocks = statfs.getBlockCount();
			availaBlock = statfs.getAvailableBlocks();
		}
	}

	public void getApkFileInfo(String apkPath) {
		File apkFile = new File(apkPath);
		if (!apkFile.exists() || !apkPath.toLowerCase().endsWith(".apk")) {
			System.out.println("文件路径不正确");
		}
		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		try {
			// 反射得到pkgParserCls对象并实例化,有参数
			Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
			Class<?>[] typeArgs = { String.class };
			Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = { apkPath };
			Object pkgParser = pkgParserCt.newInstance(valueArgs);

			// 从pkgParserCls类得到parsePackage方法
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();// 这个是与显示有关的, 这边使用默认
			typeArgs = new Class<?>[] { File.class, String.class,
					DisplayMetrics.class, int.class };
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);
			valueArgs = new Object[] { new File(apkPath), apkPath, metrics, 0 };

			// 执行pkgParser_parsePackageMtd方法并返回
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);

			// 从返回的对象得到名为"applicationInfo"的字段对象
			if (pkgParserPkg == null) {
				return;
			}
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");

			// 从对象"pkgParserPkg"得到字段"appInfoFld"的值
			if (appInfoFld.get(pkgParserPkg) == null) {
				return;
			}
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);

			// 反射得到assetMagCls对象并实例化,无参
			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Object assetMag = assetMagCls.newInstance();
			// 从assetMagCls类得到addAssetPath方法
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			// 执行assetMag_addAssetPathMtd方法
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

			// 得到Resources对象并实例化,有参数
			Resources res = getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<Resources> resCt = Resources.class
					.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = (Resources) resCt.newInstance(valueArgs);

			ApplicationItem item = null;
			// 读取apk文件的信息
			if (info != null) {
				Drawable icon = res.getDrawable(info.icon);
				String packageName = info.packageName;
				String appName = null;
				if (info.labelRes != 0)
					appName = res.getText(info.labelRes).toString();// 名字
				else
					appName = info.loadLabel(getPackageManager()).toString();
				String sourceDir = info.sourceDir;
				item = new ApplicationItem(icon, packageName, appName,
						sourceDir);
				PackageManager pm = getPackageManager();
				PackageInfo pinfo = pm.getPackageArchiveInfo(apkPath,
						PackageManager.GET_ACTIVITIES);
				item.apkPath = apkPath;
				item.version = pinfo.versionName;
				restoreApps.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void getRestoreApps() {
		File file = new File(backupPath);
		if (file.exists()) {
			String[] files = file.list();
			for (int i = 0; files != null && i < files.length; i++) {
				getApkFileInfo(backupPath + "/" + files[i]);
			}
		}
	}

	protected void getInstalledApps(boolean getSysPackages) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			List<PackageInfo> packs = getPackageManager().getInstalledPackages(
					0);
			for (int i = 0; i < packs.size(); i++) {
				PackageInfo p = packs.get(i);
				ApplicationInfo info = p.applicationInfo;
				if (!getSysPackages && info.sourceDir.startsWith("/system"))
					continue;
				Drawable icon = info.loadIcon(getPackageManager());
				String packageName = info.packageName;
				String appName = info.loadLabel(getPackageManager()).toString();
				String sourceDir = info.sourceDir;
				ApplicationItem item = new ApplicationItem(icon, packageName,
						appName, sourceDir);
				item.version = p.versionName;
				installApps.add(item);
			}
		} else if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_REMOVED)) {
			Toast.makeText(this, "没有sdCard", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		System.exit(0);
	}

}
