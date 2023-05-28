package ru.mirea.egorovakv.mireaproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.mirea.egorovakv.mireaproject.databinding.ActivityMainBinding;
import ru.mirea.egorovakv.mireaproject.databinding.FragmentHomeBinding;
import ru.mirea.egorovakv.mireaproject.databinding.FragmentTimerBinding;
import ru.mirea.egorovakv.mireaproject.ui.home.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {
    private FragmentTimerBinding binding;
    private boolean isLearningActive = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentTimerBinding.inflate(inflater, container, false);
        binding.focusEditTextNumber.setText("25");
        binding.restEditTextNumber.setText("5");
        binding.sessionEditTextNumber.setText("60");
        View root = binding.getRoot();
        binding.buttonTimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getActivity(), TimerService.class);
//                if(!isLearningActive){
                    serviceIntent.putExtra("focus duration", binding.focusEditTextNumber.getText().toString());
                    serviceIntent.putExtra("rest duration", binding.restEditTextNumber.getText().toString());
                    serviceIntent.putExtra("session duration", binding.sessionEditTextNumber.getText().toString());
                    ContextCompat.startForegroundService(getActivity(),	serviceIntent);
                    isLearningActive = true;
                    //binding.buttonTimer.setText("stop learning session");
//                }
//                else{
//                    getActivity().stopService(new Intent(getActivity(), TimerService.class));
//                    isLearningActive = false;
//                    binding.buttonTimer.setText("start learning session");
//                }

            }
        });
        return root;
    }
}