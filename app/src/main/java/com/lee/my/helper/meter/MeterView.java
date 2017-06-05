package com.lee.my.helper.meter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lee.my.helper.R;

public class MeterView extends View {

	public int radius;
	private int diameter;
	private Paint paint;
	private Paint paint2;
	private Paint textPaint;
	private RectF oval;
	private String content = "";

	public MeterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(0xFFFFFFFF);
		textPaint.setTextSize(21);
		paint.setColor(0x3300FF00);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.view);
		int radius = array.getDimensionPixelSize(R.styleable.view_radius, 0);
		int color = array.getInt(R.styleable.view_arcColor, 0);
		init(radius, color);
		array.recycle();
	}

	private void init(int radius, int color) {
		paint2.setColor(color);
		this.radius = radius;
		diameter = radius << 1;
	}

	private float value;

	public void update(float total, float use) {
		StringBuffer buffer = new StringBuffer();
		value = use * 360 / total;
		String str = Float.toString(use * 100 / total);
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
		buffer.append("%");
		content = buffer.toString();
	}

	public void update(String text, float total, float use) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(text);
		value = use * 360 / total;
		String str = Float.toString(use * 100 / total);
		int offset = str.indexOf(".");
		if (offset != -1) {
			buffer.append(str.substring(0, offset));
			if (str.length() > offset + 3)
				buffer.append(str.substring(offset, offset + 3));
		} else {
			buffer.append(str);
		}
		buffer.append("%");
		content = buffer.toString();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		oval = new RectF(0, 0, diameter, diameter);
		canvas.drawCircle(radius, radius, radius, paint);
		canvas.drawArc(oval, -90, value, true, paint2);
		canvas.drawText(content, radius - textPaint.measureText(content) / 2,
				radius, textPaint);
	}

}
