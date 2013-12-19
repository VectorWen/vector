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
	 * �Լ���д��list view ��������ˢ�µ�
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
		// ���ݿ��������
		for (int i = 0; i < 10; i++) {
			Weib weib = new Weib();
			weib.setName("Vector Huang");
			weib.setTime("1890-13-50 25:90");
			weib.setContent("����һ����Խʱ�յ��ˣ�������21���ͣ���֪����ʲô�ã�������΢����");
			weib.setTime("����Ҫ��ʲô��");
			aList.add(weib);
		}
	}

	protected void initLayout() {
		listView2 = (VectorListView2) _getView(R.id.lvTest);
		/**
		 * �������¼�������
		 */
		listView2.setOnRefreshListener(new VectorListView2.OnRefreshLoadingMoreListener() {

			@Override
			public void onRefresh() {
				// ������Ҫ���Ķ���
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
						toast("����ˢ����");
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
						toast("����ˢ������");
					}

				}.execute();
			}
		});
		/**
		 * ��������������
		 */
		listView2.setAdapter(new WeibDataAdapter(aList, this));
		/**
		 * �󶨵���¼�
		 */
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	/**
	 * �ڲ��࣬����������
	 */
	class WeibDataAdapter extends BaseAdapter {
		/**
		 * Ҫ���������
		 */
		private ArrayList<Weib> list;
		private Context context;
		/**
		 * �������䲼���ļ���
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

		// ����һ������ʵ��
		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		/**
		 * ����һ��view����ʾ
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/**
			 * ÿ�Ը�item �Ķ����ǲ�ͬ��
			 */
			convertView = inflater.inflate(R.layout.listviewitem, null);
			View view = convertView.findViewById(R.id.ll_listView_content);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					toast("�������ҡ�");
				}
			});
			/**
			 * �������
			 */
			// ....

			return convertView;
		}

	}

}
