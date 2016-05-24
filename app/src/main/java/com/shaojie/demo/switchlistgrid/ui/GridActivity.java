package com.shaojie.demo.switchlistgrid.ui;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaojie.demo.switchlistgrid.GridRecyclerView;
import com.shaojie.demo.switchlistgrid.Item;
import com.shaojie.demo.switchlistgrid.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GridActivity extends AppCompatActivity {

    @ColorInt
    private static final int[] BG_COLORS = {0xfff25f8c, 0xfffb7f77, 0xfffcc02c, 0xff2fcc87,
            0xff3dc2c7, 0xff47b2f8, 0xffb28bdc, 0xff948079, 0xff36393e};
    @DrawableRes
    private static final int[] BG_COVERS = {R.drawable.card_cover_a, R.drawable.card_cover_b, R.drawable.card_cover_c,
            R.drawable.card_cover_d, R.drawable.card_cover_e, R.drawable.card_cover_f};

    private GridRecyclerView mRecyclerView;
    private List<Item> mItems;
    private LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("GridActivity");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mItems = initData();

        mLayoutInflater = LayoutInflater.from(this);
        mRecyclerView = (GridRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        GridAdapter adapter = new GridAdapter();
        mRecyclerView.setAdapter(adapter);
    }

    private List<Item> initData() {
        List<Item> records = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Item record = new Item();
            record.setName("ITEM" + i);
            records.add(record);
        }
        return records;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Item item = (Item) v.getTag();
            if (item == null) {
                Snackbar.make(mRecyclerView, "ADD", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mRecyclerView, "ITEM:" + item.getName(), Snackbar.LENGTH_SHORT).show();
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

    private class GridAdapter extends RecyclerView.Adapter {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER ? manager.getSpanCount() : 1;
                }
            });
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
                v = mLayoutInflater.inflate(R.layout.recycleview_header, parent, false);
                return new HeaderHolder(v);
            } else {
                v = mLayoutInflater.inflate(R.layout.grid_item, parent, false);
                return new GridHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderHolder) {
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.headerView.setOnClickListener(mOnClickListener);
            } else {
                Item item = mItems.get(position - 1);// 减去header的位置
                @ColorInt int colorRes = getBgColor(position);
                GridHolder gridHolder = (GridHolder) holder;
                gridHolder.nameText.setText(item.getName());
                gridHolder.colorView.setBackgroundColor(colorRes);
                gridHolder.coverView.setImageResource(getBgCover(position));
                gridHolder.cardView.setTag(item);
                gridHolder.cardView.setOnClickListener(mOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            if (mItems == null || mItems.isEmpty()) {
                return 1;
            } else {
                return mItems.size() + 1;
            }
        }

        public class HeaderHolder extends RecyclerView.ViewHolder {

            public View headerView;

            public HeaderHolder(View v) {
                super(v);
                headerView = v.findViewById(R.id.groups_header);
            }
        }

        public class GridHolder extends RecyclerView.ViewHolder {

            public ImageView coverView;
            public TextView nameText;
            public View colorView;
            public CardView cardView;

            public GridHolder(View v) {
                super(v);
                cardView = (CardView) v.findViewById(R.id.grid_item_card_view);
                coverView = (ImageView) v.findViewById(R.id.grid_item_cover);
                nameText = (TextView) v.findViewById(R.id.grid_item_name);
                colorView = v.findViewById(R.id.grid_item_color);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
