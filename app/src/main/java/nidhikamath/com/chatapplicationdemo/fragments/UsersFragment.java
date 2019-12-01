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

public class UsersFragment extends Fragment {

    private RecyclerView recycler_users;
    private RecyclerView.LayoutManager layoutManager;
    private UserAdapter userAdapter;
    private ArrayList<User> users = new ArrayList<>();

    public UsersFragment() {
        //Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recycler_users = view.findViewById(R.id.recycler_users);

        layoutManager = new LinearLayoutManager(getContext());
        recycler_users.setLayoutManager(layoutManager);

        get_Users();

        return view;
    }

    private void get_Users() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot user: dataSnapshot.getChildren()){
                        User user1 = user.getValue(User.class);
                        if(user1!=null && firebaseUser!=null) {
                            if (!user1.getuId().equals(firebaseUser.getUid()))
                                users.add(new User(user1.getuId(), user1.getUsername(), user1.getStatus()));
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), users);
                    recycler_users.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
