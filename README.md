# FINT Consumer Personal

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

Generated from tag `v2.6.0-beta-3` on package `personal` and component `administrasjon`.
