package nidhikamath.com.chatapplicationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nidhikamath.com.chatapplicationdemo.R;
import nidhikamath.com.chatapplicationdemo.model.ChatMessage;

public class SendMessageAdapter extends RecyclerView.Adapter<SendMessageAdapter.MyViewHolder> {
    Context context;
    ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    public static final int MSG_SENDER = 1;
    public static final int MSG_RECEIVER = 0;
    private FirebaseUser firebaseUser;

    public SendMessageAdapter(Context context, ArrayList<ChatMessage> chatMessages ){
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == MSG_SENDER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_chat_item, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_chat_item, parent, false);
        }
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.tv_message.setText(chatMessage.getMessage());

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_profile;
        TextView tv_message;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatMessages.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_SENDER;
        }else {
            return MSG_RECEIVER;
        }
    }
}
