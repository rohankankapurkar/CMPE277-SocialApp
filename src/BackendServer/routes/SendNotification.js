var FCM = require('fcm-node')
    
    var serverKey = 'AAAASGhV-Zs:APA91bFchDv7REa_YY_XwVRp3wl3Jqmh6WqknNvyB3V2g1TA6-3jgPQSf7dKYJBQo-9ug7ZSw9qfgEzD2TT4mtgzf-vKE2io2X_bgQZ1jm9lDLnbKBD4pz-i2GN9a0Axgp3DeReLPlZ4' //put the generated private key path here     
    
    var fcm = new FCM(serverKey)
    
  var sendMessage =  function(message){
	  fcm.send(message, function(err, response){
        if (err) {
            console.log("Something has gone wrong!")
        } else {
            console.log("Successfully sent with response: ", response)
        }
    })
  }
	
module.exports = {sendMessage : sendMessage};