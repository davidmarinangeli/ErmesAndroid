package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.SportDatabaseRepository;

/**
 * Created by nicol on 04/12/2017.
 */

public class SportRepository {
    private static SportRepository instance = new SportRepository();

    public static SportRepository getInstance() { return instance; }

    public SportRepository() {}

    public void fetchAll(final FirebaseCallback fc) {
        SportDatabaseRepository.getInstance().fetchAllSports(fc);
    }
}
