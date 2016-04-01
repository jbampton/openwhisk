/*
 * Copyright 2015-2016 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package system.rest

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import com.jayway.restassured.RestAssured
import com.jayway.restassured.config.RestAssuredConfig
import com.jayway.restassured.config.SSLConfig
import common.WhiskProperties
import org.junit.Ignore

/**
 * Basic tests of Swagger support
 */
@RunWith(classOf[JUnitRunner])
class SwaggerTests extends FlatSpec with Matchers with RestUtil {

    "Whisk API service" should "respond to /docs with Swagger UI" in {
        val response = RestAssured.
            get(getServiceURL() + "/api/v1/docs/index.html")

        response.statusCode() should be(200)
        response.body().asString().contains("<title>Swagger UI</title>") should be(true)
    }

    it should "respond to /api-docs with Swagger XML" in {
        val response = RestAssured.
            get(getServiceURL() + "/api/v1/api-docs")

        response.statusCode() should be(200)
        response.body().asString().contains("\"swagger\":") should be(true)
    }

    it should "respond to invalid URI with status code 404" in {
        val auth = WhiskProperties.getBasicAuth
        val response = RestAssured.
            given().
            auth().basic(auth.fst, auth.snd).
            get(getServiceURL() + "/api/v1/docs/dummy")
        response.statusCode() should be(404)
    }
}
