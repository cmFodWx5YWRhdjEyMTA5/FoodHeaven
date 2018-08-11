package drunkcoder.com.foodheaven.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.R;

public class UnsubscribedUser extends Fragment implements BaseSliderView.OnSliderClickListener{
    private SliderLayout bannerSlider;
    private ImageView packsImageView;
    private Button ourPlansButton,
                    weeklyMenuButton;
    private TextView howItWorksTextView,
            faqTextView,
            callForAssistanceTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.unsubscribed_user,container,false);

        packsImageView=view.findViewById(R.id.packsImageView);
        bannerSlider=(SliderLayout)view.findViewById(R.id.bannerSlider);
        ourPlansButton=view.findViewById(R.id.ourPlansButton);
        weeklyMenuButton=view.findViewById(R.id.weeklyMenuButton);
        howItWorksTextView=view.findViewById(R.id.howItWorksTextView);
        callForAssistanceTextView=view.findViewById(R.id.callForAssisteneTextView);
        faqTextView=view.findViewById(R.id.faqTextView);

        getBannerFromFirebase();
        getpackImageFromFirebase();



        return view;
    }

    private void setBanners(String imageUrl) {

            TextSliderView textSliderView=new TextSliderView(getContext());
            textSliderView.image(imageUrl)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            bannerSlider.addSlider(textSliderView);
    }

    private void getBannerFromFirebase() {

        FirebaseDatabase.getInstance().getReference("Banners").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("urls",dataSnapshot.getValue().toString());
                setBanners(dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getpackImageFromFirebase(){
        FirebaseDatabase.getInstance().getReference("Pack").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(getContext()).load(dataSnapshot.getValue().toString()).into(packsImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static UnsubscribedUser newInstance(){
        return new UnsubscribedUser();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(getActivity(), "move to other fragment", Toast.LENGTH_SHORT).show();
    }
}
