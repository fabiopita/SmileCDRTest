import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.commons.lang3.time.StopWatch;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

public class SampleClient {

	public static void main(String[] theArgs) throws IOException {

		// Create a FHIR client
		FhirContext fhirContext = FhirContext.forR4();
		IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
		//client.registerInterceptor(new LoggingInterceptor(false));
		Bundle response = new Bundle();
		String line = "";
		ClientInterceptorImpl clientInterceptorImpl = new ClientInterceptorImpl(new StopWatch());
		client.registerInterceptor(clientInterceptorImpl);
		boolean isCached = false;
		int lines = 0;
		for (int i = 1; i<=3; i++) {
			if (i==3) {
				isCached=true;
			}
			BufferedReader br = new BufferedReader(new FileReader("Patient.csv"));
			while ((line = br.readLine()) != null) // returns a boolean value
			{
				// Search for Patient resources
				lines++;
				response = client.search().forResource("Patient").where(Patient.FAMILY.matches().value(line))
						.returnBundle(Bundle.class)
						.cacheControl(new CacheControlDirective().setNoCache(isCached))
						.execute();
			}
			if (i==3) {
				br.close();
			}
			Long averageTime = clientInterceptorImpl.getAverageTime()/lines;
			System.out.println("Average Time loop " + i + ": " + averageTime);
		}
	}

}
