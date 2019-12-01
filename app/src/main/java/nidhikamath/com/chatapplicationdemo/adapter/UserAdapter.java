package nidhikamath.com.chatapplicationdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nidhikamath.com.chatapplicationdemo.R;
import nidhikamath.com.chatapplicationdemo.SendMessageActivity;
import nidhikamath.com.chatapplicationdemo.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> users = new ArrayList<>();

    public UserAdapter(Context context, ArrayList<User> users ){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_username.setText(users.get(position).getUsername());

        if(users.get(position).getStatus().equals("online")){
            holder.iv_status.setVisibility(View.VISIBLE);
        }else{
            holder.iv_status.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SendMessageActivity.class);
                i.putExtra("uId", users.get(position).getuId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_user_profile, iv_status;
        TextView tv_username;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_user_profile = itemView.findViewById(R.id.iv_user_profile);
            iv_status = itemView.findViewById(R.id.iv_status);
            tv_username = itemView.findViewById(R.id.tv_username);
        }
    }
}
