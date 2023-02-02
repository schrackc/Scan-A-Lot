package com.example.scanalot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.scanalot.databinding.FragmentScanFragmentBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link scan_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class scan_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public scan_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment scan_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static scan_fragment newInstance(String param1, String param2) {
        scan_fragment fragment = new scan_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentScanFragmentBinding binding = FragmentScanFragmentBinding.inflate(getLayoutInflater());
        Button outlinedManualButton = binding.outlinedButton;

        //spinner code nick
       // Spinner stateSpinner = getView().findViewById(R.id.spinner);
       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // stateSpinner.setAdapter(adapter);

      //  FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        outlinedManualButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
               // fragmentTransaction.replace(R.id.nav_host_fragment_content_main,new FirstFragment()).commit();
                // NavHostFragment.findNavController(scan_fragment.this)
              //  .navigate(R.id.action_SecondFragment_to_scan_fragment);
                  }
        });

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_fragment, container, false);
    }


}
