const admin = require('firebase-admin');
admin.initializeApp();

const {addBlankUserOnRegister} = require('./User/addBlankUserOnRegister');

exports.addBlankUserOnRegister = addBlankUserOnRegister;
