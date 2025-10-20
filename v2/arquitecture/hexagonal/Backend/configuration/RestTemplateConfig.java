package [packageName].configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
    	
//    	 List<ClientHttpRequestInterceptor> interceptors
//         = restTemplate.getInterceptors();
//       if (CollectionUtils.isEmpty(interceptors)) {
//           interceptors = new ArrayList<>();
//       }
//       interceptors.add(new RestTemplateHeaderModifierInterceptor());
//       restTemplate.setInterceptors(interceptors);
       return builder.build();
    }
    

}
