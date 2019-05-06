package kk.techbytecare.barberbooking.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kk.techbytecare.barberbooking.Common.Common;
import kk.techbytecare.barberbooking.Model.BookingInformation;
import kk.techbytecare.barberbooking.R;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;

    LocalBroadcastManager localBroadcastManager;

    Unbinder unbinder;

    @BindView(R.id.txt_booking_barber)
    TextView txt_booking_barber;

    @BindView(R.id.txt_booking_time)
    TextView txt_booking_time;

    @BindView(R.id.txt_salon_address)
    TextView txt_salon_address;

    @BindView(R.id.txt_salon_name)
    TextView txt_salon_name;

    @BindView(R.id.txt_salon_phone)
    TextView txt_salon_phone;

    @BindView(R.id.txt_salon_website)
    TextView txt_salon_website;

    @BindView(R.id.salon_open_hours)
    TextView txt_salon_open_hours;

    @OnClick(R.id.btn_confirm)
    void confirmBooking()   {
        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setBarberId(Common.CurrentBarber.getBarberId());
        bookingInformation.setBarberName(Common.CurrentBarber.getName());
        bookingInformation.setCustomerName(Common.currentUser.getName());
        bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
        bookingInformation.setSalonAddress(Common.CurrentSalon.getAddress());
        bookingInformation.setSalonId(Common.CurrentSalon.getSalonId());
        bookingInformation.setSalonName(Common.CurrentSalon.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeToString(Common.CurrentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.CurrentTimeSlot));

        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(Common.city)
                .collection("Branch")
                .document(Common.CurrentSalon.getSalonId())
                .collection("Barber")
                .document(Common.CurrentBarber.getBarberId())
                .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
                .document(String.valueOf(Common.CurrentTimeSlot));

        bookingDate.
                set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        resetStaticData();
                        getActivity().finish();
                        Toast.makeText(getActivity(), "Booking Reserved!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.CurrentTimeSlot = -1;
        Common.CurrentSalon = null;
        Common.CurrentBarber = null;
        Common.currentDate.add(Calendar.DATE,0);
    }


    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance() {
        if (instance == null)   {
            instance = new BookingStep4Fragment();
        }
        return instance;
    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_barber.setText(Common.CurrentBarber.getName());
        txt_booking_time.setText(new StringBuilder(Common.convertTimeToString(Common.CurrentTimeSlot))
        .append(" at ")
        .append(simpleDateFormat.format(Common.currentDate.getTime())));

        txt_salon_address.setText(Common.CurrentSalon.getAddress());
        txt_salon_website.setText(Common.CurrentSalon.getWebsite());
        txt_salon_name.setText(Common.CurrentSalon.getName());
        txt_salon_open_hours.setText(Common.CurrentSalon.getOpenHours());
        txt_salon_phone.setText(Common.CurrentSalon.getPhone());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver,new IntentFilter(Common.KEY_CONFIRM_BOOKING));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container,false);

        unbinder = ButterKnife.bind(this,itemView);

        return itemView;
    }
}
