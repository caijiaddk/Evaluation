//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.04.16 at 11:50:14 ���� CST 
//


package com.ics.tcg.web.workflow.client.examples;

import java.io.Serializable;

public class Client_Edge extends Client_Element {

    /**
	 * 
	 */
	private static final long serialVersionUID = -125274997021587340L;
	protected Client_Edge.Source source = new Client_Edge.Source();
    protected Client_Edge.Target target = new Client_Edge.Target();

    public Client_Edge.Source getSource() {
        return source;
    }

    public void setSource(Client_Edge.Source value) {
        this.source = value;
    }

    public Client_Edge.Target getTarget() {
        return target;
    }

    public void setTarget(Client_Edge.Target value) {
        this.target = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

	public static class Source implements Serializable{

        /**
		 * 
		 */
		private static final long serialVersionUID = -6274492962401575620L;
		protected Client_OutputPort outputPort;
        protected Client_ConditionalOutputPort conditionalOutputPort;

        public Client_OutputPort getOutputPort() {
            return outputPort;
        }

        public void setOutputPort(Client_OutputPort value) {
            this.outputPort = value;
        }

        public Client_ConditionalOutputPort getConditionalOutputPort() {
            return conditionalOutputPort;
        }

        public void setConditionalOutputPort(Client_ConditionalOutputPort value) {
            this.conditionalOutputPort = value;
        }

    }

    public static class Target implements Serializable{

        /**
		 * 
		 */
		private static final long serialVersionUID = 4975733390701837086L;
		protected Client_InputPort inputPort;

        public Client_InputPort getInputPort() {
            return inputPort;
        }

        public void setInputPort(Client_InputPort value) {
            this.inputPort = value;
        }

    }

}
