/**
 * Created by rohankankapurkar on 5/2/17.
 */


// var MongoClient = require('mongodb').MongoClient;
// var assert = require('assert');
//

    //this is the location of mongoDB
// var url = 'mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017/CMPE277';
// MongoClient.connect(url, function(err, db) {
//     assert.equal(null, err);
//     console.log("Connected correctly to server.");
//     db.close();
// });

var MongoClient = require('mongodb').MongoClient;
var db;
var connected = false;

/**
 * Connects to the MongoDB Database with the provided URL
 */
exports.connect = function(url, callback){
    MongoClient.connect(url, function(err, _db){
        if (err) { throw new Error('Could not connect: '+err); }
        console.log("Succesfully connected to the DB");
        db = _db;
        connected = true;
        console.log(connected +" is connected?");
        callback(db);
        db.close();
    });
};

/**
 * Returns the collection on the selected database
 */
exports.collection = function(name){
    if (!connected) {
        throw new Error('Must connect to Mongo before calling "collection"');
    }
    return db.collection(name);

};