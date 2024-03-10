package com.gvt.af.camel.file;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

/**
 * The Purpose of this Class is to create a file poller and read from a file as soon as the file comes
 */
public class BasicFilePoller extends Main
{
    public static void main(String[] args) throws Exception
    {
        BasicFilePoller basicFilePoller = new BasicFilePoller();
        basicFilePoller.enableTrace();
        basicFilePoller.configure().addRoutesBuilder(basicRouteBuilder());
        basicFilePoller.run(args);
    }

    public static RouteBuilder basicRouteBuilder()
    {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception
            {
                from("file://data/fileInput?preMove=staging&move=.completed")
                        .split(body().tokenize("\n"))
                        .streaming()
                        .process(new Processor() {
                            public void process(Exchange msg)
                            {
                                String line = msg.getIn().getBody(String.class);
                                System.out.println("Processing line: " + line);
                            }
                        });
            }
        };

    }
}
