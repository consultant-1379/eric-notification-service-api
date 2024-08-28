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
  name "bad_subscription_deletion_request_not_found_1"
  description "should return NOT_FOUND for a request to delete an entity with specific id"
  request {
    url "/notification/v1/subscriptions/123e4567-e89b-12d3-a456-556642440000"
    method DELETE()
  }

  response {
    status NOT_FOUND()
    headers {
      contentType(applicationJson())
    }
    body( [
        errorCode: "ENS-J-09",
        userMessage: "Error deleting subscription. A subscription with the provided id '123e4567-e89b-12d3-a456-556642440000' couldn't be found",
        developerMessage: "Error deleting subscription. A subscription with the provided id '123e4567-e89b-12d3-a456-556642440000' couldn't be found",
        errorData: ["123e4567-e89b-12d3-a456-556642440000"]
    ])
  }
}
