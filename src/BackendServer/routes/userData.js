/**
 * Created by Shruti Loya on 5/4/2017.
 */


var mongo = require("./mongoConnection");
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"

function getUserData(req ,res)
{

   console.log(req.query.username);
    var username = req.query.username;
    mongo.connect(mongoURL, function(err, db){
        var coll = mongo.collection('Facebook');

        coll.findOne({"email" : username}, function(err, user){
            if (user) {
                 res.json(user);
                console.log("***************",user);

            } else {
                console.log("returned false");
                res.code = 401;

            }
        });


    });

}

exports.getUserData = getUserData;


