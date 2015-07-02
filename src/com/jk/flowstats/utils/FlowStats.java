package com.jk.flowstats.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

public class FlowStats {

	/**
	 * 获取不包含WIFI的手机GPRS接收量和发送量总和
	 * 
	 * @return
	 */
	public static long getGPRSTotal() {
		return TrafficStats.getMobileRxBytes()
				+ TrafficStats.getMobileTxBytes();
	}

	/**
	 * 
	 * 获取包含WIFI的手机GPRS接收量和发送量总和
	 * 
	 * @return
	 */
	public static long getTotal() {
		return TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
	}

	/**
	 * 获取应用的流量(包含WIFI的手机GPRS接收量和发送量总和)使用情况
	 * 
	 * @param context
	 */
	public static List<AppInfo> getNetAppInfoList(Context context) {
		List<AppInfo> appList = new ArrayList<AppInfo>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
						| PackageManager.GET_PERMISSIONS);

		for (PackageInfo packageInfo : packinfos) {
			String[] premissions = packageInfo.requestedPermissions;
			if (premissions != null && premissions.length > 0) {
				for (String premission : premissions) {
					if ("android.permission.INTERNET".equals(premission)) {
						AppInfo appInfo = new AppInfo();
						int uid = packageInfo.applicationInfo.uid;
						// 应用图标
						appInfo.setAppIcon(packageInfo.applicationInfo
								.loadIcon(context.getPackageManager()));
						// 应用uid
						appInfo.setAppUid(packageInfo.applicationInfo.uid);
						// 应用名称
						appInfo.setAppName(packageInfo.applicationInfo
								.loadLabel(context.getPackageManager())
								.toString());
						// 使用的流量
						long total = TrafficStats.getUidTxBytes(uid)
								+ TrafficStats.getUidRxBytes(uid);
						appInfo.setFlowData(total);
						appList.add(appInfo);
					}
				}
			}
		}
		return appList;
	}

	public static class AppInfo {

		private String appName;
		private Drawable appIcon;
		private int appUid;
		/** 使用的流量=发送的+接收的,单位是M **/
		private long flowData;//

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public Drawable getAppIcon() {
			return appIcon;
		}

		public void setAppIcon(Drawable appIcon) {
			this.appIcon = appIcon;
		}

		public int getAppUid() {
			return appUid;
		}

		public void setAppUid(int appUid) {
			this.appUid = appUid;
		}

		public long getFlowData() {
			return flowData;
		}

		public void setFlowData(long flowData) {
			this.flowData = flowData;
		}

	}
}
