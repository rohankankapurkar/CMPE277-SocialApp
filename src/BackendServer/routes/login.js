
/**
 * Created by rohankankapurkar on 5/2/17.
 */

var mongo = require("./mongoConnection");
//var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017/CMPE277";
//var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/<DATABASE>?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"


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

/*

function login(msg ,callback){
    //msg = {username:"rohan", password:"rohan"};
    console.log(msg.username);
    console.log(msg.password);
    console.log(msg.body);
    console.log(msg.data);
    var res = {};
    mongo.connect(mongoURL, function(err, db){
        var coll = mongo.collection('Facebook');

        coll.findOne({"username" : "rohan"}, function(err, user){
            if (user) {
                console.log("habibi is here" + user.username);
                res.code = 200;
                res.value = "Success Login";
                res.msg = msg;
                console.log("found one entry in mongo db");
                res.send({"bc": "mc"});
                //callback(null, res);


            } else {
                console.log("returned false");
                res.code = 401;
                res.value = "Failed Login";
                //callback(null, res);
                callback.send(res);
            }
        });


    });
}
*/


function login(req ,res){
    //msg = {username:"rohan", password:"rohan"}
    console.log(req.body.username);
    console.log(req.body.password);

    var username = req.body.username;
    var password = req.body.password;
    mongo.connect(mongoURL, function(err, db){
        var coll = mongo.collection('Facebook');

        coll.findOne({"email" : username,"password" :password}, function(err, user){
            if (user) {
                console.log("habibi is here" + user.email);
                //res.code = 200;
                //res.value = "Success Login";
                //res.msg = msg;
                console.log("found one entry in mongo db");
                //res.send({"bc": "mc"});
                //callback.send(res);
                res.json({"status":200,"msg": username});




            } else {
                console.log("habibi not returned false");
                res.json({"status":401,"msg": "UnSuccessful"});

                //callback(null, res);
                //callback.send(res);
            }
        });


    });
   // res.send("");
}

exports.login = login;


