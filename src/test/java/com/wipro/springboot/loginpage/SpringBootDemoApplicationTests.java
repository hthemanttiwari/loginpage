package com.wipro.springboot.loginpage;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.wipro.springboot.loginpage.ApplicationTestSpring;
import com.wipro.springboot.loginpage.Employee;



@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = ApplicationTestSpring.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class SpringBootDemoApplicationTests
{
    @LocalServerPort
    int randomServerPort;
     
    @Test
    public void testGetEmployeeListSuccess() throws URISyntaxException
    {
        RestTemplate restTemplate = new RestTemplate();
         
        final String baseUrl = "http://localhost:"+randomServerPort+"/employees/";
        URI uri = new URI(baseUrl);
     
       
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
         
        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("employeeList"));
    }
    
    @Test
    public void testAddEmployeeMissingHeader() throws URISyntaxException
    {
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/employees/";
        URI uri = new URI(baseUrl);
        Employee employee = new Employee(null, "Adam", "Gilly", "test@email.com");
         
        HttpHeaders headers = new HttpHeaders();
     
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
         
        try
        {
            restTemplate.postForEntity(uri, request, String.class);
            //Assert.fail();
        }
        catch(HttpClientErrorException ex)
        {
            //Verify bad request and missing header
            Assert.assertEquals(400, ex.getRawStatusCode());
            Assert.assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
        }
    }
}