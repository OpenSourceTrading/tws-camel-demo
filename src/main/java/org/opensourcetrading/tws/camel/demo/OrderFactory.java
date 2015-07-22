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

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.interactivebrokers.InteractiveBrokersComponent;
import org.apache.camel.component.interactivebrokers.InteractiveBrokersConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.Contract;
import com.ib.client.Order;

public class OrderFactory implements Processor {
	
	private static final Logger LOG = LoggerFactory.getLogger(OrderFactory.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		
		List<String> orderLine = in.getBody(List.class);
		
		if(orderLine.size() < 8) {
			throw new RuntimeException("Order line must have 8 columns:" +
					"symbol, currency, exchange, security type, " +
					"buy/sell, quantity, MKT/LMT, limit price");
		}
		
		Contract contract = new Contract();
        contract.symbol(orderLine.get(0));
        contract.currency(orderLine.get(1));
        contract.exchange(orderLine.get(2));
        contract.secType(orderLine.get(3));
        
        in.setHeader(InteractiveBrokersConstants.CONTRACT, contract);
        
        Order order = new Order();
        order.action(orderLine.get(4));  // BUY or SELL
        order.totalQuantity(Integer.parseInt(orderLine.get(5)));
        order.orderType(orderLine.get(6));  // MKT or LMT
        order.lmtPrice(Double.parseDouble(orderLine.get(7)));
        
        LOG.info("CSV line converted to {} order for symbol {}", order.action(), contract.symbol());
        
        in.setBody(order);
	}

}
