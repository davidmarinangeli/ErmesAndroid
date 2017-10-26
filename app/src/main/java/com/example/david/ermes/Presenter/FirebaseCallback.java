package com.example.david.ermes.Presenter;
import com.example.david.ermes.Model.Models;

import java.util.List;

/**
 * Created by nicol on 26/10/2017.
 */

public interface FirebaseCallback {
    public void callback(List<Models._Match> matches);
}
