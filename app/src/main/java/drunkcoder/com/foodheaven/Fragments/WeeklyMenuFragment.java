package drunkcoder.com.foodheaven.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Button;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.R;

public class WeeklyMenuFragment extends Fragment {
    Button sunButton,monButton,tuesButton,thrusButton,wedButton,friButton,satButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekly_menu_fragment, container, false);
        sunButton=view.findViewWithTag("Sun");
        monButton=view.findViewWithTag("Mon");
        tuesButton=view.findViewWithTag("Tues");
        wedButton=view.findViewWithTag("Wed");
        thrusButton=view.findViewWithTag("Thrus");
        friButton=view.findViewWithTag("Fri");
        satButton=view.findViewWithTag("Sat");

        return view;
    }

    public static WeeklyMenuFragment newInstance() {
        
        Bundle args = new Bundle();
        
        WeeklyMenuFragment fragment = new WeeklyMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
