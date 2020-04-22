package com.sai.googleadssample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadNativeAd()
        //loadCustomTemplateAd()
    }

    private fun loadNativeAd() {
        var startTime = 0L
        var endTime: Long

        val adUnitId = "/21818577193/standard_native_test_ad_unit"
        //val adUnitId = "/21818577193/standard_native_test_ad"

        val adLoader = AdLoader.Builder(this, adUnitId)
            .forUnifiedNativeAd { ad : UnifiedNativeAd ->
                endTime = System.currentTimeMillis()
                val msg = "Unified native ad loaded $ad in ${endTime - startTime} ms"
                //text_main.text = msg
                Log.d(TAG, msg)
                renderNativeAd(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    endTime = System.currentTimeMillis()
                    val msg = "Ad failed to load with error $errorCode in ${endTime - startTime} ms"
                    //text_main.text = msg
                    Log.e(TAG, msg)
                }
            })
            .build()

        startTime = System.currentTimeMillis()
        adLoader.loadAd(PublisherAdRequest.Builder().build())

    }

    private fun renderNativeAd(nativeAd: UnifiedNativeAd) {
        val adView = layoutInflater.inflate(R.layout.ad_unified, null) as UnifiedNativeAdView
// Set the media view.
        adView.mediaView = adView.findViewById(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

        linear_layout_ad_parent_view.removeAllViews()
        linear_layout_ad_parent_view.addView(adView)
    }

    private fun loadCustomTemplateAd() {
        var startTime = 0L
        var endTime: Long

        val adUnitId = "/21818577193/standard_native_test_ad"
        val id = "21974839810"
        val adLoader = AdLoader.Builder(this, adUnitId)
            .forCustomTemplateAd(id,
                { ad ->
                    endTime = System.currentTimeMillis()
                    val msg = "Unified native ad loaded $ad in ${endTime - startTime} ms"
                    //text_main.text = msg
                    Log.d(TAG, msg)
                },
                { ad, s ->
                    Log.d(TAG, "Ad clicked")
                })
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    endTime = System.currentTimeMillis()
                    val msg = "Ad failed to load with error $errorCode in ${endTime - startTime} ms"
                    //text_main.text = msg
                    Log.e(TAG, msg)
                }
            })
            .build()

        adLoader.loadAd(PublisherAdRequest.Builder().build())
    }
}
