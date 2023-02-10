package com.example.scanalot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.scanalot.databinding.FragmentFillCitationBinding;

import java.util.ArrayList;


public class FillCitationFragment extends Fragment {

    FragmentFillCitationBinding binding;
    TextView textView;
    Button cancelButton;
    Button savePrint;
    NavDirections navAction;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = binding.textView;
        cancelButton = binding.CancelButton;
        savePrint = binding.resultFillCitationButton;

        savePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navAction = FillCitationFragmentDirections.actionFillCitationFragment2ToPrintPreviewFragment();
                Navigation.findNavController(view).navigate(navAction);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navAction = FillCitationFragmentDirections.actionFillCitationFragment2ToScanFragment();
                Navigation.findNavController(view).navigate(navAction);
            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // set title
                builder.setTitle("Select Violations");
                // set dialog non cancelable
                builder.setCancelable(false);
                String[] citations = new String[]{"Violation A", "Violation B", "Violation C"};
                boolean[] checkboxes = new boolean[citations.length];
                ArrayList<Integer> langList = new ArrayList<>();
                builder.setMultiChoiceItems(citations, checkboxes, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            langList.add(i);
                        } else {
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(citations[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFillCitationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}