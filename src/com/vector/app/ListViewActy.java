package com.vector.app;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.vector.model.Weib;
import com.vector.view.VectorListView2;

/**
 * list view test
 * 
 * @author vector
 * 
 */
public class ListViewActy extends BaseActivity {

	/**
	 * 自己复写的list view 可以下拉刷新的
	 */
	private VectorListView2 listView2;
	private ArrayList<Weib> aList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		initData();
		initLayout();
	}

	protected void initData() {
		aList = new ArrayList<Weib>();
		// 数据库读的数据
		for (int i = 0; i < 10; i++) {
			Weib weib = new Weib();
			weib.setName("Vector Huang");
			weib.setTime("1890-13-50 25:90");
			weib.setContent("我是一个穿越时空的人，我来的21世纪，不知道做什么好，就来发微博。");
			weib.setTime("我需要做什么？");
			aList.add(weib);
		}
	}

	protected void initLayout() {
		listView2 = (VectorListView2) _getView(R.id.lvTest);
		/**
		 * 绑定下拉事件处理类
		 */
		listView2.setOnRefreshListener(new VectorListView2.OnRefreshLoadingMoreListener() {

			@Override
			public void onRefresh() {
				// 下拉后要做的东西
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						listView2.onRefreshComplete();
						toast("下拉刷新了");
					}

				}.execute();
			}

			@Override
			public void onLoadMore() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						listView2.onLoadMoreComplete(true);
						toast("加载刷新完了");
					}

				}.execute();
			}
		});
		/**
		 * 设置数据适配器
		 */
		listView2.setAdapter(new WeibDataAdapter(aList, this));
		/**
		 * 绑定点击事件
		 */
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	/**
	 * 内部类，数据是配置
	 */
	class WeibDataAdapter extends BaseAdapter {
		/**
		 * 要适配的数据
		 */
		private ArrayList<Weib> list;
		private Context context;
		/**
		 * 用来反射布局文件的
		 */
		private LayoutInflater inflater = null;

		public WeibDataAdapter(ArrayList<Weib> list, Context context) {
			this.list = list;
			this.context = context;
			inflater = LayoutInflater.from(this.context);
		}

		public int getCount() {
			return list.size();
		}

		// 返回一个数据实体
		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		/**
		 * 返回一个view来显示
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/**
			 * 每以个item 的对象都是不同的
			 */
			convertView = inflater.inflate(R.layout.listviewitem, null);
			View view = convertView.findViewById(R.id.ll_listView_content);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					toast("你点击了我。");
				}
			});
			/**
			 * 填充数据
			 */
			// ....

			return convertView;
		}

	}

}
