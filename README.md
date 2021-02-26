### Demo how to use messages technologies: Kafka, SNS, SQS, 
#### Follow instruction on to have kafka cluster run on local machine: 
```

https://docs.confluent.io/platform/current/quickstart/ce-docker-quickstart.html?utm_medium=sem&utm_source=google&utm_campaign=ch.sem_br.nonbrand_tp.prs_tgt.kafka_mt.mbm_rgn.namer_lng.eng_dv.all&utm_term=%2Bkafka%20%2Bdocker&creative=&device=c&placement=&gclid=Cj0KCQiA0-6ABhDMARIsAFVdQv8NQ4urxedABJiNW1pfa2foUAh1wIYjPDG_14ClV96QZKqfT1pVZMQaAiPlEALw_wcB

```

#### Kafka Test Producer: 
##### Run with profile: generic,local.
##### Send message:
```
localhost:9001/trigger
POST
{
    "testId": "testId-12345",
    "messagePerPeriod": 2,
    "period": 2,
    "repetition": 5,
    "text": "some message"
}

Visit: http://localhost:9021/
Look for x-test-topic and check the messages.

```
