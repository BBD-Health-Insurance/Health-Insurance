use HealthInsurance;
go

alter table CoverPlan
add debitOrderID varchar(255);
go

alter table CoverPlan
add constraint uq_debit_order_id unique (debitOrderID);
go