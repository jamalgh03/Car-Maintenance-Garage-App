//package com.example.carmaintenancegarageapp;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class ProfileActivity extends AppCompatActivity {
//
//    private TextView userName, emailProfile, phoneNumber;
//    private ImageView profileImage;
//    private Button logoutButton;
//
//    // اسم ملف SharedPreferences يجب أن يكون نفسه المستخدم في LogInActivity
//    private static final String PREFS_NAME = "MyPrefs";
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//
//        userName = findViewById(R.id.user_name);
//        emailProfile = findViewById(R.id.emailProfile);
//        phoneNumber = findViewById(R.id.phoneNumber);
//        profileImage = findViewById(R.id.profile_image);
//        logoutButton = findViewById(R.id.logout_button);
//
//        // Initialize SharedPreferences
//        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//
//        // استرجاع user_id و role من SharedPreferences
//        int userId = sharedPreferences.getInt("user_id", -1);
//        String role = sharedPreferences.getString("role", "user"); // الافتراضي "user"
//
//        if (userId != -1) {
//            fetchUserProfile(userId, role);
//        } else {
//            Toast.makeText(ProfileActivity.this, "User ID not found!", Toast.LENGTH_SHORT).show();
//        }
//
//        // إعداد زر تسجيل الخروج
//        logoutButton.setOnClickListener(v -> {
//            // مسح بيانات SharedPreferences
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear();
//            editor.apply();
//
//            // توجيه المستخدم إلى شاشة تسجيل الدخول
//            Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
//            startActivity(intent);
//            finish();
//        });
//    }
//
//    private void fetchUserProfile(int userId, String role) {
//        String url = "http://192.168.0.100/api/getUserProfile.php?user_id=" + userId; // تأكد من صحة الـ IP والمسار
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.d("ProfileResponse", response.toString());
//
//                            if (response.getString("status").equals("success")) {
//                                JSONObject data = response.getJSONObject("data");
//                                userName.setText("User Name: " + data.getString("name"));
//                                emailProfile.setText("Email: " + data.getString("email"));
//                                phoneNumber.setText("Phone: " + data.getString("phone"));
//
//                                // التعامل مع حقل picture
//                                String picture = data.optString("picture", "");
//
//                                if (!picture.isEmpty()) {
//                                    // تعيين الصورة بناءً على اسم الصورة
//                                    int resID = getResources().getIdentifier(picture, "drawable", getPackageName());
//                                    if (resID != 0) {
//                                        profileImage.setImageResource(resID);
//                                    } else {
//                                        // تعيين الصورة الافتراضية إذا لم يتم العثور على الصورة المحددة
//                                        profileImage.setImageResource(R.drawable.ahmad);
//                                        Toast.makeText(ProfileActivity.this, "Image not found, displaying default image.", Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    // تعيين الصورة الافتراضية إذا كان حقل picture فارغًا أو null
//                                    profileImage.setImageResource(R.drawable.ahmad);
//                                }
//
//                            } else {
//                                Toast.makeText(ProfileActivity.this, "خطأ: " + response.getString("message"), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(ProfileActivity.this, "خطأ في قراءة البيانات", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ProfileActivity.this, "فشل الاتصال بالخادم", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsonObjectRequest);
//    }
//}

package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName, emailProfile, phoneNumber;
    private ImageView profileImage;
    private Button logoutButton;

    // اسم ملف SharedPreferences يجب أن يكون نفسه المستخدم في LogInActivity
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        userName = findViewById(R.id.user_name);
        emailProfile = findViewById(R.id.emailProfile);
        phoneNumber = findViewById(R.id.phoneNumber);
        profileImage = findViewById(R.id.profile_image);
        logoutButton = findViewById(R.id.logout_button);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // استرجاع user_id (email) و role من SharedPreferences
        String userEmail = sharedPreferences.getString("user_id", null);
        String role = sharedPreferences.getString("role", "user"); // الافتراضي "user"

        if (userEmail != null) {
            fetchUserProfile(userEmail, role);
        } else {
            Toast.makeText(ProfileActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
            // إعادة توجيه المستخدم إلى شاشة تسجيل الدخول إذا لم يتم العثور على email
            Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }

        // إعداد زر تسجيل الخروج
        logoutButton.setOnClickListener(v -> {
            // مسح بيانات SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // توجيه المستخدم إلى شاشة تسجيل الدخول
            Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchUserProfile(String email, String role) {
        String url = "http://172.19.40.34/api/getUserProfile.php?email=" + email; // استخدام email بدلاً من user_id

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        Log.d("ProfileResponse", response.toString());

                        if (response.getString("status").equals("success")) {
                            JSONObject data = response.getJSONObject("data");
                            String name = data.getString("name");
                            String fetchedEmail = data.getString("email"); // إعادة تسمية المتغير
                            String phone = data.getString("phone_number"); // تغيير من 'phone' إلى 'phone_number'

                            userName.setText("User Name: " + name);
                            emailProfile.setText("Email: " + fetchedEmail);
                            phoneNumber.setText("Phone: " + phone);

                            // إذا كان هناك حاجة للتعامل مع صورة الملف الشخصي، تأكد من أن الحقل موجود في قاعدة البيانات الجديدة
                            // وإلا، يمكنك إزالة هذا الجزء من الكود
                            /*
                            String picture = data.optString("picture", "");

                            if (!picture.isEmpty()) {
                                // تعيين الصورة بناءً على اسم الصورة
                                int resID = getResources().getIdentifier(picture, "drawable", getPackageName());
                                if (resID != 0) {
                                    profileImage.setImageResource(resID);
                                } else {
                                    // تعيين الصورة الافتراضية إذا لم يتم العثور على الصورة المحددة
                                    profileImage.setImageResource(R.drawable.default_profile);
                                    Toast.makeText(ProfileActivity.this, "Image not found, displaying default image.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // تعيين الصورة الافتراضية إذا كان حقل picture فارغًا أو null
                                profileImage.setImageResource(R.drawable.default_profile);
                            }
                            */

                            // إذا كان هناك صورة في قاعدة البيانات الجديدة، يمكنك استخدام مكتبة مثل Glide أو Picasso لتحميل الصورة من رابط أو مسار معين:
                            /*
                            String pictureUrl = data.optString("picture_url", "");
                            if (!pictureUrl.isEmpty()) {
                                // استخدم مكتبة مثل Glide أو Picasso لتحميل الصورة من الرابط
                                Glide.with(ProfileActivity.this)
                                     .load(pictureUrl)
                                     .placeholder(R.drawable.default_profile)
                                     .into(profileImage);
                            } else {
                                profileImage.setImageResource(R.drawable.default_profile);
                            }
                            */

                        } else {
                            Toast.makeText(ProfileActivity.this, "خطأ: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this, "خطأ في قراءة البيانات", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(ProfileActivity.this, "فشل الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                    Log.e("ProfileError", error.toString());
                }
        );

        queue.add(jsonObjectRequest);
    }
}
