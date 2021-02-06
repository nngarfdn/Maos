package com.presidev.maos.login.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.presidev.maos.R;
import com.presidev.maos.login.model.Account;
import com.presidev.maos.login.preference.UserPreference;

import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.LEVEL_USER;

public class AuthRepository {
    private final String TAG = getClass().getSimpleName();

    private final Application application;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private final CollectionReference usersReference = database.collection("accounts");
    private final UserPreference userPreference;

    private final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNewAccount = new MutableLiveData<>();

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
    public MutableLiveData<Boolean> getIsNewAccount() {
        return isNewAccount;
    }

    public AuthRepository(Application application){
        this.application = application;
        userPreference = new UserPreference(application.getApplicationContext());
    }

    public void authWithGoogle(AuthCredential authCredential){
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d(TAG, "signInWithCredential: success");

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    userLiveData.postValue(firebaseUser);

                    boolean isNewAccount = task.getResult().getAdditionalUserInfo().isNewUser();
                    this.isNewAccount.postValue(isNewAccount);

                    String id = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    if (isNewAccount){
                        // Hanya level user yang bisa mendaftar via Google
                        Account account = new Account(id, name, email, LEVEL_USER);
                        userPreference.setData(account);
                        setDefaultAccountSettings(account);
                    } else {
                        getUserLevel(id, level -> {
                            Account account = new Account(id, name, email, level);
                            userPreference.setData(account);
                        });
                    }
                }
            } else {
                Log.w(TAG, "signInWithCredential: failure", task.getException());
            }
        });
    }

    public void registerWithEmail(String name, String email, String password, String level){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d(TAG, "createUserWithEmail: success");

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(updateProfileTask -> {
                        if (updateProfileTask.isSuccessful()) Log.d(TAG, "updateProfile: success");
                        else Log.w(TAG, "updateProfile: failure", task.getException());
                    });

                    firebaseUser.sendEmailVerification().addOnCompleteListener(emailVerificationTask -> {
                        if (emailVerificationTask.isSuccessful()) Log.d(TAG, "sendEmailVerification: success");
                        else Log.w(TAG, "sendEmailVerification: failure", task.getException());
                    });

                    userLiveData.postValue(firebaseUser);
                    isNewAccount.postValue(true);

                    String id = firebaseUser.getUid();
                    Account account = new Account(id, name, email, level);
                    userPreference.setData(account);
                    setDefaultAccountSettings(account);
                }
            } else {
                showToast(application.getApplicationContext(), "Email sudah terdaftar");
                Log.w(TAG, "createUserWithEmail: failure", task.getException());
            }
        });
    }

    public void loginWithEmail(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d(TAG, "signInWithEmail: success");
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    userLiveData.postValue(firebaseUser);
                    isNewAccount.postValue(false);

                    String id = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    getUserLevel(id, level -> {
                        Account account = new Account(id, name, email, level);
                        userPreference.setData(account);
                    });
                }
            } else {
                showToast(application.getApplicationContext(),  "Kata sandi salah");
                Log.w(TAG, "signInWithEmail: failure", task.getException());
            }
        });
    }

    public void sendPasswordReset(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                showToast(application.getApplicationContext(), "Permintaan setel ulang kata sandi telah dikirim ke email");
            } else {
                showToast(application.getApplicationContext(), "Email belum terdaftar");
                Log.w(TAG, "sendPasswordReset: failure", task.getException());
            }
        });
    }

    private void setDefaultAccountSettings(Account newAccount){
        DocumentReference uidReference = usersReference.document(newAccount.getId());
        uidReference.set(newAccount).addOnCompleteListener(task -> {
            if (task.isSuccessful()) Log.d(TAG, "setDefaultAccountSettings: success");
            else Log.w(TAG, "sendPasswordReset: failure", task.getException());
        });
    }

    private void getUserLevel(String userId, OnGetUserLevel callback){
        DocumentReference uidReference = usersReference.document(userId);
        uidReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                callback.onSuccess(task.getResult().getString("level"));
                Log.d(TAG, "setDefaultAccountSettings: success");
            }
            else Log.w(TAG, "sendPasswordReset: failure", task.getException());
        });
    }

    public void logout(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignIn.getClient(application, gso).signOut();
        firebaseAuth.signOut();

        UserPreference userPreference = new UserPreference(application);
        userPreference.resetData();

        userLiveData.postValue(firebaseAuth.getCurrentUser());
        isNewAccount.postValue(false);
    }

    private interface OnGetUserLevel{
        void onSuccess(String userLevel);
    }
}
