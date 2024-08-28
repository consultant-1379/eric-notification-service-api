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
  name "correct_subscription_deletion_1"
  description "should return NO_CONTENT for a request to delete an entity with random id'"
  request {
    url $(regex("/notification/v1/subscriptions/" + uuid()))
    method DELETE()
  }

  response {
    status NO_CONTENT()
  }
}
