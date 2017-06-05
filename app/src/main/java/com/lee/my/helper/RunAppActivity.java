package com.lee.my.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.my.helper.adapter.ApplicationAdapter;
import com.lee.my.helper.adapter.ApplicationItem;
import com.lee.my.helper.meter.MeterView;

public class RunAppActivity extends Activity implements OnClickListener {

	private ListView runapps;
	private ApplicationAdapter adapter;
	private MeterView memery;
	private TextView total;
	private TextView allnum;
	private TextView available;
	protected Button kill;
	protected Button checkAll;
	protected ArrayList<ApplicationItem> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runapp);
		runapps = (ListView) findViewById(R.id.apps);
		memery = (MeterView) findViewById(R.id.memery);
		total = (TextView) findViewById(R.id.total);
		allnum = (TextView) findViewById(R.id.allnum);
		available = (TextView) findViewById(R.id.available);
		kill = (Button) findViewById(R.id.kill);
		checkAll = (Button) findViewById(R.id.checkAll);

		runapps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ApplicationItem item = adapter.getItem(position);
				item.checked = !item.checked;
				adapter.notifyDataSetChanged();
			}
		});
		// String s =
		// "6873 (a.out) R 6723 6873 6723 34819 6873 8388608 77 0 0 0 41958 31 0 0 25 0 3 0 5882654 1409024 56 4294967295 134512640 134513720 3215579040 0 2097798 0 0 0 0 0 0 0 17 8899 321 123";
		// ProcessParser p = new ProcessParser(s);
		// String hi =
		// "5255 (hiapk.marketpho) S 1958 1958 0 0 -1 4194624 7229 0 0 0 116 17 0 0 20 0 13 0 1730129 113410048 6035 4294967295 32768 36564 0 0 0 0 4612 0 38120 4294967295 0 0 17 0 99 87 69 28 37";
		// p = new ProcessParser(hi);
		items = new ArrayList<ApplicationItem>();
		adapter = new ApplicationAdapter(getLayoutInflater(), items);
		runapps.setAdapter(adapter);
		flush();
	}

	private String toProcess(String str) {
		StringBuffer buffer = new StringBuffer();
		String state = str.substring(0, 1);
		if (state.equals("R"))
			buffer.append("[运行]");
		else
			buffer.append("[后台]");
		int ch = str.lastIndexOf(")");
		if (ch != -1 && str.lastIndexOf(" ") != -1) {
			str = str.substring(ch + 1, str.lastIndexOf(" "));
			float i = Integer.parseInt(str.trim()) / 1024f;
			String v = String.valueOf(i);
			ch = v.lastIndexOf(".");
			if (ch + 3 < v.length())
				buffer.append(v.substring(0, v.lastIndexOf(".") + 3));
			else
				buffer.append(v.substring(0, v.length()));
			buffer.append("MB");
		}
		return buffer.toString();
	}

	public void flush() {
		items.clear();
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = manager.getRunningAppProcesses();
		PackageManager pmManager = getPackageManager();
		for (int i = 0; i < list.size(); i++) {
			RunningAppProcessInfo info = list.get(i);
			String processName = info.processName;
			if (!processName.equals("system")
					&& !processName.equals("com.android.settings")
					&& !processName.equals("com.android.phone")
					&& !processName.equals(getPackageName())
					&& !processName.equals("com.android.launcher")) {
				try {
					ApplicationInfo aiInfo = pmManager.getApplicationInfo(
							processName, PackageManager.GET_META_DATA);
					Drawable icon = aiInfo.loadIcon(getPackageManager());
					String packageName = aiInfo.packageName;
					String appName = aiInfo.loadLabel(getPackageManager())
							.toString();
					String sourceDir = aiInfo.sourceDir;
					ApplicationItem item = new ApplicationItem(icon,
							packageName, appName, sourceDir);
					String infos = toProcess(getStatusByPid(info.pid));
					item.remark = infos;
					items.add(item);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		allnum.setText("进程数量:" + items.size());
		total.setText("总内存:" + Formatter.formatFileSize(this, getTotalMemory()));
		available.setText("可用内存:"
				+ Formatter.formatFileSize(this, getAvailMemory()));
		memery.update(getTotalMemory(), getTotalMemory() - getAvailMemory());
		adapter.notifyDataSetChanged();
	}

	public String getStatusByPid(int pid) {
		String path = "/proc/" + pid + "/status";
		String line = null;
		StringBuffer buffer = new StringBuffer();
		try {
			File file = new File(path);
			if (!file.canRead() || !file.exists())
				return "file error";
			FileReader fr = new FileReader(file);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((line = localBufferedReader.readLine()) != null) {
				if (line.indexOf("State:") != -1)
					buffer.append(line.substring("State:".length()));
				else if (line.indexOf("VmData:") != -1)
					buffer.append(line.substring("VmData:".length()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString().trim();
	}

	public String getInfoByPid(int pid) {
		String path = "/proc/" + pid + "/stat";
		String line = null;
		try {
			File file = new File(path);
			if (!file.canRead() || !file.exists())
				return "file error";
			FileReader fr = new FileReader(file);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((line = localBufferedReader.readLine()) != null) {
				return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	public String getStatMemoryByPid(int pid) {
		String path = "/proc/" + pid + "/statm";
		String line = null;
		try {
			File file = new File(path);
			if (!file.canRead() || !file.exists())
				return "file error";
			FileReader fr = new FileReader(file);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((line = localBufferedReader.readLine()) != null) {
				return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	public void getTotalMemoryByReader() {
		String str1 = "/proc/meminfo";
		String str2 = null;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				System.out.println(">>>" + str2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 采用反射机制获取内置隐藏方法
	 * 
	 * @return
	 */
	public long getTotalMemory() {
		try {
			Class<?> procClass = Class.forName("android.os.Process");
			Class<?> parameterTypes[] = new Class[] { String.class,
					String[].class, long[].class };
			Method _readProclines = procClass.getMethod("readProcLines",
					parameterTypes);
			Object arglist[] = new Object[3];
			final String[] mMemInfoFields = new String[] { "MemTotal:",
					"MemFree:", "Buffers:", "Cached:" };
			long[] mMemInfoSizes = new long[mMemInfoFields.length];
			mMemInfoSizes[0] = 30;
			mMemInfoSizes[1] = -30;
			arglist[0] = new String("/proc/meminfo");
			arglist[1] = mMemInfoFields;
			arglist[2] = mMemInfoSizes;
			if (_readProclines != null) {
				_readProclines.invoke(null, arglist);
				return mMemInfoSizes[0] * 1024;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public long getAvailMemory() {
		ActivityManager localActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
		localActivityManager.getMemoryInfo(localMemoryInfo);
		long l = localMemoryInfo.availMem;
		// return Formatter.formatFileSize(this, l);
		return l;
	}

	public void killAllPrograms() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ApplicationItem> appList = adapter.getItems();
		for (int i = 0; i < appList.size(); i++) {
			ApplicationItem item = appList.get(i);
			if (item.checked) {
				manager.restartPackage(item.packageName);
			}
		}
		flush();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == kill) {
			kill.setEnabled(false);
			handler.sendEmptyMessage(2);
		} else if (v == checkAll) {
			if (checkAll.getText().equals("全选")) {
				checkAll();
			} else {
				inverseAll();
			}
		}
		adapter.notifyDataSetChanged();
	}

	private void checkAll() {
		for (int i = 0; i < adapter.getCount(); i++) {
			ApplicationItem item = adapter.getItem(i);
			item.checked = true;
		}
		handler.sendEmptyMessage(0);
	}

	private void inverseAll() {
		for (int i = 0; i < adapter.getCount(); i++) {
			ApplicationItem item = adapter.getItem(i);
			item.checked = false;
		}
		handler.sendEmptyMessage(1);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
				checkAll.setText("反选");
			} else if (msg.what == 1) {
				checkAll.setText("全选");
			} else if (msg.what == 2) {
				killAllPrograms();
				kill.setEnabled(true);
			}
		}

	};

}
