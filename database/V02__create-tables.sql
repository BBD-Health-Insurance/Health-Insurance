use HealthInsurance;
go

drop table if exists ClaimHistory;
drop table if exists PlanHistory;
drop table if exists Dependent;
drop table if exists CoverPlan;
drop table if exists Cover;
drop table if exists MaxCover;
drop table if exists Status;
go

create table MaxCover(
	maxCoverID bigint identity(1, 1) not null,
	maxCover decimal(10, 2) default 0
	
	constraint pk_max_cover primary key (maxCoverID)
);
go

create table Status(
	statusID bigint identity(1, 1) not null,
	name varchar(10) not null,
	
	constraint pk_status primary key (statusID),
	constraint uq_status_name unique (name)
);
go

create table CoverPlan(
	coverPlanID bigint identity(1, 1) not null,
	personaID bigint not null,
	statusID bigint not null,
	
	constraint pk_cover_plan primary key (coverPlanID),
	constraint fk_cover_plan_status foreign key (statusID) references Status(statusID)
);
go

create table Dependent(
	dependentID bigint identity(1, 1) not null,
	coverPlanID bigint not null,
	personaID bigint not null,
	
	constraint pk_dependent primary key (dependentID),
	constraint fk_dependent_cover_plan foreign key (coverPlanID) references CoverPlan(coverPlanID),
	constraint uq_persona_cover_plan unique (personaID, coverPlanID)
);
go

create table ClaimHistory(
	claimHistoryID bigint identity(1, 1) not null,
	coverPlanID bigint not null,
	claimAmount decimal(10, 2) not null,
	amountPaid decimal(10, 2) not null,
	claimPersonaID bigint not null,
	"timeStamp" dateTime default current_timestamp,
	
	constraint pk_claim_history primary key (claimHistoryID),
	constraint fk_claim_history_cover_plan foreign key (coverPlanID) references CoverPlan(coverPlanID),
	constraint never_overpay check (amountPaid <= claimAmount)
);
go

-- Inserting random values so that we can restart the index correctly
insert into MaxCover(maxCover) values (0);
go

insert into "Status"("name") values ('z');
go

insert into CoverPlan(personaID, statusID) values (1, 1);
go

insert into "Dependent"(coverPlanID, personaID) values (1, 2);
go

insert into 
	ClaimHistory(coverPlanID, claimAmount, amountPaid, claimPersonaID) 
values 
	(1, 1, 1, 3);
go

delete from ClaimHistory;
delete from "Dependent";
delete from CoverPlan;
delete from "Status";
delete from MaxCover;
