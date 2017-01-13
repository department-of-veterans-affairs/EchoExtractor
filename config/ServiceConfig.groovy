brokerURL = "tcp://localhost:61616"

/**
 * The endpoint name of the UIMA AS service to use for processing. The service will be
 * registered with the broker as this service name. Clients use the broker/service combination
 * to connect to this service.
 */
endpoint = "echo"

/** Tell the service to persist the descriptors that are generated, deletes them by default **/
//deleteOnExit = false
//descriptorDirectory = "config/desc"

/** Additional service configurations  **/
casPoolSize = 500

instanceNumber = 10
createTypes=false