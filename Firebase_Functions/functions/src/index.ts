import * as functions from 'firebase-functions';

const admin = require('firebase-admin');
admin.initializeApp();

const {addBlankUserOnRegister} = require('./userFunctions/addBlankUserOnRegister');

exports.addBlankUserOnRegister = addBlankUserOnRegister;
