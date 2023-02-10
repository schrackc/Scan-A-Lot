package com.example.scanalot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentLogOutBinding;
import com.google.firebase.auth.FirebaseAuth;


// A simple {@link Fragment} subclass.
// Use the {@link logOutFragment#newInstance} factory method to
// create an instance of this fragment.

public class logOutFragment extends Fragment {

    FragmentLogOutBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        Button logOutButton = binding.logOutButton;
        Log.i("onCreate","log out button created");
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Button Click", "manual button clicked !!!!");
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                NavDirections navAction =  logOutFragmentDirections.actionLogOutFragmentToLoginActivity();
                Navigation.findNavController(view).navigate(navAction);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentLogOutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}