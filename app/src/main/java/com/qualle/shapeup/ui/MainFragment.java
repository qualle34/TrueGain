package com.qualle.shapeup.ui;

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

import com.qualle.shapeup.ChartPieFragment;
import com.qualle.shapeup.R;
import com.qualle.shapeup.client.InMemoryBackendClient;
import com.qualle.shapeup.databinding.FragmentMainBinding;
import com.qualle.shapeup.client.api.RecordSummary;
import com.qualle.shapeup.client.api.Workout;
import com.qualle.shapeup.ui.card.CardAchievementFragment;
import com.qualle.shapeup.ui.card.CardWorkoutFragment;
import com.qualle.shapeup.ui.chart.ChartBarFragment;
import com.qualle.shapeup.ui.chart.ChartRadarFragment;

import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        NavController navController = NavHostFragment.findNavController(this);

        binding.mainButtonProfile.setOnClickListener(v ->
                navController.navigate(R.id.action_nav_main_fragment_to_nav_profile_fragment));

        binding.mainButtonAllWorkouts.setOnClickListener(v ->
                navController.navigate(R.id.action_nav_main_fragment_to_workoutListFragment));

        binding.mainButtonStartWorkout.setOnClickListener(v ->
                navController.navigate(R.id.action_nav_main_fragment_to_nav_save_workout_fragment));

        Map<Float, Float> testChartData = InMemoryBackendClient.getChart();

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_chart_container, ChartBarFragment.newInstance(testChartData), null)
                .commit();


        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        LinearLayout linearLayout = binding.mainLinearLayoutWorkout;
        List<Workout> workouts = InMemoryBackendClient.getWorkouts();

        for (int i = 0; i < workouts.size(); i++) {
            Workout workout = workouts.get(i);
            FrameLayout card = new FrameLayout(getContext());
            card.setId(i + 1);

            Bundle bundle = new Bundle();
            bundle.putLong("id", workout.getId());

            card.setOnClickListener(v -> navController.navigate(R.id.action_nav_workout_list_fragment_to_nav_workout_details_fragment, bundle));

            ft.replace(card.getId(), CardWorkoutFragment.newInstance(workout.getFormattedDate(), workout.getRecords().size(), workout.getAchievementsCount()));
            linearLayout.addView(card);
        }

        ft.commit();


        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_radar_chart_container, new ChartRadarFragment(), null)
                .commit();


        FragmentTransaction ft1 = getChildFragmentManager().beginTransaction();
        LinearLayout linearLayout1 = binding.mainLinearLayoutAchievements;
        List<RecordSummary> recordSummaries = InMemoryBackendClient.getRecordsGroupByExercise();

        for (int i = 0; i < recordSummaries.size(); i++) {
            RecordSummary recordSummary = recordSummaries.get(i);
            FrameLayout card = new FrameLayout(getContext());
            card.setId(i + 10);

            ft1.replace(card.getId(), CardAchievementFragment.newInstance(recordSummary.getExercise().getName(), recordSummary.getCount()));
            linearLayout1.addView(card);
        }

        ft1.commit();


//        getChildFragmentManager().beginTransaction()
//                .setReorderingAllowed(true)
//                .add(binding.mainButtonWorkoutTest1.getId(), new WorkoutCardFragment(), null)
//                .commit();
//
//        getChildFragmentManager().beginTransaction()
//                .setReorderingAllowed(true)
//                .add(binding.mainButtonSaveWorkout.getId(), new WorkoutCardFragment(), null)
//                .commit();
//
//        getChildFragmentManager().beginTransaction()
//                .setReorderingAllowed(true)
//                .add(binding.mainButtonAllWorkouts.getId(), new WorkoutCardFragment(), null)
//                .commit();

        return binding.getRoot();
    }
}