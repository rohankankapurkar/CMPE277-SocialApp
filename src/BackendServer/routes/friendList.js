/**
 * Created by Nachiket Joshi on 5/11/2017.
 */

var mongo = require("./mongoConnection");
var sendNotification = require('./SendNotification');
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"

function discoverFriends(req, res) {

	console.log("********IN discoverFriends********");
	var email = req.body.email;
	console.log(email);
	
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

function getFriendList(req, res) {

	console.log("********IN getFriendList********");
	var email = req.body.email;
	console.log(email);
	
	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');
		
/*		collection_facebook.find({}).toArray(function(err, user) {
			if (user) {
				res.json(user);
				console.log("***************", user);

			} else {
				console.log("returned false");
				res.code = 401;

			}
		});*/

		collection_facebook.findOne (
				{'email' : email} 
				,function(err, user) {
				
				if (user) {
					res.json(user);
					console.log(user);
					console.log("NACHIKET: Your Friend List is", user);
	
				} else {
					console.log("returned false");
					res.code = 401;
	
				}
		});

	});

};

function sendFriendRequest(req, res) {

	console.log("********NACHIKET: Inside sendFriendRequest function********");

	var email = req.body.email;
	var friend_req_sent_to = req.body.friend_req_sent_to;
	var friend_token=req.body.friend_token;
	console.log("email: " + email);
	console.log("friend_req_sent_to: " + friend_req_sent_to);
	console.log("push notification to:"+friend_token);

	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');
		//status : 0 = already friends, 1 = sent, 2 = received
		
		//first query decides if you have sent a friend request already
		//A-->B
		//first update receivers document with status 2 (received) with info of A
		console.log("HELLO1");
		collection_facebook.find({
			"email" : email
		}).toArray(function(err, user) {
			if (user) {
				
				//console.log(user);
				//obtain fname and lname of B to enter in document of A
				var fname = user[0].firstname;
				var lname = user[0].lastname;
				
				//console.log("fname: " + fname + "lname: " + lname);
				
				
				//following query is for senders updation
				collection_facebook.update(
					    { "email": friend_req_sent_to},
					    { "$push": 
					        {"friends": 
					            {
								//'sender_email'  : email,
								'friend_email' : email,
								'status'  : 2,
								'fname' : fname,
								'lname' : lname
					            }
					        }
					    }, function(err, user) {
							if (user) {
								console.log(user);
								console.log("SENDING FRIEND REQUEST SUCCESSFULdabba");
							} else {
								console.log("SENDING FRIEND REQUEST FAILUREbadda");
							}
						}
					);
			}

		});

		//console.log("HELLO");
		var fnameA;
		var lnameA;
		
		//A-->B
		//second update senders document with status 1 (sent) with info of B
		collection_facebook.find({
			"email" : friend_req_sent_to
		}).toArray(function(err, user) {
			if (user) {
				
				console.log(user);
				//obtain fname and lname of B to enter in document of A
				fnameA = user[0].firstname;
				lnameA = user[0].lastname;
				
				console.log("fname: " + fnameA + "lname: " + lnameA);
				
				
				//following query is for senders updation
				collection_facebook.update(
					    { "email": email},
					    { "$push": 
					        {"friends": 
					            {
								//'sender_email'  : email,
								'friend_email' : friend_req_sent_to,
								'status'  : 1,
								'fname' : fnameA,
								'lname' : lnameA
					            }
					        }
					    }, function(err, user) {
							if (user) {
								
								/**
						* Sending notification here start
						*/
						var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera) 
							to: friend_token, 							
							notification: {
								title: 'New Friend Request', 
								body: 'You have recieved a new friend request' 
							},
							
							/*data: {  //you can send only notification or only data(or include both) 
								my_key: 'my value',
								my_another_key: 'my another value'
							}*/
						};
						
						//to: 'fssT0H5ouJE:APA91bF3xy7cOFr7dmoLaTqHlbMd2aaKQ8y3RbuJ2mzeAZvAAR1UKZgOUm4I_LmEfcqBXb3sYZ5YjIjDvSdtn_6B3f0fMO7wFAylWURLGpKFXg3tSuN0vE3GxVjpgW2349SV2BDPYDkw'
						
						sendNotification.sendMessage(message);
						//--------------------------------------------
						
								
								console.log("SENDING FRIEND REQUEST SUCCESSFUL");
								res.code = 200;
							} else {
								console.log("SENDING FRIEND REQUEST FAILURE");
								res.code = 401;
							}
						}
					);
			}
		});
	});
	res.send("temp");
};

