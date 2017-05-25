/**
 * Created by Shruti Loya on 5/4/2017.
 */


var mongo = require("./mongoConnection");
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"

function getUserData(req ,res)
{

   console.log("********IN USERDATAT:",req.query.username);
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

function updateUserProfile(req ,res)
{

    console.log("********IN UPDATE_USER_PROFILE:");

    var email = req.body.email;
    var name = req.body.name;
    var profession = req.body.profession;
    var address = req.body.address;
    var interests = req.body.interests;
    var about = req.body.about;
    var profilePic= req.body.profilePic;
    var isPrivate = req.body.isPrivate;

    mongo.connect(mongoURL, function(err, db){
        var coll = mongo.collection('Facebook');

        coll.updateOne({"email" : email}, {$set:{"displayName":name,"address":address,"profession":profession,"interests":interests,"about":about,"profilePic":profilePic,"isPrivate":isPrivate}}, function(err, user){
            if (user) {
                console.log("updated the user successfully");
                res.statuscode = 0;
                res.json({"status":0,"user":user});


            } else {
                res.statuscode = 1;
                res.message = "Error occurred while updating the user's information";
                console.log("Error occurred while updating the user's information");


            }
        });


    });

}

function updateUserPreferances(req ,res)
{

    console.log("********IN User settings:");

    var email = req.body.email;
    var notification = req.body.notification;
    var isPrivate = req.body.isPrivate;

    mongo.connect(mongoURL, function(err, db){
        var coll = mongo.collection('Facebook');

        coll.updateOne({"email" : email}, {$set:{"notification":notification,"isPrivate":isPrivate}}, function(err, user){
            if (user) {
                console.log("updated the user successfully");
                res.statuscode = 0;
                res.json({"status":0,"user":user});


            } else {
                res.statuscode = 1;
                res.message = "Error occurred while updating the user's information";
                console.log("Error occurred while updating the user's information");


            }
        });


    });

}



exports.getUserData = getUserData;
exports.updateUserProfile = updateUserProfile;

