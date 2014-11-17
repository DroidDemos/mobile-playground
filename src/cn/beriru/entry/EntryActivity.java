package cn.beriru.entry;
import android.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.beriru.app.App;


public class EntryActivity extends ListActivity{

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ActivityInfo[] infos = getAllActivities();
		setListAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				 LayoutInflater inf = LayoutInflater.from(EntryActivity.this);
				 TextView v = (TextView) inf.inflate(R.layout.simple_list_item_1, null);
				 v.setText(infos[position].name);
				 v.setTag(infos[position]);
				 return v;
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				return infos.length;
			}
		});
	}
	
	
	public ActivityInfo[] getAllActivities(){
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			return info.activities;
		} catch (NameNotFoundException e) {
			App.toast(e.getMessage());
		}
		return new ActivityInfo[]{};
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ActivityInfo  className =  (ActivityInfo) v.getTag();
		Class<? extends Activity> act = null;
		try {
			act = (Class<? extends Activity>) Class.forName(className.name);
			startActivity(new Intent(this,act));
		} catch (ClassNotFoundException e) {
			App.toast(e.getMessage());
		}
	}

}
