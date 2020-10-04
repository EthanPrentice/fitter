import * as functions from 'firebase-functions';

const admin = require('firebase-admin');
admin.initializeApp();

const {addBlankUserOnRegister} = require('./userFunctions/addBlankUserOnRegister');

exports.addBlankUserOnRegister = addBlankUserOnRegister;

//delete firestore user account
const {deleteFsUser} = require('./userFunctions/deleteFsUser');
exports.deleteFsUser = deleteFsUser;

//delete auth user
const {deleteAuthUser} = require('./userFunctions/deleteAuthUser');
exports.deleteAuthUser = deleteAuthUser;
