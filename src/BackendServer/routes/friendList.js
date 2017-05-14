/**
 * Created by Nachiket Joshi on 5/11/2017.
 */

var mongo = require("./mongoConnection");
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"

function discoverFriends(req, res) {

	console.log("********IN discoverFriends********");
	var email = req.body.email;
	console.log(email);
	
	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');
		var collection_friends = mongo.collection('Friends');
		
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

function getFriendList(req, res) {

	console.log("********IN getFriendList********");
	var email = req.body.email;
	console.log(email);
	
	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');
		var collection_friends = mongo.collection('Friends');
		
		/*collection_friends.find({ 
			"if_already_friends": "TRUE", 
			"sender_email": email 
			}, {friend_req_sent_to: 1}).toArray(function(err, user) {
	
			if (user) {
				res.json(user);
				console.log("***************", user);

			} else {
				console.log("returned false");
				res.code = 401;

			}
		});*/
		

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

function sendFriendRequest(req, res) {

	console.log("********NACHIKET: Inside sendFriendRequest function********");

	var email = req.body.email;
	var friend_req_sent_to = req.body.friend_req_sent_to;
	console.log("email: " + email);
	console.log("friend_req_sent_to: " + friend_req_sent_to);

	mongo.connect(mongoURL,
			function(err, db) {
			var collection_friends = mongo.collection('Friends');

			collection_friends.insert(
					{
					"sender_email" : email,
					"friend_req_sent_to" : friend_req_sent_to,
					"accepted_flag" : "FALSE",
					"friend_req_sent_flag" : "TRUE",
					"if_already_friends" : "FALSE"
					
					},
					function(err, user) {
						if (user) {
							
						console.log("Friend Request SENT successfully");
						res.json({msg : 200});
						
						} else {
							
							console.log("returned false");
							res.code = 401;
							res.value = "Friend request can not be sent";
							
					}
			});

		});

};

function followFriend(req, res) {

	console.log("********NACHIKET: Inside followFriend function********");

	var email = req.body.email;
	console.log("email: " + email);
	res.send("temp");

}

function getSearchFriendList(req, res) {

	console.log("********NACHIKET: Inside getSearchFriendList function********");

	var email = req.body.email;
	var searchParam = req.body.searchParam;
	console.log("email: " + email);
	console.log("searchParam: " + searchParam);

	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');

		collection_facebook.find({
			"email" : searchParam
		}).toArray(function(err, user) {
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

exports.getFriendList = getFriendList;
exports.getSearchFriendList = getSearchFriendList;
exports.discoverFriends = discoverFriends;
exports.sendFriendRequest = sendFriendRequest;
exports.followFriend = followFriend;