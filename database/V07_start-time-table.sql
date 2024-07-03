use HealthInsurance;
go

drop table if exists StartTime;
go

create table StartTime(
	startTime dateTime,

	constraint pk_start_time primary key (startTime)
);
go