package com.example.project.db;

import androidx.annotation.NonNull;

import com.example.project.bean.MyPlan;
import com.example.project.bean.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreManager {

    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void userAdd(User user, OnFirestoreUserAddListener onFirestoreUserAddListener) {

        getUser(user.name, new OnFirestoreUserListener() {

            @Override
            public void success(User user) {
                if (onFirestoreUserAddListener != null) {
                    onFirestoreUserAddListener.failed("User name already exists");
                }

            }

            @Override
            public void failed(String msg) {

                Map<String, String> userMap = new HashMap<>();
                userMap.put(user.name, user.toJSONString());
                db.document("/users/" + user.name)
                        .set(userMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (onFirestoreUserAddListener != null) {
                                    if (task.isComplete()) {
                                        onFirestoreUserAddListener.success(user);
                                    } else {
                                        onFirestoreUserAddListener.failed("save error");

                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (onFirestoreUserAddListener != null) {
                                    onFirestoreUserAddListener.failed(e.getMessage());
                                }

                            }
                        });


            }
        });

    }

    public static void getUser(String name, OnFirestoreUserListener onFirestoreUserListener) {
        db.document("/users/" + name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getData() != null && !documentSnapshot.getData().isEmpty()) {
                            User user = User.parseJSON(documentSnapshot.getData().get(name).toString());
                            if (onFirestoreUserListener != null) {
                                if (user != null && user.name.equals(name)) {
                                    onFirestoreUserListener.success(user);
                                } else {
                                    onFirestoreUserListener.failed("");
                                }
                            }

                        } else {
                            if (onFirestoreUserListener != null) {
                                onFirestoreUserListener.failed("");
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (onFirestoreUserListener != null) {
                    onFirestoreUserListener.failed("");
                }

            }
        });
    }

    public static void updateUser(User user) {
//        Map<String, String> userMap = new HashMap<>();
//        userMap.put(user.name, user.toJSONString());
        db.document("/users/" + user.name).update(user.name, user.toJSONString());
    }

    public static void insertPlan(MyPlan myPlan) {
        getPlans(myPlan.userName, new OnFirestorePlansListener() {
            @Override
            public void success(List<MyPlan> myPlans, String name, String imgTag) {
                if (myPlans == null) {
                    myPlans = new ArrayList<>();
                }
                myPlan.userName = name;
                myPlan.imgTag = imgTag;
                myPlans.add(myPlan);
                db.document("/users/" + myPlan.userName)
                        .update("plans", MyPlan.toJSONArrayString(myPlans));
            }

            @Override
            public void failed(String msg) {

            }
        });


    }

    public static void updatePlan(MyPlan myPlan) {
        getPlans(myPlan.userName, new OnFirestorePlansListener() {
            @Override
            public void success(List<MyPlan> myPlans, String name, String imgTag) {
                if (myPlans == null) {
                    myPlans = new ArrayList<>();
                }
//                myPlan.userName = name;
//                myPlan.imgTag = imgTag;
                for (int i = 0; i < myPlans.size(); i++) {
                    if (myPlan.equals(myPlans.get(i))) {
                        myPlans.set(i, myPlan);
                    }
                }
                db.document("/users/" + name)
                        .update("plans", MyPlan.toJSONArrayString(myPlans));
            }

            @Override
            public void failed(String msg) {

            }
        });


    }

    public static void getPlans(String name, OnFirestorePlansListener onFirestorePlansListener) {
        db.document("/users/" + name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getData() != null && !documentSnapshot.getData().isEmpty()) {
                            List<MyPlan> myPlans = new ArrayList<>();
                            Object plansStr = documentSnapshot.getData().get("plans");
                            User user = User.parseJSON(documentSnapshot.getData().get(name).toString());
                            if (plansStr != null) {
                                myPlans = MyPlan.parseJSONArray(plansStr.toString());
                            }

                            if (onFirestorePlansListener != null) {
                                onFirestorePlansListener.success(myPlans, user.name, user.imgTag);
                            }

                        } else {
                            if (onFirestorePlansListener != null) {
                                onFirestorePlansListener.failed("");
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onFirestorePlansListener != null) {
                            onFirestorePlansListener.failed(e.getMessage());
                        }
                    }
                });
    }

    public static void getSharePlans(OnFirestoreSharePlansListener onFirestoreSharePlansListener) {
        db.collection("/users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<MyPlan> mySharePlans = new ArrayList<>();
                        if (!queryDocumentSnapshots.getDocuments().isEmpty() && !queryDocumentSnapshots.getDocuments().isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                if (documentSnapshot.getData() != null && documentSnapshot.getData().get("plans") != null) {
                                    List<MyPlan> myPlans = MyPlan.parseJSONArray(documentSnapshot.getData().get("plans").toString());
                                    if (myPlans != null && !myPlans.isEmpty()) {
                                        for (MyPlan myPlan: myPlans) {
                                            if (myPlan.isShare.equals("1") && !myPlan.isDel.equals("1")) {
                                                mySharePlans.add(myPlan);
                                            }
                                        }
                                    }
                                }

                            }

                        }
                        if (onFirestoreSharePlansListener != null) {
                            onFirestoreSharePlansListener.success(mySharePlans);
                        }

                    }
                 })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onFirestoreSharePlansListener != null) {
                            onFirestoreSharePlansListener.failed(e.getMessage());
                        }
                    }
                });
    }


    public interface OnFirestoreUserListener {
        void success(User user);

        void failed(String msg);
    }

    public interface OnFirestoreUserAddListener {
        void success(User user);

        void failed(String msg);
    }

    public interface OnFirestorePlansListener {
        void success(List<MyPlan> myPlans, String name, String imgTag);

        void failed(String msg);
    }

    public interface OnFirestoreSharePlansListener {
        void success(List<MyPlan> myPlans);

        void failed(String msg);
    }


}
