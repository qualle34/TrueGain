package com.qualle.truegain.ui.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qualle.truegain.model.local.CurrentWorkoutProto;
import com.qualle.truegain.databinding.FragmentSaveWorkoutBinding;
import com.qualle.truegain.model.CurrentWorkoutViewModel;
import com.qualle.truegain.service.LocalService;
import com.qualle.truegain.ui.adapter.WorkoutExerciseRecyclerViewAdapter;
import com.qualle.truegain.ui.menu.BottomMenuFragment;

public class SaveWorkoutFragment extends Fragment {

    private CurrentWorkoutViewModel workoutViewModel;
    private LocalService service;

    private FragmentSaveWorkoutBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSaveWorkoutBinding.inflate(inflater, container, false);
        service = LocalService.getInstance(getContext());
        NavController navController = NavHostFragment.findNavController(this);

        binding.saveWorkoutButtonBack.setOnClickListener(v -> navController.popBackStack());

        CurrentWorkoutProto workout = service.getCurrentWorkout();

        workoutViewModel = new ViewModelProvider(this).get(CurrentWorkoutViewModel.class);
        workoutViewModel.setData(workout);



        RecyclerView recyclerView = binding.saveWorkoutRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        WorkoutExerciseRecyclerViewAdapter adapter = new WorkoutExerciseRecyclerViewAdapter(getActivity(), workoutViewModel);
        recyclerView.setAdapter(adapter);

        workoutViewModel.getWorkout()
                .observe(getViewLifecycleOwner(), newName -> adapter.notifyDataSetChanged());

        binding.saveWorkoutAddExercise.setOnClickListener(v -> {
            new BottomMenuFragment().show(getChildFragmentManager(), null);
        });

        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        service.saveWorkout(workoutViewModel.getWorkout().getValue());
    }

}