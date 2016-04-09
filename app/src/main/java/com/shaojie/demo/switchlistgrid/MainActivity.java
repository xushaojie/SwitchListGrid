package com.shaojie.demo.switchlistgrid;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @ColorInt
    private static final int[] BG_COLORS = {0xfff25f8c, 0xfffb7f77, 0xfffcc02c, 0xff2fcc87,
            0xff3dc2c7, 0xff47b2f8, 0xffb28bdc, 0xff948079, 0xff36393e};
    @DrawableRes
    private static final int[] BG_COVERS = {R.drawable.card_cover_a, R.drawable.card_cover_b};

    private GroupsRecyclerView mRecyclerView;
    private GroupAdapter mAdapter;
    private List<GroupRecord> mGroupRecords;
    private int mMode;

    private LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGroupRecords = createData();
        mMode = getMode();

        mLayoutInflater = LayoutInflater.from(this);
        mRecyclerView = (GroupsRecyclerView) findViewById(R.id.recycler_view_books);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    private List<GroupRecord> createData() {
        List<GroupRecord> records = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GroupRecord record = new GroupRecord();
            record.setGroupName("item" + i);
            records.add(record);
        }
        return records;
    }

    private int getMode() {
        return SharedPreferUtil.get("sp_key_groups_mode", GroupsRecyclerView.MODE_LIST);
    }

    private void updateMode() {
        SharedPreferUtil.put("sp_key_groups_mode", mMode);
    }

    private void toggleDisplayMode() {
        mMode = mRecyclerView.toggleMode();
        mAdapter = new GroupAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scheduleLayoutAnimation();
        updateMode();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GroupRecord item = (GroupRecord) v.getTag();
            if (item == null) {
                Snackbar.make(mRecyclerView, "ADD", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mRecyclerView, "ITEM:" + item.getGroupName(), Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    private int getBgColor(int position) {
        int index = position % BG_COLORS.length;
        return BG_COLORS[index];
    }

    private int getBgCover(int position) {
        int index = position % BG_COVERS.length;
        return BG_COVERS[index];
    }

    private class GroupAdapter extends RecyclerView.Adapter {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            if (mMode == GroupsRecyclerView.MODE_GRID) {
                final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return getItemViewType(position) == TYPE_HEADER ? manager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (TYPE_HEADER == viewType) {
                v = mLayoutInflater.inflate(R.layout.group_list_header, parent, false);
                return new HeaderHolder(v);
            } else {
                switch (mMode) {
                    case GroupsRecyclerView.MODE_LIST:
                        v = mLayoutInflater.inflate(R.layout.groups_item_list, parent, false);
                        return new ListHolder(v);
                    case GroupsRecyclerView.MODE_GRID:
                        v = mLayoutInflater.inflate(R.layout.groups_item_grid, parent, false);
                        return new GridHolder(v);
                }
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderHolder) {
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.headerView.setOnClickListener(mOnClickListener);
            } else {
                GroupRecord item = mGroupRecords.get(position - 1);// 减去header的位置
                @ColorInt int colorRes = getBgColor(position);
                if (holder instanceof ListHolder) {
                    ListHolder listHolder = (ListHolder) holder;
                    listHolder.nameText.setText(item.getGroupName());
                    listHolder.colorView.setBackgroundColor(colorRes);
                    listHolder.cardView.setTag(item);
                    listHolder.cardView.setOnClickListener(mOnClickListener);
                } else if (holder instanceof GridHolder) {
                    GridHolder gridHolder = (GridHolder) holder;
                    gridHolder.nameText.setText(item.getGroupName());
                    gridHolder.colorView.setBackgroundColor(colorRes);
                    gridHolder.coverView.setImageResource(getBgCover(position));
                    gridHolder.cardView.setTag(item);
                    gridHolder.cardView.setOnClickListener(mOnClickListener);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mGroupRecords == null || mGroupRecords.isEmpty()) {
                return 1;
            } else {
                return mGroupRecords.size() + 1;
            }
        }

        public class HeaderHolder extends RecyclerView.ViewHolder {

            public View headerView;

            public HeaderHolder(View v) {
                super(v);
                headerView = v.findViewById(R.id.groups_header);
            }
        }

        public class ListHolder extends RecyclerView.ViewHolder {

            public ImageView avatarView;
            public TextView nameText;
            public View colorView;
            public CardView cardView;

            public ListHolder(View v) {
                super(v);
                cardView = (CardView) v.findViewById(R.id.group_item_card_view);
                avatarView = (ImageView) v.findViewById(R.id.group_item_avatar);
                nameText = (TextView) v.findViewById(R.id.group_item_name);
                colorView = v.findViewById(R.id.group_item_color);
            }
        }

        public class GridHolder extends RecyclerView.ViewHolder {

            public ImageView coverView;
            public TextView nameText;
            public View colorView;
            public CardView cardView;

            public GridHolder(View v) {
                super(v);
                cardView = (CardView) v.findViewById(R.id.group_item_card_view);
                coverView = (ImageView) v.findViewById(R.id.group_item_cover);
                nameText = (TextView) v.findViewById(R.id.group_item_name);
                colorView = v.findViewById(R.id.group_item_color);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main_groups_menu, menu);
        if (mMode == GroupsRecyclerView.MODE_LIST) {
            menu.findItem(R.id.menu_item_groups_mode).setIcon(R.drawable.ic_action_group_grid);
        } else {
            menu.findItem(R.id.menu_item_groups_mode).setIcon(R.drawable.ic_action_group_list);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_groups_mode:
                toggleDisplayMode();
                invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
