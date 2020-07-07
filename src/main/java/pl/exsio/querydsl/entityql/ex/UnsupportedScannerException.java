package pl.exsio.querydsl.entityql.ex;

import pl.exsio.querydsl.entityql.entity.scanner.QObjectScanner;

public class UnsupportedScannerException extends RuntimeException {

    public UnsupportedScannerException(QObjectScanner<?> scanner, String operation) {
        super(String.format("ObjectScanner '%s' doesn't support '%s' operation",
                scanner.getClass().getName(), operation));
    }

}
