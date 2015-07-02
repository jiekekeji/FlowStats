package com.jk.flowstats;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jk.flowstats.utils.FlowStats;
import com.jk.flowstats.utils.FlowStats.AppInfo;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setTitle("流量统计示例");

		initView();
	}

	private void initView() {

		TextView tv = (TextView) findViewById(R.id.totalData);
		tv.setText("已使用GPRS数据流量:" + FlowStats.getGPRSTotal() + "字节");

		ListView appListView = (ListView) findViewById(R.id.appList);
		if (FlowStats.getNetAppInfoList(this) != null) {
			appListView.setAdapter(new MyAdapter(this, FlowStats
					.getNetAppInfoList(this)));
		}

	}

	class MyAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<AppInfo> list;

		public MyAdapter(Context context, List<AppInfo> list) {
			inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item, null);

				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.appIcon);
				holder.appName = (TextView) convertView
						.findViewById(R.id.appName);
				holder.appFlow = (TextView) convertView
						.findViewById(R.id.appFlow);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.appIcon.setImageDrawable(list.get(position).getAppIcon());
			holder.appName.setText(list.get(position).getAppName());
			holder.appFlow.setText(list.get(position).getFlowData() + "字节");
			return convertView;

		}

		public final class ViewHolder {
			public ImageView appIcon;
			public TextView appName;
			public TextView appFlow;
		}
	}
}
