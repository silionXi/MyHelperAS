package com.lee.my.helper.meter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DeleteView extends ImageView {

	protected int itemID;

	public DeleteView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DeleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DeleteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

}
