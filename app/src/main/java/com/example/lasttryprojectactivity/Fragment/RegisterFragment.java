package com.example.lasttryprojectactivity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.lasttryprojectactivity.Activity.AuthActivity;
import com.example.lasttryprojectactivity.RoomDatabase.AppViewModel;
import com.example.lasttryprojectactivity.Class.User;
import com.example.lasttryprojectactivity.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private AppViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupClickListeners();
        observeViewModel();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    private void setupClickListeners() {
        binding.registerButton.setOnClickListener(v -> registerUser());
    }

    private void observeViewModel() {

        viewModel.getCartStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null && status.equals("User registered successfully")) {
                Toast.makeText(getContext(), "Registration successful! Please login.", Toast.LENGTH_LONG).show();


                if (getActivity() instanceof AuthActivity) {
                    AuthActivity authActivity = (AuthActivity) getActivity();
                    authActivity.switchToLoginTab();
                }
            }
        });
    }

    private void registerUser() {
        String username = binding.usernameEditText.getText().toString().trim();
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();
        String address = binding.addressEditText.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }


        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }


        new Thread(() -> {
            User existingUser = viewModel.getUserByEmail(email);
            if (existingUser != null) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Email already registered", Toast.LENGTH_SHORT).show()
                );
            } else {
                User user = new User(username, email, password, phone, address);
                viewModel.registerUser(user);

            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}