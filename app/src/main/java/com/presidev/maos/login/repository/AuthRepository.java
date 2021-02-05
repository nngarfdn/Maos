package com.presidev.maos.login.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.presidev.maos.login.model.User;

import static com.presidev.maos.utils.AppUtils.showToast;

public class AuthRepository {
    private static final String TAG = AuthRepository.class.getSimpleName();

    private final Application application;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private final CollectionReference usersReference = database.collection("users");
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public AuthRepository(Application application){
        this.application = application;
    }

    public void authWithGoogle(AuthCredential authCredential){
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d(TAG, "signInWithCredential: success");

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    String id = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    User user = new User(id, name, email);
                    user.setNew(task.getResult().getAdditionalUserInfo().isNewUser());
                    userLiveData.postValue(user);

                    if (user.isNew()) setDefaultAccountSettings(user);
                }
            } else {
                Log.w(TAG, "signInWithCredential: failure", task.getException());
            }
        });
    }

    public void registerWithEmail(String name, String email, String password){
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

                    String id = firebaseUser.getUid();
                    User user = new User(id, name, email);
                    user.setNew(true);
                    userLiveData.postValue(user);

                    setDefaultAccountSettings(user);
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
                    String id = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    User user = new User(id, name, email);
                    userLiveData.postValue(user);
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

    private void setDefaultAccountSettings(User newUser){
        DocumentReference uidReference = usersReference.document(newUser.getId());
        uidReference.set(newUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) Log.d(TAG, "setDefaultAccountSettings: success");
            else Log.w(TAG, "sendPasswordReset: failure", task.getException());
        });
    }
}
