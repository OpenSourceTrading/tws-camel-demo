
Setting up TWS to work with this demo
-------------------------------------

1. Start TWS using Java WebStart (javaws)

     Demo account:

       javaws https://www.interactivebrokers.com/java/classes/latest.edemo.jnlp

     Real account:

       javaws https://www.interactivebrokers.com/java/classes/latest.jnlp

2. In TWS, go to File -> Global Configuration... -> API -> Settings
   and change the following:

     Enable ActiveX and Socket Clients         TICK
     Read-Only API                             REMOVE TICK
     Logging Level                             Detail
     Allow connections from localhost only     REMOVE TICK if you want to


Using the demo from the command line
------------------------------------

1. Make sure you have Maven 3.x and JDK 1.7+ in your $PATH

2. Clone the project from Git

     git clone https://github.com/OpenSourceTrading/tws-camel-demo

3. Build it

     cd tws-camel-demo
     ./build.sh

4. Go into the appassembler directory, configure it and run it

     cd target/appassembler
     vi config/tws-camel-demo.properties
     bin/tws-camel-demo

5. You can try putting a CSV file in the orders directory (see the
   properties file) to see how the orders are processed.

6. If you adapt the application and want to run it in a production
   environment, you simply rsync the contents of target/appassembler
   to your release area.

Using the demo from Eclipse
---------------------------

1. Install Eclipse

2. Install the m2e plugin in Eclipse

3. From the command line, clone the project from Git

     git clone https://github.com/OpenSourceTrading/tws-camel-demo

4. In Eclipse, right click in the Project Explorer panel on the left hand
   side of the window and click "Import..." -> Maven -> Existing Maven Projects

5. Select the directory where you cloned the Git repository and import
   the project.

6. Edit the file src/main/config/tws-camel-demo.properties

7. Create a Run Configuration:

      Main -> Main class:   org.apache.camel.spring.Main

      Arguments -> VM arguments:

        -Dapp.home=${project_loc:tws-api-demo}/src/main -Dlog4j.configurationFile=file://${project_loc:tws-api-demo}/src/main/config/log4j2.xml

8. Now you can run the application.

Submitting orders via CSV files
-------------------------------

One of the sample Camel routes reads a CSV file and uses each
line to create an order to buy or sell the specified instrument.

The columns in the CSV file are:

  symbol
  currency
  exchange
  security type
  buy/sell
  quantity
  MKT/LMT
  limit price

The following 2 lines will buy 20 shares in IBM at market price
and then sell 10 of them at market price.  The limit price is ignored
when market price (MKT) is specified.

IBM,USD,SMART,STK,BUY,20,MKT,0
IBM,USD,SMART,STK,SELL,10,MKT,0

Next steps
----------

See the list of Camel components to find out all the other systems
that you can integrate with using the Camel route syntax:

  http://camel.apache.org/components.html

For example, you could send market data on a JMS topic or receive
orders in JSON format from a JMS queue using just a few lines of code.
Camel makes it easy.

