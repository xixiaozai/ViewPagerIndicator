package com.example.view;

import java.util.List;

import com.example.viewpagerindicator.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout {

	private Paint mPaint;
	private Path mPath;
	private int mTrangleWidth;
	private int mTrangleHeight;
	private static final float RADIO_TRIANGLE_WIDTH = 1 / 6F;
	private final int TRAINGLE_MAX_WIDTH = (int) (getScreenWidth() / 3 * RADIO_TRIANGLE_WIDTH);
	private int mInitTranslationX;
	private int mTranslationX;
	private int mTabVisibleCount;
	private static final int COUNT_DEFAULT = 4;
	private static final int COLOR_HIGH = 0xFFFFFF;
	private List<String> mTitles;

	public ViewPagerIndicator(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("Recycle")
	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		/*
		 * 获取可见Tab的数量
		 */
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ViewPagerIndicator);
		mTabVisibleCount = a.getInt(
				R.styleable.ViewPagerIndicator_visiable_tab_count,
				COUNT_DEFAULT);
		if (mTabVisibleCount < 0) {
			mTabVisibleCount = COUNT_DEFAULT;
		}
		a.recycle();
		/*
		 * 初始化 画笔
		 */
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL);
		// 圆角：CornerPathEffect
		mPaint.setPathEffect(new CornerPathEffect(3));
	}

	/**
	 * 三角形位置的选择
	 * */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 3);
		canvas.drawPath(mPath, mPaint);
		canvas.restore();

		super.dispatchDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mTrangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH);
		mTrangleWidth = Math.min(mTrangleWidth, TRAINGLE_MAX_WIDTH);
		mInitTranslationX = w / mTabVisibleCount / 2 - mTrangleWidth / 2;
		initTriangle();
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub

		super.onFinishInflate();
		int cCount = getChildCount();
		if (cCount == 0)
			return;
		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			LinearLayout.LayoutParams lp = (LayoutParams) view
					.getLayoutParams();
			lp.weight = 0;
			lp.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(lp);
		}
		setItemClickEvent();
	}

	/*
	 * 得到屏幕宽度
	 */
	private int getScreenWidth() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/*
	 * 初始化三角形
	 */
	private void initTriangle() {
		// TODO Auto-generated method stub

		mTrangleHeight = mTrangleWidth / 2;
		mPath = new Path();
		mPath.moveTo(0, 0);
		mPath.lineTo(mTrangleWidth, 0);
		mPath.lineTo(mTrangleWidth / 2, -mTrangleHeight);
		mPath.close();
	}

	/*
	 * 指示器跟随手指进行滚动
	 */
	public void scroll(int i, float f) {
		// TODO Auto-generated method stub
		int tabWidth = getWidth() / mTabVisibleCount;
		mTranslationX = (int) (tabWidth * (f + i));
		/*
		 * 容器移动 至最后一个
		 */
		if (i >= (mTabVisibleCount - 2) && f > 0
				&& getChildCount() > mTabVisibleCount) {
			if (mTabVisibleCount != 1) {
				this.scrollTo((i - (mTabVisibleCount - 2)) * tabWidth
						+ (int) (tabWidth * f), 0);
			} else {
				this.scrollTo(i * tabWidth + (int) (tabWidth * f), 0);
			}
		}
		invalidate();
	}

	public void setTabItemTitles(List<String> titles) {
		if (titles != null && titles.size() > 0) {
			this.removeAllViews();
			mTitles = titles;
			for (String title : mTitles) {
				addView(generateTextView(title));
			}
			setItemClickEvent();
		}
	}

	// 动态添加Tab
	/*
	 * 设置可见Tab数量
	 */
	public void setVisibleTabCount(int count) {
		// TODO Auto-generated method stub
		mTabVisibleCount = count;
	}

	/*
	 * 根据title创建Tab
	 */
	private View generateTextView(String title) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = getScreenWidth() / mTabVisibleCount;
		tv.setText(title);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setTextColor(Color.BLACK);
		tv.setLayoutParams(lp);
		return null;
	}

	private ViewPager mViewPager;

	public interface PageOnChangeListener {
		public abstract void onPageScrolled(int i, float f, int j);

		public abstract void onPageSelected(int i);

		public abstract void onPageScrollStateChanged(int i);
	}

	public PageOnChangeListener mListener;

	public void setOnPageChangeListener(PageOnChangeListener listener) {
		this.mListener = listener;
	}

	/*
	 * 设置关联的ViewPager
	 */
	public void setViewPager(ViewPager viewpager, int pos) {
		mViewPager = viewpager;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int i) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.onPageSelected(i);
				}
				highLightText(i);
			}

			@Override
			public void onPageScrolled(int i, float f, int j) {
				// TODO Auto-generated method stub
				/*
				 * 三角形在切换后偏移量 *
				 */

				scroll(i, f);
				if (mListener != null) {
					mListener.onPageScrolled(i, f, j);
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.onPageScrollStateChanged(i);
				}
			}
		});
		mViewPager.setCurrentItem(pos);
		highLightText(pos);
	}

	public void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(COLOR_HIGH);
			}
		}
	}

	/**
	 * 设置高亮
	 */
	private void highLightText(int pos) {
		resetTextViewColor();
		View view = getChildAt(pos);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(Color.WHITE);
		}
	}

	/**
	 * 设置点击事件
	 */
	private void setItemClickEvent() {
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}
}
