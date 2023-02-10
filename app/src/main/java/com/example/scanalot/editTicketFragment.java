package com.example.scanalot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scanalot.databinding.FragmentEditTicketBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class editTicketFragment extends Fragment {
    FragmentEditTicketBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditTicketBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}