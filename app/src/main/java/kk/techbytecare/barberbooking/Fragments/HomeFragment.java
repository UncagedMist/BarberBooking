package kk.techbytecare.barberbooking.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kk.techbytecare.barberbooking.Adapter.HomeSliderAdapter;
import kk.techbytecare.barberbooking.Adapter.LookBookAdapter;
import kk.techbytecare.barberbooking.BookingActivity;
import kk.techbytecare.barberbooking.Common.Common;
import kk.techbytecare.barberbooking.Interface.IBannerLoadListener;
import kk.techbytecare.barberbooking.Interface.ILookBookLoadListener;
import kk.techbytecare.barberbooking.Model.Banner;
import kk.techbytecare.barberbooking.R;
import kk.techbytecare.barberbooking.Service.PicassoImageLoadService;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements IBannerLoadListener, ILookBookLoadListener {

    private Unbinder unbinder;

    @BindView(R.id.layout_user_info)
    LinearLayout layout_user_info;

    @BindView(R.id.txt_name)
    TextView txt_user_name;

    @BindView(R.id.banner_slider)
    Slider banner_slider;

    @BindView(R.id.recycler_look_book)
    RecyclerView recycler_look_book;

    @OnClick(R.id.card_view_booking)
    void booking()  {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }


    CollectionReference lookBookRef,bannerRef;

    IBannerLoadListener iBannerLoadListener;
    ILookBookLoadListener iLookBookLoadListener;

    public HomeFragment() {
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        lookBookRef = FirebaseFirestore.getInstance().collection("Lookbook");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this,view);

        Slider.init(new PicassoImageLoadService());

        iBannerLoadListener = this;
        iLookBookLoadListener = this;

        if (AccountKit.getCurrentAccessToken() != null) {
            setUserInfo();
            loadBanner();
            loadLookBook();
        }

        return view;
    }

    private void loadLookBook() {
        lookBookRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> lookBooks = new ArrayList<>();

                        if (task.isSuccessful())    {

                            for (QueryDocumentSnapshot bannerSnapshot : task.getResult())   {
                                Banner banner = bannerSnapshot.toObject(Banner.class);
                                lookBooks.add(banner);
                            }
                            iLookBookLoadListener.onLookBookLoadSuccess(lookBooks);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iLookBookLoadListener.onLookBookLoadFailed(e.getMessage());
                    }
                });
    }

    private void loadBanner() {
        bannerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> banners = new ArrayList<>();

                        if (task.isSuccessful())    {

                            for (QueryDocumentSnapshot bannerSnapshot : task.getResult())   {
                                Banner banner = bannerSnapshot.toObject(Banner.class);
                                banners.add(banner);
                            }
                            iBannerLoadListener.onBannerLoadSuccess(banners);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iBannerLoadListener.onBannerLoadFailed(e.getMessage());
                    }
                });
    }

    private void setUserInfo() {
        layout_user_info.setVisibility(View.VISIBLE);
        txt_user_name.setText(Common.currentUser.getName());
    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLookBookLoadSuccess(List<Banner> banners) {
        recycler_look_book.setHasFixedSize(true);
        recycler_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_look_book.setAdapter(new LookBookAdapter(getActivity(),banners));
    }

    @Override
    public void onLookBookLoadFailed(String message) {
        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
    }
}
