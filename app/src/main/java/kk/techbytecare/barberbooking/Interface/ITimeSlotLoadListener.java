package kk.techbytecare.barberbooking.Interface;

import java.util.List;

import kk.techbytecare.barberbooking.Model.TimeSlot;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
