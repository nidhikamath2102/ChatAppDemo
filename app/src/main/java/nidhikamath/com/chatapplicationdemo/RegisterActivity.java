package nidhikamath.com.chatapplicationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText et_username, et_email, et_password;
    Button btn_register;
    LinearLayout linear_layout_login;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        linear_layout_login = findViewById(R.id.linear_layout_login);

        FirebaseUser firebaseUser_authenticated = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser_authenticated !=null){
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(et_username.getText().toString()) || TextUtils.isEmpty(et_email.getText().toString())
                        || TextUtils.isEmpty(et_password.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "All details are necessary!!", Toast.LENGTH_SHORT).show();
                }else if(et_password.getText().toString().length()<8){
                    Toast.makeText(RegisterActivity.this, "Password must contain atleast 8 characters!", Toast.LENGTH_SHORT).show();
                }else{
                    register(et_username.getText().toString(), et_email.getText().toString(), et_password.getText().toString());
                }
            }
        });

        linear_layout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private  void register(final String username, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if(firebaseUser!=null){
                                String userId = firebaseUser.getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                                HashMap<String, String> user = new HashMap<>();
                                user.put("uId", userId);
                                user.put("username", username);
                                user.put("status", "offline");

                                databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });


                            }

                        }
                    }
                });
    }
}
