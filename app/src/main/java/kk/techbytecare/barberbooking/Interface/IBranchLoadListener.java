package kk.techbytecare.barberbooking.Interface;

import java.util.List;

import kk.techbytecare.barberbooking.Model.Salon;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> salonList);
    void onBranchLoadFailed(String message);
}
