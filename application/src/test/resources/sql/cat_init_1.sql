INSERT INTO cats (id, name, color, breed, owner_id, birth_date)
VALUES ('797160a3-df79-4dd8-9109-8edd75150f38'::uuid, 'Nick', 'RED', 'pug', null, '2004-03-04'::date),
       ('4cd44ae8-8e3d-477c-9bdc-fd90cb5a5b5e'::uuid, 'Polly', 'BLUE', 'milkshake', null, '2002-03-04'::date),
       ('9aaa7a5c-c35d-4c29-b475-ed85949be302'::uuid, 'Cracker', 'RED', 'bulldog', null, '2001-03-04'::date),
       ('f4834000-8420-45ea-bc23-cf8be87b4a11'::uuid, 'Frog', 'GREEN', 'frog', null, '2004-02-04'::date),
       ('bd222a6b-873a-46f1-be47-a519853891cf'::uuid, 'Gibiskus', 'GREEN', 'cat', null, '2004-03-01'::date),
       ('ebeaa9fd-a0db-49e1-b549-be9520c63723'::uuid, 'Freddy', 'RED', 'spaniel', null, '2024-03-04'::date),
       ('c794c2bc-fac1-4229-870e-1b2bf8a706f3'::uuid, 'Peppe', 'BLUE', 'pug', null, '2013-02-05'::date),
       ('90dd496d-cb25-44fb-bec7-0fc22c6d57b8'::uuid, 'Marmon', 'RED', 'pug', null, '2002-01-10'::date);


INSERT INTO cat_friendship (cat1_id, cat2_id)
VALUES ('c794c2bc-fac1-4229-870e-1b2bf8a706f3'::uuid, '90dd496d-cb25-44fb-bec7-0fc22c6d57b8'::uuid),
       ('90dd496d-cb25-44fb-bec7-0fc22c6d57b8'::uuid, 'c794c2bc-fac1-4229-870e-1b2bf8a706f3'::uuid);