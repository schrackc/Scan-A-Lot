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

import com.example.scanalot.databinding.FragmentEditTicketFragmentBinding;
import com.example.scanalot.databinding.FragmentLogOutFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_ticket_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_ticket_fragment extends Fragment {


    FragmentEditTicketFragmentBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding =FragmentEditTicketFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }



}