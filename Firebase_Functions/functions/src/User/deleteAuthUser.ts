import * as functions from 'firebase-functions';

const admin = require('firebase-admin');

/*
-> Listen to auth database for onDelete trigger to be thrown
-> Find Users account by the UID and delete it
*/

// Delete user in auth
export const deleteAuthUser = functions.auth.user().onDelete( async user => {
    
    return admin.auth.deleteUser(user.uid)
      .then(function() {
        console.log('Successfully deleted user');
      })
      .catch(function(error) {
        console.log('Error deleting user:', error);
      });

})

