package kk.techbytecare.barberbooking.Interface;

import java.util.List;

import kk.techbytecare.barberbooking.Model.Banner;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
