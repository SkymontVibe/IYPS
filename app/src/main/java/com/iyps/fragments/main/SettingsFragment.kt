/*
 *     Copyright (C) 2022-present StellarSand
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.iyps.fragments.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.iyps.BuildConfig
import com.iyps.R
import com.iyps.activities.MainActivity
import com.iyps.appmanager.ApplicationManager
import com.iyps.databinding.FragmentSettingsBinding
import com.iyps.fragments.bottomsheets.LicensesBottomSheet
import com.iyps.fragments.bottomsheets.SupportMethodsBottomSheet
import com.iyps.fragments.bottomsheets.ThemeBottomSheet
import com.iyps.preferences.PreferenceManager.Companion.BLOCK_SS
import com.iyps.preferences.PreferenceManager.Companion.INCOG_KEYBOARD
import com.iyps.preferences.PreferenceManager.Companion.MATERIAL_YOU
import com.iyps.utils.IntentUtils.Companion.openURL

class SettingsFragment : Fragment() {
    
    private var _binding: FragmentSettingsBinding? = null
    private val fragmentBinding get() = _binding!!
    
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return fragmentBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        
        val preferenceManager = (requireContext().applicationContext as ApplicationManager).preferenceManager
        val mainActivity = requireActivity() as MainActivity
        
        // Version
        @SuppressLint("SetTextI18n")
        fragmentBinding.version.text = "${getString(R.string.app_version)}: ${BuildConfig.VERSION_NAME}"
        
        // Theme
        fragmentBinding.theme.setOnClickListener {
                ThemeBottomSheet().show(parentFragmentManager, "ThemeBottomSheet")
            }
        
        // Material You
        fragmentBinding.materialYouSwitch.apply {
            if (Build.VERSION.SDK_INT >= 31) {
                isVisible = true
                isChecked = preferenceManager.getBoolean(MATERIAL_YOU, defValue = false)
                setOnCheckedChangeListener { _, isChecked ->
                    preferenceManager.setBoolean(MATERIAL_YOU, isChecked)
                }
            }
        }
        
        // Block screenshots
        fragmentBinding.blockScreenshotsSwitch.apply {
            isChecked = preferenceManager.getBoolean(BLOCK_SS)
            setOnCheckedChangeListener { _, isChecked ->
                preferenceManager.setBoolean(BLOCK_SS, isChecked)
                when (isChecked) {
                    true -> mainActivity.window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                                                              WindowManager.LayoutParams.FLAG_SECURE)
                    false -> mainActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }
        }
        
        // Incognito keyboard
        fragmentBinding.incognitoKeyboardSwitch.apply {
            isChecked = preferenceManager.getBoolean(INCOG_KEYBOARD)
            setOnCheckedChangeListener { _, isChecked ->
                preferenceManager.setBoolean(INCOG_KEYBOARD, isChecked)
            }
        }
    
        // Privacy policy
        fragmentBinding.privacyPolicy.setOnClickListener {
                openURL(mainActivity,
                        getString(R.string.iyps_privacy_policy_url),
                        mainActivity.activityBinding.mainCoordLayout,
                        mainActivity.activityBinding.mainBottomNav)
            }
    
        // Report an issue
        fragmentBinding.reportIssue.setOnClickListener {
            openURL(mainActivity,
                    getString(R.string.iyps_issues_url),
                    mainActivity.activityBinding.mainCoordLayout,
                    mainActivity.activityBinding.mainBottomNav)
        }
        
        // Support
        fragmentBinding.support.setOnClickListener {
            SupportMethodsBottomSheet().show(parentFragmentManager, "SupportBottomSheet")
        }
    
        // View on GitHub
        fragmentBinding.viewOnGit
            .setOnClickListener {
                openURL(mainActivity,
                        getString(R.string.iyps_github_url),
                        mainActivity.activityBinding.mainCoordLayout,
                        mainActivity.activityBinding.mainBottomNav)
            }
        
        // Third party licenses
        fragmentBinding.licenses.setOnClickListener {
            LicensesBottomSheet().show(parentFragmentManager, "LicensesBottomSheet")
        }
        
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}