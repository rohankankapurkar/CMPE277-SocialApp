/**
 * Created by rohankankapurkar on 5/8/17.
 */


var mongo = require("./mongoConnection");
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"




function sendPosts(req ,res){

    console.log("Hey man i am inside sending tweets");
    console.log(req.body.email);
    console.log(req.body.message);

    var email = req.body.email;
    var message = req.body.message;

    mongo.connect(mongoURL, function(err, db){
        var coll = mongo.collection('Facebook');
        //var val = Math.floor(1000 + Math.random() * 9000);


        var d = new Date();

        console.log("printint email"+email);
        coll.update({"email" : email}, { $push: { "myTweets":{"message":message,"time":d } } },function(err, user){
            if (user) {
                console.log("Sent tweet to mongo successfully");
                res.json({msg:"Done"});
            } else {
                //send mail if tries to register
                console.log("succesfully sent mail");



            }
        });

        coll.findOne({"email" : email},function(err, user){
        if (user) {
            console.log("Found the f**in user kacs");
            // console.log(JSON.stringify(user.friends));


/*
          user.friends.forEach(function (friend){

              console.log(friend.friend_email);
              coll.update({"email" : friend.friend_email}, { $push: { "myHomeTweets":message  } } ,function(err, user){
                  if (user) {
                      console.log("Home tweet added in  "+friend.friend_email);
                      res.json({msg:"Done"});
                  } else {
                      //send mail if tries to register
                      //console.log("Error occurred while sending tweet");
                      console.log("Error message for  "+friend.friend_email);
                  }
              });
          })
*/

            var myFriends = [];
            function findUsers(){
                console.log("*****Last here****");
            }

            function sendMail(){
                console.log("*******First******Making the array here");
                console.log(JSON.stringify(user.friends));
                user.friends.forEach(function(friend){
                    myFriends.push(friend.friend_email);
                })

            }
            function sendMail2(){
                console.log("******Second callback*****");
                console.log("Printint all my friends"+myFriends);
                var coll = mongo.collection('Facebook');

                coll.updateMany({"email" : {$in : myFriends}}, { $push: { "myHomeTweets":{"message":message,"time":d } } }, {upsert:true},function(err, user){
                    if (user) {
                        console.log("Sent tweet to mongo successfully");
                        console.log("******Wasnt that sexy piece of code******");
                    } else {
                        //send mail if tries to register
                        console.log("Error while sending");
                        console.log(err);



                    }
                });





            }

            findUsers(sendMail(),sendMail2());




        } else {
            //send mail if tries to register.
            console.log("succesfully sent mail");



        }
});



    });
}


//
// function update( index,  msg,  friends){
//     var coll = mongo.collection('Facebook');
//     if(index>=friends.size){
//         return ;
//     }
//     console.log(index+'------'+friends[0].friend_email);
//     coll.update({"email" : friends[index].friend_email}, { $push: { "myHomeTweets":msg  } } ,function(err, user){
//
//         if (user) {
//             console.log("Home tweet added in  "+user.friend_email);
//             res.json({msg:"Done"});
//         } else {
//             //send mail if tries to register
//
//             console.log(err);
//         }
//         return;
//         update(index+1, msg, friends);
//     });
// }




exports.sendPosts = sendPosts;