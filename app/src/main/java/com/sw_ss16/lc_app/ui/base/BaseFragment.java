package com.sw_ss16.lc_app.ui.base;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sw_ss16.lc_app.util.LogUtil;

import butterknife.ButterKnife;

import static com.sw_ss16.lc_app.util.LogUtil.makeLogTag;

/**
 * The base class for all fragment classes.
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class BaseFragment extends Fragment {

    private static final String TAG = makeLogTag(BaseFragment.class);

    /**
     * Inflates the layout and binds the view via ButterKnife.
     *
     * @param inflater  the inflater
     * @param container the layout container
     * @param layout    the layout resource
     * @return the inflated view
     */
    public View inflateAndBind(LayoutInflater inflater, ViewGroup container, int layout) {
        View view = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, view);

        LogUtil.logD(TAG, ">>> view inflated");
        return view;
    }
}
