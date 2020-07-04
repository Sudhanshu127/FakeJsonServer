## About

A quick to start fake server for speedy development.  
  
Implementations to be done:-  
1. ~~Run multiple services~~
2. ~~Forwarding requests~~
3. ~~Handle GET POST~~ PUT DELETE
4. ~~Integration with slackbot~~
5. Json schema management
8. ~~Connect with Redis for persistence~~ and caching
6. User configuration to send updated json schema
7. Optimize the storage using a good hash function

### Requirements

export SLACK_BOT_TOKEN  
export SLACK_SIGNING_SECRET  

### Using
TL;DR
For host configurations check the dockerfile(Isn't working)
```
git clone https://github.com/Sudhanshu127/FakeJsonServer.git
```
Add the server config and responses in main/java/test/InitialScript.java
```
gradle :runInitialScript
```

### Create a mockServer

```
FakeJsonServer servers = FakeJsonServer.getInstance(nthreads);
```
### New Server

```
MyHttpServer server = servers.getServer(hostname,port);
Response response = new Response().setGetResponse(<String>).setPostResponse(<String>);
server.newResponse(<String>url, response);
```