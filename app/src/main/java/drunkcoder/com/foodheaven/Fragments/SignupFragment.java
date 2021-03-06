package drunkcoder.com.foodheaven.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labo.kaji.fragmentanimations.PushPullAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Activities.AuthenticationActivity;
import drunkcoder.com.foodheaven.Models.Address;
import drunkcoder.com.foodheaven.Models.User;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.Utils.AuthUtil;

public class SignupFragment extends Fragment implements View.OnClickListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private AuthenticationActivity hostingActivity;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText passwordEditText,
            nameEditText;
    private Button signupButton;
    private TextView loginTextview;

    private String mobileNumber;
    private String email;
    private String password,name;
    private Address userAddress;

    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private List<User> users;


    public static SignupFragment newInstance() {

        Bundle args = new Bundle();

        SignupFragment fragment = new SignupFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        hostingActivity = (AuthenticationActivity) getActivity();


        fetchUsers();


        emailEditText = view.findViewById(R.id.emailEdittext);
        phoneEditText = view.findViewById(R.id.phonenumberEdittext);
        passwordEditText = view.findViewById(R.id.passwordEdittext);
        signupButton = view.findViewById(R.id.signupButton);
        loginTextview = view.findViewById(R.id.loginTextview);
        nameEditText = view.findViewById(R.id.nameEdittext);

        placeAutocompleteFragment=(PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.addressAutoCompleteFragment);
        //hiding search button before fragment
        placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
        //setting hint for ediittext
        EditText place;
        place= ((EditText)placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input));
        place.setHint("Enter Your Address");
        place.setTextColor(Color.WHITE);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
              userAddress= new Address((String) place.getAddress(),String.valueOf(place.getLatLng().longitude),String.valueOf(place.getLatLng().latitude));
            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getActivity(), ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        signupButton.setOnClickListener(this);
        loginTextview.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signupButton:
                removePlaceFragment();
                registerUser();
                break;
            case R.id.loginTextview:

                removePlaceFragment();
                hostingActivity.addDifferentFragment(SigninFragment.newInstance());
                break;

        }

    }

    private void removePlaceFragment() {

        getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.addressAutoCompleteFragment)).commit();
    }


    public void registerUser()
    {
        mobileNumber = phoneEditText.getText().toString().trim();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        name=nameEditText.getText().toString();

        if( !(AuthUtil.isValidEmail(email)))
        {
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return;
        }
        if(!(AuthUtil.isVailidPhone(mobileNumber))){
            phoneEditText.setError("Enter a valid mobile number");
            phoneEditText.requestFocus();
            return;
        }

        if(passwordEditText.getText().toString().trim().length()<6)
        {
            passwordEditText.setError("Password should have atleast 6 characters");
            passwordEditText.requestFocus();
            return;
        }


        if(!checkAlreadyExists()) {
            verifyPhoneNumber();
        }else {
            Toast.makeText(hostingActivity, "You are already registered,Please Login", Toast.LENGTH_SHORT).show();
            hostingActivity.addDifferentFragment(SigninFragment.newInstance());
        }

    }

    public void verifyPhoneNumber()
    {

        hostingActivity.addDifferentFragment(VerificationFragment.newInstance(email,mobileNumber,password,userAddress,name));

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return PushPullAnimation.create(PushPullAnimation.LEFT,enter,1000);
    }

    public void fetchUsers()
    {
        users = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
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

    public boolean checkAlreadyExists()
    {
        for(int i=0;i<users.size();i++)
        {
            User user = users.get(i);
            if(user.getEmail().equals(email)||user.getPhoneNumber().equals(mobileNumber)){
                return true;
            }
        }

        return false;
    }



}
