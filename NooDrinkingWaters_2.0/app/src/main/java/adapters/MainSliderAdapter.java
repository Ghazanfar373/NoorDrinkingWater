package adapters;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {
    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        switch (position) {
            case 0:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/1.png");
                break;
            case 1:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/2.png");
                break;
            case 2:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/3.png");
                break;
            case 3:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/4.png");
                break;
            case 4:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/5.png");
                break;
            case 5:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/6.png");
                break;
            case 6:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/7.png");
                break;
            case 7:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/8.png");
                break;
            case 8:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/9.jpg");
                break;
            case 9:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/10.jpg");
                break;
            case 10:
                imageSlideViewHolder.bindImageSlide("http://new.noorwaters.com/assets/images/app_slider/11.jpg");
                break;
        }
    }
}
