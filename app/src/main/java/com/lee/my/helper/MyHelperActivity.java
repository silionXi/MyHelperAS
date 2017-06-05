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
				.setIndicator("����",
						getResources().getDrawable(R.drawable.cache))
				.setContent(new Intent(this, RunAppActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("user")
				.setIndicator("�û�", getResources().getDrawable(R.drawable.user))
				.setContent(new Intent(this, UsersActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("system")
				.setIndicator("ϵͳ",
						getResources().getDrawable(R.drawable.system))
				.setContent(new Intent(this, SystemActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("backup")
				.setIndicator("����",
						getResources().getDrawable(R.drawable.backup))
				.setContent(new Intent(this, BackUpActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("restore")
				.setIndicator("��ԭ",
						getResources().getDrawable(R.drawable.restore))
				.setContent(new Intent(this, RestoreActivity.class)));
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(1);
			}
		});
		Tools.runRootCommand("");// ȡ��ROOTȨ��
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
		dialog.setTitle("��ʾ");
		dialog.setMessage("��ʼ���У����Ժ󡭡�");
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
			item.remark = "�ɱ���";
			item.same = false;
		}
		for (int j = 0; j < restoreApps.size(); j++) {
			ApplicationItem item = restoreApps.get(j);
			item.remark = "�ɻ�ԭ";
			item.same = false;
		}

		for (int i = 0; i < installApps.size(); i++) {
			ApplicationItem item = installApps.get(i);
			for (int j = 0; j < restoreApps.size(); j++) {
				ApplicationItem item2 = restoreApps.get(j);
				if (item.appName.equals(item2.appName)) {
					item.same = true;
					item2.same = true;
					item.remark = "�ѱ���";
					item2.remark = "�Ѱ�װ";
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
			item.remark = "�ɱ���";
			item.same = false;
		}
		for (int j = 0; j < restoreApps.size(); j++) {
			ApplicationItem item = restoreApps.get(j);
			item.remark = "�ɻ�ԭ";
			item.same = false;
		}

		for (int i = 0; i < installApps.size(); i++) {
			ApplicationItem item = installApps.get(i);
			for (int j = 0; j < restoreApps.size(); j++) {
				ApplicationItem item2 = restoreApps.get(j);
				if (item.appName.equals(item2.appName)) {
					item.same = true;
					item2.same = true;
					item.remark = "�ѱ���";
					item2.remark = "�Ѱ�װ";
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
			System.out.println("�ļ�·������ȷ");
		}
		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		try {
			// ����õ�pkgParserCls����ʵ����,�в���
			Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
			Class<?>[] typeArgs = { String.class };
			Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = { apkPath };
			Object pkgParser = pkgParserCt.newInstance(valueArgs);

			// ��pkgParserCls��õ�parsePackage����
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();// ���������ʾ�йص�, ���ʹ��Ĭ��
			typeArgs = new Class<?>[] { File.class, String.class,
					DisplayMetrics.class, int.class };
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);
			valueArgs = new Object[] { new File(apkPath), apkPath, metrics, 0 };

			// ִ��pkgParser_parsePackageMtd����������
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);

			// �ӷ��صĶ���õ���Ϊ"applicationInfo"���ֶζ���
			if (pkgParserPkg == null) {
				return;
			}
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");

			// �Ӷ���"pkgParserPkg"�õ��ֶ�"appInfoFld"��ֵ
			if (appInfoFld.get(pkgParserPkg) == null) {
				return;
			}
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);

			// ����õ�assetMagCls����ʵ����,�޲�
			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Object assetMag = assetMagCls.newInstance();
			// ��assetMagCls��õ�addAssetPath����
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			// ִ��assetMag_addAssetPathMtd����
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

			// �õ�Resources����ʵ����,�в���
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
			// ��ȡapk�ļ�����Ϣ
			if (info != null) {
				Drawable icon = res.getDrawable(info.icon);
				String packageName = info.packageName;
				String appName = null;
				if (info.labelRes != 0)
					appName = res.getText(info.labelRes).toString();// ����
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
			Toast.makeText(this, "û��sdCard", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		System.exit(0);
	}

}
