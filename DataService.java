package com.codevengers.firebase_config;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DataService {

    private static Firestore db;

    static {
        try {
            initializeFirebase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(
                "habbit_tracker\\src\\main\\resources\\habbit-tracker.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    public void addData(String collection, String document, Map<String, Object> data)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(document);
        ApiFuture<WriteResult> result = docRef.set(data);
        result.get();
    }

    public DocumentSnapshot getData(String collection, String document)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(document);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        return future.get();
    }

    public boolean authenticateUser(String username, String password) throws FirebaseAuthException {
        try {
            DocumentSnapshot document = db.collection("users").document(username).get().get();
            if (document.exists()) {
                String storedPassword = document.getString("password");
                return password.equals(storedPassword);
            }
            return false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateHabitProgress(String collection, String documentId, String habitName, double progress)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Map<String, Object> habits = (Map<String, Object>) document.get("habits");
            if (habits != null) {
                Map<String, Object> habit = (Map<String, Object>) habits.get(habitName);
                if (habit != null) {
                    double currentProgress = (double) habit.get("progress");
                    int days = (int) (long) habit.get("days");
                    double newProgress = (currentProgress * days + progress) / days;
                    habit.put("progress", newProgress);
                    habits.put(habitName, habit);
                    document.getReference().update("habits", habits);
                }
            }
        }
    }

    public void updateTaskCompletion(String collection, String userEmail, String taskName, boolean completed)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(userEmail)
                .collection("userTasks").document(taskName);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            document.getReference().update("completed", completed);
        }
    }

    public void deleteTask(String collection, String userEmail, String taskName)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(userEmail)
                .collection("userTasks").document(taskName);
        ApiFuture<WriteResult> result = docRef.delete();
        result.get();
    }
}