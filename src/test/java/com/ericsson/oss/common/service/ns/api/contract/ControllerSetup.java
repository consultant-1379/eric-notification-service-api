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

package com.ericsson.oss.common.service.ns.api.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

@ExtendWith(SpringExtension.class)
abstract class ControllerSetup {

    @MockBean
    Validator validator;

  void standaloneSetup(Object... controllers) {
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    mappingJackson2HttpMessageConverter.setObjectMapper(
        new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule()));

    // https://github.com/spring-cloud/spring-cloud-contract/issues/1428
    EncoderConfig encoderConfig =
        new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false);

    RestAssuredMockMvc.config =
        new RestAssuredMockMvcConfig().encoderConfig(encoderConfig);

    RestAssuredMockMvc.standaloneSetup(
        MockMvcBuilders
            .standaloneSetup(controllers)
            .setMessageConverters(mappingJackson2HttpMessageConverter)
            //  Use dummy validator in order mock BadRequest case
            .setValidator(validator));
  }
}
