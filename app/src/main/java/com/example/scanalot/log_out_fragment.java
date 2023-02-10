package com.example.scanalot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scanalot.databinding.FragmentLogOutFragmentBinding;
import com.example.scanalot.databinding.FragmentScanFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link log_out_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class log_out_fragment extends Fragment {

    FragmentLogOutFragmentBinding binding;
    NavDirections navAction;
    Button logOutButton;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        logOutButton = binding.logOutButton;
        Log.i("onCreate","log out button created");
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Button Click", "manual button clicked !!!!");
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
               navAction =  log_out_fragmentDirections.actionLogOutFragmentToLoginActivity();
                Navigation.findNavController(view).navigate(navAction);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentLogOutFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}