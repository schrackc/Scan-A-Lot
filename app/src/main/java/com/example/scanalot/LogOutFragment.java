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

/**
 * This class is used for the LogOutFragment. It creates the fragment and uses the fragment_log_out layout. This will be used for logging the user
 * out of both the app and Firebase.
 *
 * @author Andrew Hoffer
 * @Created 1/25/23
 * @Contributors Andrew Hoffer - 1/25/23 - Created the fragment
 */

public class LogOutFragment extends Fragment {
    FragmentLogOutBinding binding;
    NavDirections navAction;
    Button btnLogOut;

    /**
     * Used after the view is created. We are setting an event listener to the log out button. Based on when it is clicked, we are using Firebase
     * method to log out the user and then navigate the user back to the login page.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogOut = binding.logOutButton;
        Log.i("onCreate", "log out button created");
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                //Navigate to destination
                navAction =LogOutFragmentDirections.actionLogOutFragmentToLoginActivity();
                Navigation.findNavController(view).navigate(navAction);
                //Kill printer properly
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.printDisconnect();
                //kill Activity
                getActivity().finish();
            }
        });

    }

    /**
     * Method in which executes during the creation of the view. It is creating an instance of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogOutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Cleans up resources when view is destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}