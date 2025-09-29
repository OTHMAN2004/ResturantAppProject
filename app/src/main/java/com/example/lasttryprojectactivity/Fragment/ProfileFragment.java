package com.example.lasttryprojectactivity.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.lasttryprojectactivity.R;
import com.example.lasttryprojectactivity.RoomDatabase.AppViewModel;
import com.example.lasttryprojectactivity.Utilities.SessionManager;
import com.example.lasttryprojectactivity.Class.User;
import com.example.lasttryprojectactivity.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AppViewModel viewModel;
    private SessionManager sessionManager;
    private static final int REQUEST_IMAGE_PICK = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1002;
    private static final int REQUEST_CAMERA_PERMISSION = 1003;
    private static final int REQUEST_STORAGE_PERMISSION = 1004;
    private String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupClickListeners();
        loadUserProfile();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        sessionManager = new SessionManager(requireContext());
    }

    private void setupClickListeners() {
        binding.saveButton.setOnClickListener(v -> saveProfile());
        binding.logoutButton.setOnClickListener(v -> showLogoutConfirmation());
        binding.profileImageView.setOnClickListener(v -> showImagePickerDialog()); // إضافة هذا السطر
    }

    private void loadUserProfile() {
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            viewModel.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    binding.usernameEditText.setText(user.getUsername());
                    binding.emailEditText.setText(user.getEmail());
                    binding.phoneEditText.setText(user.getPhone());
                    binding.addressEditText.setText(user.getAddress());


                    loadProfileImage(user.getProfileImageUri());
                }
            });
        }
    }

    private void loadProfileImage(String imageUri) {
        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                Uri uri = Uri.parse(imageUri);
                Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.ic_profile) // صورة افتراضية
                        .error(R.drawable.ic_profile)
                        .circleCrop()
                        .into(binding.profileImageView);
            } catch (Exception e) {
                binding.profileImageView.setImageResource(R.drawable.ic_profile);
            }
        } else {
            binding.profileImageView.setImageResource(R.drawable.ic_profile);
        }
    }

    private void saveProfile() {
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            String username = binding.usernameEditText.getText().toString().trim();
            String email = binding.emailEditText.getText().toString().trim();
            String phone = binding.phoneEditText.getText().toString().trim();
            String address = binding.addressEditText.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }


            String currentImageUri = "";
            int userIdFromSession = sessionManager.getUserId();
            if (userIdFromSession != -1) {

            }

            User user = new User(username, email, "", phone, address, currentImageUri);
            user.setId(userId);
            viewModel.updateUser(user);

            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void logout() {
        sessionManager.logoutUser();
        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();


        if (getActivity() != null) {
            getActivity().finish();
        }
    }


    private void showImagePickerDialog() {
        String[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Select Profile Picture")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {

                        openCamera();
                    } else if (which == 1) {

                        openGallery();
                    } else {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
            return;
        }


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Error creating image file", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.example.lasttryprojectactivity.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
            return;
        }

        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";


        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null) {
            storageDir = requireContext().getFilesDir(); // استخدام مجلد داخلي كمكرر
        }

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == requireActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    binding.profileImageView.setImageURI(selectedImageUri);
                    saveProfileImageUri(selectedImageUri.toString());
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (currentPhotoPath != null) {
                    File imgFile = new File(currentPhotoPath);
                    if (imgFile.exists()) {
                        binding.profileImageView.setImageURI(Uri.fromFile(imgFile));
                        saveProfileImageUri(Uri.fromFile(imgFile).toString());
                    } else {

                        Toast.makeText(getContext(), "Failed to capture image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void saveProfileImageUri(String imageUri) {

        int userId = sessionManager.getUserId();
        if (userId != -1) {

            Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getContext(), "Storage permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}