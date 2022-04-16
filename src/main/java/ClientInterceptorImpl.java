import java.io.IOException;

import org.apache.commons.lang3.time.StopWatch;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

public class ClientInterceptorImpl implements IClientInterceptor {
	private StopWatch stopWatch;
	private Long averageTime = 0l;
	
	public ClientInterceptorImpl(StopWatch stopWatch) {
		this.stopWatch = stopWatch;
	}
	
	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		stopWatch.start();
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		stopWatch.stop();
		averageTime += stopWatch.getTime();
		stopWatch.reset();
	}

	public Long getAverageTime() {
		return averageTime;
	}

}
