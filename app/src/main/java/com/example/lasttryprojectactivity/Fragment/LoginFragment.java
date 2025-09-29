package com.example.lasttryprojectactivity.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lasttryprojectactivity.Activity.MainActivity;
import com.example.lasttryprojectactivity.Class.User;
import com.example.lasttryprojectactivity.R;
import com.example.lasttryprojectactivity.RoomDatabase.AppViewModel;
import com.example.lasttryprojectactivity.Utilities.SessionManager;
import com.example.lasttryprojectactivity.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AppViewModel viewModel;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupClickListeners();
        sessionManager = new SessionManager(requireContext());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    private void setupClickListeners() {
        binding.loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = viewModel.loginUser(email, password);
        if (user != null) {
            sessionManager.createLoginSession(user.getId(), user.getUsername());
            Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            if (getActivity() != null) {
                getActivity().finish();
            }
        } else {
            Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}