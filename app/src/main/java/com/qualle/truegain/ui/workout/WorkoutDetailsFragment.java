package com.qualle.truegain.ui.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qualle.truegain.R;
import com.qualle.truegain.databinding.FragmentWorkoutDetailsBinding;
import com.qualle.truegain.model.local.ExerciseDetailsProto;
import com.qualle.truegain.model.local.VolumeProto;
import com.qualle.truegain.model.local.WorkoutDetailsProto;
import com.qualle.truegain.service.LocalService;
import com.qualle.truegain.ui.adapter.ExerciseVolumeRecyclerViewAdapter;
import com.qualle.truegain.ui.card.CardExerciseFragment;
import com.qualle.truegain.ui.chart.ChartPieFragment;

import java.util.List;

public class WorkoutDetailsFragment extends Fragment {

    private static final String ARG_ID = "id";

    private long id;

    private FragmentWorkoutDetailsBinding binding;
    private LocalService service;

    public WorkoutDetailsFragment() {
    }

    public static WorkoutDetailsFragment newInstance(long id) {
        WorkoutDetailsFragment fragment = new WorkoutDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutDetailsBinding.inflate(inflater, container, false);
        service = LocalService.getInstance(getContext());
        NavController navController = NavHostFragment.findNavController(this);

        binding.workoutButtonBack.setOnClickListener(v -> navController.popBackStack());

        WorkoutDetailsProto workoutDetails = service.getWorkoutById(id);

        binding.workoutDetailsExerciseCount.setText(workoutDetails.getExercisesCount() + " Exercises");


        List<VolumeProto> volumeForExercises = workoutDetails.getVolumeForExercises();
        RecyclerView recyclerView = binding.workoutVolumeRecyclerView;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(getSpanCount(volumeForExercises.size()), LinearLayoutManager.HORIZONTAL);
        staggeredGridLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        ExerciseVolumeRecyclerViewAdapter adapter = new ExerciseVolumeRecyclerViewAdapter(volumeForExercises);
        recyclerView.setAdapter(adapter);


        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.workout_chart_container, ChartPieFragment.newInstance(workoutDetails.getVolumeForBodyParts()), null)
                .commit();


        List<ExerciseDetailsProto> exercises = workoutDetails.getExerciseDetailsList();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        LinearLayout linearLayout = binding.workoutLinearLayoutExercises;


        for (int i = 0; i < exercises.size(); i++) {
            FrameLayout card = new FrameLayout(getContext());
            card.setId(i + 20);

            ft.replace(card.getId(), CardExerciseFragment.newInstance(exercises.get(i)));
            linearLayout.addView(card);
        }

        ft.commit();


        return binding.getRoot();
    }

    private int getSpanCount(int size) {

        if (size < 3) {
            return 1;
        } else if (size < 6) {
            return 2;
        } else {
            return 3;
        }
    }
}