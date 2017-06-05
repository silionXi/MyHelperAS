package com.lee.my.helper;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lee.my.helper.adapter.AppAdapter;
import com.lee.my.helper.adapter.AppItem;
import com.lee.my.helper.tools.Tools;

public class UsersActivity extends Activity {

	public PackageManager pm;
	public ActivityManager am;
	public AppAdapter appItems;
	public ListView appsList;
	public Vector<String> strings = new Vector<String>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		pm = getPackageManager();
		appItems = new AppAdapter(getLayoutInflater());
		setContentView(R.layout.services);
		appsList = (ListView) findViewById(R.id.appsList);
		appsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AppItem item = appItems.getItem(position);
				if (item.checked) {
					closeAutoStartApp(item);
				} else {
					startAutoStartApp(item);
				}
			}
		});
		flush();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				appsList.setAdapter(appItems);
			}
		});
	}

	protected void flush() {
		// TODO Auto-generated method stub
		strings.clear();
		appItems.clear();
		Intent intent = new Intent("android.intent.action.BOOT_COMPLETED");
		List<ResolveInfo> enable = pm.queryBroadcastReceivers(intent, 0);
		for (int i = 0; i < enable.size(); i++) {
			ResolveInfo info = enable.get(i);
			if (!info.activityInfo.applicationInfo.sourceDir
					.startsWith("/system")) {
				String pName = info.activityInfo.packageName;
				if (!pName.equals("com.android.vending")) {
					AppItem item = new AppItem(this, true, pName,
							info.activityInfo.name);
					item.remark = "开机启动(<font color=#00FF00>启用</font>)";
					appItems.add(item);
					strings.add(pName);
				}
			}
		}
		List<ResolveInfo> disable = pm.queryBroadcastReceivers(intent, 600);
		for (int i = 0; i < disable.size(); i++) {
			ResolveInfo info = disable.get(i);
			if (!info.activityInfo.applicationInfo.sourceDir
					.startsWith("/system")) {
				String pName = info.activityInfo.packageName;
				if (!pName.equals("com.android.vending")) {
					if (!strings.contains(pName)) {
						AppItem item = new AppItem(this, false, pName,
								info.activityInfo.name);
						item.remark = "开机启动(<font color=#FF0000>禁用</font>)";
						appItems.add(item);
					}
				}
			}
		}
		handler.sendEmptyMessage(1);
	}

	private ProgressDialog progressDialog;

	/** 关闭开机启动项管理 */
	public void closeAutoStartApp(final AppItem item) {
		progressDialog = ProgressDialog.show(this, null, "正在禁止开机启动项，请等待.....");
		new Thread() {
			public void run() {
				Tools.runRootCommand("pm disable " + item.packageName + "/"
						+ item.name);
				flush();
			}
		}.start();

	}

	/** 恢复开机启动项管理 */
	public void startAutoStartApp(final AppItem item) {
		progressDialog = ProgressDialog.show(this, null, "正在恢复开机启动项，请等待.....");
		new Thread() {
			public void run() {
				Tools.runRootCommand("pm enable " + item.packageName + "/"
						+ item.name);
				flush();
			}
		}.start();
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				if (progressDialog != null)
					progressDialog.dismiss();
				appItems.notifyDataSetChanged();
				break;
			}
		}

	};

}