package com.example.david.ermes.View.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Presenter.utils.TimeUtils;
import com.example.david.ermes.R;
import com.example.david.ermes.View.ProgressDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // TODO : creare un sign-in/up presenter in cui creare la logica dell'activity
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    private Button registrati_button;
    private Intent result_intent;
    private Spinner sportspinner;
    private SpinnerAdapter adapter;
    private String selected_sport;
    private android.support.v7.widget.Toolbar toolbar;

    private MaterialEditText mail_editText;
    private MaterialEditText password_editText;
    private MaterialEditText name_editText;


    private Calendar born_date_calendar;

    private TextView born_date_button;
    private TextView city_button;
    private Place place_selected;

    private String photoURL = "";
    private ProgressDialog progressDialog;

    private final int PLACE_AUTOCOMPLETE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);

        result_intent = getIntent();

        // prendo l'istanza del FBAuth
        mAuth = FirebaseAuth.getInstance();

        born_date_calendar = Calendar.getInstance();
        born_date_calendar.clear();

        toolbar = findViewById(R.id.signup_toolbar);

        // vado ad istanziare lo spinner
        sportspinner = findViewById(R.id.sport_spinner);


        born_date_button = findViewById(R.id.born_date_textview);
        city_button = findViewById(R.id.textCity);

        // setto i parametri dell'editext login e password
        mail_editText = findViewById(R.id.signup_mail);
        password_editText = findViewById(R.id.signup_pwd);
        name_editText = findViewById(R.id.username_signup);

        registrati_button = findViewById(R.id.registrati_button);

        toolbar.setTitle("Registrazione");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registrati_button.setOnClickListener(this);
        city_button.setOnClickListener(view -> findPlace());
        sportspinner.setOnItemSelectedListener(new itemSelectedListener());

        born_date_button.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(onDateSetListener);
            datePickerDialog.setMaxDate(Calendar.getInstance());
            datePickerDialog.show(getFragmentManager(), "datepickerdialog");
        });

        final ArrayList<String> arraySpinner = new ArrayList<>();
        SportRepository.getInstance().fetchAll(object -> {
            for (Sport s : (ArrayList<Sport>) object) {
                arraySpinner.add(s.getName());
            }
            adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, arraySpinner);
            sportspinner.setAdapter(adapter);

        });


        if (result_intent.getExtras() != null) {
            mail_editText.setText(result_intent.getStringExtra("mail"));
            name_editText.setText(result_intent.getStringExtra("name"));

            name_editText.setEnabled(false);
            mail_editText.setEnabled(false);

            password_editText.setVisibility(View.GONE);
            password_editText.setHelperText("Utilizzeremo la password di Google");

            photoURL = result_intent.getStringExtra("photoURL");
        }

    }

    private void findPlace() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_CODE && resultCode == RESULT_OK) {
            place_selected = PlaceAutocomplete.getPlace(this, data);
            city_button.setText(place_selected.getName());
            city_button.setTextColor(Color.BLACK);
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            // TODO: Handle the error.
            Log.i(TAG, status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    private void signUpNormal(final String name, final String email, final String password, final String city) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");

                        SportRepository.getInstance().fetchSportByName(selected_sport, object -> {
                            if (object != null) {
                                Sport found_sport = (Sport) object;
                                new User(
                                        name,
                                        email,
                                        task.getResult().getUser().getUid(),
                                        city,
                                        found_sport.getID(),
                                        photoURL,
                                        born_date_calendar.getTimeInMillis()
                                ).save(object1 -> finish());
                            } else {
                                Snackbar.make(getWindow().getDecorView(), "Errore nella ricerca dello sport", Snackbar.LENGTH_LONG)
                                        .setAction(":(", null);
                            }

                            DatabaseManager.get().setLogged(true);
                            progressDialog.dismiss();
                        });

                    } else {
                        DatabaseManager.get().setLogged(false);

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }

                    // ...
                });
    }

    private class itemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            selected_sport = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registrati_button) {

            if (result_intent.getExtras() != null) {
                // se sono un utente Google e devo registrarmi avrò dei dati già fillati
                manageNewGoogleUser();
            } else {
                // altrimenti riempio a mano tutto e controllo
                manageNewNormalUser();
            }
        }
    }

    private void manageNewNormalUser() {
        if (isAllFilled(1)) {
            signUpNormal(name_editText.getText().toString(),
                    mail_editText.getText().toString(),
                    password_editText.getText().toString(),
                    place_selected.getName().toString());

        } else {
            if (TextUtils.isEmpty(password_editText.getText().toString()))
                password_editText.setError("Inserisci una password");

            if (TextUtils.isEmpty(mail_editText.getText().toString()))
                mail_editText.setError("Inserisci una mail");

            if (TextUtils.isEmpty(name_editText.getText().toString()))
                name_editText.setError("Inserisci un username");
        }
    }

    private void manageNewGoogleUser() {
        if (isAllFilled(2)) {
            progressDialog.show();

            SportRepository.getInstance().fetchSportByName(selected_sport, object -> {
                Sport found_sport = (Sport) object;

                new User(result_intent.getStringExtra("name"),
                        result_intent.getStringExtra("mail"),
                        result_intent.getStringExtra("uid"),
                        place_selected.getName().toString(),
                        found_sport.getID(),
                        photoURL,
                        born_date_calendar.getTimeInMillis()
                ).save(object1 -> {
                    progressDialog.dismiss();
                    DatabaseManager.get().setLogged(true);
                    finish();
                });
            });
        } else {
            DatabaseManager.get().setLogged(false);

            if (TextUtils.isEmpty(mail_editText.getText().toString()))
                mail_editText.setError("Inserisci una mail");

            if (TextUtils.isEmpty(name_editText.getText().toString()))
                name_editText.setError("Inserisci un username");
        }
    }

    private boolean isAllFilled(int index) {
        if (index == 1) {
            return !TextUtils.isEmpty(mail_editText.getText().toString()) &&
                    (!TextUtils.isEmpty(password_editText.getText().toString())) &&
                    (!TextUtils.isEmpty(name_editText.getText().toString())) &&
                    (place_selected != null && !TextUtils.isEmpty(place_selected.getName().toString()));
        } else {
            return (!TextUtils.isEmpty(mail_editText.getText().toString()) &&
                    (!TextUtils.isEmpty(name_editText.getText().toString())) &&
                    (place_selected != null && !TextUtils.isEmpty(place_selected.getName().toString())));
        }
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePicker, int year, int month, int day) {
            born_date_calendar.set(Calendar.YEAR, year);
            born_date_calendar.set(Calendar.MONTH, month);
            born_date_calendar.set(Calendar.DAY_OF_MONTH, day);

            born_date_button.setText(TimeUtils.fromMillistoYearMonthDay(born_date_calendar.getTimeInMillis()));

        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
