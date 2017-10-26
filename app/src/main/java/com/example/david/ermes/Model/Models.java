package com.example.david.ermes.Model;

import com.example.david.ermes.Presenter.Match;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nicol on 17/10/2017.
 */

public class Models {
    public static class _User {
        public String name;

        public _User() {}

        public _User(String name) {
            this.name = name;
        }
    }

    public static class _Match {
        public Date date;
        public String location;
        public String owner;

        public _Match() {}

        public _Match(String idOwner, Date date, String location) {
            this.date = date;
            this.location = location;
            this.owner = idOwner;
        }

        public Match convertToMatch() {
            return new Match(this.owner, this.location, this.date);
        }

        public static List<Match> convertToMatchList(List<Models._Match> list) {
            List<Match> matches = new ArrayList<>();
            for (Models._Match m : list) {
                matches.add(m.convertToMatch());
            }
            return matches;
        }
    }
}
