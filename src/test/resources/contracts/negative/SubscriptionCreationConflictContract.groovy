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
  name "bad_subscription_creation_request_id_conflict_1"
  description "should return CONFLICT with requested entity with generated 'id' in body"
  request {
    url "/notification/v1/subscriptions"
    method POST()
    headers {
      contentType(applicationJson())
      accept(applicationJson())
    }
    body( [
      address: "http://conflict.test.host",
      subscriptionFilter: [
        [
          "eventType" : "ServiceOrderCreateEvent"
        ]
      ]
    ])
  }
  response {
    status CONFLICT()
    headers {
      contentType(applicationJson())
    }
    body( [
        errorCode: "ENS-K-03",
        userMessage: "Error creating subscription. A subscription with the same properties already exists. Subscription id: '14bbad9a-4d6c-4920-8c11-72e47532cbc9'",
        developerMessage: "Error creating subscription. A subscription with the same properties already exists. Subscription id: '14bbad9a-4d6c-4920-8c11-72e47532cbc9'",
        errorData: ["14bbad9a-4d6c-4920-8c11-72e47532cbc9"]
    ])
  }
}
