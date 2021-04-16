package org.nhind.xdr.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.nhind.xdr.DocumentRepositoryAbstract;
import org.nhindirect.xd.routing.RoutingResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT,
		properties= {"direct.xd.usestreams=true"})
public abstract class SpringBaseTest 
{
	@Autowired
	protected DocumentRepositoryAbstract xdrRepo;
	
	@Autowired
	protected RoutingResolver resolver;
	
	@Before
	public void setUp()
	{
		xdrRepo.setResolver(resolver);
	}
}
