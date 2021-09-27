package com.example.yandexweatherwork.repository.facadeuser

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.yandexweatherwork.repository.ConstantsRepository
import com.squareup.picasso.Picasso

class GetAndSetImage {

    fun inflateImage(
        imageView: ImageView,
        iconCode: String?
    ) {
        imageView.loadUrl("https://yastatic.net/weather/i/icons/blueye/color/svg/$iconCode.svg")
    }

    fun inflateImage(imageView: ImageView) {
        when(Math.round(Math.random() * 1)) {
            0L ->
                Glide
                    .with(imageView)
                    .load(ConstantsRepository.CITY_IMAGE_LINK)
                    .into(imageView)
            1L ->
                Picasso
                    .get()
                    .load(ConstantsRepository.CITY_IMAGE_LINK)
                    .into(imageView)
        }
    }

    private fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }
}