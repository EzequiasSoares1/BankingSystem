package com.accenture.academico.bankingsystem.integrate;
import com.accenture.academico.bankingsystem.integrate.controllers.*;
import com.accenture.academico.bankingsystem.integrate.security.CryptServiceTest;
import com.accenture.academico.bankingsystem.integrate.security.TokenServiceTest;
import com.accenture.academico.bankingsystem.integrate.services.*;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@Testable
@SelectClasses({
    AuthenticationServiceTest.class,
    UserServiceTest.class,
    CryptServiceTest.class,
    TokenServiceTest.class,
    AddressServiceTest.class,
    AgencyServiceTest.class,
    ClientServiceTest.class,
    AccountServiceTest.class,
    UserControllerTest.class,
    AgencyControllerTest.class,
    AgencyControllerTest.class,
    AccountControllerTest.class,
    AuthenticationControllerTest.class,
    AddressControllerTest.class
})
public class SuiteIntegrateTest {
}
