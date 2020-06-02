## About

A quick to start fake server for speedy development.  
  
Implementations to be done:-  
1. ~~Run multiple services~~
2. ~~Forwarding requests~~
3. ~~Handle GET POST~~ PUT DELETE
4. ~~Integration with slackbot~~
5. Json schema management
8. Connect with Redis for persistence and caching
6. User configuration to send updated json schema
7. Optimize the storage using a good hash function

### Requirements

export SLACK_BOT_TOKEN  
export SLACK_SIGNING_SECRET  

### Create a mockServer

```
FakeJsonServer.getInstance()
```