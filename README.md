# FINT Consumer Skeleton

[![Build Status](https://travis-ci.org/FINTprosjektet/fint-consumer-skeleton.svg?branch=master)](https://travis-ci.org/FINTprosjektet/fint-consumer-skeleton)

## Setup

The `SubscriberService` is added to the project to receive events.  
Custom code should be added in this class (or replaced by a new implementation) to alter the logic when new events are received.

## Local test

To run a complete test setup locally use the `./gradlew runAll` task.  
This will start a local rabbitmq, a nodejs test-client that subsribes to and replies back for health check messages, and the fint-consumer Spring boot application.


## Configuration

The configuration used to integration with RabbitMQ is based on the [fint-events](https://github.com/FINTlibs/fint-events#configuration) library.