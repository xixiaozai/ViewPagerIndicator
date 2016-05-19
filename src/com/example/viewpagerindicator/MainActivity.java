package com.example.viewpagerindicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.view.ViewPagerIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ViewPagerIndicator mIndicator;
	private List<String> mTitles = Arrays.asList("短信", "推荐", "编辑","短信1", "推荐1", "编辑1","短信2", "推荐2", "编辑2");
	private List<VpSimpleFragment> mContents = new ArrayList<VpSimpleFragment>();
	private FragmentPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main2);
		initView();
		initDatas();
		mIndicator.setVisibleTabCount(3);
		mIndicator.setTabItemTitles(mTitles);
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager, 0);
//		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int i) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onPageScrolled(int i, float f, int j) {
//				// TODO Auto-generated method stub
//				/*
//				 * 三角形在切换后偏移量 *
//				 */
//				mIndicator.scroll(i, f);
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int i) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
		mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indiccator);

	}

	private void initDatas() {
		// TODO Auto-generated method stub
		for (String title : mTitles) {
			VpSimpleFragment fragment = VpSimpleFragment.newInstance(title);
			mContents.add(fragment);
		}
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				return mContents.get(position);
			}
		};
	}

}
