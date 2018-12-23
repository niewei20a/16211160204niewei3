package com.example.a18199.a16211160204niewei;


import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a18199.a16211160204niewei.Activity.MainActivity;
import com.example.a18199.a16211160204niewei.Activity.SettingActivity;
import com.example.a18199.a16211160204niewei.Utils.SPUtils;
import com.example.a18199.a16211160204niewei.Utils.ToastUtil;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment {
    private CheckBoxPreference checkBox_day;
    private CheckBoxPreference checkBox_no_pic;
    private ListPreference list_size;
    private PreferenceScreen pre_download;
    private String TAG = "set";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        checkBox_day = (CheckBoxPreference) findPreference("pre_day");
        checkBox_no_pic = (CheckBoxPreference) findPreference("pre_no_pic");
        list_size = (ListPreference) findPreference("pre_font_size");
        pre_download = (PreferenceScreen) findPreference("pre_download");
        if (SPUtils.getData("theme", "").equals("night")) {
            checkBox_day.setChecked(true);
        } else if (SPUtils.getData("theme", "").equals("day")) {
            checkBox_day.setChecked(false);
        }
        if (SPUtils.getData("picture", "").equals("no")) {
            checkBox_no_pic.setChecked(true);
        } else if (SPUtils.getData("picture", "").equals("have")) {
            checkBox_no_pic.setChecked(false);
        }
        checkBox_day.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!checkBox_day.isChecked()) {
                    SPUtils.putData("theme", "night");
                    ToastUtil.showShort(getActivity(), "night");
                } else {
                    SPUtils.putData("theme", "day");
                    ToastUtil.showShort(getActivity(), "day");
                }
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim, R.anim.in);
                return true;
            }
        });
        checkBox_no_pic.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!checkBox_no_pic.isChecked()) {
                    SPUtils.putData("picture", "no");
                } else {
                    SPUtils.putData("picture", "have");
                }
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
