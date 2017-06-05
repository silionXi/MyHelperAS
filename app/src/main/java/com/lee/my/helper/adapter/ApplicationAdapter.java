package com.lee.my.helper.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.my.helper.MyHelperActivity;
import com.lee.my.helper.R;
import com.lee.my.helper.meter.DeleteView;

public class ApplicationAdapter extends BaseAdapter implements OnClickListener {

	protected List<ApplicationItem> items;
	protected LayoutInflater inflater;

	public ApplicationAdapter(LayoutInflater inflater,
			ArrayList<ApplicationItem> list) {
		super();
		// TODO Auto-generated constructor stub
		this.inflater = inflater;
		items = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public ApplicationItem getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public List<ApplicationItem> getItems() {
		return items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (convertView == null) {
			LinearLayout linearlayout = (LinearLayout) inflater.inflate(
					R.layout.item, null);
			view = linearlayout;
		} else {
			LinearLayout linearlayout = (LinearLayout) convertView;
			view = linearlayout;
		}
		ImageView imageview = (ImageView) view.findViewById(R.id.icon);
		Drawable drawable = getItem(position).icon;
		imageview.setImageDrawable(drawable);
		TextView textview = (TextView) view.findViewById(R.id.appName);
		String s = getItem(position).appName;
		textview.setText(s);
		Typeface typeface = Typeface.defaultFromStyle(Typeface.BOLD);
		textview.setTypeface(typeface);
		textview.setTextSize(18F);
		textview.setSingleLine(true);
		if (getItem(position).same)
			textview.setTextColor(Color.parseColor("#00ff00"));
		else
			textview.setTextColor(Color.parseColor("#a8e7ef"));
		TextView remark = (TextView) view.findViewById(R.id.remark);
		if (getItem(position).version != null)
			remark.setText(Html.fromHtml(getItem(position).remark + " °æ±¾:"
					+ getItem(position).version));
		else
			remark.setText(Html.fromHtml(getItem(position).remark));
		textview.setGravity(Gravity.CENTER_VERTICAL);
		ImageView check = (ImageView) view.findViewById(R.id.enable);
		DeleteView delete = null;
		if (getItem(position).apkPath != null) {
			delete = (DeleteView) view.findViewById(R.id.delete);
			delete.setItemID(position);
			delete.setOnClickListener(this);
		}
		if (getItem(position).checked) {
			check.setBackgroundResource(R.drawable.check_on);
			if (delete != null)
				delete.setVisibility(View.VISIBLE);
		} else {
			check.setBackgroundResource(R.drawable.check_off);
			if (delete != null)
				delete.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof ImageView) {
			DeleteView delete = (DeleteView) v;
			String path = getItem(delete.getItemID()).apkPath;
			File file = new File(path);
			file.deleteOnExit();
			items.remove(delete.getItemID());
			MyHelperActivity.flushAppList();
		}
	}

}
