package com.sw_ss16.lc_app.ui.learning_center_list;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.sw_ss16.lc_app.R;
import com.sw_ss16.lc_app.backend.RawMaterialFreezer;
import com.sw_ss16.lc_app.content.LearningCenter;
import com.sw_ss16.lc_app.content.LearningCenterDefroster;

import java.util.Calendar;

/**
 * Shows a list of all available quotes.
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class StudyRoomListFragment extends ListFragment {

    private Callback callback = dummyCallback;

    private LearningCenterDefroster lc_contentmanager = new LearningCenterDefroster();


    /**
     * A callback interface. Called whenever a item has been selected.
     */
    public interface Callback {
        void onItemSelected(String id);
    }

    /**
     * A dummy no-op implementation of the Callback interface. Only used when no active Activity is present.
     */
    private static final Callback dummyCallback = new Callback() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MyListAdapter());
        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        lc_contentmanager.setApplicationContext(getActivity().getApplicationContext());
        callback.onItemSelected(lc_contentmanager.getLcObject(lc_contentmanager.getListOfFavLcIds().get(position)).id);
    }

    /**
     * onAttach(Context) is not called on pre API 23 versions of Android.
     * onAttach(Activity) is deprecated but still necessary on older devices.
     */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /**
     * Deprecated on API 23 but still necessary for pre API 23 devices.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (!(context instanceof Callback)) {
            throw new IllegalStateException("Activity must implement callback interface.");
        }

        callback = (Callback) context;
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            lc_contentmanager.setApplicationContext(getActivity().getApplicationContext());

            return lc_contentmanager.getListOfFavLcIds().size();
        }

        @Override
        public LearningCenter getItem(int position) {
            lc_contentmanager.setApplicationContext(getActivity().getApplicationContext());
            return lc_contentmanager.getLcObject(lc_contentmanager.getListOfFavLcIds().get(position));
        }

        @Override
        public long getItemId(int position) {
            lc_contentmanager.setApplicationContext(getActivity().getApplicationContext());
            return Long.parseLong(lc_contentmanager.getListOfFavLcIds().get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_article, container, false);
            }

            final LearningCenter item = getItem(position);
            ((TextView) convertView.findViewById(R.id.article_title)).setText(item.name);
            ((TextView) convertView.findViewById(R.id.article_subtitle)).setText(item.description);
            setFullnessColor(convertView, item.id);
            final ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);
            Glide.with(getActivity()).load(item.image_out_url).asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });

            return convertView;
        }
    }

    public StudyRoomListFragment() {
    }

    public void setFullnessColor(View convertView, String id) {
        RawMaterialFreezer database = new RawMaterialFreezer(getActivity().getApplicationContext());
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        int current_day = calendar.get(Calendar.DAY_OF_WEEK);
        current_day--;
        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);

        int display_hour = current_hour;
        String meridiem = "";
        if (current_hour <= 12)
            meridiem = "AM";
        else if (current_hour > 12) {
            meridiem = "PM";
            display_hour -= 12;
        }

        String query_string = "LC_ID = " + id +
                " AND WEEKDAY = " + current_day +
                " AND HOUR = " + current_hour;
        String[] columns = new String[]{"ID", "LC_ID", "WEEKDAY", "HOUR", "FULLNESS"};

        Cursor cursor = sqLiteDatabase.query("statistics", columns, query_string, null, null, null, null);

        cursor.moveToFirst();
        boolean statistic_ok = true;
        System.out.println("Cursor: " + cursor.getCount());
        if (cursor.getCount() > 0) {

            String fullness = cursor.getString(cursor.getColumnIndex("FULLNESS"));
            int full = Integer.parseInt(fullness);

            String fullness_description = "";

            if (full >= 75) {
                System.out.println("Set Color Full");
                ((TextView) convertView.findViewById(R.id.article_fullness_color)).setBackgroundResource(R.color.red_full);
            } else {
                System.out.println("Set Color NOT Full");
                ((TextView) convertView.findViewById(R.id.article_fullness_color)).setBackgroundResource(R.color.green_empty);
            }
        }

        database.close();
        cursor.close();
    }
}
