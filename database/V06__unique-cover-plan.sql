use HealthInsurance;
go

alter table CoverPlan
add constraint uq_persona_id unique(personaID);
go
