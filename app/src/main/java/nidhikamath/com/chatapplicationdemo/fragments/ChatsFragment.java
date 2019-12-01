package nidhikamath.com.chatapplicationdemo.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nidhikamath.com.chatapplicationdemo.R;
import nidhikamath.com.chatapplicationdemo.adapter.UserAdapter;
import nidhikamath.com.chatapplicationdemo.model.User;
import nidhikamath.com.chatapplicationdemo.model.UserChatList;


public class ChatsFragment extends Fragment {

    private RecyclerView recycler_chats;
    private RecyclerView.LayoutManager layoutManager;
    private UserAdapter userAdapter;
    private ArrayList<User> users = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ArrayList<UserChatList> userChatLists = new ArrayList<>();

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);

        recycler_chats = v.findViewById(R.id.recycler_chats);

        layoutManager = new LinearLayoutManager(getContext());
        recycler_chats.setLayoutManager(layoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserChatList").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot chatlist: dataSnapshot.getChildren()){
                        UserChatList userChat = chatlist.getValue(UserChatList.class);
                        userChatLists.add(userChat);
                    }

                    readChat();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

    private void readChat() {
        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot usersdatasnapshot: dataSnapshot.getChildren()){
                        User user = usersdatasnapshot.getValue(User.class);
                        for(UserChatList userChatList: userChatLists){
                            if(user.getuId().equals(userChatList.getId())){
                                users.add(user);
                            }
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), users);
                    recycler_chats.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
