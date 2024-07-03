use HealthInsurance;
go

drop table if exists StockExchange;
go

CREATE TABLE StockExchange (
    BusinessID BIGINT,
    TradingID BIGINT,
    
    constraint pk_business_id primary key (BusinessID)
);
go

CREATE TRIGGER trg_OnlyOneRow
ON StockExchange
INSTEAD OF INSERT, UPDATE
AS
BEGIN
    IF (SELECT COUNT(*) FROM StockExchange) > 0
    BEGIN
        RAISERROR ('Only one row is allowed in the StockExchange table.', 16, 1);
        ROLLBACK TRANSACTION;
    END
    ELSE
    BEGIN
        INSERT INTO StockExchange(BusinessID, TradingID)
        SELECT BusinessID, TradingID
        FROM inserted;
    END
END;
go