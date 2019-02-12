package master.naucnacentrala.delegate;


import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * This is an empty service implementation illustrating how to use a plain Java
 * class as a BPMN 2.0 Service Task delegate.
 */
public class LoggerDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("USAO U LOGGER");
        LOGGER.info("\n\n Proces: " + execution.getProcessDefinitionId() +" \n\n");
        LOGGER.info("Bussines Key: " + execution.getProcessBusinessKey());

    }

}