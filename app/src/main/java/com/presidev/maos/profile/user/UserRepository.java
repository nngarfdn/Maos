package com.presidev.maos.profile.user;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.presidev.maos.callback.OnImageUploadCallback;

import java.util.Map;

import static com.presidev.maos.utils.ImageUtils.convertUriToByteArray;
import static com.presidev.maos.utils.ImageUtils.getCompressedByteArray;

public class UserRepository {
    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public final CollectionReference reference = database.collection("users");

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    public MutableLiveData<User> getUserLiveData(){
        return userLiveData;
    }

    public void query(String userId){
        reference.document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        User user = task.getResult().toObject(User.class);
                        userLiveData.postValue(user);
                        Log.d(TAG, "query: " + user.getName());
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void insert(User user){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was added");
                    else Log.w(TAG, "Error adding document", task.getException());
                });
    }

    public void update(User user){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.document(userId)
                .update((Map<String, Object>) user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }

    public void update(String image){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.document(userId)
                .update("logo", image)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Image was updated");
                    else Log.w(TAG, "Error updating image", task.getException());
                });
    }

    public void uploadImage(Context context, Uri uri, String folderName, String fileName, OnImageUploadCallback callback){
        byte[] image = convertUriToByteArray(context, uri);
        image = getCompressedByteArray(image, true); // Jika foto profil, perkecil ukuran

        StorageReference reference = storage.getReference().child(folderName + "/" + fileName);
        UploadTask uploadTask = reference.putBytes(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri1 -> {
            callback.onSuccess(uri1.toString());
            Log.d(TAG, "Image was uploaded");
        })).addOnFailureListener(e -> Log.w(TAG, "Error uploading image", e));
    }

    public void deleteImage(String imageUrl){
        storage.getReferenceFromUrl(imageUrl).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Image was deleted"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting image", e));
    }
}
