package com.example.a18199.a16211160204niewei.Tab;


import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import android.preference.PreferenceFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a18199.a16211160204niewei.Activity.DownloadActivity;
import com.example.a18199.a16211160204niewei.Activity.SettingActivity;
import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Utils.DataCleanManager;
import com.example.a18199.a16211160204niewei.Utils.SPUtils;
import com.example.a18199.a16211160204niewei.Utils.ToastUtil;

import org.litepal.LitePal;

import java.io.File;

public class SettingFragment extends PreferenceFragment {
    private CheckBoxPreference checkBox_day;
    private CheckBoxPreference checkBox_no_pic;
    private ListPreference list_size;
    private PreferenceScreen pre_download;
    private PreferenceScreen pre_clear;
    private String TAG = "set";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void init() {
        checkBox_day = (CheckBoxPreference) findPreference("pre_day");
        checkBox_no_pic = (CheckBoxPreference) findPreference("pre_no_pic");
        list_size = (ListPreference) findPreference("pre_font_size");
        pre_download = (PreferenceScreen) findPreference("pre_download");
        pre_clear = (PreferenceScreen) findPreference("pre_clear");

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
        if (SPUtils.getData("font", "").equals("large")) {
            list_size.setValue("large");
        } else if (SPUtils.getData("font", "").equals("small")) {
            list_size.setValue("small");
        } else {
            list_size.setValue("medium");
        }
        list_size.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = newValue.toString();
                switch (value) {
                    case "medium":
                        getActivity().setTheme(R.style.Default_TextSize_Middle);
                        SPUtils.putData("font", "medium");
                        break;
                    case "small":
                        getActivity().setTheme(R.style.Default_TextSize_Small);
                        SPUtils.putData("font", "small");
                        break;
                    case "large":
                        getActivity().setTheme(R.style.Default_TextSize_Big);
                        SPUtils.putData("font", "large");
                        break;
                    default:
                        getActivity().setTheme(R.style.Default_TextSize_Middle);
                        SPUtils.putData("font", "medium");
                        break;
                }
                SendBroad();
                return true;
            }
        });
        pre_download.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), DownloadActivity.class));
                getActivity().overridePendingTransition(R.anim.alpha_out, R.anim.alpha_in);
                return true;
            }
        });
        try {
            pre_clear.setSummary("当前缓存为" + DataCleanManager.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pre_clear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LitePal.deleteAll("NewsDetail");
                String i = "";
                try {
                    i = DataCleanManager.getTotalCacheSize(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (i.equals("0.0Byte")) {
                    ToastUtil.showShortString(getActivity(), "缓存已清理！");
                } else {
                    DataCleanManager.clearAllCache(getActivity());
                    ToastUtil.showShortString(getActivity(), "清理成功");
                }
                try {
                    pre_clear.setSummary("当前缓存为" + DataCleanManager.getTotalCacheSize(getActivity()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                SendBroad();
                return true;
            }
        });
        checkBox_day.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!checkBox_day.isChecked()) {
                    SPUtils.putData("theme", "night");

                } else {
                    SPUtils.putData("theme", "day");
                }
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.alpha_out, R.anim.alpha_in);
                SendBroad();
                return true;
            }
        });
    }

    public void SendBroad() {
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("message", "recreate");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        Log.d("receivermessage", "Got message: 发送了");
    }
}
