package com.example.david.ermes.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by nicol on 15/01/2018.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    private List<User> friendsList;
    private List<Long> datesList;
    private List<String> sportsList;
    private Context context;

    public FriendsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.default_friend_item,
                parent,
                false
        );

        return new FriendsAdapter.FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return friendsList != null ? friendsList.size() : 0;
    }

    public void refreshList(Map<Friendship, User> friends) {
        this.friendsList = new ArrayList<>();
        this.datesList = new ArrayList<>();
        this.sportsList = new ArrayList<>();

        if (friends != null) {
            for (Map.Entry<Friendship, User> entry : friends.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    SportRepository.getInstance().fetchSportById(entry.getValue().getIdFavSport(),
                            new FirebaseCallback() {
                                @Override
                                public void callback(Object object) {
                                    if (object != null) {
                                        friendsList.add(entry.getValue());
                                        datesList.add(entry.getKey().getDate());
                                        sportsList.add(((Sport) object).getName());
                                    }
                                }
                            });
                }
            }

            notifyDataSetChanged();
        }
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView friendName;
        TextView friendInfo;
        TextView friendshipDate;

        public FriendViewHolder(View itemView) {
            super(itemView);

            friendName = itemView.findViewById(R.id.friend_name);
            friendInfo = itemView.findViewById(R.id.friend_info);
            friendshipDate = itemView.findViewById(R.id.friendship_date);
        }

        public void bind(int position) {
            User friend = friendsList.get(position);
            Long date = datesList.get(position);
            String sport = sportsList.get(position);

            Calendar now = Calendar.getInstance();
            Calendar birth = Calendar.getInstance();
            birth.setTimeInMillis(friend.getBirthDate());

            int year_now = now.get(Calendar.YEAR), year_birth = birth.get(Calendar.YEAR);
            int month_now = now.get(Calendar.MONTH), month_birth = birth.get(Calendar.MONTH);
            int day_now = now.get(Calendar.DAY_OF_MONTH), day_birth = birth.get(Calendar.DAY_OF_MONTH);

            int age = year_now - year_birth;
            if (month_now < month_birth || (month_now == month_birth && day_now < day_birth)) {
                age--;
            }

            friendName.setText(friend.getName());
            friendInfo.setText(String.valueOf(age) + " anni | " +
                    sport);
            friendshipDate.setText("Amici dal " + TimeUtils.fromMillistoYearMonthDay(date));
        }
    }
}
