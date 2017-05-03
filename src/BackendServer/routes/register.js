/**
 * Created by rohankankapurkar on 5/3/17.
 */



var mongo = require("./mongoConnection");
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"



function register(req ,res){

    console.log("inside register user");
    console.log(req.body.email);
    console.log(req.body.password);
    console.log(req.body.firstname);



    var email = req.body.email;
    var password = req.body.password;
    var firstname = req.body.firstname;
    var lastname = req.body.lastname;
    var password_confirm = req.body.password_confirm;

    mongo.connect(mongoURL, function(err, db){
        var coll = mongo.collection('Facebook');

        coll.insert({"email" : email, "password": password, "firstname" : firstname, "lastname" : lastname, "password_confirm": password_confirm}, function(err, user){
            if (user) {
                console.log("User added successfully");
                res.json({msg: email});
            } else {
                console.log("returned false");
                res.code = 401;
                res.value = "Failed Login";
            }
        });
    });
}
exports.register = register;


