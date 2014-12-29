package com.example.androidsocketsserver;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AndroidSocketsSenderForJapan extends Activity {
	static final String[] PREFS = { "北海道", "青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県", "茨城県", "栃木県", "群馬県", "埼玉県", "千葉県", "東京都", "神奈川県", "新潟県", "富山県", "石川県", "福井県", "山梨県", "長野県", "岐阜県", "静岡県", "愛知県", "三重県", "滋賀県", "京都府", "大阪府", "兵庫県", "奈良県", "和歌山県", "鳥取県", "島根県", "岡山県", "広島県", "山口県", "徳島県", "香川県",
			"愛媛県", "高知県", "福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県" };

	int mDraggingPosition = -1;
	SampleAdapter mAdapter;
	SortableListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.senderforjapan);
		mAdapter = new SampleAdapter();
		mListView = (SortableListView) findViewById(R.id.list);
		mListView.setDragListener(new DragListener());
		mListView.setSortable(true);
		mListView.setAdapter(mAdapter);
	}

	class SampleAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return PREFS.length;
		}

		@Override
		public String getItem(int position) {
			return PREFS[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			}
			final TextView view = (TextView) convertView;
			view.setText(PREFS[position]);
			view.setVisibility(position == mDraggingPosition ? View.INVISIBLE : View.VISIBLE);
			return convertView;
		}
	}

	class DragListener extends SortableListView.SimpleDragListener {
		@Override
		public int onStartDrag(int position) {
			mDraggingPosition = position;
			mListView.invalidateViews();
			return position;
		}

		@Override
		public int onDuringDrag(int positionFrom, int positionTo) {
			if (positionFrom < 0 || positionTo < 0 || positionFrom == positionTo) {
				return positionFrom;
			}
			int i;
			if (positionFrom < positionTo) {
				final int min = positionFrom;
				final int max = positionTo;
				final String data = PREFS[min];
				i = min;
				while (i < max) {
					PREFS[i] = PREFS[++i];
				}
				PREFS[max] = data;
			} else if (positionFrom > positionTo) {
				final int min = positionTo;
				final int max = positionFrom;
				final String data = PREFS[max];
				i = max;
				while (i > min) {
					PREFS[i] = PREFS[--i];
				}
				PREFS[min] = data;
			}
			mDraggingPosition = positionTo;
			mListView.invalidateViews();
			return positionTo;
		}

		@Override
		public boolean onStopDrag(int positionFrom, int positionTo) {
			mDraggingPosition = -1;
			mListView.invalidateViews();
			return super.onStopDrag(positionFrom, positionTo);
		}
	}
}