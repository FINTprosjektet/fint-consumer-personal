# FINT Consumer Personal

[![Build Status](https://travis-ci.org/FINTprosjektet/fint-consumer-personal.svg?branch=master)](https://travis-ci.org/FINTprosjektet/fint-consumer-personal) 
[![Coverage Status](https://coveralls.io/repos/github/FINTprosjektet/fint-consumer-personal/badge.svg?branch=master)](https://coveralls.io/github/FINTprosjektet/fint-consumer-personal?branch=master)


## Run consumer locally

```bash
npm start --prefix test-provider
```

* Open browser and hit `http://localhost:8080/swagger-ui.html`
* Check to see if you get any results from the endpoints


## Log payload

To log the event payload add the configuration to `application.properties` or as an environment variable.

```
logging.level.no.fint.consumer.service.SubscriberService: DEBUG
```