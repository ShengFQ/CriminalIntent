package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/*import android.app.Fragment;
 import android.app.FragmentManager;*/
/*import android.support.v4.app.Fragment;
 import android.support.v4.app.FragmentManager;
 import android.support.v4.app.NavUtils;*/
/**
 * add in 2014-10-26 chapter7
 * 
 * @version 0.7
 * @author SHENGFQ display crime detail info and modify the info
 * */
public class CrimeFragment extends Fragment {
	private String tag = "CrimeFragment";
	// the entity
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private ImageButton mPhotoButton;// add in chapter19
	private Button mSuspectButton;// add in chapter20
	private Button mDialButton;//add in chapter20
	private ImageView mPhotoView;// add in chapter20
	public static final String EXTRA_CRIME_ID = "com.brignerdranch.android.criminalintent.crime_id";
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	// add in chapter20
	private static final int REQUEST_PHOTO = 1;
	// add in chapter21
	private static final int REQUEST_CONTACT = 2;
	// add in chapter20
	private static final String DIALOG_IMAGE = "image";

	public void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "start onCreate");
		// the ciyclelife of fragment instance
		super.onActivityCreated(savedInstanceState);
		// mCrime=new Crime();
		// add in 2014-11-03 chapter10.1
		// UUID
		// crimeid=(UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		// mCrime=CrimeLab.getInstance(getActivity()).getCrime(crimeid);
		// add in2014-11-03 chapter10.2
		UUID id = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.getInstance(getActivity()).getCrime(id);
		// add in chapter 16 set has optionsMenu
		setHasOptionsMenu(true);

	}

	// create and config fragment by onCreateView
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle saveInstanceState) {
		Log.i(tag, "start onCreateView");
		// View v=inflater.inflate(R.id.fragmentContainer,
		// parent,false);//这个方法的乱用导致app无法跑起来。
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		// add in chapter16 show the navigation bar
		if (NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mCrime.setTitle(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		mDateButton = (Button) v.findViewById(R.id.crime_date);
		// mDateButton.setText(mCrime.getDate().toString());
		updateDate();
		// mDateButton.setEnabled(false);
		mDateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// by CrimeFragment's Activity tuoguan DatepickFragment
				FragmentManager fm = getActivity().getSupportFragmentManager();// TODO
																				// 该处使得采用android.support.v4.app.Fragment
				// FragmentManager fm=getActivity().getFragmentManager();
				DatepickeFragment datePickefm = DatepickeFragment
						.getInstance(mCrime.getDate());
				datePickefm.setTargetFragment(CrimeFragment.this, REQUEST_DATE);// set
																				// target
																				// fragment
				datePickefm.show(fm, DIALOG_DATE);// fragmentmanager add
													// DialogFragment
			}
		});

		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mCrime.setSolved(isChecked);
					}
				});

		// add in chapter19
		mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
				// startActivity(i);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});
		// add in chapter20
		mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Photo p = mCrime.getPhoto();
				if (p == null) {
					return;
				}
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(p.getFilename())
						.getAbsolutePath();
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);

			}
		});
		// If camera is not avaliable,disable camera functionality
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
				|| pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD
				|| Camera.getNumberOfCameras() > 0;
		if (!hasACamera) {
			mPhotoButton.setEnabled(false);
		}

		//选择已经安装的app来发送crime
		Button reportButton = (Button) v.findViewById(R.id.crime_reportButton);
		reportButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PackageManager pm = getActivity().getPackageManager();
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				//getCrimeReport()
				i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				i.putExtra(Intent.EXTRA_SUBJECT,
						getString(R.string.crime_report_subject));
				i = Intent.createChooser(i, getString(R.string.send_report));
				List<ResolveInfo> activities = pm.queryIntentActivities(i, 0);
				boolean isIntentSafe = activities.size() > 0;
				if (isIntentSafe) {
					startActivity(i);
				} else {
					Toast.makeText(getActivity(), "has no such intent", 2000);
				}
			}
		});

		// add in chapter21
		mSuspectButton = (Button) v.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PackageManager pm = getActivity().getPackageManager();
				Intent i = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				List<ResolveInfo> activities=pm.queryIntentActivities(i, 0);
				if(activities.size()>0){
				startActivityForResult(i, REQUEST_CONTACT);
				}else{
					Toast.makeText(getActivity(), "has no such intent", 2000);
				}
			}

		});

		if (mCrime.getSuspect() != null) {
			mSuspectButton.setText(mCrime.getSuspect());
		}
		mDialButton=(Button)v.findViewById(R.id.crime_dialButton);
		mDialButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PackageManager pm = getActivity().getPackageManager();
				Uri number=Uri.parse("tel:5551234");
				Intent i=new Intent(Intent.ACTION_DIAL,number);
				List<ResolveInfo> activities=pm.queryIntentActivities(i, 0);
				if(activities.size()>0){
				startActivity(i);
				}
			}
		});

		return v;
	}

	// add in 2015-03-05 chapter20
	/**
	 * 从Crime对象中获取图片路径
	 * */
	private void showPhoto() {
		Photo p = mCrime.getPhoto();
		BitmapDrawable b = null;
		if (p != null) {
			String path = getActivity().getFileStreamPath(p.getFilename())
					.getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}

	// add in 2014-11-03 chapter10
	public static CrimeFragment getInstance(UUID crimeId) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_CRIME_ID, crimeId);
		CrimeFragment crimeFragment = new CrimeFragment();
		crimeFragment.setArguments(bundle);
		return crimeFragment;
	}

	// add in 2014-11-03 chapter10
	// return resultcode to mother window
	public void returnResult() {
		getActivity().setResult(Activity.RESULT_OK, null);
	}

	// 解析DatepickeFragment的回传结果
	// TODO 这个方法很实用*******************************
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date) data
					.getSerializableExtra(DatepickeFragment.EXTRA_DATE);
			mCrime.setDate(date);
			// mDateButton.setText(mCrime.getDate().toString());
			updateDate();
		} else if (requestCode == REQUEST_PHOTO) {
			String filename = data
					.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if (filename != null) {
				// Log.i(tag,"filename: "+filename);
				Photo p = new Photo(filename);
				mCrime.setPhoto(p);
				// add in chapter20
				showPhoto();
				Log.i(tag, "Crime: " + mCrime.getTitle() + " has a photo");
			}
		} else if (requestCode == REQUEST_CONTACT) {
			Uri contactUri = data.getData();
			String[] queryFields = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
			Cursor c = getActivity().getContentResolver().query(contactUri,
					queryFields, null, null, null);
			if (c.getCount() == 0) {
				c.close();
				return;
			}
			c.moveToFirst();
			String suspect = c.getString(0);
			mCrime.setSuspect(suspect);
			mSuspectButton.setText(suspect);
			c.close();
		}
	}

	private void updateDate() {
		mDateButton.setText(mCrime.getDate().toString());
	}

	/**
	 * add in chapter18 practise add options menu-delete
	 * */
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_detail, menu);

	}

	// add in chapter16,when onCreate add setHasOptionsMenu=true
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// android.R.id.Home????? why is this?
		case android.R.id.home:
			/*
			 * method 1,show the back stack's activity instance,and let the one
			 * into top
			 */
			// Intent intent=new Intent(getActivity(),CrimeListActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// this is
			// amazing
			// startActivity(intent);
			// onDestroy();
			/* method 2 use NavUtils:this is very perfect */
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		case R.id.menu_item_delete_crime:
			CrimeLab.getInstance(getActivity()).deleteCrime(mCrime);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * add in chapter17 in 2014-12-25 onPause call the saveData into local file
	 * system
	 * */
	public void onPause() {
		super.onPause();
		CrimeLab.getInstance(getActivity()).saveCrimes();
	}

	/**
	 * add in chapter20 in 2015-03-05
	 * */
	public void onStart() {
		super.onStart();
		showPhoto();
	}

	public void onStop() {
		super.onStop();
		PictureUtils.cleanImagView(mPhotoView);
	}

	private String getCrimeReport() {
		String solvedString = null;
		if (mCrime.isSolved()) {
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_unsolved);
		}
		String dateFormat = "EEE, MMM dd";
		String dateString = dateFormat.format(dateFormat, mCrime.getDate()).toString();
		String suspect = mCrime.getSuspect();
		if (suspect == null) {
			suspect = getString(R.string.crime_report_no_suspect);
		} else {
			suspect = getString(R.string.crime_report_suspect, suspect);
		}
		String report = getString(R.string.crime_report, mCrime.getTitle(),
				dateString, solvedString, suspect);
		return report;
	}
}
