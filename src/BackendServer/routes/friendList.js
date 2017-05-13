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
    res.send("temp");

}

function followFriend(req ,res){

    console.log("********NACHIKET: Inside followFriend function********");

    var email = req.body.email;
    console.log("email: "+ email);
    res.send("temp");

}

function getSearchFriendList(req ,res){

    console.log("********NACHIKET: Inside getSearchFriendList function********");

    var email = req.body.email;
    var searchParam = req.body.searchParam;
    console.log("email: "+ email);
    console.log("searchParam: "+ searchParam);
    
	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');
		
		collection_facebook.find({"email":  searchParam }).toArray(function(err, user) {
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

exports.getSearchFriendList = getSearchFriendList;
exports.getFriendList = getFriendList;
exports.sendFriendRequest = sendFriendRequest;
exports.followFriend = followFriend;