const admin = require('firebase-admin');
admin.initializeApp();

const {addBlankUserOnRegister} = require('./User/addBlankUserOnRegister');

exports.addBlankUserOnRegister = addBlankUserOnRegister;

const {deleteAuthUser} = require('./User/deleteAuthUser');

exports.deleteAuthUser = deleteAuthUser;

const {deleteFsUser} = require('./User/deleteFsUser');

exports.deleteFsUser = deleteFsUser;
