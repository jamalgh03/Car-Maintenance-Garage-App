//package com.example.carmaintenancegarageapp;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.text.InputType;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class LogInActivity extends AppCompatActivity {
//
//    private EditText emailEditText, passwordEditText;
//    private Button continueButton, signUpButton;
//    private ImageButton openedEyeButton, closedEyeButton;
//    private boolean isPasswordVisible = false;
//
//    // اسم ملف SharedPreferences
//    private static final String PREFS_NAME = "MyPrefs";
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_log_in);
//
//        // Initialize SharedPreferences
//        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//
//        // التحقق من حالة تسجيل الدخول
//        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
//            String role = sharedPreferences.getString("role", "user");
//            if (role.equals("admin")) {
//                startActivity(new Intent(LogInActivity.this, AdminActivity.class));
//            } else {
//                startActivity(new Intent(LogInActivity.this, MainActivity.class));
//            }
//            finish();
//            return;
//        }
//
//        // Initialize views
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        continueButton = findViewById(R.id.continueButton);
//        signUpButton = findViewById(R.id.signUpButton);
//        openedEyeButton = findViewById(R.id.openedEyeButton);
//        closedEyeButton = findViewById(R.id.closedEyeButton);
//
//        // Default state: password hidden
//        closedEyeButton.setVisibility(View.VISIBLE);
//        openedEyeButton.setVisibility(View.GONE);
//
//        // Set up button listeners
//        continueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logIn();
//            }
//        });
//
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Redirect to SignUpActivity
//                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        closedEyeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                togglePasswordVisibility();
//            }
//        });
//
//        openedEyeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                togglePasswordVisibility();
//            }
//        });
//    }
//
//    private void togglePasswordVisibility() {
//        if (isPasswordVisible) {
//            // Hide password
//            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            passwordEditText.setSelection(passwordEditText.getText().length()); // Keep cursor at the end
//            closedEyeButton.setVisibility(View.VISIBLE);
//            openedEyeButton.setVisibility(View.GONE);
//            isPasswordVisible = false;
//        } else {
//            // Show password
//            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
//            passwordEditText.setSelection(passwordEditText.getText().length());
//            closedEyeButton.setVisibility(View.GONE);
//            openedEyeButton.setVisibility(View.VISIBLE);
//            isPasswordVisible = true;
//        }
//    }
//
//    private void logIn() {
//        // Get user inputs
//        String usernameOrEmail = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//
//        // Validate inputs
//        if (TextUtils.isEmpty(usernameOrEmail) || TextUtils.isEmpty(password)) {
//            Toast.makeText(LogInActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // URL for login
//        String url = "http://192.168.0.100/api/login.php";
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Handle server response
//                        Log.d("LogInResponse", response);
//
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//
//                            if (success) {
//                                String role = jsonResponse.getString("role");
//                                String message = jsonResponse.getString("message");
//                                int userId = jsonResponse.getInt("user_id");
//
//                                // جلب بيانات المستخدم من السيرفر بعد تسجيل الدخول
//                                fetchUserDetails(userId, role, message);
//                            } else {
//                                String message = jsonResponse.getString("message");
//                                Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(LogInActivity.this, "خطأ في قراءة البيانات", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle errors
//                        Log.e("LogInError", error.toString());
//                        Toast.makeText(LogInActivity.this, "Failed to log in! Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // Send data as POST parameters
//                Map<String, String> params = new HashMap<>();
//                params.put("usernameOrEmail", usernameOrEmail);
//                params.put("password", password);
//                return params;
//            }
//        };
//
//        // Add the request to the Volley queue
//        queue.add(stringRequest);
//    }
//
//    private void fetchUserDetails(int userId, String role, String message) {
//        // URL for fetching user details
//        String url = "http://192.168.0.100/api/getUserProfile.php?user_id=" + userId;
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,
//                response -> {
//                    try {
//                        Log.d("UserDetailsResponse", response.toString());
//
//                        if (response.getString("status").equals("success")) {
//                            JSONObject data = response.getJSONObject("data");
//                            String name = data.getString("name");
//                            String email = data.getString("email");
//                            String phone = data.getString("phone");
//
//                            // حفظ البيانات في SharedPreferences
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putBoolean("isLoggedIn", true);
//                            editor.putString("role", role);
//                            editor.putInt("user_id", userId);
//                            editor.putString("name", name);
//                            editor.putString("email", email);
//                            editor.putString("phone", phone);
//                            editor.apply();
//
//                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
//
//                            if (role.equals("admin")) {
//                                Intent intent = new Intent(LogInActivity.this, AdminActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            }
//                            finish();
//                        } else {
//                            String errorMsg = response.getString("message");
//                            Toast.makeText(LogInActivity.this, "خطأ: " + errorMsg, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(LogInActivity.this, "خطأ في قراءة البيانات", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> {
//                    Toast.makeText(LogInActivity.this, "فشل الاتصال بالخادم", Toast.LENGTH_SHORT).show();
//                    Log.e("UserDetailsError", error.toString());
//                }
//        );
//
//        queue.add(jsonObjectRequest);
//    }
//}

