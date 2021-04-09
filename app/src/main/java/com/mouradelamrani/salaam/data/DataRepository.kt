package com.mouradelamrani.salaam.data

import com.mouradelamrani.salaam.R
import com.mouradelamrani.salaam.data.models.Surah
import com.mouradelamrani.salaam.data.models.Verse
import com.mouradelamrani.salaam.data.versas.*

object DataRepository {

    val surahs = listOf(
        Surah(
            "Al-Fatiha",
            R.string.surah_al_fatiha_description,
            R.drawable.ic_surah_al_fatiha,
            R.drawable.ic_surah_al_fatiha_logo
        ),
        Surah(
            "An-Nas",
            R.string.surah_an_nas_description,
            R.drawable.ic_surah_an_nas,
            R.drawable.ic_surah_an_nas_logo
        ),
        Surah(
            "Al-Falaq",
            R.string.surah_al_falaq_description,
            R.drawable.ic_surah_al_falaq,
            R.drawable.ic_surah_al_falaq_logo
        ),
        Surah(
            "Al-Ikhlas",
            R.string.surah_al_ikhlas_description,
            R.drawable.ic_surah_al_ikhlas,
            R.drawable.ic_surah_al_ikhlas_logo
        ),
        Surah(
            "Al-Masad",
            R.string.surah_al_masad_description,
            R.drawable.ic_surah_al_masad,
            R.drawable.ic_surah_al_masad_logo
        ),
        Surah(
            "An-Nasr",
            R.string.surah_an_nasr_description,
            R.drawable.ic_surah_an_nasr,
            R.drawable.ic_surah_an_nasr_logo
        ),
        Surah(
            "Al-Kafirun",
            R.string.surah_al_kafirun_description,
            R.drawable.ic_surah_al_kafirun,
            R.drawable.ic_surah_al_kafirun_logo
        ),
        Surah(
            "Al-Kawthar",
            R.string.surah_al_kawthar_description,
            R.drawable.ic_surah_al_kawthar,
            R.drawable.ic_surah_al_kawthar_logo
        ),
        Surah(
            "Al-Ma'un",
            R.string.surah_al_maun_description,
            R.drawable.ic_surah_al_maun,
            R.drawable.ic_surah_al_maun_logo
        ),
        Surah(
            "Quraysh",
            R.string.surah_quraysh_description,
            R.drawable.ic_surah_quraysh,
            R.drawable.ic_surah_quraysh_logo
        ),
        Surah(
            "Al-Fil",
            R.string.surah_al_fil_description,
            R.drawable.ic_surah_al_fil,
            R.drawable.ic_surah_al_fil_logo
        ),
        Surah(
            "Al-Humazah",
            R.string.surah_al_humazah_description,
            R.drawable.ic_surah_al_humazah,
            R.drawable.ic_surah_al_humazah_logo
        ),
        Surah(
            "Al-‘Asr",
            R.string.surah_al_asr_description,
            R.drawable.ic_surah_al_asr,
            R.drawable.ic_surah_al_asr_logo
        ),
        Surah(
            "At-Takathur",
            R.string.surah_at_takathur_description,
            R.drawable.ic_surah_at_takathur,
            R.drawable.ic_surah_at_takathur_logo
        ),
        Surah(
            "Al-Qari’ah",
            R.string.surah_al_qariah_description,
            R.drawable.ic_surah_al_qariah,
            R.drawable.ic_surah_al_qariah_logo
        ),
        Surah(
            "Al-‘Adiyat",
            R.string.surah_al_adiyat_description,
            R.drawable.ic_surah_al_adiyat,
            R.drawable.ic_surah_al_adiyat_logo
        ),
        Surah(
            "Az-Zalzalah",
            R.string.surah_az_zalzalah_description,
            R.drawable.ic_surah_az_zalzalah,
            R.drawable.ic_surah_az_zalzalah_logo
        ),
        Surah(
            "Al-Bayyinah",
            R.string.surah_al_bayyinah_description,
            R.drawable.ic_surah_al_bayyinah,
            R.drawable.ic_surah_al_bayyinah_logo
        ),
        Surah(
            "Al-Qadr",
            R.string.surah_al_qadr_description,
            R.drawable.ic_surah_al_qadr,
            R.drawable.ic_surah_al_qadr_logo
        ),
        Surah(
            "Al-‘Alaq",
            R.string.surah_al_alaq_description,
            R.drawable.ic_surah_al_alaq,
            R.drawable.ic_surah_al_alaq_logo
        ),
        Surah(
            "At-Tin",
            R.string.surah_at_tin_description,
            R.drawable.ic_surah_at_tin,
            R.drawable.ic_surah_at_tin_logo
        ),
        Surah(
            "Ash-Sharh",
            R.string.surah_ash_sharh_description,
            R.drawable.ic_surah_ash_sharh,
            R.drawable.ic_surah_ash_sharh_logo
        ),
        Surah(
            "Ad-Duhaa",
            R.string.surah_ad_duhaa_description,
            R.drawable.ic_surah_ad_duhaa,
            R.drawable.ic_surah_ad_duhaa_logo
        ),
        Surah(
            "Al-Layl",
            R.string.surah_al_layl_description,
            R.drawable.ic_surah_al_layl,
            R.drawable.ic_surah_al_layl_logo
        ),
        Surah(
            "Ash-Shams",
            R.string.surah_ash_shams_description,
            R.drawable.ic_surah_ash_shams,
            R.drawable.ic_surah_ash_shams_logo
        ),
        Surah(
            "Al-Balad",
            R.string.surah_al_balad_description,
            R.drawable.ic_surah_al_balad,
            R.drawable.ic_surah_al_balad_logo
        ),
        Surah(
            "Al-Fajr",
            R.string.surah_al_fajr_description,
            R.drawable.ic_surah_al_fajr,
            R.drawable.ic_surah_al_fajr_logo
        ),
        Surah(
            "Al-Ghashiyah",
            R.string.surah_al_ghashiyah_description,
            R.drawable.ic_surah_al_ghashiyah,
            R.drawable.ic_surah_al_ghashiyah_logo
        ),
        Surah(
            "Al-A’la",
            R.string.surah_al_ala_description,
            R.drawable.ic_surah_al_ala,
            R.drawable.ic_surah_al_ala_logo
        ),
        Surah(
            "At-Tariq",
            R.string.surah_at_tariq_description,
            R.drawable.ic_surah_at_tariq,
            R.drawable.ic_surah_at_tariq_logo
        ),
        //i start work fff
            Surah(
                    "Al-Buruj",
                    R.string.surah_Al_Burooj,
                    R.drawable.ic_surah_al_buruj,
                    R.drawable.ic_surah_al_buruj_logo
            ),
            Surah(
                    "Al-Inshiqaq",
                    R.string.surah_Al_Inshiqaq,
                    R.drawable.ic_surah_al_inshiqaq,
                    R.drawable.ic_surah_al_inshiqaq_logo
            ),
            Surah(
                    "Al-Mutaffifin",
                    R.string.surah_Al_Mutaffifin,
                    R.drawable.ic_surah_al_mutaffifin,
                    R.drawable.ic_surah_al_mutaffifin_logo
            ),

            Surah(

                    "Al-Infitar",
                    R.string.surah_Al_Infitaar,
                    R.drawable.ic_surah_al_infitar,
                    R.drawable.ic_surah_al_infitar_logo
            ),
            Surah(
                    "At-Takwir",
                    R.string.surah_At_Takwir,
                    R.drawable.ic_surah_at_takwir,
                    R.drawable.ic_surah_at_takwir_logo
            ),
            Surah(
                    "'Abasa",
                    R.string.surah_Abasa,
                    R.drawable.ic_surah_abasa,
                    R.drawable.ic_surah_abasa_logo
            ),
            Surah(
                    "An-Nazi’at",
                    R.string.surah_an_naziat,
                    R.drawable.ic_surah_an_nazi_at,
                    R.drawable.ic_surah_an_nazi_at_logo
            ),
        Surah(
            "An-Naba",
            R.string.surah_an_naba,
            R.drawable.ic_surah_an_naba,
            R.drawable.ic_surah_an_naba_logo
        )
    )

