# projector-queues
- tests Redis (rdb, aof, rdb+aof modes) queue
- tests Beanstalkd queue
- all tests do insert 10000, 30000, 50000 key-value pairs in the queue and calculate execution time needed to publish and consume all values

# Run Queues
```shell
docker-compose up -d
```

# Run application script
1. Change [property file](./app/src/main/resources/application.yml): `application.message-count` for each test
2. For Redis different persistence configuration change [property file](./app/src/main/resources/application.yml): `application.redis.port` 
for each test (6379 - RDB, 6378 - AOF, 6377 - AOF+RDB)
3. For redis test uncomment [function](./app/src/main/kotlin/com/jdum/projector/queues/AppApplication.kt) - testRedisQueues
4. For beanstalk test uncomment [function](./app/src/main/kotlin/com/jdum/projector/queues/AppApplication.kt) - testBeanstalkdQueue
5. Run
```shell
./gradlew clean bootRun
```

# Test Results

## RDB
Publish 10000 messages time elapsed: **7.520405S** seconds

Publish 30000 messages time elapsed: **22.409409S** seconds

Publish 50000 messages time elapsed: **37.055697S** seconds

## AOF
Publish 10000 messages time elapsed: **7.497575S** 

Publish 30000 messages time elapsed: **22.602579S** seconds

Publish 50000 messages time elapsed: **36.966387S** seconds

## AOF-RDB
Publish 10000 messages time elapsed: **7.387266S** seconds

Publish 30000 messages time elapsed: **22.119329S** seconds

Publish 50000 messages time elapsed: **38.010681S** seconds

## BEANSTALKD
Publish 10000 messages time elapsed: **6.38759S** seconds

Publish 30000 messages time elapsed: **18.574115S** seconds

Publish 50000 messages time elapsed: **32.506086S** seconds
