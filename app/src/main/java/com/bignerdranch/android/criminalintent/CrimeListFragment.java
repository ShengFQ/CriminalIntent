package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

//import android.app.ListFragment;

/**
 * create in 2014-11-02 display the CrimeLab arrayList<Crime> chapter 9
 * */
public class CrimeListFragment extends ListFragment {
	// add static variable tag
	private static final String tag = "CrimeListFragment";

	private static final int REQUEST_CRIME = 1;
	// add ArrayList<Crime>
	private ArrayList<Crime> mCrimes;

	private boolean mSubtitleVisible;// add in chapter16 for recording subtitle
										// status.

	// fragement's method not be protected,must be public ,because it will usage
	// by activity
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// add in chapter16
		setHasOptionsMenu(true);
		setRetainInstance(true);
		mSubtitleVisible = false;// add in chapter16
		// setContentView(R.layout.crime_list_fragment);
		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.getInstance(getActivity()).getCrimes();
		// above all ,you can see the waiting in the window ,now we should show
		// the ArrayList in the default view
		// method 1:display the default arrayAdapter
		// ArrayAdapter<Crime> adapter=new
		// ArrayAdapter<Crime>(getActivity(),android.R.layout.simple_list_item_1,mCrimes);
		// setListAdapter(adapter);
		// method 2:display the customerize ArrayAdapter
		CrimeAdapter adapter = new CrimeAdapter(getActivity(), mCrimes);
		setListAdapter(adapter);

	}

	// add in chapter16
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		// the device was landscape and will be see the subtitle.
		if (mSubtitleVisible) {
			getActivity().getActionBar().setSubtitle(R.string.subtitle);
		}
		// View v=(View)inflater.inflate(R.layout.fragment_crimes_list, parent);
		// 使用android.R.id.list资源id获取ListFragment管理的ListView.
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		// add in chapter18
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// in api 11 and lower 注册上下文操作菜单登记视图
			registerForContextMenu(listView);
		} else {
			// use contextual action bar on higher
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
				public void onItemCheckedStateChanged(ActionMode mode,
						int position, long id, boolean checked) {

				}

				// ActionMode.Callback methods
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();//操作模式负责对上下文操作栏进行配置
					mode.setTitle(R.string.delete_crime);
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}

				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				public void onDestroyActionMode(ActionMode mode) {
				}

				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					switch (item.getItemId()) {
					case R.id.menu_item_delete_crime:
						CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
						CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
						for (int i = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								crimeLab.deleteCrime(adapter.getItem(i));
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;

					default:
						return false;
					}
				}

			});
		}
		return v;
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime crime = ((CrimeAdapter) getListAdapter()).getItem(position);
		// Log.d(TAG, crime.getTitle());
		// Toast.makeText(getActivity(),"title:"+crime.getTitle(),3000).show();
		// Intent intent = new Intent(getActivity(),
		// CrimeActivity.class);//comment in chapter11
		Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
		intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
		// startActivity(intent);
		startActivityForResult(intent, REQUEST_CRIME);
	}

	// add in 2014-11-03 chapter10
	public void onResume() {
		super.onResume();// don't forget it
		((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
	}

	public void onActivityResult(int requestCode, int resultcode, Intent data) {
		if (requestCode == REQUEST_CRIME) {
			Toast.makeText(getActivity(), "return from CrimeFragment:", 3000)
					.show();
		}
		((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
	}

	/***
	 * add in chapter16 由fragmentManager负责调用
	 * */
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO why? why do this
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible && showSubtitle != null) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}

	// chatpter 16:press optionsItem will exec call back for this method
	// actually：onOptionsItemSelected
	// my：onOptionsItemSeleted
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(tag, "call back func,when optionsItem selected");

		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			Crime crime = new Crime();
			mCrimes.add(crime);
			// go to detail
			Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
			intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivityForResult(intent, 0);
			return true;
		case R.id.menu_item_show_subtitle:
			if (getActivity().getActionBar().getSubtitle() == null) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				mSubtitleVisible = true;// add in chapter16
				item.setTitle(R.string.hide_subtitle);
			} else {
				getActivity().getActionBar().setSubtitle(null);
				mSubtitleVisible = false;// add in chapter16
				item.setTitle(R.string.show_subtitle);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 这是实例化上下文菜单的回调方法，详情查看高焕堂老师的android框架
	 * add in chapter18, add contextMenu instance
	 * 注意：如果没有registerForContextMenu(listView);登记视图则该方法不执行
	 * */
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenu.ContextMenuInfo menuInfo) {
		//TODO 上述参数代表什么意思
		//如果定义了多个上下文菜单，通过检查传入的View的ID，进行实例化个性化的上下文资源
		//View?? --->
		/*switch (view.getId()) {
		case R.id.:
			
			break;

		default:
			break;
		}*/
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,
				menu);
	}

	/**
	 * add in chapter 18,callback for contextmenu actionListener
	 * 注意：如果没有registerForContextMenu(listView);登记视图则该方法不执行
	 * */
	public boolean onContextItemSelected(MenuItem item) {
		// 1.获取选中的index
		// 2.通过index还原为Crime对象
		// 3.调用CrimeLab的deleteCrime方法 *如果有多个菜单要判断MenuItem的资源id
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;// TODO why this position can be used by
										// ArrayAdapter
		CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
		Crime crime = adapter.getItem(position);
		switch (item.getItemId()) {
		case R.id.context_menu_item_delete_crime:
			CrimeLab.getInstance(getActivity()).deleteCrime(crime);
			// notify the adapter
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);// TODO why??
	}
}
