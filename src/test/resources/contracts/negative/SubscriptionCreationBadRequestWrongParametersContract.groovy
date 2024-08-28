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
  name "bad_subscription_creation_request_wrong_param_1"
  description "should return BAD_REQUEST, because 'eventFilter' field is wrong"
  request {
    url "/notification/v1/subscriptions"
    method POST()
    headers {
      contentType(applicationJson())
      accept(applicationJson())
    }
    body( [
      address: "http://my.test.host",
      subscriptionFilter: [
        [
          "eventType" : "ServiceOrderCreateEvent" ,
          "filterCriteria" : "a=1 and b==2" ,
          "fields" : "event.eventType,event.eventId,event.priority"
        ]
      ],
      tenant: "tenant1"
    ])
  }
  response {
    status BAD_REQUEST()
    headers {
      contentType(applicationJson())
    }
    body( [
        errorCode: "ENS-C-02",
        userMessage: "Error creating subscription. The request has wrong parameters: field 'filterCriteria' has invalid RSQL format. filterCriteria=a=1 and b==2",
        developerMessage: "Error creating subscription. The request has wrong parameters: field 'filterCriteria' has invalid RSQL format. filterCriteria=a=1 and b==2",
        errorData: ["field 'filterCriteria' has invalid RSQL format. filterCriteria=a=1 and b==2"]
    ] )
  }
}
