package com.accenture.academico.bankingsystem.integrate;
import com.accenture.academico.bankingsystem.integrate.config.GlobalExceptionHandlerTestIntegrate;
import com.accenture.academico.bankingsystem.integrate.controllers.*;
import com.accenture.academico.bankingsystem.integrate.security.CryptServiceTestIntegrate;
import com.accenture.academico.bankingsystem.integrate.security.TokenServiceTestIntegrate;
import com.accenture.academico.bankingsystem.integrate.services.*;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@Testable
@SelectClasses({
    AuthenticationServiceTestIntegrate.class,
    UserServiceTestIntegrate.class,
    CryptServiceTestIntegrate.class,
    TokenServiceTestIntegrate.class,
    AddressServiceTestIntegrate.class,
    AgencyServiceTestIntegrate.class,
    ClientServiceTestIntegrate.class,
    AccountServiceTestIntegrate.class,
    UserControllerTestIntegrate.class,
    AgencyControllerTestIntegrate.class,
    AgencyControllerTestIntegrate.class,
    AccountControllerTestIntegrate.class,
    AuthenticationControllerTestIntegrate.class,
    AddressControllerTestIntegrate.class,
    GlobalExceptionHandlerTestIntegrate.class

})
public class SuiteIntegrateTest {
}
