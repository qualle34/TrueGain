package com.qualle.truegain.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.qualle.truegain.R;
import com.qualle.truegain.client.BackendClient;
import com.qualle.truegain.client.ClientModule;
import com.qualle.truegain.client.api.UserProfile;
import com.qualle.truegain.config.DaggerApplicationComponent;
import com.qualle.truegain.databinding.FragmentProfileSecurityBinding;
import com.qualle.truegain.service.AuthenticationHandler;
import com.qualle.truegain.service.LocalService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSecurityFragment extends Fragment {

    private FragmentProfileSecurityBinding binding;

    @Inject
    public LocalService service;

    @Inject
    public BackendClient client;

    @Inject
    public AuthenticationHandler authenticationHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileSecurityBinding.inflate(inflater, container, false);
        NavController navController = NavHostFragment.findNavController(this);

        DaggerApplicationComponent.builder()
                .clientModule(ClientModule.getInstance(getContext())).build()
                .inject(this);

        try {
            authenticationHandler.holdAuthentication();
        } catch (Exception e) {
            navController.navigate(R.id.action_nav_main_fragment_to_nav_greeting_fragment);
            return binding.getRoot();
        }

        binding.profileSecurityButtonBack.setOnClickListener(v -> navController.popBackStack());

        client.getUserProfile(service.getAuthorizationHeader()).enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                if (response.isSuccessful()) {

                    UserProfile dto = response.body();
                    binding.profileSecurityEmail.setText(dto.getUser().getEmail());
                    binding.profileSecurityPassword.setText("* * * * * * * * * *");
                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                t.printStackTrace();
            }
        });

        binding.profileSecurityEmail.setOnClickListener(v ->
                navController.navigate(R.id.action_nav_profile_security_fragment_to_nav_profile_security_email_fragment));

        binding.profileSecurityPassword.setOnClickListener(v ->
                navController.navigate(R.id.action_nav_profile_security_fragment_to_nav_profile_security_password_fragment));

        binding.profileSecurityLogout.setOnClickListener(v -> {
            authenticationHandler.logout();
            navController.navigate(R.id.action_nav_profile_security_fragment_to_nav_greeting_fragment);
        });


        return binding.getRoot();
    }
}