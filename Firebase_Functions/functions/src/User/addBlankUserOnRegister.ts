import * as functions from 'firebase-functions';

const admin = require('firebase-admin');

// Adds a blank user into the database when they are registered
export const addBlankUserOnRegister = functions.auth.user().onCreate( async user => {

    return admin.firestore().collection('users').add({
        age : null,
        birth_date : null,
        name : {
            first : null,
            last : null
        },
        owned_workouts : [],
        subscribed_workouts : [],
        personal_records : {},
        current_weight : null,
        current_height : null,
        weight_goal : {}
    });

})
