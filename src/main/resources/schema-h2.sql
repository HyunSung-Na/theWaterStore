drop table if exists business_time CASCADE;
drop table if exists holiday_time CASCADE;
drop table if exists store CASCADE;

create table business_time (
                               id bigint generated by default as identity,
                               close varchar(255),
                               day varchar(255),
                               open varchar(255),
                               store_id bigint,
                               primary key (id)
);

create table holiday_time (
                              id bigint generated by default as identity,
                              holiday_time varchar(255),
                              store_id bigint,
                              primary key (id)
);

create table store (
                       id bigint generated by default as identity,
                       address varchar(255),
                       description varchar(255),
                       level integer not null,
                       name varchar(255),
                       owner varchar(255),
                       phone_number varchar(255),
                       primary key (id)
);

alter table business_time
    add constraint FKdpgpgp256opvbyypitxeinul2
        foreign key (store_id)
            references store;

alter table holiday_time
    add constraint FKqnwwc0t96i461qctihdtad2q
        foreign key (store_id)
            references store;
