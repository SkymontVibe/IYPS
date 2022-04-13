package com.iyps.fragments.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.iyps.R;

import java.util.Objects;

public class AboutFragment extends Fragment {

    String version;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_settings_about, container,  false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // VERSION
        try {
            version = getResources().getString(R.string.app_version)
                          + " "
                          + requireContext().getPackageManager()
                                             .getPackageInfo(requireContext()
                                             .getPackageName(), 0)
                                             .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ((TextView)view.findViewById(R.id.version)).setText(version);

        // AUTHORS
        view.findViewById(R.id.authors_holder)
                .setOnClickListener(v ->
                        AuthorsBottomSheet());

        // PRIVACY POLICY
        view.findViewById(R.id.privacy_policy_holder)
                .setOnClickListener(v ->
                        OpenURL(requireActivity(), "https://github.com/the-weird-aquarian/IYPS/blob/master/PRIVACY.md"));

        // LICENSES
        view.findViewById(R.id.licenses_holder)
                .setOnClickListener(v ->
                        OpenURL(requireActivity(), "https://github.com/the-weird-aquarian/IYPS/blob/master/LICENSE"));

        // VIEW ON GITHUB
        view.findViewById(R.id.view_on_git_holder)
                .setOnClickListener(v ->
                        OpenURL(requireActivity(), "https://github.com/the-weird-aquarian/IYPS"));

    }

    // AUTHORS BOTTOM SHEET
    private void AuthorsBottomSheet(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.CustomBottomSheetTheme);
        bottomSheetDialog.setCancelable(true);

        @SuppressLint("InflateParams") View view  = getLayoutInflater().inflate(R.layout.bottom_sheet_authors, null);
        bottomSheetDialog.setContentView(view);

        // TITLE
        ((TextView)(view.findViewById(R.id.bottom_sheet_title))).setText(getString(R.string.authors));

        // AUTHOR 1
        view.findViewById(R.id.author_1).setOnClickListener(v -> {
            OpenURL(requireActivity(), "https://github.com/the-weird-aquarian");
            bottomSheetDialog.dismiss();
        });

        // AUTHOR 2
        view.findViewById(R.id.author_2).setOnClickListener(v -> {
            OpenURL(requireActivity(), "https://github.com/parveshnarwal");
            bottomSheetDialog.dismiss();
        });

        // SHOW BOTTOM SHEET WITH CUSTOM ANIMATION
        Objects.requireNonNull(bottomSheetDialog.getWindow()).getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        bottomSheetDialog.show();
    }

    // OPEN LINKS
    public static void OpenURL(Activity activityFrom, String URL) {

        try
        {
            activityFrom.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
        }
        // IF BROWSERS NOT INSTALLED, SHOW TOAST
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(activityFrom, activityFrom.getResources().getString(R.string.no_browsers), Toast.LENGTH_SHORT).show();
        }

    }

}