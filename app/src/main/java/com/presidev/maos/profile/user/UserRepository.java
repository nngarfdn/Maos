package com.presidev.maos.profile.user;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.presidev.maos.callback.OnImageUploadCallback;

import java.util.HashMap;
import java.util.Map;

import static com.presidev.maos.utils.Constants.FOLDER_PROFILE;
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
                        if (user != null) Log.d(TAG, "query: " + user.getName());
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void queryByEmail(String email){
        reference.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        User user = null;
                        if (task.getResult().getDocuments().size() > 0){
                            user = task.getResult().getDocuments().get(0).toObject(User.class);
                            Log.d(TAG, "query: " + user.getName());
                        }
                        userLiveData.postValue(user);
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void insert(User user){
        reference.document(user.getId())
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was added");
                    else Log.w(TAG, "Error adding document", task.getException());
                });
    }

    public void update(User user){
        reference.document(user.getId())
                .update(objectToHashMap(user))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }

    public void uploadImage(Context context, Uri uri, String folderName, String fileName, OnImageUploadCallback callback){
        byte[] image = convertUriToByteArray(context, uri);
        image = getCompressedByteArray(image, folderName.equals(FOLDER_PROFILE)); // Jika foto profil, perkecil ukuran

        StorageReference reference = storage.getReference().child(folderName + "/" + fileName);
        UploadTask uploadTask = reference.putBytes(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uriResult -> {
            callback.onSuccess(uriResult.toString());
            Log.d(TAG, "Image was uploaded");
        })).addOnFailureListener(e -> Log.w(TAG, "Error uploading image", e));
    }

    public void deleteImage(String imageUrl){
        storage.getReferenceFromUrl(imageUrl).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Image was deleted"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting image", e));
    }

    public void addSnapshotListener(String userId){
        reference.addSnapshotListener((value, error) -> {
            if (error != null) Log.w(TAG, "Listen failed", error);
            else if (value != null){
                query(userId);
                Log.d(TAG, "Changes detected");
            }
        });
    }

    private Map<String, Object> objectToHashMap(User user){
        Map<String, Object> document = new HashMap<>();
        document.put("id", user.getId());
        document.put("photo", user.getPhoto());
        document.put("name", user.getName());
        document.put("email", user.getEmail());
        document.put("whatsApp", user.getWhatsApp());
        document.put("idCard", user.getIdCard());
        document.put("address", user.getAddress());
        document.put("province", user.getProvince());
        document.put("regency", user.getRegency());
        document.put("district", user.getDistrict());
        return document;
    }
}
