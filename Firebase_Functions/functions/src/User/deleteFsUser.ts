import * as functions from 'firebase-functions';

const admin = require('firebase-admin');

const db = admin.firestore();



// Delete user account in firestore
export const deleteFsUser = functions.firestore.document('users/{userID}')
    .onDelete((snap, context) => {
    deleteCollection(db, 'users/{userID}/exercise_log', snap.size);
    deleteCollection(db, 'users/{userID}/weight_log', snap.size);
    //deleteCollection(db, 'users/{userID}/user_preferences', snap.size);
});


async function deleteCollection(db, collectionPath, batchSize) {
  const collectionRef = db.collection(collectionPath);
  const query = collectionRef.orderBy('id').limit(batchSize);

  return new Promise((resolve, reject) => {
    deleteQueryBatch(db, query, resolve).catch(reject);
  });
}

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

    // Recurse on the next process tick, to avoid
    // exploding the stack.
    //process.nextTick(() => {
    //  deleteQueryBatch(db, query, resolve);
    //});
}

/*
Second Function

-> Listen to the Users Table for onDelete to be triggered
-> Check to see if there are any subcollections initialized (They are stored under the UID of the user in another collection)
    -> These DBs would be the weight_logs, exercise_logs and user_preferences
-> If they exist delete and await confirmation that they were deleted successfully



*/
