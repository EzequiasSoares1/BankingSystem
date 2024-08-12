package com.accenture.academico.bankingsystem.unit;
import com.accenture.academico.bankingsystem.integrate.config.GlobalExceptionHandlerTestIntegrate;
import com.accenture.academico.bankingsystem.unit.mappers.*;
import com.accenture.academico.bankingsystem.unit.middlewares.AccountNumberGeneratorTest;
import com.accenture.academico.bankingsystem.unit.middlewares.UserToolsTest;
import com.accenture.academico.bankingsystem.unit.domain.*;
import com.accenture.academico.bankingsystem.unit.services.CodeAccessTest;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@Testable
@SelectClasses({
    UserTest.class,
    AccountTest.class,
    AddressTest.class,
    AgencyTest.class,
    ClientTest.class,
    TransactionHistoryTest.class,
    UserToolsTest.class,
    AccountNumberGeneratorTest.class,
    UserMapperTest.class,
    AccountMapperTest.class,
    AgencyMapperTest.class,
    ClientMapperTest.class,
    PixKeyMapperTest.class,
    TransactionHistoryMapperTest.class,
    TransactionMapperTest.class,
    CodeAccessTest.class
})
public class SuiteUnitTest {
}
