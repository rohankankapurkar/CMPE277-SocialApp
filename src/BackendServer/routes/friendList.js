/**
 * Created by Nachiket Joshi on 5/11/2017.
 */

var mongo = require("./mongoConnection");
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"

function getFriendList(req, res) {

	console.log("********IN GET FRIEND LIST:", req.query.username);
	var username = req.query.username;
	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');

		collection_facebook.find({}).toArray(function(err, user) {
			if (user) {
				res.json(user);
				console.log("***************", user);

			} else {
				console.log("returned false");
				res.code = 401;

			}
		});

	});

}

function sendFriendRequest(req ,res){

    console.log("********NACHIKET: Inside sendFriendRequest function********");

    var email = req.body.email;
    console.log("email: "+ email);

    /* mongo.connect(mongoURL, function(err, db){
        var collection_friends = mongo.collection('Friends');

       collection_friends.updateOne(
        		{"email" : email}, {
        			$set:{"displayName":name,
        			"address":address,
        			"profession":profession,
        			"interests":interests,
        			"about":about,
        			"profilePic":profilePic,
        			"isPrivate":isPrivate}}, function(err, user){
		            
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

    });*/

}

exports.getFriendList = getFriendList;
exports.sendFriendRequest = sendFriendRequest;