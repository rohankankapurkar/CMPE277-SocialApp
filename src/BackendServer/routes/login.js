
/**
 * Created by rohankankapurkar on 5/2/17.
 */

var mongo = require("./mongoConnection");
//var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017/CMPE277";
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/<DATABASE>?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"


// var MongoClient = require('mongodb').MongoClient
//     , format = require('util').format;
// MongoClient.connect('mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/<DATABASE>?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin', function (err, db) {
//     if (err) {
//         throw err;
//     } else {
//         console.log("successfully connected to the database");
//     }
//     db.close();
// });


function login(msg,callback){
    msg = {username:"rohan", password:"rohan"};

    var res = {};
    mongo.connect(mongoURL, function(){
        console.log('Connected to mongo at: ' + mongoURL);
        var coll = mongo.collection('Facebook');
        console.log("printing the username here"+msg.username);

        coll.findOne({username: msg.username, password:msg.password}, function(err, user){
            if (user) {
                console.log("habibi is here" +user.username);
                res.code = 200;
                res.value = "Success Login";
                res.msg = msg;
                console.log("found one entry in mongo db");
                callback(null, res);


            } else {
                console.log("returned false");
                res.code = 401;
                res.value = "Failed Login";
                //callback(null, res);
            }
        });


    });
}

exports.login = login;