function followFriend(req, res) {

	console.log("********NACHIKET: Inside followFriend function********");
	
	var email = req.body.email;
	var follow_req_sent_to = req.body.friend_req_sent_to;
	console.log("email: " + email);
	console.log("friend_req_sent_to: " + follow_req_sent_to);
	
	//following query will add user in follow field of the FACEBOOK collection
	
	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');
		collection_facebook.update(
			    { "email": email},
			    { "$push": 
			        {"follow": 
			            {
						//'sender_email'  : email,
						'friend_email' : follow_req_sent_to,
						'status'  : 0,
						'fname' : "",
						'lname' : ""
			            }
			        }
			    }, function(err, user) {
					if (user) {
						console.log("FOLLOW SUCCESSFUL");
						res.code = 200;
					} else {
						console.log("FOLLOW FAILURE");
						res.code = 401;
					}
				}
			);
		}
	);
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


function acceptFriendRequest(req, res) {

	console.log("********NACHIKET: Inside acceptFriendRequest function********");

	var emailTo = req.body.emailTo;
	var emailFrom = req.body.emailFrom;
	console.log("emailTO: " + emailTo + "emailFrom: " + emailFrom);

	
	
	mongo.connect(mongoURL, function(err, db) {
		var collection_facebook = mongo.collection('Facebook');
		//status : 0 = already friends, 1 = sent, 2 = received
		
		//first update senders database with status 0
		console.log("emailTO: " + emailTo + "emailFrom: " + emailFrom);
		//following query is for updating the status variable in senders document
		collection_facebook.update({
			
			"email": emailTo ,     
		    "friends" :{$elemMatch : {"friend_email" : emailFrom}}},
		     {"$set":{"friends.$.status" : 0}
			/*
			$and : [ 
				{ "email" : email }, 
				{"friends.friend_email": friend_req_sent_to} 
				]
		}, {
			$set : {
				"friends.status": 0
			}*/
		},

		function(err, user) {
			if (user) {
				console.log("FRIEND REQUEST ACCEPT DONE IN RECEIVERS DOCUMENT");

			} else {
				console.log("FRIEND REQUEST ACCEPT DONE IN RECEIVERS DOCUMENT");
			}
		});
		
		console.log("STEP 1 IS DONE");
		console.log("emailTO: " + emailTo + "emailFrom: " + emailFrom);
		
		//following query is for updating the status variable in receivers document
		collection_facebook.update({
			
			"email": emailFrom ,     
		    "friends" :{"$elemMatch" : {"friend_email" : emailTo}}},
		     {"$set":{"friends.$.status" : 0}
		    
		     /*
			$and : [ 
				{ "email" : friend_req_sent_to }, 
				{"friends.friend_email": email} 
				]
		}, {
			$set : {
				"friends.status": 0
			}*/
		},

		function(err, user) {
			if (user) {
				console.log("FRIEND REQUEST ACCEPT DONE IN SENDERS DOCUMENT");
				res.code = 200;
			} else {
				console.log("FRIEND REQUEST ACCEPT DONE IN SENDERS DOCUMENT");
				res.code = 401;
			}
		});
		
	});
	
	res.send("temp");

}


exports.acceptFriendRequest = acceptFriendRequest;
exports.getFriendList = getFriendList;
exports.getSearchFriendList = getSearchFriendList;
exports.discoverFriends = discoverFriends;
exports.sendFriendRequest = sendFriendRequest;
exports.followFriend = followFriend;