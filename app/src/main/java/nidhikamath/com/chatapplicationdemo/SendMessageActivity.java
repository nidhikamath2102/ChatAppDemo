package nidhikamath.com.chatapplicationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nidhikamath.com.chatapplicationdemo.adapter.SendMessageAdapter;
import nidhikamath.com.chatapplicationdemo.model.ChatMessage;
import nidhikamath.com.chatapplicationdemo.model.User;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SendMessageActivity extends AppCompatActivity {

    TextView tv_username;
    ImageView iv_profile;
    EditText et_message;
    ImageButton ibtn_send;

    RecyclerView recycler_view_message;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    Bundle b;

    SendMessageAdapter sendMessageAdapter;
    ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    private String receiver_uId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tv_username = findViewById(R.id.tv_username);
        iv_profile = findViewById(R.id.iv_profile);

        recycler_view_message = findViewById(R.id.recycler_view_message);
        et_message = findViewById(R.id.et_message);
        ibtn_send = findViewById(R.id.ibtn_send);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SendMessageActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        recycler_view_message.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        b = getIntent().getExtras();
        if(b!=null){
            //uId

            receiver_uId = b.getString("uId");
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(receiver_uId);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user!=null) {
                            tv_username.setText("" + user.getUsername());
                        }

                        readMessage(firebaseUser.getUid(), receiver_uId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            ibtn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMessage(firebaseUser.getUid(), receiver_uId, et_message.getText().toString());
                    et_message.setText("");
                }
            });

        }


    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chat").push().setValue(hashMap);

        final DatabaseReference databaseReference_chat = FirebaseDatabase.getInstance().getReference("UserChatList").child(firebaseUser.getUid()).child(receiver_uId);

        databaseReference_chat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    databaseReference_chat.child("id").setValue(receiver_uId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage(final String sender, final String receiver){
        chatMessages = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        ChatMessage chatMessage = dataSnapshot1.getValue(ChatMessage.class);
                        if (chatMessage != null && ((chatMessage.getReceiver().equals(sender) && chatMessage.getSender().equals(receiver)) ||
                                (chatMessage.getReceiver().equals(receiver) && chatMessage.getSender().equals(sender)))) {
                            chatMessages.add(chatMessage);
                        }
                    }

                    sendMessageAdapter = new SendMessageAdapter(SendMessageActivity.this, chatMessages);
                    recycler_view_message.setAdapter(sendMessageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateStatus(String status){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus("offline");
    }
}
