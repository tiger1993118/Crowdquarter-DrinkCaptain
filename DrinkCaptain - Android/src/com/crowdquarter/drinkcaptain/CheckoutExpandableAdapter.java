package com.crowdquarter.drinkcaptain;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckoutExpandableAdapter extends BaseExpandableListAdapter {

	private String[] aGroup = { "Shipping", "Payment", "Billing" };

	private Context context;

	private List<String> listChild;

	public CheckoutExpandableAdapter(Context c, List<String> listChild) {
		this.context = c;
		this.listChild = listChild;
	}

	@Override
	public int getGroupCount() {
		return aGroup.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return aGroup[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return listChild.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(
					R.layout.checkout_info_listview, null);
		}
		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		tvName.setText((String) getGroup(groupPosition));

		ImageView ivPlus = (ImageView) convertView.findViewById(R.id.ivPlus);
		TextView tvEdit = (TextView) convertView.findViewById(R.id.tvEdit);
		if (isExpanded) {
			tvEdit.setVisibility(View.VISIBLE);
			ivPlus.setVisibility(View.INVISIBLE);
			tvEdit.setOnClickListener(new onEditListener());
		} else {
			ivPlus.setVisibility(View.VISIBLE);
			tvEdit.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(
					R.layout.checkout_info_child_listview, null);
		}
		TextView tvInfo = (TextView) convertView.findViewById(R.id.tvInfo);
		tvInfo.setText(listChild.get(groupPosition));

		ImageView ivPayment = (ImageView) convertView
				.findViewById(R.id.ivPayment);

		if (groupPosition == 1)
			ivPayment.setVisibility(View.VISIBLE);
		else
			ivPayment.setVisibility(View.INVISIBLE);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public class onEditListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}

	}
}
