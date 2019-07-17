package com.pac.ideatech.noordrinkingwater;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {
    @Override
    public int getItemCount() {
        return 7;
    }


//
//
//

//
//    http://codingbiz.com/projects/new_erp/images/app_slider/7.png
//    http://codingbiz.com/projects/new_erp/images/app_slider/8.png
//    http://codingbiz.com/projects/new_erp/images/app_slider/9.jpg
//
//    http://codingbiz.com/projects/new_erp/images/app_slider/11.jpg
    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        switch (position) {
            case 0: imageSlideViewHolder.bindImageSlide("http://codingbiz.com/projects/new_erp/images/app_slider/8.png");
                break;
            case 1:
                imageSlideViewHolder.bindImageSlide("http://codingbiz.com/projects/new_erp/images/app_slider/5.png");
                break;
            case 2:
                imageSlideViewHolder.bindImageSlide("http://codingbiz.com/projects/new_erp/images/app_slider/1.png");
                break;
            case 3:
                imageSlideViewHolder.bindImageSlide(" http://codingbiz.com/projects/new_erp/images/app_slider/10.jpg");
                break;
            case 4:
                imageSlideViewHolder.bindImageSlide("http://codingbiz.com/projects/new_erp/images/app_slider/3.png");
                break;
            case 5:
                imageSlideViewHolder.bindImageSlide("http://codingbiz.com/projects/new_erp/images/app_slider/4.png");
                break;
            case 6:
                imageSlideViewHolder.bindImageSlide("http://codingbiz.com/projects/new_erp/images/app_slider/6.png");
                break;
        }
    }
}
