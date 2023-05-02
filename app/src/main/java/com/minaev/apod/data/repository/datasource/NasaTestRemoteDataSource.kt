package com.minaev.apod.data.repository.datasource

import com.minaev.apod.data.remote.model.ApodResponse
import kotlinx.coroutines.delay

class NasaTestRemoteDataSource: INasaRemoteDataSource {

    override suspend fun getApodToday() : ApodResponse{
        return apodResponse
    }

    override suspend fun getApodList(count: Int): List<ApodResponse> {
        delay(2000)
        return listOf(apodResponse, apodResponse2)
    }

    private val apodResponse = ApodResponse(
            "KPNO",
            "2004-04-18",
            "Astronomers divide stars into different  spectral types. First started in the 1800s, the spectral type was originally meant to classify the strength of hydrogen absorption lines. A few types that best describe the temperature of the star remain in use today.  The seven main spectral types OBAFGKM are shown above with the spectrum of a single \"O\" star at the top followed by two spectra each from the progressively cooler designations, respectively.  Historically, these letters have been remembered with the mnemonic \"Oh Be A Fine Girl/Guy Kiss Me.\"  Frequent classroom contests, however, have come up with other more/less politically correct mnemonics such as \"Oven Baked Apples From Grandpa's/Grandma's Kitchen. Mmmm.\"  Our Sun has spectral type \"G\".",
            "https://apod.nasa.gov/apod/image/0105/obafgkm_noao_big.jpg",
            "image",
            "v1",
            "Stellar Spectral Types: OBAFGKM",
            "https://apod.nasa.gov/apod/image/0105/obafgkm_noao.jpg"
        )

    private val apodResponse2 = ApodResponse(
        "INAOEPAURANOAONSF",
        "2003-10-12",
        "Almost every object in the above photograph is a galaxy.  The Coma Cluster of Galaxies pictured above is one of the densest clusters known - it contains thousands of galaxies.  Each of these galaxies houses billions of stars - just as our own Milky Way Galaxy does.  Although nearby when compared to most other clusters, light from the Coma Cluster still takes hundreds of millions of years to reach us.  In fact, the Coma Cluster is so big it takes light millions of years just to go from one side to the other!  Most galaxies in Coma and other clusters are  ellipticals, while most galaxies outside of clusters are spirals.  The nature of Coma's X-ray emission is still being investigated.",
        "https://apod.nasa.gov/apod/image/0008/coma_noao_big.jpg",
        "image",
        "v1",
        "The Coma Cluster of Galaxies",
        "https://apod.nasa.gov/apod/image/0008/coma_noao.jpg"
    )
}