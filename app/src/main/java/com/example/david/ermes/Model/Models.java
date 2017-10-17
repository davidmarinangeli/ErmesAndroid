package com.example.david.ermes.Model;

import java.util.Date;

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

        public _Match() {}

        public _Match(Date date, String location) {
            this.date = date;
            this.location = location;
        }
    }
}
