package com.lee.my.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.my.helper.adapter.ApplicationAdapter;
import com.lee.my.helper.adapter.ApplicationItem;
import com.lee.my.helper.meter.MeterView;

public class BackUpActivity extends Activity implements OnClickListener {

	protected static final String backupPath = "/Android/backup/";
	protected MeterView memery;
	protected TextView total;
	protected TextView used;
	protected TextView available;
	protected ListView apps;
	public static ApplicationAdapter adapter;
	protected Button backup;
	protected Button checkAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup);
		memery = (MeterView) findViewById(R.id.memery);
		total = (TextView) findViewById(R.id.total);
		used = (TextView) findViewById(R.id.used);
		available = (TextView) findViewById(R.id.available);
		apps = (ListView) findViewById(R.id.apps);
		backup = (Button) findViewById(R.id.backup);
		checkAll = (Button) findViewById(R.id.checkAll);
		adapter = new ApplicationAdapter(getLayoutInflater(),
				MyHelperActivity.installApps);
		apps.setAdapter(adapter);
		apps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ApplicationItem item = adapter.getItem(position);
				item.checked = !item.checked;
				adapter.notifyDataSetChanged();
			}
		});
		float block = MyHelperActivity.block;
		float totalBlocks = MyHelperActivity.totalBlocks;
		float availaBlock = MyHelperActivity.availaBlock;
		memery.update(totalBlocks, totalBlocks - availaBlock);
		total.setText("总量:" + toMB(totalBlocks * block));
		used.setText("已用:" + toMB((totalBlocks - availaBlock) * block));
		available.setText("可用:" + toMB(availaBlock * block));
	}

	private String toMB(float size) {
		StringBuffer buffer = new StringBuffer();
		String str = Float.toString(size / 1024 / 1024);
		int offset = str.indexOf(".");

		if (offset != -1) {
			buffer.append(str.substring(0, offset));
			if (str.length() > offset + 3)
				buffer.append(str.substring(offset, offset + 3));
			else
				buffer.append(str.substring(offset));
		} else {
			buffer.append(str);
		}
		buffer.append("MB");
		return buffer.toString();
	}

	protected boolean backup() {
		File file = Environment.getExternalStorageDirectory();
		File backup = new File(file.getPath() + "/Android");
		if (!backup.exists())
			backup.mkdir();
		backup = new File(file.getPath() + backupPath);
		if (!backup.exists())
			backup.mkdir();
		// 去掉日期文件夹
		// String date = DateFormat.format("/yyyy-MM-dd",
		// System.currentTimeMillis()).toString();
		// backup = new File(backup.getPath() + date);
		// if (!backup.exists())
		// backup.mkdir();
		int count = 0;
		int index = 1;
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).checked)
				count++;
		}
		for (int i = 0; i < adapter.getCount(); i++) {
			ApplicationItem item = adapter.getItem(i);
			dialog.setMax(count);
			dialog.setProgress(index);
			Message msg = new Message();
			msg.what = 4;
			msg.obj = "正在备份：" + item.appName;
			handler.sendMessage(msg);
			if (!item.checked)
				continue;
			int last = item.sourceDir.lastIndexOf("/") + 1;
			String newName = item.sourceDir.substring(last);
			if (!copyFile(item.sourceDir, backup.getPath() + "/" + newName,
					item.appName)) {
				Toast.makeText(this, item.appName + "备份失败！", Toast.LENGTH_LONG)
						.show();
			}
			index++;
		}
		handler.sendEmptyMessage(3);
		return true;
	}

	protected boolean copyFile(String src, String dst, String appName) {
		try {
			File oldfile = new File(src);
			if (oldfile.exists()) {
				File outFile = new File(dst);
				if (!outFile.exists()) {
					int byteread = 0;
					InputStream inStream = new FileInputStream(oldfile);
					outFile.createNewFile();
					FileOutputStream fs = new FileOutputStream(outFile);
					byte[] buffer = new byte[1024 * 1024];// 1MB
					while ((byteread = inStream.read(buffer)) != -1) {
						fs.write(buffer, 0, byteread);
					}
					inStream.close();
					fs.close();
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backup) {
			backup.setEnabled(false);
			handler.sendEmptyMessage(2);
		} else if (v == checkAll) {
			if (checkAll.getText().equals("全选")) {
				checkAll();
			} else {
				inverseAll();
			}
			adapter.notifyDataSetChanged();
		}
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
				doBackUp();
			} else if (msg.what == 3) {
				dialog.dismiss();
				backup.setEnabled(true);
				Toast.makeText(BackUpActivity.this, "恭喜你完成了备份！",
						Toast.LENGTH_LONG).show();
				MyHelperActivity.flush();
				adapter.notifyDataSetChanged();
			} else if (msg.what == 4) {
				dialog.setMessage(msg.obj.toString());
			}
		}

	};

	private ProgressDialog dialog;
	private String message = "正在处理,请稍后……";

	private void doBackUp() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("备份");
		dialog.setMessage(message);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.show();
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				backup();
			}

		}.start();
	}

}
