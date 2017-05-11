/**
 * Created by rohankankapurkar on 5/3/17.
 */


var sendmail = require('sendmail');
var smtpTransport = require('nodemailer-smtp-transport');


var mongo = require("./mongoConnection");
var mongoURL = "mongodb://rohan:rohan@cluster0-shard-00-00-v6jmi.mongodb.net:27017,cluster0-shard-00-01-v6jmi.mongodb.net:27017,cluster0-shard-00-02-v6jmi.mongodb.net:27017/CMPE277?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"

var nodemailer = require('nodemailer');
var xoauth2 = require('xoauth2');



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
        var val = Math.floor(1000 + Math.random() * 9000);
        console.log(val);

        coll.insert({email : email, "password": password, "firstname" : firstname, "lastname" : lastname, "password_confirm": password_confirm, "verification" : val, "isPrivate" : "false"}, function(err, user){
            if (user) {
                //logic to send mail here
                var mailer = require("nodemailer");

// Use Smtp Protocol to send Email
                var smtpTransport = nodemailer.createTransport({
                    service: "gmail",
                    host: "smtp.gmail.com",
                    auth: {
                        user: "cmpe277slash@gmail.com",
                        pass: "rohankankapurkar"
                    }
                });


                var mailOptions={
                    to : email,
                    subject : 'Here is your authentication code from CMPE 277 project',
                    text : val+"  Enter this code in the application "
                }
                console.log(mailOptions);
                smtpTransport.sendMail(mailOptions, function(error, response){
                    if(error){
                        console.log(error);
                        res.end("error");
                    }else{
                        console.log("Message sent: " + response.message);
                        res.end("sent");
                    }
                });








        console.log("User added successfully");

                res.json({msg: val});
            } else {
                console.log("returned false");
                res.code = 401;
                res.value = "Failed Login";
            }
        });

    });
}
exports.register = register;


