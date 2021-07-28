package org.nhind.xdr.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nhind.xdr.DocumentRepositoryAbstract;
import org.nhindirect.xd.routing.RoutingResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("streams")
public abstract class SpringBaseTest 
{
	@Autowired
	protected DocumentRepositoryAbstract xdrRepo;
	
	@Autowired
	protected RoutingResolver resolver;
	
	@BeforeEach
	public void setUp()
	{
		xdrRepo.setResolver(resolver);
	}
}
