package com.qualle.truegain.ui.workout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qualle.truegain.R;
import com.qualle.truegain.databinding.FragmentWorkoutListBinding;
import com.qualle.truegain.service.LocalService;
import com.qualle.truegain.ui.adapter.WorkoutListRecyclerViewAdapter;
import com.qualle.truegain.ui.listener.WorkoutListClickListener;

public class WorkoutListFragment extends Fragment implements WorkoutListClickListener {

    private FragmentWorkoutListBinding binding;
    private LocalService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutListBinding.inflate(inflater, container, false);
        service = LocalService.getInstance(getContext());
        NavController navController = NavHostFragment.findNavController(this);

        binding.workoutListButtonBack.setOnClickListener(v -> navController.popBackStack());

        Context context = binding.getRoot().getContext();
        RecyclerView recyclerView = binding.workoutListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(new WorkoutListRecyclerViewAdapter(this, service.getWorkouts()));


        return binding.getRoot();
    }


    @Override
    public void onWorkoutClick(long workoutId) {
        NavController navController = NavHostFragment.findNavController(this);

        Bundle args = new Bundle();
        args.putLong("id", workoutId);

        navController.navigate(R.id.action_nav_workout_list_fragment_to_nav_workout_details_fragment, args);
    }
}