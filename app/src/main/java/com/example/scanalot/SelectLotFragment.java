package com.example.scanalot;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scanalot.databinding.FragmentSelectLotBinding;

import org.checkerframework.checker.units.qual.A;

/**
 * This class is used for the SelectLotFragment. It creates the fragment and uses the fragment_select_layout layout. This will be used for
 * when the user wants to select the parking lot they want to scan. This allows for the app to know which cars belong and dont belong in the
 * selected lot.
 *
 * @author Andrew Hoffer
 * @Created 1/21/23
 * @Contributors Andrew Hoffer - 1/21/23 - Created the fragment
 */
public class SelectLotFragment extends Fragment {

    FragmentSelectLotBinding binding;
    TicketDataViewModel viewModel;

    ///////////////Spinner Selecting Interface - Implemented in MainActivity.java///////////////
    private Spinner selectLotSpinner;
    private OnSpinnerSelectedListener listener;

    public interface OnSpinnerSelectedListener{
        void onSpinnerSelected(String item);

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnSpinnerSelectedListener){
            listener = (OnSpinnerSelectedListener) context;
        }
        else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }
///////////////End of SpinnerSelecting Interface///////////////

    /**
     * Method in which executes after the view has been created. It is saving the state of the view.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get the view model which contains the live,changeable data that was made in main activity
      //  viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);

    }

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentSelectLotBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_lot, container, false);
        
        viewModel = new ViewModelProvider(requireActivity()).get(TicketDataViewModel.class);

        selectLotSpinner = view.findViewById(R.id.selectLotSpinner);

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.parkingLots, android.R.layout.simple_spinner_item);*/


        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,viewModel.getArrParkingLotList().getValue().toArray());

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectLotSpinner.setAdapter(adapter);
        selectLotSpinner.setSelection(0, false);
        // Handles selecting from the lot spinner. Passed to main so that the TextView can be updated.
        // sets a listener and turns the selection into a string that can be passed to text.
        selectLotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                //set the live data object so we can use this in other fragments
                viewModel.setParkingLot(selectedItem);
                listener.onSpinnerSelected(selectedItem);

            }
            // Handles nothing selected. Writes to log.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("spinner", "Nothing Selected on SelectLot Spinner!");
            }
        });
        return view;
    }




    /**
     * Cleans up resources when view is destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_select_lot, container, false);
//
//        selectLotSpinner = view.findViewById(R.id.selectLotSpinner);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.parkingLots, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        selectLotSpinner.setAdapter(adapter);
//
//        selectLotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                listener.onSpinnerSelected(selectedItem);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        return view;
//    }




    }