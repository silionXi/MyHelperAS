package com.lee.my.helper.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.my.helper.R;

public class AppAdapter extends BaseAdapter {

	protected List<AppItem> list;
	protected Context context;
	protected LayoutInflater inflater;

	public AppAdapter(LayoutInflater inflater) {
		super();
		// TODO Auto-generated constructor stub
		list = new ArrayList<AppItem>();
		this.inflater = inflater;
	}

	public void add(AppItem item) {
		list.add(item);
	}

	public void clear() {
		list.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public AppItem getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
		textview.setTextColor(Color.parseColor("#a8e7ef"));
		textview.setGravity(Gravity.CENTER_VERTICAL);
		TextView remark = (TextView) view.findViewById(R.id.remark);
		remark.setText(Html.fromHtml(getItem(position).remark));
		ImageView check = (ImageView) view.findViewById(R.id.enable);
		if (getItem(position).checked)
			check.setBackgroundResource(R.drawable.check_on);
		else
			check.setBackgroundResource(R.drawable.check_off);
		return view;
	}

}