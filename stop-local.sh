#!/bin/bash

echo stopping create-loan / saga application
kill -9 $(cat create-loan.pid) && rm -f create-loan.pid

echo stopping loan service
kill -9 $(cat loan.pid) && rm -f loan.pid

echo stopping applicant service
kill -9 $(cat applicant.pid) && rm -f applicant.pid

echo stopping loan model service
kill -9 $(cat loan-model.pid) && rm -f loan-model.pid

echo stopping lra-coordinator
podman kill --cidfile lra-coordinator.cid
