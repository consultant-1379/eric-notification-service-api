/*******************************************************************************
 * COPYRIGHT Ericsson 2021
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

package com.ericsson.oss.common.service.ns.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Structure of the notification event consumed from the message bus.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class NsEvent {
  String eventID; // Unique Identifier of the event
  String eventType; // Type of the event
  Timestamp eventTime; // Occurrence Time of the event
  String tenant; // The tenant of the event
  String payLoad; // Event body
  String descriptor; // Optional description of the event
  String additionalInformation; // Optional - A serialized JSON String, additional information of the event
}