package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button continueButton, signUpButton;
    private ImageButton openedEyeButton, closedEyeButton;
    private boolean isPasswordVisible = false;

    // اسم ملف SharedPreferences
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // التحقق من حالة تسجيل الدخول
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            String role = sharedPreferences.getString("role", "user");
            if (role.equals("admin")) {
                startActivity(new Intent(LogInActivity.this, AdminActivity.class));
            } else {
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
            }
            finish();
            return;
        }

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        continueButton = findViewById(R.id.continueButton);
        signUpButton = findViewById(R.id.signUpButton);
        openedEyeButton = findViewById(R.id.openedEyeButton);
        closedEyeButton = findViewById(R.id.closedEyeButton);

        // Default state: password hidden
        closedEyeButton.setVisibility(View.VISIBLE);
        openedEyeButton.setVisibility(View.GONE);

        // Set up button listeners
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to SignUpActivity
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        closedEyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        openedEyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setSelection(passwordEditText.getText().length()); // Keep cursor at the end
            closedEyeButton.setVisibility(View.VISIBLE);
            openedEyeButton.setVisibility(View.GONE);
            isPasswordVisible = false;
        } else {
            // Show password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordEditText.setSelection(passwordEditText.getText().length());
            closedEyeButton.setVisibility(View.GONE);
            openedEyeButton.setVisibility(View.VISIBLE);
            isPasswordVisible = true;
        }
    }

    private void logIn() {
        // Get user inputs
        String usernameOrEmail = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(usernameOrEmail) || TextUtils.isEmpty(password)) {
            Toast.makeText(LogInActivity.this, "جميع الحقول مطلوبة!", Toast.LENGTH_SHORT).show();
            return;
        }

        // (اختياري) التحقق من تنسيق البريد الإلكتروني إذا كان الإدخال هو بريد إلكتروني
        if (usernameOrEmail.contains("@") && !android.util.Patterns.EMAIL_ADDRESS.matcher(usernameOrEmail).matches()) {
            Toast.makeText(LogInActivity.this, "تنسيق البريد الإلكتروني غير صالح!", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL for login
        String url = "http://192.168.0.59/api/login.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle server response
                        Log.d("LogInResponse", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String role = jsonResponse.getString("role");
                                String message = jsonResponse.getString("message");
                                String userId = jsonResponse.getString("user_id"); // user_id يمكن أن يكون email للـ Admin و email للمستخدمين

                                if (role.equals("admin")) {
                                    // حفظ البيانات في SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.putString("role", role);
                                    editor.putString("user_id", userId); // email للـ Admin
                                    editor.apply();

                                    Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();

                                    // الانتقال إلى AdminActivity
                                    Intent intent = new Intent(LogInActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (role.equals("user")) {
                                    // جلب بيانات المستخدم من السيرفر بعد تسجيل الدخول
                                    fetchUserDetails(userId, role, message);
                                } else {
                                    Toast.makeText(LogInActivity.this, "دور غير معروف", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String message = jsonResponse.getString("message");
                                Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LogInActivity.this, "خطأ في قراءة البيانات", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("LogInError", error.toString());
                        Toast.makeText(LogInActivity.this, "فشل في تسجيل الدخول! يرجى المحاولة مرة أخرى.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Send data as POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("usernameOrEmail", usernameOrEmail);
                params.put("password", password);
                return params;
            }
        };

        // Add the request to the Volley queue
        queue.add(stringRequest);
    }

    private void fetchUserDetails(String userEmail, String role, String message) {
        // URL لجلب تفاصيل المستخدم باستخدام email والدور
        String url = "http://192.168.56.1/getUserProfile.php?email=" + userEmail + "&role=" + role;

        Log.d("FetchUserDetails", "URL: " + url); // سجل عنوان الـ URL

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        Log.d("UserDetailsResponse", response.toString());

                        if (response.getString("status").equals("success")) {
                            JSONObject data = response.getJSONObject("data");
                            String name = data.getString("name");
                            String email = data.getString("email");
                            String phone = data.getString("phone_number");

                            // حفظ البيانات في SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("role", role);
                            editor.putString("user_id", email); // email للمستخدم العادي
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("phone_number", phone);
                            editor.apply();

                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();

                            // الانتقال إلى MainActivity
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String errorMsg = response.getString("message");
                            Toast.makeText(LogInActivity.this, "خطأ: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LogInActivity.this, "خطأ في قراءة البيانات", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(LogInActivity.this, "فشل الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                    Log.e("UserDetailsError", error.toString());
                }
        );

        queue.add(jsonObjectRequest);
    }
}
