package com.accenture.academico.bankingsystem.unit;
import com.accenture.academico.bankingsystem.unit.config.GlobalExceptionHandlerTest;
import com.accenture.academico.bankingsystem.unit.mappers.UserConverterTest;
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
    GlobalExceptionHandlerTest.class,
    UserConverterTest.class,
    CodeAccessTest.class,
})
public class SuiteUnitTest {
}
