use HealthInsurance;
go

delete from ClaimHistory;
delete from "Dependent";
delete from CoverPlan;
delete from MaxCover;
delete from "Status";
go

dbcc checkident ('ClaimHistory', reseed, 0);
dbcc checkident ('Dependent', reseed, 0);
dbcc checkident ('CoverPlan', reseed, 0);
dbcc checkident ('MaxCover', reseed, 0);
dbcc checkident ('Status', reseed, 0);
go

-- MaxCover Insertions
insert into 
	MaxCover (maxCover)
values 
	(1000);
go

-- Status Insertions
insert into 
	Status("name")
values
	('None'),
	('Active'),
	('Inactive'),
	('Dead');
go
	
-- CoverPlan Insertions
insert into 
	CoverPlan(personaID, statusID)
values
	(1, 2),
	(2, 2),
	(3, 2),
	(4, 2);
go
	
-- Dependent Insertions skipped because its too complicated with the ages.

-- ClaimHistory Insertions
insert into 
	ClaimHistory(coverPlanID, claimAmount, amountPaid, claimPersonaID)
values 
	(1, 200, 200, 11),
	(2, 500, 500, 12),
	(3, 1000, 1000, 13),
	(4, 2000, 1000, 14)
go