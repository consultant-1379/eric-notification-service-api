/*******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
 *
 *
 * The copyright to the computer program(s) herein is the property of
 *
 * Ericsson Inc. The programs may be used and/or copied only with written
 *
 * permission from Ericsson Inc. or in accordance with the terms and
 *
 * conditions stipulated in the agreement/contract under which the
 *
 * program(s) have been supplied.
 ******************************************************************************/

package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  name "correct_all_subscriptions_get_1"
  description "should return OK with all entities in DB"
  request {
    urlPath('/notification/v1/subscriptions')
    method GET()
  }
  response {
    status OK()
    headers {
      contentType(applicationJson())
    }
    body( [[
      id: anyUuid(),
      address: "http://my.test.host",
      subscriptionFilter: [
        [
          "eventType" : "ServiceOrderCreateEvent" ,
          "filterCriteria" : "event.priority=gt=1;event.priority=lt=10;(tenant==tenant1,tenant==tenant2)",
          "fields" : "event.eventType,event.eventId,event.priority"
        ],
        [
          "eventType" : "ServiceOrderDeleteEvent" ,
          "filterCriteria" : "event.priority=gt=10;event.priority=lt=20;(tenant==tenant1,tenant==tenant3)",
          "fields" : "event.eventType,event.eventId"
        ]
      ],
      tenant: "tenant1"
    ], [
      id: anyUuid(),
      address: "http://conflict.test.host",
      subscriptionFilter: [
        [
          "eventType" : "ServiceOrderCreateEvent"
        ]
      ]
    ]])
  }
}


