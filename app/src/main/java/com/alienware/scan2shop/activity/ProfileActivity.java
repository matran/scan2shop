package com.alienware.scan2shop.activity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.dialogs.EmailDialog;
import com.alienware.scan2shop.dialogs.NamesDialog;
import com.alienware.scan2shop.dialogs.PhoneDialog;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
import com.alienware.scan2shop.services.VolleyMultipartRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ProfileActivity extends AppCompatActivity {
    private Button profileImageButton;
    private TextView namesTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private UserSqliteHandler userDB;
    private HashMap<String, String> user;
    public String firstname;
    public  String lastname;
    public  String email;
    public String phoneno;
    public String photo="null";
    private View namesView;
    private View emailView;
    private View phoneView;
    private View profileProgressBar;
    private CircularImageView profileImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        Toolbar  toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profileProgressBar=findViewById(R.id.profileProgressBar);
        profileImageButton=findViewById(R.id.setPhotoButton);
        profileImage=findViewById(R.id.profileImage);
        userDB = new UserSqliteHandler(getApplicationContext());
        user = userDB.getUserDetails();
        profileImageButton=findViewById(R.id.setPhotoButton);
        namesTextView=findViewById(R.id.profileNames);
        emailTextView=findViewById(R.id.profileEmailAddress);
        phoneTextView=findViewById(R.id.profilePhoneNumber);
        namesView=findViewById(R.id.nameSection);
        emailView=findViewById(R.id.emailSection);
        phoneView=findViewById(R.id.phoneSection);
        firstname=user.get("firstname");
        lastname=user.get("secondname");
        namesTextView.setText(firstname+" "+lastname);
        email=user.get("email");
        phoneno=user.get("phone");
        photo=user.get("photo");
        phoneTextView.setText(phoneno);
        emailTextView.setText(email);
        namesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showNamesDialog();
            }
        });
        emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailDialog();
            }
        });
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneDialog();
            }
        });
        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              picImage();
            }
        });
        Glide.with(this).load(photo)
                .into(profileImage);
    }

    private void refreshViews(){
        user.clear();
        user = userDB.getUserDetails();
        firstname=user.get("firstname");
        lastname=user.get("secondname");
        email=user.get("email");
        phoneno=user.get("phone");
        namesTextView.setText(firstname+" "+lastname);
        phoneTextView.setText(phoneno);
        emailTextView.setText(email);
    }
    private void picImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }
    private void showNamesDialog() {
        FragmentManager fm=getSupportFragmentManager();
        NamesDialog namesDialog = NamesDialog.newInstance(firstname,lastname);
        namesDialog.show(fm, "names_dialog");
    }
    private void showEmailDialog() {
        FragmentManager fm=getSupportFragmentManager();
        EmailDialog emailDialog = EmailDialog.newInstance(email);
        emailDialog.show(fm, "email_dialog");
    }
    private void showPhoneDialog(){
        FragmentManager fm=getSupportFragmentManager();
        PhoneDialog phoneDialog = PhoneDialog.newInstance(phoneno);
        phoneDialog.show(fm, "phone_dialog");
    }
    public void updateNames(String firstname,String lastname){
        updateAccount(firstname,lastname,user.get("email"),user.get("phone"));
    }

    public void updateEmail(String email){
        updateAccount(user.get("firstname"),user.get("secondname"),email,user.get("phone"));
    }
    public void updateAccount(final String firstname,final String lastname,final String email,String phoneno) {
        profileProgressBar.setVisibility(View.VISIBLE);
        final String url = Config.URL_REGISTER;
        HashMap<String, String> params = new HashMap<>();
        final String phone=phoneno;
        params.put("id",userDB.getUserid());
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("phone", phone);
        params.put("email", email);
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.PUT,url,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){
                profileProgressBar.setVisibility(View.INVISIBLE);
                try{
                    String status=response.getString("status");
                    if (status.equals("success")){
                        userDB.updateUser(firstname,lastname,phone, email,"null");
                        refreshViews();
                        Toast.makeText(ProfileActivity.this, "Details updated successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ProfileActivity.this, "Failed to update try again", Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    profileProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError e){
                profileProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        str.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0F));
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "req_update");
    }
    @Override
    public void onPause() {
        super.onPause();
        Scan2ShopApplication.getInstance().cancelPendingRequests("req_update");

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK && data!=null) {
                Uri imageUri = data.getData();
                try {
                    //getting bitmap object from uri
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    //displaying selected image to imageview
                    profileImage.setImageBitmap(bitmap);

                    //calling the method uploadBitmap to upload image
                    uploadBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        profileProgressBar.setVisibility(View.VISIBLE);
        final String userid=userDB.getUserid();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.URL_PROFILE_PHOTO,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        profileProgressBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String status=obj.getString("status");
                            if(status.equals("success")){
                                String photo=obj.getString("url");
                                userDB.updateImage(photo);
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        profileProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public void sendPhoneForVerification(final String phoneno){
        profileProgressBar.setVisibility(View.VISIBLE);
        String url= Config.URL_PHONE+phoneno;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                profileProgressBar.setVisibility(View.INVISIBLE);
                try {

                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")) {
                        Intent intent=new Intent(ProfileActivity.this,ProfilePhoneVerifyActivity.class);
                        intent.putExtra("phone",phoneno);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    profileProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                profileProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq,"phone_verify");
    }
}
