/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.espresso.DataAdapterSample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An activity displaying a long list with a text view and a toggle button. The last clicked row is
 * displayed at the top.
 */
public class LongListActivity extends Activity {
    @VisibleForTesting
    protected static final String ROW_TEXT = "ROW_TEXT";
    @VisibleForTesting
    protected static final String ROW_ENABLED = "ROW_ENABLED";
    @VisibleForTesting
    protected static final int NUMBER_OF_ITEMS = 100;
    @VisibleForTesting
    protected static final String ITEM_TEXT_FORMAT = "item: %d";
    private List<Map<String, Object>> data = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ListView mListView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.list_activity);

        //初始化数据
        populateData();
        mListView = (ListView) findViewById(R.id.list);
        String[] from = new String[]{ROW_TEXT, ROW_ENABLED};
        int[] to = new int[]{R.id.rowContentTextView, R.id.rowToggleButton};
        layoutInflater = getLayoutInflater();
        ListAdapter adapter = new LongListAdapter(from, to);
        mListView.setAdapter(adapter);
    }

    @VisibleForTesting
    protected static Map<String, Object> makeItem(int forRow) {
        Map<String, Object> dataRow = Maps.newHashMap();
        dataRow.put(ROW_TEXT, String.format(ITEM_TEXT_FORMAT, forRow));
        dataRow.put(ROW_ENABLED, forRow == 1);
        return dataRow;
    }

    private void populateData() {
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            data.add(makeItem(i));
        }
    }

    private class LongListAdapter extends SimpleAdapter {
        public LongListAdapter(String[] from, int[] to) {
            super(LongListActivity.this, LongListActivity.this.data, R.layout.list_item, from, to);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TextView) findViewById(R.id.selection_row_value)).setText(
                            String.valueOf(position));
                }
            });
            return super.getView(position, convertView, parent);
        }
    }
}