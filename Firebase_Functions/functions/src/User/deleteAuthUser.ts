import * as functions from 'firebase-functions';

const admin = require('firebase-admin');

// Delete user in auth
export const deleteAuthUser = functions.auth.user().onDelete( async user => {
    const uid = user.data().user_uid;
    
    return admin.auth.deleteUser(uid)
      .then(function() {
        console.log('Successfully deleted user');
      })
      .catch(function(error) {
        console.log('Error deleting user:', error);
      });

})

/*

First function I think would look like this:

-> Listen to auth database for onDelete trigger to be thrown
-> Find Users account by the UID and delete it


*/
