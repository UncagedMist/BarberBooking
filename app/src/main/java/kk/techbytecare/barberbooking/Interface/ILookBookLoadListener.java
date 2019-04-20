package kk.techbytecare.barberbooking.Interface;

import java.util.List;

import kk.techbytecare.barberbooking.Model.Banner;

public interface ILookBookLoadListener {
    void onLookBookLoadSuccess(List<Banner> banners);
    void onLookBookLoadFailed(String message);
}
