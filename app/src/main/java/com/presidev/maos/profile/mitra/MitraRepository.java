package com.presidev.maos.profile.mitra;

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

public class MitraRepository {
    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public final CollectionReference reference = database.collection("mitra");

    private final MutableLiveData<Mitra> mitraLiveData = new MutableLiveData<>();
    public MutableLiveData<Mitra> getMitraLiveData(){
        return mitraLiveData;
    }

    public void query(String userId){
        reference.document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Mitra mitra = task.getResult().toObject(Mitra.class);
                        mitraLiveData.postValue(mitra);
                        Log.d(TAG, "query: " + mitra.getName());
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void insert(Mitra mitra){
        reference.document(mitra.getId())
                .set(mitra)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was added");
                    else Log.w(TAG, "Error adding document", task.getException());
                });
    }

    public void update(Mitra mitra){
        reference.document(mitra.getId())
                .update(objectToHashMap(mitra))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }

    public void uploadImage(Context context, Uri uri, String folderName, String fileName, OnImageUploadCallback callback){
        byte[] image = convertUriToByteArray(context, uri);
        image = getCompressedByteArray(image, folderName.equals(FOLDER_PROFILE));

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

    private Map<String, Object> objectToHashMap(Mitra mitra){
        Map<String, Object> document = new HashMap<>();
        document.put("id", mitra.getId());
        document.put("logo", mitra.getLogo());
        document.put("name", mitra.getName());
        document.put("email", mitra.getEmail());
        document.put("description", mitra.getDescription());
        document.put("whatsApp", mitra.getWhatsApp());
        document.put("address", mitra.getAddress());
        document.put("province", mitra.getProvince());
        document.put("regency", mitra.getRegency());
        document.put("district", mitra.getDistrict());
        document.put("rules", mitra.getRules());
        document.put("COD", mitra.isCOD());
        document.put("kirimLuarKota", mitra.isKirimLuarKota());
        return document;
    }
}
