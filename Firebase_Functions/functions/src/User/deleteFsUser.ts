import * as functions from 'firebase-functions';

const admin = require('firebase-admin');

const db = admin.firestore();

/*
-> Listen to the Users Table for onDelete to be triggered
-> Check to see if there are any subcollections initialized
-> These subcollections would be the weight_logs, exercise_logs and user_preferences
-> If they exist delete and await confirmation that they were deleted successfully
*/

// Delete user account in firestore
export const deleteFsUser = functions.firestore.document('users/{userID}')
    .onDelete((snap, context) => {
    deleteCollection(db, 'users/{userID}/exercise_log', snap.size);
    deleteCollection(db, 'users/{userID}/weight_log', snap.size);
    deleteCollection(db, 'users/{userID}/user_preferences', snap.size);
});


//delete a collection (generic) function
//calls a helper function to delete docs in batch
async function deleteCollection(db, collectionPath, batchSize) {
  const collectionRef = db.collection(collectionPath);
  const query = collectionRef.orderBy('id').limit(batchSize);

  return new Promise((resolve, reject) => {
    deleteQueryBatch(db, query, resolve).catch(reject);
  });
}

//helper function to delete docs in batch 
async function deleteQueryBatch(db, query, resolve) {
  const snapshot = await query.get();

  const batchSize = snapshot.size;
  if (batchSize === 0) {
    // When there are no documents left, we are done
    resolve();
    return;
  }
  // Delete documents in a batch
    const batch = db.batch();
    snapshot.docs.forEach((doc) => {
      batch.delete(doc.ref);
    });
    await batch.commit();
}

