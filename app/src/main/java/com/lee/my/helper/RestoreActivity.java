package com.lee.my.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.lee.my.helper.tools.Tools;

public class RestoreActivity extends Activity implements OnClickListener {

	protected MeterView memery;
	protected TextView total;
	protected TextView used;
	protected TextView available;
	protected ListView apps;
	protected Button restore;
	protected Button checkAll;
	public static ApplicationAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restore);
		memery = (MeterView) findViewById(R.id.memery);
		total = (TextView) findViewById(R.id.total);
		used = (TextView) findViewById(R.id.used);
		available = (TextView) findViewById(R.id.available);
		apps = (ListView) findViewById(R.id.apps);
		restore = (Button) findViewById(R.id.restore);
		checkAll = (Button) findViewById(R.id.checkAll);
		adapter = new ApplicationAdapter(getLayoutInflater(),
				MyHelperActivity.restoreApps);
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

	private ProgressDialog dialog;

	protected void hideInstall(String apkPath) {
		// TODO Auto-generated method stub
		Tools.runRootCommand("pm install -r " + apkPath);
	}

	protected Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				checkAll.setText("反选");
				break;
			case 2:
				checkAll.setText("全选");
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(RestoreActivity.this, "恭喜你，已经成功还原！",
						Toast.LENGTH_LONG).show();
				MyHelperActivity.flush();
				adapter.notifyDataSetChanged();
				break;
			case 4:
				ApplicationItem item = (ApplicationItem) msg.obj;
				String message = "正在安装:" + item.appName;
				dialog.setMessage(message);
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == restore) {
			installChecked();
		} else if (v == checkAll) {
			if (checkAll.getText().equals("全选")) {
				checkAll();
			} else {
				inverseAll();
			}
			adapter.notifyDataSetChanged();
		}
	}

	private void installChecked() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("还原");
		dialog.setMessage("");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				int count = 0;
				int index = 1;
				for (int i = 0; i < adapter.getCount(); i++) {
					if (adapter.getItem(i).checked)
						count++;
				}
				for (int i = 0; i < adapter.getCount(); i++) {
					ApplicationItem item = adapter.getItem(i);
					if (item.checked) {
						dialog.setMax(count);
						dialog.setProgress(index);
						Message msg = new Message();
						msg.what = 4;
						msg.obj = item;
						handler.sendMessage(msg);
						hideInstall(item.apkPath);
						index++;
					}
				}
				handler.sendEmptyMessage(3);
			}
		}).start();
	}

	private void checkAll() {
		for (int i = 0; i < adapter.getCount(); i++) {
			ApplicationItem item = adapter.getItem(i);
			item.checked = true;
		}
		handler.sendEmptyMessage(1);
	}

	private void inverseAll() {
		for (int i = 0; i < adapter.getCount(); i++) {
			ApplicationItem item = adapter.getItem(i);
			item.checked = false;
		}
		handler.sendEmptyMessage(2);
	}

}