    val verses = mapOf<String, List<Verse>>(
        "Al-Fatiha" to AlFatihaVerses.verses,
        "An-Nas" to AnNasVerses.verses,
        "Al-Falaq" to AlFalaqVerses.verses,
        "Al-Ikhlas" to AlIkhlasVerses.verses,
        "Al-Masad" to AlMasadVerses.verses,
        "An-Nasr" to AnNasrVerses.verses,
        "Al-Kafirun" to AlKafirunVerses.verses,
        "Al-Kawthar" to AlKawtharVerses.verses,
        "Al-Ma'un" to AlMaunVerses.verses,
        "Quraysh" to QurayshVerses.verses,
        "Al-Fil" to AlFilVerses.verses,
        "Al-Humazah" to AlHumazahVerses.verses,
        "Al-‘Asr" to AlAsrVerses.verses,
        "At-Takathur" to AtTakathurVerses.verses,
        "Al-Qari’ah" to AlQariahVerses.verses,
        "Al-‘Adiyat" to AlAdiyatVerses.verses,
        "Az-Zalzalah" to AzZalzalahVerses.verses,
        "Al-Bayyinah" to AlBayyinahVerses.verses,
        "Al-Qadr" to AlQadrVerses.verses,
        "Al-‘Alaq" to AlAlaqVerses.verses,
        "At-Tin" to AtTinVerses.verses,
        "Ash-Sharh" to AshSharhVerses.verses,
        "Ad-Duhaa" to AdDuhaaVerses.verses,
        "Al-Layl" to AlLaylVerses.verses,
        "Ash-Shams" to AshShamsVerses.verses,
        "Al-Balad" to AlBaladVerses.verses,
        "Al-Fajr" to AlFajrVerses.verses,
        "Al-Ghashiyah" to AlGhashiyahVerses.verses,
        "Al-A’la" to AlAlaVerses.verses,
        "At-Tariq" to AtTariqVerses.verses,
        //i start work now
       "An-Naba"  to AnNaba.verses,
        "An-Nazi’at" to AnNaziat.verses,
         "'Abasa" to AbasaVerses.verses,
        "At-Takwir" to AtTakwirVerses.verses,
        "Al-Infitar" to AlinfitaarVerses.verses,
        "Al-Mutaffifin" to AlMutaffifinVerses.verses,
        "Al-Inshiqaq"  to AlInshiqaqVerses.verses,
        "Al-Buruj" to AlBuroojVerses.verses


        )


    fun getSurah(name: String): Surah? = surahs.firstOrNull { s -> s.name == name }

}