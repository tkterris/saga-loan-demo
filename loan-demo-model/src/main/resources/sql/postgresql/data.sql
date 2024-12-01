\connect sagademo;
---
--- load applicant data
---
INSERT INTO sagademo.applicant (id, "limit", approved, limitupdatedate, comment, limitused) VALUES (1, 1800.00, false, NULL, 'Nico Iamaleava applicant', 0.00);
INSERT INTO sagademo.applicant (id, "limit", approved, limitupdatedate, comment, limitused) VALUES (2, 300.00, false, NULL, 'Dylan Sampson applicant', 0.00);
INSERT INTO sagademo.applicant (id, "limit", approved, limitupdatedate, comment, limitused) VALUES (3, 1500.00, false, NULL, 'James Pearce applicant', 0.00);

---
--- load loan data
---
INSERT INTO sagademo.loan (id, amount, applicantid, approved, loanrequestdate, loancanceldate, loanapprovaldate, comment) VALUES (1, 50.00, 1, false, '2023-12-15 13:27:01-05', NULL, NULL, NULL);
INSERT INTO sagademo.loan (id, amount, applicantid, approved, loanrequestdate, loancanceldate, loanapprovaldate, comment) VALUES (2, 150.00, 2, false, '2023-06-22 18:45:01-04', NULL, NULL, NULL);
INSERT INTO sagademo.loan (id, amount, applicantid, approved, loanrequestdate, loancanceldate, loanapprovaldate, comment) VALUES (3, 1000.00, 3, false, '2022-01-19 08:45:00-05', NULL, NULL, NULL);

