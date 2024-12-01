SELECT EXISTS (
   SELECT 1
   FROM pg_tables
   WHERE schemaname = 'sagademo'
   AND tablename = 'applicant'
);
