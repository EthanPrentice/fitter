import * as functions from 'firebase-functions';
const admin = require('firebase-admin');

// Adds a blank user into the database when they are registered
export const addBlankUserOnRegister = functions.auth.user().onCreate( async user => {

    const blankUser = {
        birth_date : null,
        name : {
            first : null,
            last : null
        },
        gender: null,
        owned_workouts : [],
        subscribed_workouts : [],
        personal_records : {},
        current_weight : null,
        weight_goal : {},
        user_preferences : {
            weight_unit : null
        }
    }

    return admin.firestore().collection('users').doc(user.uid).set(blankUser);
    
})