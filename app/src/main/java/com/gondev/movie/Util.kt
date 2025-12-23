package com.gondev.movie

fun makeImgPath(imgName: String, width: String = "w500"): String {
    return "https://image.tmdb.org/t/p/${width}${imgName}";
}