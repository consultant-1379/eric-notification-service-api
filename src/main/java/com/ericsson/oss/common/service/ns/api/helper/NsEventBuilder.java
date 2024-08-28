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

package com.ericsson.oss.common.service.ns.api.helper;

import com.ericsson.oss.common.service.ns.api.model.NsEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class NsEventBuilder {
  
  private static final String SIMPLE_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

  /**
   * Private constructor to avoid allocating this static class.
   */
  private NsEventBuilder() {
  }
  
  /**
   * Prepare an event (NsEvent) to be delivered to Notification Service via message bus (not including the optional descriptor)
   * @param eventType The event type
   * @param tenant The event tenant
   * @param payload The payload
   * @return The NsEvent filled with the fields expected by Notification Service
   */
  public static NsEvent build(String eventType, String tenant, Object payload) {
    NsEvent event = new NsEvent();
    event.setEventID(UUID.randomUUID().toString());
    event.setEventTime(getCurrentTimestamp());
    event.setEventType(eventType);
    event.setPayLoad(serialize(payload));
    event.setTenant(tenant);
    return event;
  }
  
  /**
   * Prepare an event (NsEvent) to be delivered to Notification Service via message bus (including the optional descriptor)
   * @param eventType The event type
   * @param tenant The event tenant
   * @param payload The payload
   * @param descriptor The optional descriptor
   * @return The NsEvent filled with the fields expected by Notification Service
   */
  public static NsEvent build(String eventType, String tenant, Object payload, String descriptor) {
    NsEvent event = new NsEvent();
    event.setEventID(UUID.randomUUID().toString());
    event.setEventTime(getCurrentTimestamp());
    event.setEventType(eventType);
    event.setPayLoad(serialize(payload));
    event.setTenant(tenant);
    event.setDescriptor(descriptor);
    return event;
  }

  /**
   * Prepare an event (NsEvent) to be delivered to Notification Service via message bus (including the optional
   * additionalInformation)
   * @param eventType The event type
   * @param tenant The event tenant
   * @param payload The payload
   * @param additionalInformation A serialized JSON String, additional information of the event
   * @return The NsEvent filled with the fields expected by Notification Service
   */
  public static NsEvent build(String eventType, String tenant, Object payload, Object additionalInformation) {
    NsEvent event = new NsEvent();
    event.setEventID(UUID.randomUUID().toString());
    event.setEventTime(getCurrentTimestamp());
    event.setEventType(eventType);
    event.setPayLoad(serialize(payload));
    event.setTenant(tenant);
    event.setAdditionalInformation(serialize(additionalInformation));
    return event;
  }

  /**
   * Prepare an event (NsEvent) to be delivered to Notification Service via message bus (including the optional descriptor
   * and additionalInformation)
   * @param eventType The event type
   * @param tenant The event tenant
   * @param payload The payload
   * @param descriptor The optional descriptor
   * @param additionalInformation A serialized JSON String, additional information of the event
   * @return The NsEvent filled with the fields expected by Notification Service
   */
  public static NsEvent build(String eventType, String tenant, Object payload, String descriptor, Object additionalInformation) {
    NsEvent event = new NsEvent();
    event.setEventID(UUID.randomUUID().toString());
    event.setEventTime(getCurrentTimestamp());
    event.setEventType(eventType);
    event.setPayLoad(serialize(payload));
    event.setTenant(tenant);
    event.setDescriptor(descriptor);
    event.setAdditionalInformation(serialize(additionalInformation));
    return event;
  }

  /**
   * Create a time stamp with the current date and time.
   *
   * @return The "now" time stamp
   */
  private static Timestamp getCurrentTimestamp() {
    TimeZone tz = TimeZone.getTimeZone("GMT");
    SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_PATTERN);
    sdf.setTimeZone(tz);
    final String date = sdf.format(new Timestamp(System.currentTimeMillis()));
    return getStrToTimestamp(date);
  }

  /**
   * Convert a string to a time stamp.
   *
   * @param date The string with the date/time to be converted in format YYYY-MM-DD HH:MM:SS
   * @return The relevant timestamp, or null in case or wrong string
   */
  private static Timestamp getStrToTimestamp(final String date) {
    try {
      final SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_PATTERN);
      final Date parsedDate = dateFormat.parse(date);
      return new Timestamp(parsedDate.getTime());
    } catch (final ParseException e) {
      return null;
    }
  }
  
  /**
   * Serialize in JSON a generic object.
   *
   * @param obj The object to be serialized
   * @return The string with the JSON (or null in case of failure)
   */
  public static String serialize(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (final IOException | NullPointerException e) {
      return null;
    }
  }
  
}
