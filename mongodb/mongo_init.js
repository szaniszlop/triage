conn = Mongo("mongodb://root:example@localhost:27017/admin")
db = conn.getDB("admin") 

var userCursor = db.system.users.find({user: "testUser"})
var createUser = true

if (userCursor.hasNext()){
  createUser = false
}
if( createUser ){
  db.createUser(
    {
      user: "testUser",
      pwd: "password",
      roles: [
         { role: "readWrite", db: "testapi" }
      ]
    }
  )
}

db = conn.getDB("testapi") 
var result = db.tenants.deleteMany({});

db.tenants.insertMany([
    {
       _id: "1",
       name: "UBS Switzerland AG",
       businessUnits: [ 
        {id: "1_UBS_CH", name: "UBS Switzerland AG"},
        {id: "1_UBS_CH_RCMNoEmp", name: "Retail Banking no employee Data"},
        {id: "1_UBS_CH_RCMEmp", name: "Retail Banking with employee Data"},
       ]
     },
     {
      _id:"5",
      name: "UBS AG",
      businessUnits: [ 
       {id: "1_UBS_AG", name: "UBS AG"},
       {id: "1_UBS_AG_BRB", name: "UBS AG Bank to Bank"},
       {id: "1_UBS_AG_RCP", name: "UBS AG retail"},
      ]
     }
 ])
 