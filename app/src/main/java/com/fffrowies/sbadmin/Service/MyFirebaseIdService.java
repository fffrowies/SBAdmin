package com.fffrowies.sbadmin.Service;

import com.fffrowies.sbadmin.Common.Common;
import com.fffrowies.sbadmin.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (Common.currentUser != null)
            updateTokenToFirebase(refreshedToken);
    }

    private void updateTokenToFirebase(String refreshedToken) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token token = new Token(refreshedToken, false);     //false because this token is from Client
        tokens.child(Common.currentUser.getPhone()).setValue(token);
    }
}
