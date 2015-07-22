/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensourcetrading.tws.camel.demo;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteBuilder extends SpringRouteBuilder {

    private Logger logger = LoggerFactory.getLogger(RouteBuilder.class);

    @Override
    public void configure() throws Exception {

        /**
         * Log some information about the configuration.
         */
        logger.info("Route init.");

        /**
         * Create some strings to re-use in the routes.
         */

        String interactiveBrokersCommon =
                "interactivebrokers:{{tws.host}}:{{tws.port}}?clientId={{tws.clientId}}";
        String marketData = interactiveBrokersCommon +
                "&consumerType=MARKET_DATA_TOP";
        String log = "log:org.opensourcetrading.tws.camel.demo?level=INFO&showHeaders=true";


        /**
         * These Camel routes handle messages with market data ticks
         */
        from(marketData +
                "&symbol=IBKR&currency=USD&exchangeName=ISLAND&securityType=STK")
            .to("seda:market.data");

        from(marketData +
                "&symbol=IBM&currency=USD&exchangeName=SMART&securityType=STK")
            .to("seda:market.data");

        from(marketData +
                "&symbol=GSK&currency=GBP&exchangeName=LSE&securityType=STK")
            .to("seda:market.data");

        from(marketData +
                "&symbol=EUR&currency=USD&exchangeName=IDEALPRO&securityType=CASH")
            .to("seda:market.data");

        // XAU - Gold bullion - check trading permissions
        /* from(marketData +
                "&symbol=XAUUSD&currency=USD&exchangeName=SMART&securityType=CMDTY")
            .to("seda:market.data"); */

        // XAG - Silver bullion - check trading permissions
        /* from(marketData +
                "&symbol=XAGUSD&currency=USD&exchangeName=SMART&securityType=CMDTY")
            .to("seda:market.data"); */

        // All of the market data routes send the ticks to this route which just
        // logs them
        from("seda:market.data")
            .to(log);

        /**
         * These Camel routes handle trade reports.
         * They just log them to the Java logger
         * using the Camel log component.
         */

        from(interactiveBrokersCommon + "&consumerType=TRADE_REPORTS")
            .to("seda:trade.reports");

        from("seda:trade.reports")
            .to(log);

        /**
         * This Camel route picks up CSV files from a directory, each line
         * of each file is an order.
         * Sends the orders to Interactive Brokers.
         */
        from("file://{{order.directory}}")
            .unmarshal().csv()  // Converts body to a List of Lists
            .split(body())   // Each List to a separate Exchange
            .process(new OrderFactory())  // Build Order object
            .to(interactiveBrokersCommon + "&producerType=ORDERS");
    }

}

