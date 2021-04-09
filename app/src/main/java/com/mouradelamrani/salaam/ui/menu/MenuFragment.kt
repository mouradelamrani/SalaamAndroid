package com.mouradelamrani.salaam.ui.menu

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.mouradelamrani.salaam.BuildConfig
import com.mouradelamrani.salaam.Links
import com.mouradelamrani.salaam.LocaleHelper
import com.mouradelamrani.salaam.R
import com.mouradelamrani.salaam.extensions.dpToPx
import com.mouradelamrani.salaam.extensions.observe
import com.mouradelamrani.salaam.extensions.setVisible
import com.mouradelamrani.salaam.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_menu.*
import java.util.*

class MenuFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_menu

    private lateinit var localeHelper: LocaleHelper
    private lateinit var viewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localeHelper = LocaleHelper.getInstance(requireContext())
    }

    override fun initViews() {
        buttonLanguage.setOnClickListener { viewModel.toggleLanguagesVisible() }
        buttonFeedback.setOnClickListener { sendEmailFeedback() }
        buttonRating.setOnClickListener { openGooglePlay() }
        ivLanguageEn.setOnClickListener { if (!localeHelper.isCurrentLocaleEnglish) updateLanguage(true) }
        ivLanguageNl.setOnClickListener { if (localeHelper.isCurrentLocaleEnglish) updateLanguage(false) }
        updateLanguageView()
    }

    override fun initViewModels() {
        viewModel = ViewModelProvider(baseActivity).get(MenuViewModel::class.java)
    }

    override fun observeViewModels() {
        viewModel.languagesVisible.observe(viewLifecycleOwner, this::showLayoutLanguages)
    }

    private fun showLayoutLanguages(visible: Boolean) {
        layoutLanguages.setVisible(visible)
        buttonFeedback.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = if (visible) 0 else dpToPx(50)
        }
    }

    private fun updateLanguage(isEnglish: Boolean) {
        localeHelper.setNewLocale(if (isEnglish) Locale.US else Locale("nl"))
        baseActivity.recreate()
    }

    private fun updateLanguageView() {
        val isEnglish = localeHelper.isCurrentLocaleEnglish
        ivLanguageEn.isSelected = isEnglish
        tvLanguageEnTitle.setVisible(isEnglish)
        tvLanguageEnText.setVisible(isEnglish)
        ivLanguageNl.isSelected = !isEnglish
        tvLanguageNlTitle.setVisible(!isEnglish)
        tvLanguageNlText.setVisible(!isEnglish)
    }

    private fun sendEmailFeedback() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Links.SUPPORT_EMAIL, null))
        emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.feedback_mail_subject_s, getString(R.string.app_name))
        )
        startActivity(Intent.createChooser(emailIntent, getString(R.string.fragment_menu_feedback)))
    }

    private fun openGooglePlay() {
        val googlePlayAppId = BuildConfig.APPLICATION_ID
        val uri = Uri.parse("market://details?id=" + googlePlayAppId)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            requireContext().startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            requireContext().startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + googlePlayAppId)))
        }
    }

}