package com.servilink.webmasterapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.servilink.webmasterapp.Database.UserDatabase;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends AppCompatActivity {

    JSONParser jsonParser;
    UserDatabase database;
    Comman comman;
    TextView user_profile_name, user_profile_short_bio, tv_auth, tv_dob, tv_role, tv_mobile;
    CircleImageView imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        Toolbar toolbar = findViewById(R.id.Mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        jsonParser = new JSONParser();
        comman = new Comman(UserProfile.this);
        comman.sideBar(toolbar, UserProfile.this, UserProfile.this);
        database = new UserDatabase(UserProfile.this);
        imageButton = findViewById(R.id.user_profile_photo);
        user_profile_name = findViewById(R.id.user_profile_name);
        user_profile_short_bio = findViewById(R.id.user_profile_short_bio);
        tv_auth = findViewById(R.id.tv_auth);
        tv_dob = findViewById(R.id.tv_dob);
        tv_role = findViewById(R.id.tv_role);
        tv_mobile = findViewById(R.id.tv_mobile);

        try {
            Comman.intertnetStricNode();
            String responseValue = jsonParser.getData(ApiUrl.getuserdetail + database.getUserName(), Comman.getSavedUserData(UserProfile.this, Comman.Key_Usertoken));
            JSONObject userObject = new JSONObject(responseValue);

            user_profile_name.setText(userObject.getString("username"));
            user_profile_short_bio.setText(userObject.getString("email"));
            tv_auth.setText(userObject.getString("higherAuth"));
            tv_role.setText(userObject.getString("role"));
            tv_mobile.setText(userObject.getString("phone"));
            tv_dob.setText(userObject.getString("dob"));

            String image = jsonParser.registerDevice(ApiUrl.getImageDetail, database.getUserName(), database.getUserToken());
            Bitmap bitmap = comman.StringToBitMap(image);

            imageButton.setImageBitmap(bitmap);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}
