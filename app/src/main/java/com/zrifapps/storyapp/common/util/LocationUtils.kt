package com.zrifapps.storyapp.common.util

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

object LocationUtils {

    suspend fun getAddressFromLatLng(context: Context, lat: Double?, lon: Double?): String {
        if (lat == null || lon == null) return "Unknown location"

        return withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getAddressFromLatLngNew(context, lat, lon)
                } else {
                    getAddressFromLatLngOld(context, lat, lon)
                }
            } catch (e: Exception) {
                "Unknown location"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun getAddressFromLatLngNew(
        context: Context,
        lat: Double,
        lon: Double,
    ): String {
        return suspendCancellableCoroutine { continuation ->
            val geocoder = Geocoder(context, Locale.getDefault())
            geocoder.getFromLocation(lat,
                lon,
                1,
                @RequiresApi(Build.VERSION_CODES.TIRAMISU) object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<android.location.Address>) {
                        continuation.resume(
                            addresses.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
                        )
                    }

                    override fun onError(errorMessage: String?) {
                        continuation.resume("Unknown location")
                    }
                })
        }
    }


    @Suppress("DEPRECATION")
    private fun getAddressFromLatLngOld(context: Context, lat: Double, lon: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        return addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
    }
}
