use HealthInsurance;
go

drop table if exists Persona;

create table Persona(
	personaID bigint identity(1, 1) not null,
	username varchar(100) not null,
	salary money not null,
	timeOfBirth datetime default current_timestamp,
	
	constraint pk_persona primary key (personaID)
);

insert into 
	Persona(username, salary, timeOfBirth)
values 
	('katlego', 1500, sysdatetime()),
    ('Alex', 4000, dateadd(hour, -2, sysdatetime())),
    ('Shaariq', 7500, dateadd(hour, -4, sysdatetime())),
    ('Devon', 20000, dateadd(hour, -6, sysdatetime()));
go