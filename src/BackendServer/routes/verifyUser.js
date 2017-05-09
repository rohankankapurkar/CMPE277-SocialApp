/**
 * Created by rohankankapurkar on 5/8/17.
 */


var mongo = require("./mongoConnection");
//var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017/CMPE277";
//var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/<DATABASE>?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"




function verify(req ,res){
    //msg = {username:"rohan", password:"rohan"}
    //console.log(req.body.confirm_id);

    var confirm_id = req.body.confirm_id;
    var email = req.body.email;
    mongo.connect(mongoURL, function(err, db){
        var coll = mongo.collection('Facebook');
        console.log("before in monig");
        console.log("confirm id"+confirm_id);
        console.log("email"+email);
        var x =parseInt(confirm_id);

        // coll.find({$and:[{"email" : email}, {"verification":confirm_id}]} , function(err, user){
        //     console.log(user.email+"user printing ");
        //     if (user) {
        //         console.log("found the user in mongo for verification"+confirm_id+ "   "+email);
        //
        //         res.json({msg: email});
        //
        //     } else {
        //         console.log("Thre is no user for this combination");
        //
        //         res.json({msg: "failed"});
        //     }
        // });
        // Find some documents
        // coll.find().toArray(function(err, user) {
        //     //assert.equal(err, null);
        //    // assert.equal(2, docs.length);
        //     console.log("Found the following records"+user);
        //     console.dir(user[0]);
        //     res.json({"msg":"succes"});
        // });

         var w = coll.find({$and: [{'verification':x},{'email' : email}] } ).toArray(function(err, items) {

            if(items.length>0) {
                var json_response = {
                    "results": items, "statusCode": 200
                };
                //console.log(items[0].email);
                console.log(items.toString());



                res.json(json_response);
            }
            else{
                var json_response_1 = {
                    "results" : items, "statusCode" : 400
                };
                res.json(json_response_1);
            }


        });











    });
    // res.send("");
}

exports.verify = verify;