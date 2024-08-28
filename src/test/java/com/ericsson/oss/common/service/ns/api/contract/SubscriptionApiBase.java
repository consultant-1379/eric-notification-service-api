/** *****************************************************************************
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
 ***************************************************************************** */
package com.ericsson.oss.common.service.ns.api.contract;

import com.ericsson.oss.common.service.ns.api.SubscriptionApi;
import com.ericsson.oss.common.service.ns.api.model.NsSubscriptionFilter;
import com.ericsson.oss.common.service.ns.api.model.NsSubscriptionRequest;
import com.ericsson.oss.common.service.ns.api.model.NsSubscriptionResponse;
import com.ericsson.oss.orchestration.so.common.error.message.factory.builders.ErrorMessageBuilder;
import com.ericsson.oss.orchestration.so.common.error.message.factory.exception.BaseErrorMessageFactoryException;
import com.ericsson.oss.orchestration.so.common.error.message.factory.message.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public abstract class SubscriptionApiBase extends ControllerSetup {

  private final Logger logger = LoggerFactory.getLogger(SubscriptionApiBase.class);

  private ErrorMessageBuilder errorMessageBuilder;
  
  @MockBean
  SubscriptionApiDummyImpl subscriptionController;

  @PostConstruct
  void init() throws BaseErrorMessageFactoryException {
    errorMessageBuilder = new ErrorMessageBuilder();
  }
  
  @BeforeEach
  void setup(TestInfo testInfo) throws BaseErrorMessageFactoryException {
    
    logger.info("Test Name = {}", testInfo.getDisplayName());

    NsSubscriptionFilter filter1 = new NsSubscriptionFilter()
            .eventType("ServiceOrderCreateEvent")
            .filterCriteria("event.priority=gt=1;event.priority=lt=10;(tenant==tenant1,tenant==tenant2)")
            .fields("event.eventType,event.eventId,event.priority");
    NsSubscriptionFilter filter2 = new NsSubscriptionFilter()
            .eventType("ServiceOrderDeleteEvent")
            .filterCriteria("event.priority=gt=10;event.priority=lt=20;(tenant==tenant1,tenant==tenant3)")
            .fields("event.eventType,event.eventId");

    switch (testInfo.getDisplayName()) {
      /*
       * Positive Tests
       */
      case "validate_correct_subscription_creation_1()":
      case "validate_correct_subscription_creation_2()": {
        NsSubscriptionResponse createdSubscription = new NsSubscriptionResponse()
          .id(UUID.randomUUID())
          .address("http://my.test.host")
          .tenant("tenant1")
          .subscriptionFilter(Arrays.asList(filter1, filter2));

        given(subscriptionController.createSubscription(any(NsSubscriptionRequest.class), any()))
          .willReturn(new ResponseEntity<>(createdSubscription, HttpStatus.CREATED));
        break;
      }

      case "validate_correct_single_subscription_get_1()": {
        NsSubscriptionResponse subscriptionById = new NsSubscriptionResponse()
          .id(UUID.fromString("123e4567-e89b-12d3-a456-556642440001"))
          .address("http://my.test.host")
          .tenant("tenant1")
          .subscriptionFilter((Arrays.asList(filter1, filter2)));

        given(subscriptionController.getSubscription(UUID.fromString("123e4567-e89b-12d3-a456-556642440001")))
          .willReturn(new ResponseEntity<>(subscriptionById, HttpStatus.OK));
        break;
      }

      case "validate_correct_all_subscriptions_get_1()": {
        NsSubscriptionResponse subscriptionResp = new NsSubscriptionResponse()
          .id(UUID.randomUUID())
          .address("http://conflict.test.host")
          .subscriptionFilter(Arrays.asList(new NsSubscriptionFilter().eventType("ServiceOrderCreateEvent")));

        NsSubscriptionResponse subscriptionById = new NsSubscriptionResponse()
          .id(UUID.fromString("123e4567-e89b-12d3-a456-556642440001"))
          .address("http://my.test.host")
          .tenant("tenant1")
          .subscriptionFilter((Arrays.asList(filter1, filter2)));

        given(subscriptionController.getSubscriptions())
          .willReturn(new ResponseEntity<>(Arrays.asList(subscriptionById, subscriptionResp), HttpStatus.OK));
        break;
      }
    
      case "validate_correct_subscription_deletion_1()": {
        given(subscriptionController.deleteSubscription(any(UUID.class)))
          .willReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        break;
      }

      /*
       * Negative Tests
       */
      case "validate_bad_subscription_creation_request_missing_param_1()": {
        NsSubscriptionRequest badRequest = new NsSubscriptionRequest()
          .address("http://my.test.host")
          .tenant("tenant1")
          .subscriptionFilter(Arrays.asList(new NsSubscriptionFilter()
          .filterCriteria("event.priority=gt=1;event.priority=lt=10;(tenant==tenant1,tenant==tenant2)")
          .fields("event.eventType,event.eventId,event.priority")));

        List<String> errorData1 = new ArrayList<>();
        errorData1.add("subscriptionFilter[0].eventType");
        ErrorMessage badRequestError = errorMessageBuilder.errorCode("ENS-B-00").errorData(errorData1).build();

        given(subscriptionController.createSubscription(badRequest, null))
          .willReturn(new ResponseEntity(badRequestError, HttpStatus.BAD_REQUEST));
        break;
      }

      case "validate_bad_subscription_creation_request_wrong_param_1()": {
        NsSubscriptionRequest wrongRequest = new NsSubscriptionRequest()
          .address("http://my.test.host")
          .tenant("tenant1")
          .subscriptionFilter(Arrays.asList(new NsSubscriptionFilter()
            .eventType("ServiceOrderCreateEvent")
            .filterCriteria("a=1 and b==2")
            .fields("event.eventType,event.eventId,event.priority")));

        List<String> errorData3 = new ArrayList<>();
        errorData3.add("field 'filterCriteria' has invalid RSQL format. filterCriteria=a=1 and b==2");
        ErrorMessage wrongRequestError = errorMessageBuilder.errorCode("ENS-C-02").errorData(errorData3).build();

        given(subscriptionController.createSubscription(wrongRequest, null))
         .willReturn(new ResponseEntity(wrongRequestError, HttpStatus.BAD_REQUEST));
        break;
      }

      case "validate_bad_subscription_creation_request_id_conflict_1()": {
        NsSubscriptionRequest conflictedRequest = new NsSubscriptionRequest()
          .address("http://conflict.test.host")
          .subscriptionFilter(Arrays.asList(new NsSubscriptionFilter().eventType("ServiceOrderCreateEvent")));
        List<String> errorData2 = new ArrayList<>();
        errorData2.add("14bbad9a-4d6c-4920-8c11-72e47532cbc9");
        ErrorMessage conflictedSubscriptionError = errorMessageBuilder.errorCode("ENS-K-03").errorData(errorData2).build();

        given(subscriptionController.createSubscription(conflictedRequest, null))
        .willReturn(new ResponseEntity(conflictedSubscriptionError, HttpStatus.CONFLICT));
        break;
      }

      case "validate_bad_subscription_deletion_request_not_found_1()": {
        List<String> errorData4 = new ArrayList<>();
        errorData4.add("123e4567-e89b-12d3-a456-556642440000");
        ErrorMessage deleteNotFoundError = errorMessageBuilder.errorCode("ENS-J-09").errorData(errorData4).build();

        given(subscriptionController.deleteSubscription(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")))
          .willReturn(new ResponseEntity(deleteNotFoundError, HttpStatus.NOT_FOUND));
        break;
      }
    
      case "validate_bad_subscription_get_request_not_found_1()": {
        List<String> errorData4 = new ArrayList<>();
        errorData4.add("123e4567-e89b-12d3-a456-556642440000");
        ErrorMessage getNotFoundError = errorMessageBuilder.errorCode("ENS-J-06").errorData(errorData4).build();

        given(subscriptionController.getSubscription(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")))
          .willReturn(new ResponseEntity(getNotFoundError, HttpStatus.NOT_FOUND));
        break;
      }

      case "validate_subscription_creation_blank_api_key()": {
        List<String> errorData = new ArrayList<>();
        errorData.add("X-API-KEY header was set but is blank");
        ErrorMessage blankApiKeyError = errorMessageBuilder.errorCode("ENS-B-02").errorData(errorData).build();

        given(subscriptionController.createSubscription(any(), anyString()))
                .willReturn(new ResponseEntity(blankApiKeyError, HttpStatus.BAD_REQUEST));
        break;
      }
    }
    
    standaloneSetup(subscriptionController);
  }

  /**
   * SubscriptionApi requires an implementation to make possible Controller behavior. In fact, methods of this implementation are
   * never called really, because they are mocked in setup() method above.
   */
  @RestController
  class SubscriptionApiDummyImpl implements SubscriptionApi {

    @Override
    public ResponseEntity<NsSubscriptionResponse> createSubscription(NsSubscriptionRequest nsSubscriptionRequest, String X_API_KEY) {
      return null;
    }

    @Override
    public ResponseEntity<Void> deleteSubscription(UUID id) {
      return null;
    }

    @Override
    public ResponseEntity<NsSubscriptionResponse> getSubscription(UUID id) {
      return null;
    }

    @Override
    public ResponseEntity<List<NsSubscriptionResponse>> getSubscriptions() {
      return null;
    }
  }
}
